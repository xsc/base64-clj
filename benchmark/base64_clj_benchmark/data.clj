(ns base64-clj-benchmark.data
  (:use criterium.core))

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

;; ## Macro for Main Function

(defmacro defbench
  [id encode-fn decode-fn]
  `(defn ~id
     [& args#]
     (if (= (first args#) "--quick")
       (do
         (println ~(str "Quick Benchmarking `" encode-fn "` ..."))
         (quick-bench (~encode-fn s0) :verbose)
         (println)
         (println ~(str "Quick Benchmarking `" decode-fn "` ..."))
         (quick-bench (~decode-fn s1) :verbose))
       (do
         (println ~(str "Benchmarking `" encode-fn "` ..."))
         (bench (~encode-fn s0) :verbose)
         (println)
         (println ~(str "Benchmarking `" decode-fn "` ..."))
         (bench (~decode-fn s1) :verbose)))))
