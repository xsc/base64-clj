(ns ^{ :doc "Base64 Encoding/Decoding in Clojure."
       :author "Yannick Scherer" }
  base64-clj.core
  (:use base64-clj.alphabet
        base64-clj.utils)
  (:import java.nio.ByteBuffer))

;; ## Encoding
;;
;; ### Write Helpers

(defmacro write-base64
  "Write the given three octets to the given ByteBuffer, converting
   them to four Base64-encoded sextets first. If o2 or o1 is `nil`
   padding bytes will be written. If o1 is `nil`, o2 is automatically
   assumed to be `nil`, too."
  [b o0 o1 o2]
  (let [v (gensym "v")]
    `(let [~v (int 
                (bit-or
                  (<< (->int ~o0) 16) 
                  ~(if o1 `(<< (->int ~o1) 8) `ZERO)
                  ~(if o2 `(->int ~o2) `ZERO)))]
       (doto ~b
         (.put (int->base64-byte (get-sextet ~v 0)))
         (.put (int->base64-byte (get-sextet ~v 1)))
         (.put ~(if o1 `(int->base64-byte (get-sextet ~v 2)) `BASE64_PAD))
         (.put ~(if (and o1 o2) `(int->base64-byte (get-sextet ~v 3)) `BASE64_PAD))))))

(defmacro write-octets
  "Write `n` octets of the given byte array to the given ByteBuffer, converting
   them to four Base64-encoded sextets (possibly padding bytes) first."
  [buffer data idx n]
  `(condp = ~n
      1 (write-base64 ~buffer (get-byte-at ~data ~idx 0) nil nil)
      2 (write-base64 ~buffer (get-byte-at ~data ~idx 0) (get-byte-at ~data ~idx 1) nil)
      3 (write-base64 ~buffer (get-byte-at ~data ~idx 0) (get-byte-at ~data ~idx 1) (get-byte-at ~data ~idx 2))))

;; ### Encoder

(defn encode-bytes
  "Encode the given Byte Array to its Base64 Representation."
  ^"[B"
  [^"[B" data]
  (let [len (int (count data))
        cap (int (encode-result-size len))
        b (ByteBuffer/allocate cap)]
    (loop [i (int 0)]
      (if (< i len)
        (let [next-i (unchecked-add-int i THREE)
              n (if (<= next-i len) THREE (unchecked-remainder-int (unchecked-subtract-int len i) THREE))]
          (write-octets b data i n)
          (recur next-i))
        (.array b)))))

(defn encode
  "Encode the given String to its UTF-8 Base64 Representation."
  ([^String s] (encode s "UTF-8"))
  ([^String s ^String encoding]
   (let [data (encode-bytes (.getBytes s encoding))]
     (String. data "UTF-8"))))

;; ## Decoding
;;
;; ### Read Helpers

(defmacro read-base64
  "Read the given four sextets into the given ByteBuffer by converting them
   to three octets first."
  [b s0 s1 s2 s3]
  `(let [v# (int (bit-or (<< ~s0 18) (<< ~s1 12) (<< (or ~s2 0) 6) (or ~s3 0)))]
     (.put ~b (get-octet v# 0))
     (when ~s2 
       (.put ~b (get-octet v# 1))
       (when ~s3
         (.put ~b (get-octet v# 2))))))

(defmacro read-sextets
  "Read the four sextets at the given index in the given byte array into
   the given ByteBuffer."
  [buffer data idx]
  `(let [s0# (get-byte-at ~data ~idx 0)
         s1# (get-byte-at ~data ~idx 1)
         s2# (get-byte-at ~data ~idx 2)
         s3# (get-byte-at ~data ~idx 3)]
    (read-base64 ~buffer
      (base64-byte->int s0#)
      (base64-byte->int s1#)
      (when-not (= s2# BASE64_PAD)
        (base64-byte->int s2#))
      (when-not (or (= s2# BASE64_PAD) (= s3# BASE64_PAD))
        (base64-byte->int s3#)))))

;; ### Decoder

(defn decode-bytes
  "Decode the given Base64 Byte Array."
  ^"[B"
  [^"[B" data]
  (let [len (int (count data))]
    (when-not (zero? (unchecked-remainder-int len FOUR))
      (throw (IllegalArgumentException. "Expects a byte array whose length is dividable by 4.")))
    (let [cap (let [x (aget data (unchecked-dec-int len))
                    y (aget data (unchecked-subtract-int len 2))
                    s (decode-result-size len)]
                (cond (= y BASE64_PAD) (- s 2)
                      (= x BASE64_PAD) (- s 1)
                      :else s))
          b (ByteBuffer/allocate (int cap))]
      (loop [i (int 0)]
        (if (< i len)
          (let [next-i (unchecked-add-int i FOUR)]
            (when (<= next-i len)
              (read-sextets b data i))
            (recur next-i))
          (.array b))))))

(defn decode
  "Decode the given Base64-encoded String to its String Representation."
  ([^String s] (decode s "UTF-8"))
  ([^String s ^String encoding]
   (let [data (decode-bytes (.getBytes s "UTf-8"))]
     (String. data encoding))))
