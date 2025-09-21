(ns artlib.sampler)

(defn create-sampler-2d
   "Enhanced sampler with configurable edge handling.

    Args:
    - grid: vector of floats
    - width: grid width
    - height: grid height
    - edge-mode: :clamp (default), :wrap, or :mirror

    Edge modes:
    - :clamp: clamp coordinates to grid bounds
    - :wrap: wrap coordinates (tiling)
    - :mirror: mirror coordinates at edges"
   [grid width height & {:keys [edge-mode] :or {edge-mode :clamp}}]
   (let [max-x (dec width)
         max-y (dec height)]
     (fn sample [x y]
       (let [[x y] (case edge-mode
                     :clamp [(max 0.0 (min 1.0 x))
                             (max 0.0 (min 1.0 y))]
                     :wrap [(mod x 1.0) (mod y 1.0)]
                     :mirror [(let [x (mod (Math/abs (double x)) 2.0)]
                                (if (> x 1.0) (- 2.0 x) x))
                              (let [y (mod (Math/abs (double y)) 2.0)]
                                (if (> y 1.0) (- 2.0 y) y))])

             gx (* x max-x)
             gy (* y max-y)

             x0 (int gx)
             y0 (int gy)
             x1 (case edge-mode
                  :clamp (min (inc x0) max-x)
                  (:wrap :mirror) (mod (inc x0) width))
             y1 (case edge-mode
                  :clamp (min (inc y0) max-y)
                  (:wrap :mirror) (mod (inc y0) height))

             wx (- gx x0)
             wy (- gy y0)

             v00 (nth grid (+ (* y0 width) x0))
             v10 (nth grid (+ (* y0 width) x1))
             v01 (nth grid (+ (* y1 width) x0))
             v11 (nth grid (+ (* y1 width) x1))

             v0 (+ (* (- 1.0 wx) v00) (* wx v10))
             v1 (+ (* (- 1.0 wx) v01) (* wx v11))
             result (+ (* (- 1.0 wy) v0) (* wy v1))]
         result))))