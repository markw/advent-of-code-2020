(defn parse-nums [filename] 
  (map #(Integer. %) (.split (slurp filename) "\n")))

(let [nums (parse-nums "input-day1.txt")]
  (let [pairs (for [x nums y nums] [x y])]
    (println 
      (reduce *
        (first
          (filter 
            (fn [[a b]]
              (= 2020 (+ a b)))
            pairs))))))
