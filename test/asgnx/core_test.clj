(ns asgnx.core-test
  (:require [clojure.test :refer :all]
            [clojure.core.async :refer [<!!]]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.test.check.generators :as gen]
            [asgnx.core :refer :all]
            [asgnx.kvstore :as kvstore :refer [put! get!]]))



(deftest words-test
  (testing "that sentences can be split into their constituent words"
    (is (= ["a" "b" "c"] (words "a b c")))
    (is (= [] (words "   ")))
    (is (= [] (words nil)))
    (is (= ["a"] (words "a")))
    (is (= ["a"] (words "a ")))
    (is (= ["a" "b"] (words "a b")))))


(deftest cmd-test
  (testing "that commands can be parsed from text messages"
    (is (= "foo" (cmd "foo")))
    (is (= "foo" (cmd "foo x y")))
    (is (= nil   (cmd nil)))
    (is (= ""    (cmd "")))))


(deftest args-test
  (testing "that arguments can be parsed from text messages"
    (is (= ["x" "y"] (args "foo x y")))
    (is (= ["x"] (args "foo x")))
    (is (= [] (args "foo")))
    (is (= [] (args nil)))))


(deftest parsed-msg-test
  (testing "that text messages can be parsed into cmd/args data structures"
    (is (= {:cmd "foo"
            :args ["x" "y"]}
           (parsed-msg "foo x y")))
    (is (= {:cmd "foo"
            :args ["x"]}
           (parsed-msg "foo x")))
    (is (= {:cmd "foo"
            :args []}
           (parsed-msg "foo")))
    (is (= {:cmd "foo"
            :args ["x" "y" "z" "somereallylongthing"]}
           (parsed-msg "foo x y z somereallylongthing")))))

(deftest welcome-test
  (testing "that welcome messages are correctly formatted"
    (is (= "Welcome bob" (welcome {:cmd "welcome" :args ["bob"]})))
    (is (= "Welcome bob" (welcome {:cmd "welcome" :args ["bob" "smith"]})))
    (is (= "Welcome bob smith jr" (welcome {:cmd "welcome" :args ["bob smith jr"]})))))


(deftest homepage-test
  (testing "that the homepage is output correctly"
    (is (= cs4278-brightspace (homepage {:cmd "homepage" :args []})))))


(deftest format-hour-test
  (testing "that 0-23 hour times are converted to am/pm correctly"
    (is (= "1am" (format-hour 1)))
    (is (= "1pm" (format-hour 13)))
    (is (= "2pm" (format-hour 14)))
    (is (= "12am" (format-hour 0)))
    (is (= "12pm" (format-hour 12)))))


(deftest formatted-hours-test
  (testing "that the office hours data structure is correctly converted to a string"
    (is (= "from 8am to 10am in the chairs outside of the Wondry"
           (formatted-hours {:start 8 :end 10 :location "the chairs outside of the Wondry"})))
    (is (= "from 4am to 2pm in the chairs outside of the Wondry"
           (formatted-hours {:start 4 :end 14 :location "the chairs outside of the Wondry"})))
    (is (= "from 2pm to 10pm in the chairs outside of the Wondry"
           (formatted-hours {:start 14 :end 22 :location "the chairs outside of the Wondry"})))))


(deftest office-hours-for-day-test
  (testing "testing lookup of office hours on a specific day"
    (is (= "from 8am to 10am in the chairs outside of the Wondry"
           (office-hours {:cmd "office hours" :args ["thursday"]})))
    (is (= "from 8am to 10am in the chairs outside of the Wondry"
           (office-hours {:cmd "office hours" :args ["tuesday"]})))
    (is (= "there are no office hours on that day"
           (office-hours {:cmd "office" :args ["wednesday"]})))
    (is (= "there are no office hours on that day"
           (office-hours {:cmd "office" :args ["monday"]})))))


(deftest create-router-test
  (testing "correct creation of a function to lookup a handler for a parsed message"
    (let [router (create-router {"hello" #(str (:cmd %) " " "test")
                                 "argc"  #(count (:args %))
                                 "echo"  identity
                                 "default" (fn [& a] "No!")})
          msg1   {:cmd "hello"}
          msg2   {:cmd "argc" :args [1 2 3]}
          msg3   {:cmd "echo" :args ["a" "z"]}
          msg4   {:cmd "echo2" :args ["a" "z"]}]
      (is (= "hello test" ((router msg1) msg1)))
      (is (= "No!" ((router msg4) msg4)))
      (is (= 3 ((router msg2) msg2)))
      (is (= msg3 ((router msg3) msg3))))))
