(ns asgnx.kvstore
  "Provides a protocol for working with key/value stores.

  Different key/value (kv) store implementations can be hidden behind this
  protocol. The default implementation below uses a Clojure atom to provide
  management of a kv store. The asgnx.aws.s3 namespace provides an implementation
  backed by S3.

  DO NOT use these for real applications unless you completely understand the
  ramifications of not having transactions.
  "
  (:require [clojure.core.async :as async :refer [go]]))

(defprotocol KeyStore
  (put!     [s key value])
  (remove!  [s key])
  (get!     [s key] [s key not-found])
  (list!    [s key-prefix]))


;; Asgn 2
;;
;; @Todo:
;;
;; Implement a state put function that inserts the provided value `v`
;; into the map `m` at the key path `ks`.
;;
;; Examples:
;;
;; (state-put {} [:a :b :c] 1) => {:a {:b {:c 1}}})
;; (state-put {:a {:b 2}} [:a :b] 3) => {:a {:b 3})
;;
;; See the tests in asgnx.kvstore-test for a complete spec.
;;
(defn state-put [m ks v])

;; Asgn 2
;;
;; @Todo:
;;
;; Implement a state remove function that removes the value at
;; keypath `ks`
;;
;; Examples:
;;
;; (state-remove {:a {:b {:c 1}}} [:a :b :c]) => {:a {:b {}}})
;; (state-remove {:a {:b 2}} [:a]) => {})
;; (state-remove {:a {:b 2 :d 3}} [:a :d]) => {:a {:b 2}})
;;
;; See the tests in asgnx.kvstore-test for a complete spec.
;;
(defn state-remove [m ks])

;; Asgn 2
;;
;; @Todo:
;;
;; Implement a multi-arity state get function that retrieves the value at
;; keypath `ks`
;;
;; One arity should take a default value that is returned if there is no value
;; at the specified key path.
;;
;; Examples:
;;
;; (state-get {:a {:b {:c 1}}} [:a :b :c]) => 1
;; (state-get {:a {:b 2}} [:a]) => {:b 2})
;; (state-get {:a {:b 2 :d 3}} [:a :d]) => 3
;; (state-get {:a {:b 2 :d 3}} [:a :f] 100) => 100
;;
;; See the tests in asgnx.kvstore-test for a complete spec.
;;
(defn state-get [& args]) ;; Change the signature!


;; Asgn 2
;;
;; @Todo:
;;
;; Implement a state keys function that retrieves the keys beneath
;; keypath `ks`
;;
;; Examples:
;;
;; (state-keys {:a {:b {:c 1}}} [:a :b]) => [:c]
;; (state-keys {:a {:b 2}} [:a]) => [:b])
;; (state-keys {:a {:b 2 :d 3}} [:a]) => [:b :d]
;;
;; See the tests in asgnx.kvstore-test for a complete spec.
;;
(defn state-keys [m ks])


;; An in-memory store that mimics the side-effect based stores
;;  that will typically be used (e.g., db, file system, etc.).
;;  A Clojure atom is used to provide atomic access to the state.
(deftype MemStore [matom]
  KeyStore
  (put! [this ks v]  (go (swap! matom state-put ks v)))
  (remove! [this ks] (go (swap! matom state-remove ks)))
  (get! [s ks]       (go (state-get @matom ks)))
  (get! [s ks dv]    (go (state-get @matom ks dv)))
  (list! [s ks]      (go (state-keys @matom ks))))


(defn create
  ([] (MemStore. (atom {})))
  ([matom] (MemStore. matom)))


(defn action-assoc-in [system {:keys [ks v]}]
  (put! (:state-mgr system) ks v))


(defn action-dissoc-in [system {:keys [ks]}]
  (remove! (:state-mgr system) ks))


(def action-handlers
  {:assoc-in action-assoc-in
   :dissoc-in action-dissoc-in})
