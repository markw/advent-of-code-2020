;(def starting [0 3 6])
(def starting [0,5,4,1,10,14,7])

(def target 30000000)

(def starting-spoken-before
  (reduce
    (fn [acc [n index]]
      (assoc acc index (list n)))
    (vec (repeat target nil))
    (map vector (drop 1 (range)) starting)))

(defn spoken-before?
  [m k]
  (>= (count (get m k)) 2))

(defn update-indices
  [m k n]
  (let [indices (get m k)
        new-indices (if (nil? indices) 
                      (list n n)
                      (list n (first indices)))]
    (assoc m k new-indices)))

(loop [spoken-before starting-spoken-before 
       last-spoken (last starting) 
       index (+ 1 (count starting))]
  (cond
    (= target (dec index)) 
    (println "Answer: " last-spoken)
    
    (spoken-before? spoken-before last-spoken)
    (let [next-n (reduce - (get spoken-before last-spoken))]
      (recur (update-indices spoken-before next-n index) next-n (inc index)))

    :default
    (recur (update-indices spoken-before 0 index) 0 (inc index))))
