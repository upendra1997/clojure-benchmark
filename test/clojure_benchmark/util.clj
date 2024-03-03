(ns clojure-benchmark.util
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]))


(defn write-to-file [file-name f & args]
  (let [file-name (format "profiling-result/%s" file-name)
        _         (io/make-parents file-name)]
    (with-open [file (io/writer file-name)]
      (binding [*out* file]
        (apply f args)))))
