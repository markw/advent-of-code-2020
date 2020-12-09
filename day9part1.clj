(defn parse-nums [filename] 
  (map #(Integer. %) (.split (slurp filename) "\n")))

(defn is-sum-of-two-previous
  [nums]
  (let [target (last nums)
        xs (drop-last nums)
        pairs (for [a xs b xs :when (not (= a b))] [a b])]
    (< 0 (count 
           (filter 
             (fn [[a b]] 
               (= target (+ a b))) 
             pairs)))))

(let [all-nums (parse-nums "input-day9.txt")]
  (loop [n 0]
    (let [xs (take 26 (drop n all-nums))]
      (if (not (is-sum-of-two-previous xs))
        (println (last xs))
        (recur (inc n))))))
