(defn abs [n] (max n (- n)))

(defn parse-num
  [s]
  (Integer. (apply str (rest s))))

(defn manhattan-distance
  [distances]
  (+ 
    (abs (- (distances \W) (distances \E)))
    (abs (- (distances \N) (distances \S)))))

(defn change-dir
  [dir LR degrees]
  (let [dirs ({\R "ESWN" \L "ENWS"} LR)
        index (.indexOf dirs (str dir))]
    (.charAt dirs (mod (+ index (/ degrees 90)) 4))))

(defn change-position 
  [distances s]
  ;;(println s distances)
  (condp some s
    #{\N \E \W \S} :>> (fn [ch] (update-in distances [ch] + (parse-num s)))

    #{\L \R} :>> (fn [ch] (assoc distances \D (change-dir (distances \D) ch (parse-num s))))

    #{\F} (update-in distances [(distances \D)] + (parse-num s))

    distances)) 

(defn navigate [instructions]
  (reduce change-position {\N 0 \E 0 \W 0 \S 0 \D \E} instructions))

(defn assert-equal
  [expected actual]
  (assert (= expected actual) (str "expected: " expected " actual: " actual)))

(assert-equal \W (change-dir \E \R 180))
(assert-equal \N (change-dir \W \L 270))
(assert-equal {\N 10 \E 0 \W 0 \S 0 \D \E} (navigate ["N10"])) 
(assert-equal {\N 10 \E 0 \W 0 \S 0 \D \S} (navigate ["N10" "R90"])) 
(assert-equal {\N 10 \E 0 \W 0 \S 8 \D \S} (navigate ["N10" "R90" "F8"])) 
(assert-equal 18 (manhattan-distance {\N 11 \E 3 \W 17 \S 7 \D \S}))

(let [instructions (.split (slurp "input-day12.txt") "\n")]
  (println (manhattan-distance (navigate instructions))))
