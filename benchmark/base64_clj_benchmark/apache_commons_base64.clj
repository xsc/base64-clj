(ns
  base64-clj-benchmark.apache-commons-base64
  (:use base64-clj-benchmark.data)
  (:import org.apache.commons.codec.binary.Base64))

(defn apache-base64-encode
  [^String s]
  (Base64/encodeBase64String (.getBytes s "UTF-8")))

(defn apache-base64-decode
  [^String s]
  (String. (Base64/decodeBase64 s) "UTF-8"))

(defbench -main apache-base64-encode apache-base64-decode)
