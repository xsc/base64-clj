(ns ^{ :doc "Base64 Encoding/Decoding in Clojure"
       :author "Yannick Scherer" }
  base64-clj-benchmark.base64-naive
  (:use base64-clj-benchmark.data))

;; ## Conversion Functions

(def ^:private ^:const base64-chars 
  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=")

(def ^:private int->base64-char
  "Convert 6-bit Integer (or the padding value 64) to Base64 Character."
  #(.charAt base64-chars %))

(def ^:private base64-char->int
  "Convert Base64 Character to 6-bit Integer (or the padding value 64)."
  (let [reverse-table (into {} (map vector base64-chars (range)))]
    #(get reverse-table %)))

;; ## Conversion Helpers

(defn- create-n-tet
  "Given two m-bit numbers `a` and `b` create an n-bit number by taking the `bits-of-a`
   lowest bits of `a` and the `(n - bits-of-a)` hightest bits f `b`."
  [n m a b bits-of-a]
  (let [bits-of-b (- n bits-of-a)
        mask-a (int (Math/pow 2 bits-of-a))]
    (+ (bit-shift-left (mod a mask-a) bits-of-b)
       (bit-shift-right b (- m bits-of-b)))))

(def ^:private create-sextet (partial create-n-tet 6 8))
(def ^:private create-octet (partial create-n-tet 8 6))

;; ## Encoding

(defn- octets->sextets
  "Convert a seq of 8-bit numbers to an equivalent seq of 6-bit numbers using the value
   64 as padding at the end."
  [octet-seq]
  (let [octets (map #(mod % 256) octet-seq)
        tupels (partition 3 3 nil octets)]
    (mapcat
      (fn [[a b c]]
        (vector
          (create-sextet 0 a 0)
          (create-sextet a (or b 0) 2)
          (if-not (or b c) 64 (create-sextet (or b 0) (or c 0) 4))
          (if-not c 64 (create-sextet c 0 6))))
      tupels)))

(defn base64-encode
  "Encode String to Base64 Representation."
  [string]
  (let [sextets (octets->sextets (map int string))]
    (apply str (map int->base64-char sextets))))

;; ## Decoding

(defn- sextets->octets
  "Convert a seq of 8-bit numbers to an equivalent seq of 6-bit numbers, interpreting
   the value 64 as padding (and thus end)."
  [sextet-seq]
  (let [[sextets _] (split-with #(not (= 64 %)) sextet-seq)
        tupels (partition 4 4 nil sextets)]
    (mapcat
      (fn [[a b c d]]
        (cons 
          (create-octet a b 6)
          (when c 
            (cons
              (create-octet b c 4)
              (when d [(create-octet c d 2)])))))
      tupels)))

(defn base64-decode
  "Decode Base64 String to UTF-8 representation."
  [string]
  (let [octets (sextets->octets (map base64-char->int string))]
    (apply str (map char octets))))


;; ## Benchmark

(defbench -main base64-encode base64-decode)
