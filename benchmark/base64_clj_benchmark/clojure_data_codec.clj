(ns
  base64-clj-benchmark.clojure-data-codec
  (:use base64-clj-benchmark.data)
  (:require [clojure.data.codec.base64 :as base64]))

(defbench -main base64/encode base64/decode)
