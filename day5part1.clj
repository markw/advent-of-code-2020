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

(println
  (apply max
    (map (comp id parse-seat)
         (.split (slurp "input-day5.txt") "\n"))))

;;(println (id (parse-seat "BFFFBBFRRR")))
;;(println (id (parse-seat "FFFBBBFRRR")))
;;(println (id (parse-seat "BBFFBBFRLL")))
