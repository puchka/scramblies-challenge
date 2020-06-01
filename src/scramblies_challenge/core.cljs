(ns scramblies-challenge.core
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [go <!]]))

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:query {:str1 ""
                                  :str2 ""}
                          :result ""}))

(defn- handler [evt]
  (.preventDefault evt)
  (go (let [response (<! (http/post "/api/scramble"
                                    {:json-params (:query @app-state)}))]
        (if (= 200 (:status response))
          (swap! app-state assoc :result (:scramblep (:body response)))
          (swap! app-state assoc :result  "Error")))))

(defn scramble-form []
  [:div {:class "container"}
   [:h1 {:class "mx-auto"} "Scramble"]
   [:form { :class "columns small-4"}
    [:div {:class"form-group"}
     [:label {:for "str1"} "Str1"]
     [:input {:type "text" :name "str1" :id "str1" :class "form-control"
              :value (get-in @app-state [:query :str1])
              :on-change #(swap! app-state assoc-in [:query :str1] (-> % .-target .-value))}]]
    [:div {:class"form-group"}
     [:label {:for "str2"} "Str2"]
     [:input {:type "text" :name "str2" :id "str2" :class "form-control"
              :value (get-in @app-state [:query :str2])
              :on-change #(swap! app-state assoc-in [:query :str2] (-> % .-target .-value))}]]
    [:div [:input {:type "submit" :class "button" :value "Scramble"
                   :id "scramble"
                   :on-click handler}]]]
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
