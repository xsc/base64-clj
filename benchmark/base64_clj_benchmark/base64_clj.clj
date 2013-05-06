(ns ^{:doc "Benchmarking base64-clj using Criterium."
      :author "Yannick Scherer" }
  base64-clj-benchmark.base64-clj
  (:use base64-clj.core
        base64-clj-benchmark.data))

(defbench -main encode decode)
