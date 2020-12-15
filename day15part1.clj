;(def starting [0 3 6])
(def starting [0,5,4,1,10,14,7])

(def starting-spoken
  (into {} 
        (map 
          (fn [index n] (vector n (list index)))
          (drop 1 (range))
          starting)))

(defn spoken-before?
  [m k]
  (>= (count (m k)) 2))

(defn update-indices
  [m k n]
  (assoc m k (take 2 (conj (get m k) n))))

(loop [spoken-before starting-spoken last-spoken (last starting) index (+ 1 (count starting))]
  ;(println "spoken" spoken-before "last" last-spoken "index" index)
  (when (= 0 (mod index 1000000))(println index))
  (cond
    (= 2020 (dec index)) 
    (println last-spoken)
    
    (spoken-before? spoken-before last-spoken)
    (let [indices (get spoken-before last-spoken)
          next-n (apply - indices)]
      ;(println "spoken before:" last-spoken "diff" last-spoken-diff)
      (recur (update-indices spoken-before next-n index) next-n (inc index)))

    :default
    (recur (update-indices spoken-before 0 index) 0 (inc index))))
