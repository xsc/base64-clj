(defproject base64-clj "0.1.0-SNAPSHOT"
  :description "Fast Base64 Encoding/Decoding in Clojure"
  :url "https://github.com/xsc/base64-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {:dev {:dependencies [[midje "1.5-alpha9"]]
                   :plugins [[lein-midje "3.0-alpha4"]]}
             :benchmark {:dependencies [[criterium "0.4.1"]]
                         :source-paths ["benchmark"]}})
