(ns base64-clj-benchmark.data
  (:use criterium.core))

;; ## Data

(defn random-data
  [n]
  (apply str (take n (map char (repeatedly #(rand-int 256))))))

;; ## Macro for Main Function

(defmacro defbench
  [id encode-fn decode-fn]
  `(defn ~id
     [& args#]
     (let [d# (random-data 128)
           e# (~encode-fn d#)]
       (if (= (first args#) "--quick")
         (do
           (println ~(str "Quick Benchmarking `" encode-fn "` ..."))
           (quick-bench (~encode-fn d#) :verbose)
           (println)
           (println ~(str "Quick Benchmarking `" decode-fn "` ..."))
           (quick-bench (~decode-fn e#) :verbose))
         (do
           (println ~(str "Benchmarking `" encode-fn "` ..."))
           (bench (~encode-fn d#) :verbose)
           (println)
           (println ~(str "Benchmarking `" decode-fn "` ..."))
           (bench (~decode-fn e#) :verbose))))))
