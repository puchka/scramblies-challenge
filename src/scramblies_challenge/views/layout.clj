(ns scramblies-challenge.views.layout
  (:use [hiccup.page :only (html5 include-css include-js)]))

(defn application []
  (html5 {:lang "en"}
         [:head
          [:title "Scramble"]
          (include-css "//stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css")
          (include-css "/css/design.css")]

         [:body
     
          [:div {:id "app"}]
          (include-js "/js/app.js")
          [:script
           "scramblies_challenge.core.init();"
           ]]))
