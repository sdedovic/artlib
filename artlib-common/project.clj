(defproject com.dedovic/artlib-common "0.0.21-SNAPSHOT"
  :description "Utilities for making generative art"
  :monolith/inherit true

  :dependencies [[org.clojure/clojure :scope "provided"]]

  :managed-dependencies [[org.clojure/clojure "1.12.0"]
                         [org.clojure/core.match "1.0.0"]
                         [net.mikera/core.matrix "0.63.0"]

                         ;; this
                         [com.dedovic/artlib-core "0.0.21-SNAPSHOT"]
                         [com.dedovic/artlib-common "0.0.21-SNAPSHOT"]
                         [com.dedovic/artlib-cuda "0.0.21-SNAPSHOT"]]

  :profiles {:test
             {:dependencies
              [[net.mikera/imagez "0.12.0"]] }}

  :source-paths ["src/clj"]
  :test-paths ["test/clj"]

  :java-source-paths ["src/java"]
  :resource-paths ["resources"]
  :aot :all)
