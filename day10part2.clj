;;(def xs [28 33 18 42 31 14 46 20 48 47 24 23 49 45 19 38 39 11 1 32 25 35 8 17 7 9 4 2 34 10 3])
;;(def xs [16 10 15 5 1 11 7 19 6 12 4])
;;(def xs [1 2 3 4 5])
(def xs (map #(Integer. %)(.split (slurp "input-day10.txt") "\n")))

(defn can-skip?
  [a b]
  (<= (- b a) 3))

(defn tree
  [xs]
  (loop [xs xs acc {}]
    (if (= 1 (count xs))
      acc
      (let [n (first xs)
            ys (take 3 (rest xs))
            can-skip (filter (partial can-skip? n) ys)]
        (recur (rest xs)(assoc acc n can-skip))))))

(def count-paths
  (memoize 
    (fn [tree n]
      (let [nodes (get tree n)]
        (if (nil? nodes)
          1
          (reduce + (map 
                      (partial count-paths tree) 
                      nodes)))))))

(let [highest (apply max xs)
      all-xs (vec (sort (conj xs 0 (+ 3 highest))))]
  ;;(println (tree all-xs))
  (println (count-paths (tree all-xs) (first all-xs))))


