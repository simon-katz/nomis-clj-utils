(ns com.nomistech.clojure-utils-test
  (:require [midje.sweet :refer :all]
            [com.nomistech.clojure-utils :refer :all]))

(fact "`member?` tests"
      (fact "Returns truthy if the item is in the collection"
            (member? :b [:a :b :c]) => truthy)
      (fact "Returns falsey if the item is not in the collection"
            (member? :d []) => falsey
            (member? :d [:a :b :c]) => falsey))

(fact "`submap?` tests"
      (fact "Returns truthy if first arg is a submap of second arg"
            (submap? {} {}) => truthy
            (submap? {} {:a 1}) => truthy
            (submap? {:a 1 :b 2} {:a 1 :b 2}) => truthy
            (submap? {:b 2 :d 4} {:a 1 :b 2 :c 3 :d 4 :e 5}) => truthy)
      (fact "Returns falsey if first arg is nota submap of second arg"
            (submap? {:a 1} {}) => falsey
            (submap? {:a 1 :b 2 :c 3 :d 4 :e 5} {:b 2 :d 4}) => falsey))

(fact "`position` and `positions` tests"
      (fact "`position` tests"
            (position even? []) => nil
            (position even? [12]) => 0
            (position even? [11 13 14]) => 2
            (position even? [11 13 14 14]) => 2)
      (fact "`positions` tests"
            (positions even? []) => []
            (positions even? [12]) => [0]
            (positions even? [11 13 14]) => [2]
            (positions even? [11 13 14 14 15]) => [2 3]))

(fact "`last-index-of-char-in-string` tests"
      (fact (last-index-of-char-in-string \c "") => -1
            (last-index-of-char-in-string \c "xyz") => -1
            (last-index-of-char-in-string \c "c") => 0
            (last-index-of-char-in-string \c "abc") => 2
            (last-index-of-char-in-string \c "abcde") => 2
            (last-index-of-char-in-string \c "abcce") => 3))

(fact "`do1` tests"
      (fact "Fails to compile when there is one form"
            (macroexpand-1 '(do1)) => (throws clojure.lang.ArityException))
      (fact "Returns value of first form when there is one form"
            (do1 :a) => :a)
      (fact "Returns value of first form when there are two form"
            (do1 :a :b) => :a)
      (fact "Returns value of first form when there are three forms"
            (do1 :a :b :c) => :a))

;; TODO: Tests needed for `def-cyclic-printers`.
