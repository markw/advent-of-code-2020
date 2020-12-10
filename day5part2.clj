(defn pow2
  [n]
  (if (= 0 n)
    1
    (* 2 (pow2 (dec n)))))
 
(defn parse 
  [s mn mx expo upchar]
  (if (empty? s)
    mn
    (let [rs (apply str (rest s))
          delta (pow2 expo)]
      (if (= upchar (first s))
        (parse rs (+ mn delta) mx          (dec expo) upchar)
        (parse rs mn           (- mx delta)(dec expo) upchar)))))

(defn id [[r c]] (+ c (* r 8)))
(defn parse-row [s] (parse s 0 127 6 \B))
(defn parse-col [s] (parse s 0 8 2 \R))
(defn parse-seat 
  [s]
  (list 
    (parse-row (subs s 0 7))
    (parse-col (subs s 7 10))))

(defn find-my-seat
  [ids]
  (if (> 2 (count ids))
    nil
    (let [a (inc (first ids))
          b (second ids)]
      (if (not= a b)
        a
        (find-my-seat (rest ids))))))

(let [seat-ids (sort
                 (map (comp id parse-seat)
                     (.split (slurp "input-day5.txt") "\n")))]
  (println (find-my-seat seat-ids)))

