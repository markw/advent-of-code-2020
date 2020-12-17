
(defn between?
  [[a b] n]
  (and (<= a n)(<= n b)))

(defn parse-range
  [s]
  (mapv #(Integer. %)(.split s "-")))

(defn parse-ranges
  [ss]
  (mapv parse-range ss))

(defn validator-fn
  [[r0 r1]]
  (fn [n]
    (or (between? r0 n)
        (between? r1 n))))

(defn show [s] 
  (println s) s)

(defn s-to-validator
  [s]
  (-> (.split s ":")
      (get 1)
      (.trim)
      (.split " ")
      ((juxt first last))
      (parse-ranges)
      (validator-fn)))

(defn invalid-for-all?
  [validators n]
  (every? false? (validators n)))

(defn solve
  [input]
  (let [range-input (filter #(.contains % " or ") input)
        ticket-input (drop 1 (filter #(.contains % ",") input))
        tickets (mapcat (fn [s]
                          (map #(Integer. %) (.split s ","))) 
                        ticket-input)
        validators (apply juxt (mapv s-to-validator range-input))]
    (reduce + (filter (partial invalid-for-all? validators) (flatten tickets)))))

(let [input (.split (slurp "input-day16.txt") "\n")]
  (println (solve input)))
