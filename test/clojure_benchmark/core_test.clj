(ns clojure-benchmark.core-test
  (:require [clojure.test :refer :all]
            [taoensso.tufte :refer :all]
            [criterium.core :refer [benchmark report-result]]
            [clojure-benchmark.util :refer [write-to-file]]
            [clojure-benchmark.core :refer :all]))

(defn fib [n]
  (cond (<= n 0) 0
        (= n 1) 1
        :else (+ (fib (- n 1)) (fib (- n 2)))))

(defn noob-fib [n] (p :pro (fib n)))

(def lazy-fib (lazy-cat [0 1] (map + lazy-fib (rest lazy-fib))))

(defn pro-fib [n]
    (p :pro (nth lazy-fib n)))

(deftest a-test
  (testing "test"
    (is (= (noob-fib 10) 55))
    (is (= (noob-fib 10) 55))
    (is (= (pro-fib 13) 233))
    (is (= (pro-fib 13) 233))))


(deftest ^:bench benchmark-fib
  (profile {}
    (let [input    [10 13]
          noob-fib (benchmark (doseq [inp input] (noob-fib inp)) {:verbose true :runtime true})
          pro-fib  (benchmark (doseq [inp input] (pro-fib inp)) {:verbose true :runtime true})]
      (write-to-file "pro-fib.txt" report-result pro-fib :verbose :os :runtime)
      (write-to-file "noob-fib.txt" report-result noob-fib :verbose :os :runtime))))
