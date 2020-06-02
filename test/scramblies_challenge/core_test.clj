(ns scramblies-challenge.core-test
  (:require [clojure.test :refer :all]
            [scramblies-challenge.core :refer :all]
            [cheshire.core :as cheshire]
            [ring.mock.request :as mock]))

(deftest scramble-test
  (testing "Test scramble? with example inputs."
    (is (scramble? "rekqodlw" "world"))
    (is (scramble? "cedewaraaossoqqyt" "codewars"))
    (is (not (scramble? "katas" "steak")))))

(defn- parse-body [body]
  (cheshire/parse-string (slurp body) true))

(deftest scramble-api-test
  (testing "Test POST request to /api/scramble returns expected response"
    (let [data {:str1 "rekqodlw"
                :str2 "world"}
          response (app (-> (mock/request :post "/api/scramble")
                            (mock/content-type "application/json")
                            (mock/body  (cheshire/generate-string data))))
          body     (parse-body (:body response))]
      (is (= (:status response) 200))
      (is (= "true" (:scramblep body))))))
