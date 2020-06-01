(ns scramblies-challenge.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [compojure.route :as route]
            [ring.util.http-response :refer :all]
            [scramblies-challenge.views.layout :as layout])
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

(defn scramble-api []
  (api
   (POST "/api/scramble" []
         :body [d Data]
         :return Response
         (ok {:scramblep (str (scramble? (:str1 d) (:str2 d)))}))))

(def app
  (defroutes my-routes
    (scramble-api)
    (GET "/" req (layout/application))
    (route/resources "/")
    (route/not-found "404 Not Found")))

(defn -main
  []
  (jetty/run-jetty app {:port 3000}))
