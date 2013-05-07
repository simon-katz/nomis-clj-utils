(ns com.nomistech.clj-utils-test
  (:require [midje.sweet :refer :all]
            [com.nomistech.clj-utils :refer :all]))

(fact "`in?` tests"
      (fact "Returns truthy if the item is in the collection"
            (in? :b [:a :b :c]) => truthy)
      (fact "Returns falsey if the item is in the collection"
            (in? :d [:a :b :c]) => falsey))
