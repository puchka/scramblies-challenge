(ns scramblies-challenge.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [ring.util.http-response :refer :all])
  (:gen-class))

(s/defschema Data
  {:str1 s/Str
   :str2 s/Str})

(s/defschema Response
  {:scramblep s/Str})

(defn count-occurrence-char
  [s c]
  (count (filter #(= % c) s)))

(defn scramble? [str1 str2]
  (every? #(>= (count-occurrence-char str1 %)
               (count-occurrence-char str2 %)) str2))

(def app
  (api
    (POST "/api/scramble" []
      :body [d Data]
      :return Response
      (ok {:scramblep (str (scramble? (:str1 d) (:str2 d)))}))))

(defn -main
  []
  (jetty/run-jetty app {:port 3000}))
