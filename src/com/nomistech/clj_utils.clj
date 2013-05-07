(ns com.nomistech.clj-utils)

(defn in? [item coll] ; TODO: Do you want this?
  (some #{item} coll))