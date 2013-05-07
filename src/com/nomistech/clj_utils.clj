(ns com.nomistech.clj-utils)

(defn member? [item coll] ; TODO: Do you want this?
  (some #{item} coll))