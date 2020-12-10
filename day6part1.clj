(defn group [strs]
  (loop [ss strs current [] groups []]
    (let [s (first ss)]
      (if (empty? ss)
        (conj groups current)
        (if (empty? s)
          (recur (rest ss) [] (conj groups current))
          (recur (rest ss) (conj current s) groups))))))

(println
  (reduce +
    (map 
      (fn [group] (count (distinct (reduce concat group))))
      (group (.split (slurp "input-day6.txt") "\n")))))
