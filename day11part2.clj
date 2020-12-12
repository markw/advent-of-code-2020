(defn show-layout
  [layout]
  (println "----------")
  (doseq [row layout] 
    (println row)))

(defn layout
  [filename]
  (mapv vec (.split (slurp filename) "\n")))

(defn seat? [layout rc] (not= \. (get-in layout rc)))
(defn occupied? [seat] (= \# seat))
(defn empty-seat? [seat] (= \L seat))

(def add-rcs
  (fn [rc0 rc1]
    (mapv + rc0 rc1)))

(defn first-seat
  [layout rc offset]
  (first
    (filter
      (partial seat? layout)
      (take-while some? (drop 1 (iterate (partial add-rcs offset) rc)))))) 

(def offsets [[0 1][1 0][0 -1][-1 0][1 1][1 -1][-1 1][-1 -1]])

(defn first-seats
  [layout rc]
  (let [seats (map (partial first-seat layout rc) offsets)]
    (filterv some? (map (partial get-in layout) seats))))

(defn seat-with-neighbors
  [layout rc] 
  {:rc rc 
   :seat (get-in layout rc) 
   :neighbors (first-seats layout rc)})

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
              num-occupied-neighbors (fn [] (count (filter occupied? neighbors)))]
          ;;(println "seat=" seat "=L?" (= \L (:seat seat)) "=#?" (= \# (:seat seat)) "freqs" (frequencies (:neighbors seat)))
          (cond
            (and (empty-seat? seat-val)(= 0 (num-occupied-neighbors)))
            (recur (rest seats) (assoc-in new-layout (:rc seat) \#))

            (and (occupied? seat-val)(<= 5 (num-occupied-neighbors)))
            (recur (rest seats) (assoc-in new-layout (:rc seat) \L))
          
            :default (recur (rest seats) new-layout)))))))

(defn apply-rules-repeatedly
  [layout]
  (loop [layout0 layout counter 1]
    (println "applying rules..." counter)
    (let [layout1 (apply-rules layout0)]
      (if (= layout0 layout1)
        layout0
        (recur layout1 (inc counter))))))

(let [layout (layout "input-day11.txt")
      _ (println "thinking so hard...")
      final-layout (apply-rules-repeatedly layout)]
  (show-layout final-layout)
  (let [num-occupied (count (filter occupied? (flatten final-layout)))]
    (println num-occupied)
    (assert (= 2306 num-occupied))))
