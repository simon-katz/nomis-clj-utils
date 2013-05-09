(ns com.nomistech.clj-utils)

;;;; ___________________________________________________________________________
;;;; ---- member? ----

(defn member? [item coll] ; TODO: Do you want this?
  (some #{item} coll))

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
  [type]
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
