(ns artlib.noise.gradient-noise
  (:require [artlib.core :as core]
            [artlib.acceleration.core :as accel]))

(defn noise2-raw [[width height] opts]
  (if-let [accelerator (core/accelerator)]
    (if (satisfies? accel/NoiseHelper accelerator)
      (let [opts {:scale  (or (:scale opts) [1 1])
                  :offset (or (:offset opts) [0 0])}]
        (accel/noise2 accelerator [width height] opts))
      (println "WARNING This accelerator does not implement the NoiseHelper protocol"))
  (println "WARNING No accelerator found")))

(defn noised2-raw [[width height] opts]
  (if-let [accelerator (core/accelerator)]
    (if (satisfies? accel/NoiseHelper accelerator)
      (let [opts {:scale  (or (:scale opts) [1 1])
                  :offset (or (:offset opts) [0 0])}]
        (accel/noised2 accelerator [width height] opts))
      (println "WARNING This accelerator does not implement the NoiseHelper protocol"))
    (println "WARNING No accelerator found")))
