(ns clojure-benchmark.bench
  (:require
    [clojure.java.io :as io]
    [clj-async-profiler.core :as prof]
    [taoensso.tufte :as tufte]))

;; necessary to print it so that jvm know that we are using its result
(def prof-started (let [start (atom (prof/start {}))
                        _     (println "profiling started." (prof/status))]
                       @start))

(alter-var-root #'tufte/*ns-filter* (constantly "*"))

;; accumulator for tufte result
(def stats-accumulator (tufte/add-accumulating-handler! {:ns-pattern "*"}))

;; before exiting make sure that we are printing profiling result
(System/setSecurityManager
  (proxy [SecurityManager] []
         (checkExit [status]
                    (let [_         (println "profiling ended." (prof/status))
                          _         (io/make-parents "profiling-result/random-file")
                          result    (prof/stop)
                          file-name (.getName result)]
                      (io/make-parents (format "profiling-result/%s" file-name))
                      (io/copy result (io/as-file (format "profiling-result/%s" file-name)))
                         (with-open [writer (io/writer (format "profiling-result/tufte-prof-%d.txt" (System/currentTimeMillis)))]
                                    (.write writer (tufte/format-grouped-pstats @stats-accumulator)))))
         (checkCreateClassLoader [& args]
                                 true)
         (checkPermission [& args]
                          true)))

