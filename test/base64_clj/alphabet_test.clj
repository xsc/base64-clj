(ns ^{ :doc "Testing the Base64 Alphabet and Conversions"
       :author "Yannick Scherer" }
  base64-clj.alphabet-test
  (:use midje.sweet
        base64-clj.alphabet))

(fact "about alphabets"
  (count BASE64_DEFAULT) => 64
  (count BASE64_URL_SAFE) => 64
  (count BASE64_DECODE) => 123)

(let [values (range 64)]
  (fact "about encoding/decoding symmetry."
    (let [encoded (map int->base64-byte values)
          decoded (map base64-byte->int encoded)]
      encoded => (contains [(byte \+) (byte \/)] :gaps-ok)
      encoded =not=> (contains [(byte \-) (byte \_)] :gaps-ok)
      decoded => (just values))
    (let [encoded (map #(int->base64-byte % true) values)
          decoded (map base64-byte->int encoded)]
      encoded => (contains [(byte \-) (byte \_)] :gaps-ok)
      encoded =not=> (contains [(byte \+) (byte \/)] :gaps-ok)
      decoded => (just values))))
