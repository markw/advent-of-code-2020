(println "Java" (System/getProperty "java.version"))
;(def starting [0 3 6])
(def starting [0,5,4,1,10,14,7])

(def target 30000000)
;(def target 2020)

(def target-index (inc target))

(def starting-spoken
  (reduce
    (fn [acc [n index]]
      (assoc acc index [n n]))
    (vec (repeat target nil))
    (map vector (drop 1 (range)) starting)))

(defn next-indices
  [spoken next-spoken index]
  (let [prev-index (get spoken next-spoken)]
    (if (nil? prev-index)
      [index index]
      [index (get prev-index 0)])))

(time
  (loop [spoken starting-spoken
         last-spoken (last starting)
         index (inc (count starting))]
    (if (= target-index index)
      (println "Answer: " last-spoken)
      (let [next-spoken (reduce - (get spoken last-spoken))]
        (recur (assoc spoken 
                      next-spoken 
                      (next-indices spoken next-spoken index))
               next-spoken 
               (inc index))))))
