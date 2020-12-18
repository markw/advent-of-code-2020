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

(def neighbor-deltas (vec 
                       (for [w [-1 0 1] 
                             x [-1 0 1] 
                             y [-1 0 1] 
                             z [-1 0 1] 
                             :when (not= 0 w x y z)] 
                         (vector w x y z))))

(defn neighbor-addresses
  [wzyx]
  (map #(mapv + wzyx %) neighbor-deltas))

;(prn (neighbor-addresses [1 1 1]))

(defn addresses-for-cycle
  [n size]
  (for [w (range (- n)(inc n)) 
        z (range (- n)(inc n)) 
        y (range (- n)(+ n size))
        x (range (- n)(+ n size))]
    (vector w z y x)))

(defn num-active-neighbors
  [cubes wzyx]
  (count 
    (filter 
      true?
      (map 
        (partial get-in cubes)
        (neighbor-addresses wzyx)))))


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
    (fn [acc wzyx]
      (let [active? (get-in cubes wzyx false)
            num-active-neighbors (num-active-neighbors cubes wzyx)
            become-active? (apply-rules active? num-active-neighbors)]
        (if become-active?
          (assoc-in acc wzyx true)
          acc)))
    {}
    (addresses-for-cycle n size)))


(def initial-cubes (reduce 
                     (fn [acc wzyx] (assoc-in acc wzyx true))
                     {}
                     (for [x (range size) 
                           y (range size)
                           :when (= \# (get-in input [y x]))]
                      (vector 0 0 y x))))

(println (count-active (reduce apply-cycle initial-cubes (range 1 7))))
