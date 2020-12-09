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

(defn first-num-that-is-not-sum-of-two-previous
  [all-nums size]
  (loop [n 0]
    (let [xs (take (inc size) (drop n all-nums))]
      (if (not (is-sum-of-two-previous xs))
        (last xs)
        (recur (inc n))))))

(defn first-contiguous-block-that-sums-to-target
  [nums target]
  (loop [nums nums xs []]
    (let [xs (conj xs (first nums))
          n (reduce + xs)]
      (cond
        (= n target) xs
        (> n target) []
        (empty? nums) []
        :default (recur (rest nums) xs)))))

(let [all-nums (parse-nums "input-day9.txt")
      target (first-num-that-is-not-sum-of-two-previous all-nums 25)]
   (println "target:" target)
   (loop [nums all-nums]
     (let [xs (first-contiguous-block-that-sums-to-target nums target)]
       (if (not(empty? xs))
         (let [min (apply min xs)
               max (apply max xs)] 
           (println xs) 
           (println min max) 
           (println (+ min max)))
         (recur (rest nums))))))
