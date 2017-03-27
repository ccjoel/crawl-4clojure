(ns crawl-4clojure.core
  (:require [org.httpkit.client :as http]
            [net.cgrand.enlive-html :as html]
            [clojure.edn :as edn])
  (:gen-class))

(def base-url "http://www.4clojure.com")
(def problem-url (str base-url "/problem/"))
(def solution-url (str problem-url "/solutions/"))

(def ring-session-id (-> (slurp (clojure.java.io/resource "config.edn"))
                         edn/read-string
                         :4clojure-cookie))

(defn with-abs-path
  "Gets absolute path of server file."
  [filename]
  (str (.getCanonicalPath (clojure.java.io/file ".")) (java.io.File/separator) filename))

(defn problem-str
  ""
  [problem-number title tags desc test-cases solution]
  (str "\n;; #####################################################\n\n"
   ";; Problem #" problem-number " - " title "\n\n"
   ";; " tags "\n\n"
   ";; " desc
   "\n\n"
   ";; Test Cases:\n\n"
   test-cases
   "\n\n"
   ";; Solution:\n\n"
   solution
   "\n\n"))

(defn get-content
  [dom]
  ((comp first :content first) dom))

(def options {:user-agent "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36"
              :headers {"Cookie"
                        (str "ring-session=" ring-session-id ";")}})

(defn get-problem-details-if-solved
  [problem-number]
  (let [{:keys [status headers body error] :as resp} @(http/get (str problem-url problem-number) options)]
    (if error
      (println "Failed, exception: " error)
      (do
        (println "HTTP GET success: " status)
        (if (= 200 status)
          (let [dom (html/html-snippet body)
                solved? (not (empty? (html/select dom [:#solutions-link])))]
            (if solved?
              (do
                (println "Problem solved! Populating info...")
                (let [title (get-content (html/select dom [:#prob-title]))
                      tags  (clojure.string/join " " (for [l (:content (get-content (html/select dom [:#tags])))]
                                                       (first (:content l))))
                      desc (get-content (html/select dom [:#prob-desc]))
                      test-cases (clojure.string/join "\n" (for [n (html/select dom [:.test])]
                                                             (first (:content n))))
                      solution (first (:content (first (html/select dom [:#code-box]))))]
                  ;; spit all gathered results here
                  (spit (with-abs-path "problems-solved.clj")
                        (problem-str problem-number title tags desc test-cases solution)
                        :append true)
                  (Thread/sleep 10)))
              (printf "Exercise %s not solved. Will skip.\n" problem-number))))))))

(defn get-all-problems
  [end]
  (dotimes [n end]
    (get-problem-details-if-solved (+ n 1))))

(defn -main
  "Will loop through app problems, populating result file"
  [& args]
  (get-all-problems 1 150))
