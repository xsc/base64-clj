(ns ^{ :doc "Utility Functions for Base64 Encoding/Decoding"
       :author "Yannick Scherer" }
  base64-clj.utils)

;; ## Constants

(def ^:const ZERO (int 0))
(def ^:const ONE (int 1))
(def ^:const TWO (int 2))
(def ^:const THREE (int 3))
(def ^:const FOUR (int 4))
(def ^:const SEXTET_MASK (int 0x3F))
(def ^:const OCTET_MASK (int 0xFF))

;; ## Shorthands

(defmacro << 
  "'Typed' left-shift shorthand."
  [x n]
  `(int (bit-shift-left (int ~x) (int ~n))))

(defmacro >> 
  [x n]
  "'Typed' right-shift shorthand."
  `(int (bit-shift-right (int ~x) (int ~n))))

;; ## Length Calculation

(defn encode-result-size
  "Given the length of an UTF-8 byte array, compute the size of the Base64 result array."
  [n]
  (int (* (/ (- (+ n 2) (mod (+ n 2) 3)) 3) 4)))

(defn decode-result-size
  "Given the length of a Base64-encoded UTF-8 byte array, compute the maximum size of the 
   decoding result array."
  [n]
  (int (/ (* n 6) 8)))

;; ## Data Access

(defn ->int
  "Convert byte value to integer, adjusting values in [-128;127] to [0;255]."
  [b]
  (int
    (if (< (byte b) ZERO)
      (unchecked-add-int (byte b) (int 256))
      b)))

(defn ->byte
  "Convert integer value to byte, adjusting values in [0;255] to [-128;127]."
  [b]
  (byte
    (if (> (int b) (int 127))
      (unchecked-subtract-int (int b) (int 256))
      b)))

(defmacro get-byte-at
  "Get the byte value of the character at the given index in the given string."
  [s idx offset]
  `(byte (aget ~s (unchecked-add (int ~idx) (int ~offset)))))

(defmacro get-sextet
  "Given a 24-bit value, get the sextet at the given index. `n` has to be
   a constant number."
  [i n]
  `(byte 
     (bit-and
       (bit-shift-right (int ~i) ~(* (- 3 n) 6))
       SEXTET_MASK)))

(defmacro get-octet
  "Given a 24-bit value, get the octet at the given index. `n` has to be a
   constant number."
  [i n]
  `(->byte 
     (bit-and
       (bit-shift-right (int ~i) ~(* (- 2 n) 8))
       OCTET_MASK)))
