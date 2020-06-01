(ns scramblies-challenge.core
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [go <!]]))

;; define your app data so that it doesn't get over-written on reload

(defn- is-valid? [s]
  (re-matches #"^[a-z]+$" s))

(defonce app-state (atom {:query {:str1 ""
                                  :str2 ""}
                          :result ""
                          :valid false}))

(defn- handler [evt]
  (.preventDefault evt)
  (go (let [response (<! (http/post "/api/scramble"
                                    {:json-params (:query @app-state)}))]
        (if (= 200 (:status response))
          (swap! app-state assoc :result (:scramblep (:body response)))
          (swap! app-state assoc :result  "An unexpected error occured.")))))

(defn- change-handler [evt]
  (let [name (get {"str1" :str1
                   "str2" :str2} (-> evt .-target .-name))]
    (swap! app-state assoc-in [:query name]
           (-> evt .-target .-value))
    (swap! app-state assoc :valid (and (is-valid? (get-in @app-state [:query :str1] ))
                                       (is-valid? (get-in @app-state [:query :str2]))))))

(defn scramble-form []
  [:div {:class "container"}
   [:h1 {:class "mx-auto"} "Scramble"]
   [:form { :class "columns small-4"}
    [:div {:class"form-group"}
     [:label {:for "str1"} "Str1"]
     [:input {:type "text" :name "str1" :id "str1" :class "form-control"
              :value (get-in @app-state [:query :str1])
              :on-change change-handler}]]
    [:div {:class"form-group"}
     [:label {:for "str2"} "Str2"]
     [:input {:type "text" :name "str2" :id "str2" :class "form-control"
              :value (get-in @app-state [:query :str2])
              :on-change change-handler}]]
    [:div {:class"form-group"}
     [:input {:type "submit" :class "button" :cass "form-control"
              :value "Scramble"
              :id "scramble"
              :on-click handler
              :disabled (not (:valid @app-state))}]]]
   [:div {:class
          (if (= "" (:result @app-state))
            "hidden"
            (if (= "true" (:result @app-state))
              "alert alert-success"
              "alert alert-danger"))}
    (:result @app-state)]])

(defn start []
  (reagent/render-component [scramble-form]
                            (. js/document (getElementById "app"))))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (start))
