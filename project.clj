(defproject scramblies-challenge "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.597" :scope "provided"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [metosin/compojure-api "2.0.0-alpha31"]
                 [reagent "0.9.1"]
                 [hiccup "1.0.5"]
                 [cljs-http "0.1.46"]]
  :main ^:skip-aot scramblies-challenge.core
  :target-path "target/%s"
  :profiles {:test {:dependencies [[cheshire "5.10.0"]
                                   [ring-mock "0.1.5"]]}
             :uberjar {:aot :all}}
  :plugins [[lein-cljsbuild "1.1.7"]]
  :cljsbuild {:builds [{:id "main"
                        :source-paths ["src"]
                        :figwheel true
                        :compiler {:main "scramblies-challenge.core"
                                   :output-to "resources/public/js/app.js"
                                   :output-dir "resources/public/js/out"
                                   :asset-path "js/out"}}]})
