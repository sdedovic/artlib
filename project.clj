(defproject com.dedovic/artlib-parent "0.0.21-SNAPSHOT"
  :plugins [[lein-changelog "0.3.2"]
            [lein-pprint "1.3.2"]
            [lein-monolith "1.10.3"]
            [com.dedovic/lein-version "1.0.0"]]

  :monolith {:inherit 
             [:url :license :deploy-repositories :release-tasks]

             :inherit-leaky 
             [:repositories :license]
             
             :project-dirs 
             ["artlib-core" "artlib-cuda" "artlib-common"]}

  :aliases {"install-all" ["monolith" "each" "install"]}

  :url "https://github.com/sdedovic/artlib-core"
  :license {:name "Apache License, Version 2.0" 
            :url  "https://www.apache.org/licenses/LICENSE-2.0.html"}

  :deploy-repositories  [["releases" {:sign-releases false
                                      :url           "https://clojars.org/repo"
                                      :username      :env/clojars_user
                                      :password      :env/clojars_token}]]

  :managed-dependencies [[org.clojure/clojure "1.12.0"]
                         [org.clojure/core.match "1.0.0"]
                         [net.mikera/core.matrix "0.63.0"]

                         ;; this
                         [com.dedovic/artlib-core "0.0.21-SNAPSHOT"]
                         [com.dedovic/artlib-common "0.0.21-SNAPSHOT"]
                         [com.dedovic/artlib-cuda "0.0.21-SNAPSHOT"]

                         ; progress bar
                         [progrock "0.1.2"]

                         ; graphics
                         [quil "4.3.1323"]

                         ; serde
                         [org.clojure/data.json "2.4.0"]
                         [org.locationtech.jts/jts-core "1.18.1"]

                         ; numerical integration
                         [org.apache.commons/commons-math3 "3.6.1"]]

  ;; this is awful and i feel like it can be improved easily
  :release-tasks [;; 1 - tests
                  ["vcs" "assert-committed"]
                  ["monolith" "each" "test"]

                  ;; 2 - bump to release versions
                  ;; 2.1 - bump project versions
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["monolith" "each" "change" "version" "leiningen.release/bump-version" "release"]

                  ;; 2.2 - bump version of artlib-common
                  ["change" ":managed-dependencies:com.dedovic/artlib-common" "leiningen.release/bump-version" "release"]
                  ["monolith" "each" "change" ":managed-dependencies:com.dedovic/artlib-common" "leiningen.release/bump-version" "release"]

                  ;; 2.3 - bump version of artlib-core
                  ["change" ":managed-dependencies:com.dedovic/artlib-core" "leiningen.release/bump-version" "release"]
                  ["monolith" "each" "change" ":managed-dependencies:com.dedovic/artlib-core" "leiningen.release/bump-version" "release"]

                  ;; 2.4 - bump version of artlib-cuda
                  ["change" ":managed-dependencies:com.dedovic/artlib-cuda" "leiningen.release/bump-version" "release"]
                  ["monolith" "each" "change" ":managed-dependencies:com.dedovic/artlib-cuda" "leiningen.release/bump-version" "release"]

                  ;; 3 - changelog release
                  ["changelog" "release"]

                  ;; 4 - commit changes
                  ["vcs" "commit"]
                  ["vcs" "tag" "--no-sign"]                 ;; TODO: start signing things

                  ;; 5 - deploy to clojars
                  ["monolith" "each" "deploy"]

                  ;; 6 - bump version for new dev cycle
                  ;; 6.1 - bump project versions
                  ["change" "version" "leiningen.release/bump-version"]
                  ["monolith" "each" "change" "version" "leiningen.release/bump-version"]

                  ;;; 6.2 - bump version of artlib-common
                  ["change" ":managed-dependencies:com.dedovic/artlib-common" "leiningen.release/bump-version"]
                  ["monolith" "each" "change" ":managed-dependencies:com.dedovic/artlib-common" "leiningen.release/bump-version"]

                  ;; 6.3 - bump version of artlib-core
                  ["change" ":managed-dependencies:com.dedovic/artlib-core" "leiningen.release/bump-version"]
                  ["monolith" "each" "change" ":managed-dependencies:com.dedovic/artlib-core" "leiningen.release/bump-version"]

                  ;; 6.4 - bump version of artlib-cuda
                  ["change" ":managed-dependencies:com.dedovic/artlib-cuda" "leiningen.release/bump-version"]
                  ["monolith" "each" "change" ":managed-dependencies:com.dedovic/artlib-cuda" "leiningen.release/bump-version"]

                  ;; 7 - commit changes
                  ["vcs" "commit"]
                  ["vcs" "push"]
                  
                  ;; 8 - start next cycle
                  ["monolith" "each" "install"]]

  :dependencies [[org.clojure/clojure :scope "provided"]])
