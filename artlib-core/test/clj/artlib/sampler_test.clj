(ns artlib.sampler-test
  (:require [clojure.test :refer [deftest is testing]]
            [artlib.sampler :refer [create-sampler-2d]]))

(defn approx=
  "Check if two floating point numbers are approximately equal"
  ([a b] (approx= a b 1e-6))
  ([a b epsilon]
   (< (Math/abs (- a b)) epsilon)))

(deftest test-clamp-mode
  (testing "Clamp mode edge handling"
    (let [grid-2x2 [1.0 3.0 7.0 9.0]
          sampler (create-sampler-2d grid-2x2 2 2 :edge-mode :clamp)]

      (testing "Out-of-bounds coordinates are clamped"
        (is (approx= (sampler -0.5 -0.5) 1.0) "Negative coords clamped to top-left")
        (is (approx= (sampler 1.5 1.5) 9.0) "Coords > 1 clamped to bottom-right")
        (is (approx= (sampler -0.1 0.5) 4.0) "X negative, Y valid")
        (is (approx= (sampler 0.5 -0.1) 2.0) "X valid, Y negative")
        (is (approx= (sampler 1.1 0.5) 6.0) "X > 1, Y valid")
        (is (approx= (sampler 0.5 1.1) 8.0) "X valid, Y > 1")))))

(deftest test-wrap-mode
  (testing "Wrap mode edge handling"
    (let [grid-2x2 [1.0 3.0 7.0 9.0]
          sampler (create-sampler-2d grid-2x2 2 2 :edge-mode :wrap)]

      (testing "Coordinates wrap around"
        (is (approx= (sampler 1.5 0.5) (sampler 0.5 0.5)) "X wraps: 1.5 -> 0.5")
        (is (approx= (sampler 0.5 1.5) (sampler 0.5 0.5)) "Y wraps: 1.5 -> 0.5")
        (is (approx= (sampler -0.5 0.5) (sampler 0.5 0.5)) "X wraps: -0.5 -> 0.5")
        (is (approx= (sampler 0.5 -0.5) (sampler 0.5 0.5)) "Y wraps: -0.5 -> 0.5")
        (is (approx= (sampler 2.25 0.0) (sampler 0.25 0.0)) "Multiple wraps")))))

(deftest test-mirror-mode
  (testing "Mirror mode edge handling"
    (let [grid-2x2 [1.0 3.0 7.0 9.0]
          sampler (create-sampler-2d grid-2x2 2 2 :edge-mode :mirror)]

      (testing "Coordinates mirror at edges"
        (is (approx= (sampler 1.5 0.5) (sampler 0.5 0.5)) "X mirrors: 1.5 -> 0.5")
        (is (approx= (sampler -0.5 0.5) (sampler 0.5 0.5)) "X mirrors: -0.5 -> 0.5")
        (is (approx= (sampler 0.5 1.5) (sampler 0.5 0.5)) "Y mirrors: 1.5 -> 0.5")
        (is (approx= (sampler 0.5 -0.5) (sampler 0.5 0.5)) "Y mirrors: -0.5 -> 0.5")))))

(deftest test-3x3-grid
  (testing "3x3 grid interpolation"
    (let [grid-3x3 [1.0 2.0 3.0
                    4.0 5.0 6.0
                    7.0 8.0 9.0]
          sampler (create-sampler-2d grid-3x3 3 3)]

      (testing "Corner values"
        (is (approx= (sampler 0.0 0.0) 1.0) "Top-left")
        (is (approx= (sampler 1.0 0.0) 3.0) "Top-right")
        (is (approx= (sampler 0.0 1.0) 7.0) "Bottom-left")
        (is (approx= (sampler 1.0 1.0) 9.0) "Bottom-right"))

      (testing "Center and interpolation points"
        (is (approx= (sampler 0.5 0.5) 5.0) "Exact center should be 5.0")
        ;; Quarter point interpolation: between cells (0,0), (1,0), (0,1), (1,1)
        ;; At (0.25, 0.25) we're 0.5 into the first cell in both directions
        ;; So we interpolate: 1*0.25 + 2*0.25 + 4*0.25 + 5*0.25 = 3.0
        ;; Actually: bilinear gives us (1*0.5 + 2*0.5)*0.5 + (4*0.5 + 5*0.5)*0.5 = 1.5*0.5 + 4.5*0.5 = 3.0
        ;; Wait, let me recalculate: at (0.25, 0.25) in 3x3 grid:
        ;; gx = 0.25 * 2 = 0.5, gy = 0.25 * 2 = 0.5
        ;; x0=0, y0=0, wx=0.5, wy=0.5
        ;; Interpolating between 1,2,4,5: (1*0.5+2*0.5)*0.5 + (4*0.5+5*0.5)*0.5 = 1.5*0.5 + 4.5*0.5 = 3.0
        (is (approx= (sampler 0.25 0.25) 3.0) "Quarter point interpolation")))))

(deftest test-edge-cases
  (testing "Edge cases"
    (testing "1x1 grid"
      (let [single-cell [42.0]
            sampler (create-sampler-2d single-cell 1 1)]
        (is (approx= (sampler 0.0 0.0) 42.0) "1x1 at origin")
        (is (approx= (sampler 0.5 0.5) 42.0) "1x1 at center")
        (is (approx= (sampler 1.0 1.0) 42.0) "1x1 at corner")))

    (testing "1x3 grid (single row)"
      (let [single-row [10.0 20.0 30.0]
            sampler (create-sampler-2d single-row 3 1)]
        (is (approx= (sampler 0.0 0.0) 10.0) "Left edge")
        (is (approx= (sampler 0.5 0.0) 20.0) "Middle")
        (is (approx= (sampler 1.0 0.0) 30.0) "Right edge")
        (is (approx= (sampler 0.25 0.0) 15.0) "Interpolated between 10 and 20")))

    (testing "3x1 grid (single column)"
      (let [single-col [10.0 20.0 30.0]
            sampler (create-sampler-2d single-col 1 3)]
        (is (approx= (sampler 0.0 0.0) 10.0) "Top edge")
        (is (approx= (sampler 0.0 0.5) 20.0) "Middle")
        (is (approx= (sampler 0.0 1.0) 30.0) "Bottom edge")
        (is (approx= (sampler 0.0 0.25) 15.0) "Interpolated between 10 and 20")))))

(deftest test-interpolation-precision
  (testing "Interpolation precision and consistency"
    (let [grid-2x2 [0.0 1.0 2.0 3.0]
          sampler (create-sampler-2d grid-2x2 2 2)]

      (testing "Linear gradients"
        ;; Test that interpolation along edges is linear
        (let [samples (map #(sampler % 0.0) [0.0 0.25 0.5 0.75 1.0])]
          (is (= samples [0.0 0.25 0.5 0.75 1.0]) "Top edge should be linear gradient")))

      (testing "Symmetry with symmetric grid"
        ;; Test with a symmetric grid where diagonal symmetry should hold
        (let [symmetric-grid [1.0 2.0 2.0 1.0]  ; Symmetric across diagonal
              sym-sampler (create-sampler-2d symmetric-grid 2 2)]
          (is (approx= (sym-sampler 0.25 0.75) (sym-sampler 0.75 0.25))
              "Symmetric grid should have diagonal symmetry"))))))