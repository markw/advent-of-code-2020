(def input [".#." 
            "..#"
            "###"])

(def input ["....#..."
            ".#..###."
            ".#.#.###"
            ".#....#."
            "...#.#.#"
            "#......."
            "##....#."
            ".##..#.#"])

(def size (count input))

(def neighbor-deltas (vec (for [x [-1 0 1] y [-1 0 1] z[-1 0 1] :when (not= 0 x y z)] (vector x y z))))

(defn between?
  [a b n]
  (and (<= a n)(<= n b)))

(defn neighbor-addresses
  [zyx]
  (map #(mapv + zyx %) neighbor-deltas))

;(prn (neighbor-addresses [1 1 1]))

(defn addresses-for-cycle
  [n size]
  (for [z (range (- n)(inc n)) 
        y (range (- n)(+ n size))
        x (range (- n)(+ n size))]
    (vector z y x)))

(defn num-active-neighbors
  [cubes zyx]
  (count 
    (filter 
      true?
      (map 
        (partial get-in cubes)
        (neighbor-addresses zyx)))))


(defn count-active
  ([cubes] (count-active cubes 0))
  ([cubes accum]
   (loop [cubes cubes accum accum] 
     (if (empty? cubes) 
       accum
       (let [[k v] (first cubes)]
         (if (map? v)
           (+ 
             (count-active v accum)
             (count-active (rest cubes) accum))
           (recur (rest cubes) 
                  (inc accum))))))))


;(defn traverse
;  ([cubes] (traverse cubes [] []))
;  ([cubes path accum]
;   (loop [cubes cubes path path accum accum] 
;     (if (empty? cubes) 
;       accum
;       (let [[k v] (first cubes)]
;         (if (map? v)
;           (concat 
;             (traverse v (conj path k) accum)
;             (traverse (rest cubes) path accum))
;           (recur (rest cubes) 
;                  path 
;                  (conj accum (vector (conj path k) v)))))))))
;
;(prn "answer:" (traverse initial-cubes))

;(prn (addresses-for-cycle 1 3))


(defn apply-rules
  [active? num-active-neighbors]
  (cond
    (and active? 
         (or (= 2 num-active-neighbors)
             (= 3 num-active-neighbors)))
    true
    
    (and (not active?)
         (= 3 num-active-neighbors))
    true

    :default false))


(defn apply-cycle
  [cubes n]
  (reduce
    (fn [acc zyx]
      (let [active? (get-in cubes zyx false)
            num-active-neighbors (num-active-neighbors cubes zyx)
            become-active? (apply-rules active? num-active-neighbors)]
        (if become-active?
          (assoc-in acc zyx true)
          acc)))
    {}
    (addresses-for-cycle n size)))


(def initial-cubes (reduce 
                     (fn [acc zyx] (assoc-in acc zyx true))
                     {}
                     (for [x (range size) 
                           y (range size)
                           :when (= \# (get-in input [y x]))]
                      (vector 0 y x))))

(println (count-active (reduce apply-cycle initial-cubes (range 1 7))))
