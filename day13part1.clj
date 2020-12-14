(defn earliest-departure
  [ts bus]
  (let [earliest (first 
                   (drop-while
                    (partial > ts)
                    (iterate (partial + bus) 0)))]
    {:bus bus :earliest earliest}))

(defn bus-of-earliest-departure
  [ts, ids]
  (first 
    (sort-by 
      :earliest 
      (map 
        (partial earliest-departure ts) 
        ids))))

(defn in-service
  [s]
  (map #(Integer. %)
       (filter (partial not= "x")(.split s ","))))

(defn id-times-wait
  [ts, bus])

(defn solve
  [ts, ids]
  (let [earliest (bus-of-earliest-departure ts ids)]
    (* (:bus earliest)(- (:earliest earliest) ts))))

;;(let [input ["939" "7,13,x,x,59,x,31,19"]])
(let [input (.split (slurp "input-day13.txt") "\n")]
  (println (solve
             (Integer. (first input))
             (in-service (second input)))))
