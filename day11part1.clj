(defn show-layout
  [layout]
  (println "----------")
  (doseq [row layout] 
    (println row)))

(defn layout
  [filename]
  (mapv vec (.split (slurp filename) "\n")))

(defn occupied-seat? [seat] (= \# seat))
(defn empty-seat? [seat] (= \L seat))

(def offsets [[0 1][1 0][0 -1][-1 0][1 1][1 -1][-1 1][-1 -1]])

(def neighbor-rcs
  (memoize 
    (fn [rc]
      (mapv (fn [a b] (mapv + a b)) 
            (repeat rc) 
            offsets))))

(defn neighbors
  [layout rc]
  (filter some? (map (partial get-in layout) 
                     (neighbor-rcs rc))))

(defn seat-with-neighbors
  [layout rc] 
  {:rc rc 
   :seat (get-in layout rc) 
   :neighbors (neighbors layout rc)})

(def layout-rcs
  (memoize
    (fn [num-rows num-cols]
      (for [r (range num-rows) c (range num-cols)] 
        [r c]))))

(defn apply-rules
  [layout]
  (let [num-rows (count layout)
        num-cols (count (first layout))
        rcs (layout-rcs num-rows num-cols)
        seats (mapv (partial seat-with-neighbors layout) rcs)]
    ;;(doseq [seat seats] (println seat))
    (loop [seats seats new-layout layout] 
      ;(println (count seats) "seats empty?" (empty? seats))
      (if (empty? seats)
        new-layout
        (let [seat (first seats)
              seat-val (:seat seat)
              neighbors (:neighbors seat)
              num-occupied-neighbors (fn [] (count (filter occupied-seat? neighbors)))]
          (cond
            (and (empty-seat? seat-val)(zero? (num-occupied-neighbors)))
            (recur (rest seats) (assoc-in new-layout (:rc seat) \#))

            (and (occupied-seat? seat-val)(<= 4 (num-occupied-neighbors)))
            (recur (rest seats) (assoc-in new-layout (:rc seat) \L))
          
            :default (recur (rest seats) new-layout)))))))

(defn apply-rules-repeatedly
  [layout]
  (loop [layout0 layout counter 1]
    (println "applying rules..." counter)
    (let [layout1 (apply-rules layout0)]
      ;;(show-layout layout1)
      (if (= layout0 layout1)
        layout0
        (recur layout1 (inc counter))))))

(let [layout (layout "input-day11.txt")
      final-layout (apply-rules-repeatedly layout)]
  (show-layout layout)
  (show-layout final-layout)
  (let [expected-num-occupied 2494
        num-occupied(count (filter occupied-seat? (flatten final-layout)))]
    (assert (= expected-num-occupied num-occupied)(str expected-num-occupied "!=" num-occupied))))
