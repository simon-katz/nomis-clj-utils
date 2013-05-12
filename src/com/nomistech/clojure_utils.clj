(ns com.nomistech.clojure-utils
  (require [clojure.set]))

;;;; ___________________________________________________________________________
;;;; ---- member? ----

(defn member? [item coll] ; TODO: Do you want this?
  (some #{item} coll))

;;;; ___________________________________________________________________________
;;;; ---- submap? ----

(defn submap? [m1 m2]
  (clojure.set/subset? (set m1) (set m2)))

;;;; ___________________________________________________________________________
;;;; ---- position ----
;;;; ---- positions ----

;;;; From http://stackoverflow.com/questions/4830900

(defn ^:private indexed
  "Returns a lazy sequence of [index, item] pairs, where items come
  from 's' and indexes count up from zero.

  (indexed '(a b c d))  =>  ([0 a] [1 b] [2 c] [3 d])"
  [s]
  (map vector (iterate inc 0) s))

(defn positions
  "Returns a lazy sequence containing the positions at which pred
   is true for items in coll."
  [pred coll]
  (for [[idx elt] (indexed coll) :when (pred elt)] idx))

(defn position
  [pred coll]
  (first (positions pred coll)))

;;;; ___________________________________________________________________________
;;;; ---- last-index-of-char-in-string ----

(defn last-index-of-char-in-string [^Character char ^String string]
  ;; Effect of type hints:
  ;;   Without:
  ;;     (time (dotimes [i 1000000] (last-index-of-char-in-string \c "abcdef")))
  ;;     "Elapsed time: 2564.688 msecs"
  ;;   With:
  ;;     (time (dotimes [i 1000000] (last-index-of-char-in-string \c "abcdef")))
  ;;     "Elapsed time: 18.44 msecs"
  (.lastIndexOf string (int char)))

;;;; ___________________________________________________________________________
;;;; ---- do1 ----

(defmacro do1
  ;; Copied from http://en.wikibooks.org/wiki/Clojure_Programming/Concepts.
  [first-form & other-forms]
  `(let [x# ~first-form]
     ~@other-forms
     x#))

;;;; ___________________________________________________________________________
;;;; ---- def-cyclic-printers ----

(defmacro def-cyclic-printers
  "Define methods on
     `clojure.core/print-method`
   and
    `clojure.pprint/simple-dispatch`
  that allow instances of type to be printed when they are part of
  circular structures. When an instance is encountered while it is
  already being printed a special token is printed
  instead of getting into an endless loop."
  [^clojure.lang.Symbol type]
  `(do

     (defn print-fun# [~'v] (into {} ~'v))

     (def text-for-cyclic-printing# ~(str "##" (.getName type) "##"))

     (def ^:dynamic *being-printed?*# #{})

     (defmethod print-method ~type [~'v ~'writer]
       (if (*being-printed?*# ~'v)
         (.write ~'writer text-for-cyclic-printing#)
         (binding [*being-printed?*# (conj *being-printed?*# ~'v)]
           (print-method (print-fun# ~'v) ~'writer))))

     #_
     (defmethod print-dup ~type [~'v ~'writer]
       ;; I'm not sure what print-dup is. Hmmm, `*print-dup*` is about
       ;; making the written thing be readable, so this is probably
       ;; useless or bad.
       (if (*being-printed?*# ~'v)
         (.write ~'writer text-for-cyclic-printing#)
         (binding [*being-printed?*# (conj *being-printed?*# ~'v)]
           (print-dup (print-fun# ~'v) ~'writer))))

     (defmethod clojure.pprint/simple-dispatch ~type
       [~'v]
       (if (*being-printed?*# ~'v)
         (clojure.pprint/write text-for-cyclic-printing#)
         (binding [*being-printed?*# (conj *being-printed?*# ~'v)]
           (clojure.pprint/pprint (print-fun# ~'v)))))))
