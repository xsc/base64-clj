(ns ^{ :doc "Alphabets for Base64 Encoding/Decoding."
       :author "Yannick Scherer" }
  base64-clj.alphabet)

;; ## Alphabet
;;
;; An Alphabet is a 65 character string with the first 64 characters describing
;; the characters used for encoding 6-bit values.

(def ^:const BASE64_DEFAULT 
  "Base64 Alphabet."
  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/")

(def ^:const BASE64_URL_SAFE
  "Base64 URL-safe Alphabet ('+' and '/' replaced by '-' and '_')."
  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_")

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

(defn int->base64-byte
  "Convert 6-bit integer (or the padding value 64) to Base64 byte."
  ([i] 
   (byte 
     (let [i (int (.charAt BASE64_DEFAULT (int i)))]
       (if (> i (int 127))
         (unchecked-subtract-int i (int 128))
         i))))
  ([i url-safe?] 
   (byte (.charAt (if url-safe? BASE64_URL_SAFE BASE64_DEFAULT) (int i)))))

(defn base64-byte->int
  "Convert Base64 character to 6-bit integer, the padding value 64 or the no-such-value
   indicator 65."
  [c]
  (let [i (byte c)]
    (if (< i (byte 123))
      (int (.charAt BASE64_DECODE i))
      (int 65))))
