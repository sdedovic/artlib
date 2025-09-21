(defproject com.dedovic/artlib-core "0.0.19-SNAPSHOT"
  :description "Utilities for making generative art"
  :monolith/inherit true

  :dependencies [[org.clojure/clojure :scope "provided"]
                 [com.dedovic/artlib-common]

                 [net.mikera/core.matrix]
                 [progrock]
                 [quil]

                 ; serde
                 [org.clojure/data.json]
                 [org.locationtech.jts/jts-core]

                 ; numerical integration
                 [org.apache.commons/commons-math3]]

  :managed-dependencies [[org.clojure/clojure "1.12.0"]
                         [org.clojure/core.match "1.0.0"]
                         [net.mikera/core.matrix "0.63.0"]

                         ;; this
                         [com.dedovic/artlib-core "0.0.19-SNAPSHOT"]
                         [com.dedovic/artlib-common "0.0.19-SNAPSHOT"]
                         [com.dedovic/artlib-cuda "0.0.19-SNAPSHOT"]

                         ; progress bar
                         [progrock "0.1.2"]

                         ; graphics
                         [quil "4.3.1323"]

                         ; serde
                         [org.clojure/data.json "2.4.0"]
                         [org.locationtech.jts/jts-core "1.18.1"]

                         ; numerical integration
                         [org.apache.commons/commons-math3 "3.6.1"]]

  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :java-source-paths ["src/java"]
  :resource-paths ["resources"]
  :aot :all)
