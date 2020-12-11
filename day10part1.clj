(defn diffs 
  [xs]
  (loop [xs xs ds []]
    (if (< (count xs) 2)
      ds 
      (let [[x y] xs]
        (recur (rest xs) (conj ds (- y x)))))))

;;(def xs [28 33 18 42 31 14 46 20 48 47 24 23 49 45 19 38 39 11 1 32 25 35 8 17 7 9 4 2 34 10 3])
(def xs (map #(Integer. %)(.split (slurp "input-day10.txt") "\n")))
(let [highest (apply max xs)
      all (vec (sort (conj xs 0 (+ 3 highest))))
      freqs (frequencies (diffs all))]
  (println freqs)
  (println (* (get freqs 1)(get freqs 3))))
