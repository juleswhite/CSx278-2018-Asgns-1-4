(ns asgnx.deploy-test
  (:require [clojure.test :refer :all]
            [clojure.edn :as edn]
            [clojure.core.async :refer [<!!]]
            [clj-http.client :as client]))


(deftest deployment-test
  (testing "the integration and handling of messages"
    (let [{:keys [endpoint phone-number]} (edn/read-string (slurp "deploy.edn"))]
      (is (not (nil? endpoint)))
      (is (not (nil? phone-number)))
      (let [resp (client/post endpoint {:form-params {:Body "welcome bob"
                                                      :From phone-number
                                                      :To phone-number}})]

        (println "Got response:" resp)
        (is (= 200 (:status resp)))
        (is (= "Welcome bob" (:body resp)))))))
