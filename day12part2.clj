(require '[clojure.set :as sets])

(defn abs [n] (max n (- n)))

(defn parse-num [s] (Integer. (apply str (rest s))))

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

(defn change-waypoint
  [waypoint dir delta]
  (condp some (str dir)
    #{\N \E \W \S} :>> (fn [ch] (update-in waypoint [ch] (fnil + 0) delta)) 
    #{\R \L} :>> (fn [ch] (sets/rename-keys waypoint (into {} (map 
                                                                (fn [k] [k (change-dir k ch delta)]) 
                                                                (keys waypoint)))))
    waypoint))

(defn apply-waypoint
  [position delta]
  (let [new-distances (reduce 
                        (fn [dist [k v]] 
                          (update-in dist [k] (partial + (* delta v))))
                        (:distances position)
                        (:waypoint position))]
    (assoc position :distances new-distances)))

(defn change-position 
  [position s]
  ;;(println s position)
  (condp some s
    #{\L \R \N \E \W \S} :>> (fn [ch] (assoc position :waypoint (change-waypoint (:waypoint position) ch (parse-num s))))

    #{\F} :>> (fn [_] (apply-waypoint position (parse-num s)))

    position)) 

(defn navigate [instructions]
  (reduce change-position 
          {:distances {\N 0 \E 0 \W 0 \S 0} :waypoint {\E 10 \N 1}} 
          instructions))

(defn assert-equal
  [expected actual]
  (assert (= expected actual) (str "expected: " expected " actual: " actual)))

(assert-equal \W (change-dir \E \R 180))
(assert-equal \N (change-dir \W \L 270))
(assert-equal {\E 10 \N 4} (change-waypoint {\E 10 \N 1} \N 3))
(assert-equal {\S 10 \E 4} (change-waypoint {\E 10 \N 4} \R 90))

(assert-equal {:distances {\N 10 \E 100 \W 0 \S 0} :waypoint {\E 10 \N 1}} (navigate ["F10"])) 
(assert-equal {:distances {\N 10 \E 100 \W 0 \S 0} :waypoint {\E 10 \N 4}} (navigate ["F10" "N3"])) 
(assert-equal {:distances {\N 38 \E 170 \W 0 \S 0} :waypoint {\E 10 \N 4}} (navigate ["F10" "N3" "F7"])) 
(assert-equal {:distances {\N 38 \E 170 \W 0 \S 0} :waypoint {\S 10 \E 4}} (navigate ["F10" "N3" "F7" "R90"])) 
(assert-equal {:distances {\N 38 \E 214 \W 0 \S 110} :waypoint {\S 10 \E 4}} (navigate ["F10" "N3" "F7" "R90" "F11"])) 

(assert-equal {:distances {\N 0 \E 0 \W 0 \S 0} :waypoint {\S 10 \E 1 \N 4}} (navigate ["R90" "N2" "N2"])) 

(assert-equal 286 (manhattan-distance (:distances (navigate ["F10" "N3" "F7" "R90" "F11"]))))

(let [instructions (.split (slurp "input-day12.txt") "\n")]
  (println (manhattan-distance (:distances (navigate instructions)))))

