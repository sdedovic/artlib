(ns artlib.geometry.contour
  (:require [artlib.core :as core]
            [artlib.acceleration.core :as accel])
  (:import (java.awt.image BufferedImage)
           (java.util HashMap)))

(defn calc-contour-lines [heightmap threshold]
  (if-let [accelerator (core/accelerator)]
    (if (satisfies? accel/ContourHelper accelerator)
      (accel/compute-contour-lines accelerator heightmap threshold)
      (println "WARNING This accelerator does not implement the ContourHelper protocol."))
    (println "WARNING No accelerator found, cannot compute contour lines.")))

(defn polygonize [segments]
  (let [adj-map (new HashMap (count segments))
        adj-map-rev (new HashMap (count segments))
        traverse (fn [root forward backward]
                   (loop [start root
                          string []]
                     (let [end (get forward start)]
                       (cond
                         ;; the start is terminal
                         (nil? end)
                         (do
                           (.remove forward start)
                           (conj string start))

                         ;; a cycle is detected
                         (= end root)
                         (do
                           (.remove forward start)
                           (.remove forward end)
                           (.remove backward start)
                           (.remove backward end)
                           (conj string start end))

                         ;; there is more to traverse
                         :else
                         (do
                           (.remove forward start)
                           (.remove backward end)
                           (recur end (conj string start)))))))]
    (doseq [segment segments]
      (.put adj-map (first segment) (last segment))
      (.put adj-map-rev (last segment) (first segment)))
    (loop [all []]
      (if-let [entry (first adj-map)]
        (let [root (.getKey entry)
              a (traverse root adj-map adj-map-rev)
              b (traverse root adj-map-rev adj-map)
              poly (concat (reverse (rest b)) a)]
          (recur (conj all poly)))
        all))))

(defn multi-contour
  "Compute a vec of contours (stored as a polyline vec) for each value in the supplied ranges vec. The vec will have
    metadata for its threshold value. e.g.

    (let [ranges (range 0.05 0.95 0.01)]
      (doseq [poly (multi-contour heightmap ranges]
        (draw-poly poly)))
   "
  [^BufferedImage heightmap ranges]
  (->> ranges
       (calc-contour-lines heightmap)
       (mapcat #(->> %
                     (map (fn [[[x1 y1] [x2 y2]]]
                            [[(/ x1 (.getWidth heightmap)) (/ y1 (.getHeight heightmap))]
                             [(/ x2 (.getWidth heightmap)) (/ y2 (.getHeight heightmap))]]))
                     polygonize))))