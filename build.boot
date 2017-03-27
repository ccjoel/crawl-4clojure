(def project 'crawl-4clojure)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"resources" "src"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "RELEASE"]
                            [adzerk/boot-test "RELEASE" :scope "test"]
                            [http-kit "2.2.0"]
                            [enlive "1.1.6"]])

(task-options!
 aot {:namespace   #{'crawl-4clojure.core}}
 pom {:project     project
      :version     version
      :description "Get all your 4clojure problems with your solutions!"
      :url         "http://teh0xqb.github.io"
      :scm         {:url "https://github.com/teh0xqb/crawl-4clojure"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}}
 jar {:main        'crawl-4clojure.core
      :file        (str "crawl-4clojure-" version "-standalone.jar")})

(deftask build
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp (aot) (pom) (uber) (jar) (target :dir dir))))

(deftask run
  "Run the project."
  [a args ARG [str] "the arguments for the application."]
  (require '[crawl-4clojure.core :as app])
  (apply (resolve 'app/-main) args))

(require '[adzerk.boot-test :refer [test]])
