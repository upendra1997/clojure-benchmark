(defproject clojure-benchmark "0.1.0-SNAPSHOT"
  :description "snippets to setup benchamrking tests in clojure"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :test-selectors {:default (complement :bench)
                   :not     (fn [m s] (not (contains? m (keyword s))))
                   :bench   :bench}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.taoensso/tufte "2.4.5"]]
  :profiles {:test    [{:dependencies [[org.clojure/test.check "1.1.1"]
                                      [criterium "0.4.6"]]}]
             :bench   [{:test-paths   ["bench"]
                        :global-vars  {*assert* true}
                        :jvm-opts     ["-Djdk.attach.allowAttachSelf"
                                       "-XX:+UnlockDiagnosticVMOptions"
                                       "-XX:+DebugNonSafepoints"]
                        :dependencies [[com.clojure-goes-fast/clj-async-profiler "1.0.4"]]}
                       :test]}
  :repl-options {:init-ns clojure-benchmark.core})
