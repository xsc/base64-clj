# base64-clj

Convert Data to [Base64](http://en.wikipedia.org/wiki/Base64) and back in a reasonable amount of time. 

[![endorse](https://api.coderwall.com/xsc/endorsecount.png)](https://coderwall.com/xsc)
[![Build Status](https://travis-ci.org/xsc/base64-clj.png?branch=master)](https://travis-ci.org/xsc/base64-clj)

This is at least as fast as the [Apache Commons Base64 Codec](http://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/binary/Base64.html) (whose just-as-simple usage you can see [here](https://github.com/xsc/base64-clj/blob/master/benchmark/base64_clj_benchmark/apache_commons_base64.clj)), but slower than Alexander Taggart's [data.codec](https://github.com/clojure/data.codec).

## Usage

__Leiningen__

```clojure
[base64-clj "0.1.1"]
```

__REPL__

```clojure
(require '[base64-clj.core :as base64])

(base64/encode "Hello, World!")
;; => "SGVsbG8sIFdvcmxkIQ=="
(base64/decode "SGVsbG8sIFdvcmxkIQ==")
;; => "Hello, World!"

(base64/encode-bytes (.getBytes "Hello, World!"))
;; => #<byte[] [B@28a34522>
(base64/decode-bytes (.getBytes "SGVsbG8sIFdvcmxkIQ=="))
;; => #<byte[] [B@18b0af83>

(base64/encode "ÄÖÜ" "ISO-8859-1")
;; => "xNbc"
(base64/encode "ÄÖÜ" "UTF-8")
;; => "w4TDlsOc"

(base64/decode "xNbc")
;; => "???"
(base64/decode "xNbc" "ISO-8859-1")
;; => "ÄÖÜ"
```

## Tests

You can run [Midje](https://github.com/marick/Midje) tests using the following Leiningen command:

```
lein midje
```

## Benchmarks

You can run [Criterium](https://github.com/hugoduncan/criterium) benchmarks on different Base64 codecs with the following
Leiningen command:

```
lein benchmark base64-clj-benchmark.<ID> [--quick]
```

__ID__ is one of:

- `base64-clj` : evaluate this project
- `base64-naive` : evaluate a naive and intuitive implementation
- `apache-commons-base64` : evaluate the Apache Commons Codec
- `clojure-data-codec` : evaluate `clojure.data.codec.base64`

## License

Copyright &copy; Yannick Scherer

Distributed under the Eclipse Public License, the same as Clojure.
