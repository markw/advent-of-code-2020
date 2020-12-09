(defn parse-nums [filename] 
  (map #(Integer. %) (.split (slurp filename) "\n")))

(let [nums (parse-nums "input-day1.txt")]
  (let [xs (for [x nums y nums z nums] [x y z])]
    (println 
      (reduce *
        (first
          (filter 
            (fn [[a b c]]
              (= 2020 (+ a b c)))
            xs))))))
