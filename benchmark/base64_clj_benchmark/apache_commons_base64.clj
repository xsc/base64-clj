(ns
  base64-clj-benchmark.apache-commons-base64
  (:use base64-clj-benchmark.data)
  (:import org.apache.commons.codec.binary.Base64))

(defmacro apache-base64-encode
  [d]
  `(Base64/encodeBase64String ~d))

(defmacro apache-base64-decode
  [d]
  `(Base64/decodeBase64 ~d))

(defbench -main apache-base64-encode apache-base64-decode)
