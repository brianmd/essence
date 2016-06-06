(ns murphydye.essence.core
  (:require [murphydye.utils.core :as u])
  )


"
Entity (Instance?) is an instantiated object -- existence in Aquinas-speak.
Essence is effectively the class definition.
   (this includes attribute definitions and behavior modes)

Why do I have a cache? What is it's purpose?

There are two competing ideas I'm struggling with:
  1. The committed (true) id and attribute values
  2. The information being modified, but not yet committed.
as well as a third:
  3. Calculated values that should be cached to prevent re-performing expensive operations

The third option is an appropriate use of having a cache (though should it be on the object itself, or as a property?)

The second option should be a value object

So no, get rid of that cache!!!

Note 2: what I've been labels 'essences' is really 'entity'
"

;; (require '[clojure.test :refer :all])
;; (is (= 3 2) "testing is")
;; (defn id->atom [id]
;;   (atom {:id id}))
;; (id->atom (as-integer "  044"))

;; (defrecord Essence [data])

;; (defn atom? [x]
;;   (instance? clojure.lang.Atom x))
;;   ;; (= clojure.lang.Atom (type x)))
;; (comment
;;   (assert (= false (atom? 'x)))
;;   (assert (= true (atom? (atom 'x))))
;;   )
;; (defn atomize [x]
;;   (if (atom? x) x (atom x)))



(defprotocol Essence
  (id [this])
  (cache [this])
  (clear-cache [this])
  (get-val [this k])
  (set-val [this k v])
  (get-cached [this k f])
  )

(println "before defn essence")
(defn essence [this]
  (id this))

(extend-protocol Essence
  Object
  ;; (id [this] (:id (deref (:cache this))))
  (cache [this] (deref (:cache this)))
  (clear-cache [this] (reset! (:cache this) {:id (id this)}))
  (get-val [this k] ((deref (:cache this)) k))
  (set-val [this k v] (swap! (:cache this) assoc k v))
  (id [this] (get-val this :id))
  (get-cached [this k f]
    (if-let [result (get-val this k)]
      result
      (let [result (f this)]
        (set-val this k result)
        result
        ))))

;; (id (atom {:cache {:id 3}}))

(println "before defn essence")

;; (defrecord Entity [id external-id])  ;; external-name
(defrecord Entity [id])  ;; external-name
(defrecord AttributeDefinition [id keyname title descript datatype width validations default children])
(defrecord AttributeDefinition [id keyname title validations default children])
(defrecord AttributeValidationDefinition [id keyname title func])
(defrecord AttributeValidation [validation-definition params])

(defrecord Relationship [])

(defn new-entity [m]
  "map should contain minimally an id"
  (->Entity (atomize m)))
;; (new-entity {:id 3})
;; (id (new-entity {:id 3}))


;; specific validation types:
;;   type, regex, one-of|range, required?, multiplicity



(defrecord gui-element [attr-def element-def])
(defrecord gui-element-definition [id keyname])

(defrecord gui-element-properties [visible? width-dim height-dim sort-direction sort-seq filter-val])
(defrecord dimension [value min-value max-value calculattion-fn])

