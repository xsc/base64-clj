(ns ^{ :doc "Testing Base64 encoding."
       :author "Yannick Scherer" }
  base64-clj.base64-test
  (:use midje.sweet
        base64-clj.core))

;; ## Main Test Data
;;
;; From: http://en.wikipedia.org/wiki/Base64

(def s0
  (str "Man is distinguished, not only by his reason, but by this singular passion from other "
       "animals, which is a lust of the mind, that by a perseverance of delight in the continued "
       "and indefatigable generation of knowledge, exceeds the short vehemence of any carnal pleasure."))

(def s1 
  (str "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz"
       "IHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2Yg"
       "dGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGlu"
       "dWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRo"
       "ZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4="))

;; ## Tests

(tabular
  (fact "about encoding"
    (encode ?txt) => ?r)
  ?txt             ?r
  s0               s1
  "pleasure."      "cGxlYXN1cmUu"
  "leasure."       "bGVhc3VyZS4="
  "easure."        "ZWFzdXJlLg=="
  "asure."         "YXN1cmUu"
  "Specials:ÄÖÜ"   "U3BlY2lhbHM6w4TDlsOc")

(tabular
  (fact "about decoding"
    (decode ?txt) => ?r)
  ?txt                   ?r
  s1                     s0
 "cGxlYXN1cmUu"          "pleasure."
 "bGVhc3VyZS4="          "leasure."
 "ZWFzdXJlLg=="          "easure."
 "YXN1cmUu"              "asure."
  "U3BlY2lhbHM6w4TDlsOc" "Specials:ÄÖÜ")

(fact "about encoding/decoding a random string"
  (let [s (apply str (take 1024 (repeatedly #(char (rand-int 256)))))
        e (encode s)]
    (decode e) => s))
