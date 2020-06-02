(ns scramblies-challenge.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.api.sweet :refer [defroutes api POST GET]]
            [schema.core :as s]
            [compojure.route :as route]
            [ring.util.http-response :refer [ok]]
            [scramblies-challenge.views.layout :as layout])
  (:gen-class))

;; Schema for input data
(s/defschema Data
  {:str1 s/Str
   :str2 s/Str})

;; Schema for response
(s/defschema Response
  {:scramblep s/Str})

(defn count-occurrence-char
  "Count the occurrence of a character c in a given string s"
  [s c]
  (count (filter #(= % c) s)))

(defn scramble?
  "Returns true if a portion of str1 characters can be rearranged
  to match str2, otherwise returns false"
  [str1 str2]
  (every? #(>= (count-occurrence-char str1 %)
               (count-occurrence-char str2 %)) str2))

;; Scramble API
(def scramble-api
  (api
   (POST "/api/scramble" []
         :body [d Data]
         :return Response
         (ok {:scramblep (str (scramble? (:str1 d) (:str2 d)))}))))

;; Define routes for the application
(def app
  (defroutes scramble-challenge-routes
    scramble-api
    (GET "/" req (layout/application))
    (route/resources "/")
    (route/not-found "404 Not Found")))

;; Entry point of the application
(defn -main
  []
  (jetty/run-jetty app {:port 3000}))
