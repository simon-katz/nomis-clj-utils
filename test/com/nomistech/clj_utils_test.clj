(ns com.nomistech.clj-utils-test
  (:require [midje.sweet :refer :all]
            [com.nomistech.clj-utils :refer :all]))

(fact "`member?` tests"
      (fact "Returns truthy if the item is in the collection"
            (member? :b [:a :b :c]) => truthy)
      (fact "Returns falsey if the item is not in the collection"
            (member? :d [:a :b :c]) => falsey))
