(defproject com.dedovic/artlib-cuda "0.0.20-SNAPSHOT"
  :description "GPU (via CUDA) accelerated utilities for making generative art"
  :monolith/inherit true

  :dependencies [[org.clojure/clojure :scope "provided"]
                 [org.clojure/core.match]

                 [com.dedovic/artlib-common]

                 [net.mikera/imagez "0.12.0"]

                 [uncomplicate/clojurecuda "0.21.0"]
                 [uncomplicate/commons "0.16.1"]
                 [org.uncomplicate/clojure-cpp "0.4.0"]]

  :managed-dependencies [[org.clojure/clojure "1.12.0"]
                         [org.clojure/core.match "1.0.0"]
                         [net.mikera/core.matrix "0.63.0"]

                         ;; this
                         [com.dedovic/artlib-core "0.0.20-SNAPSHOT"]
                         [com.dedovic/artlib-common "0.0.20-SNAPSHOT"]
                         [com.dedovic/artlib-cuda "0.0.20-SNAPSHOT"]

                         ; progress bar
                         [progrock "0.1.2"]

                         ; graphics
                         [quil "4.3.1323"]

                         ; serde
                         [org.clojure/data.json "2.4.0"]
                         [org.locationtech.jts/jts-core "1.18.1"]

                         ; numerical integration
                         [org.apache.commons/commons-math3 "3.6.1"]]

  :profiles {:test 
             {:dependencies 
              [[org.bytedeco/cuda "12.6-9.5-1.5.11" :classifier "linux-x86_64-redist"]] }}

  :source-paths ["src/clj" "src/cuda"]
  :java-source-paths ["src/java"]
  :resource-paths ["resources"]
  :aot :all)
