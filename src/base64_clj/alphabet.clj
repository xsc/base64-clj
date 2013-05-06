(ns ^{ :doc "Alphabets for Base64 Encoding/Decoding."
       :author "Yannick Scherer" }
  base64-clj.alphabet)

;; ## Alphabet
;;
;; An Alphabet is a 65 character string with the first 64 characters describing
;; the characters used for encoding 6-bit values.

(def BASE64_DEFAULT 
  "Base64 Alphabet."
  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/")

(def ^:const BASE64_PAD (byte \=))

;; ## Decode Alphabet
;;
;; The Decode Alphabet is a String containing 123 (= `(int \z)`) characters, mapping 
;; Base64 characters to their 6-bit representation, the padding value 64 or the invalid 
;; value 65.

(defmacro ^:private generate-decode-table
  "This macro creates the decode table at compile time."
  []
  (->> (concat
         (repeat 43 65)
         [62 65 62 65 63]
         (range 52 62)
         (repeat 7 65)
         (range 0 26)
         (repeat 4 65)
         [63 65]
         (range 26 52))
    (map char)
    (apply str)))

(def ^:const BASE64_DECODE
  "Base64 Decode Alphabet."
  (generate-decode-table))

;; ## Conversion

(defmacro int->base64-byte
  "Convert 6-bit integer to Base64 byte."
  [i] 
  `(byte (.charAt BASE64_DEFAULT (int ~i))))

(defmacro base64-byte->int
  "Convert Base64 character to 6-bit integer."
  [c]
  `(int (.charAt BASE64_DECODE (int ~c))))
