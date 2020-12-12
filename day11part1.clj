(defn show-layout
  [layout]
  (println "----------")
  (doseq [row layout] 
    (println row)))

(defn layout
  [filename]
  (mapv vec (.split (slurp filename) "\n")))

(defn occupied? [seat] (= \# seat))

(defn neighbors
  [layout rc]
  ;;(println rc)
  (let [offsets [[0 1][1 0][0 -1][-1 0][1 1][1 -1][-1 1][-1 -1]]
        rcs (mapv (fn [a b] (mapv + a b)) (repeat rc) offsets)]
    (filter some? (map (partial get-in layout) rcs))))

(defn seat-with-neighbors
  [layout rc] 
  {:rc rc 
   :seat (get-in layout rc) 
   :neighbors (neighbors layout rc)})

(defn apply-rules
  [layout]
  (let [num-rows (count layout)
        num-cols (count (first layout))
        rcs (for [r (range num-rows) c (range num-cols)] [r c])
        seats (mapv (partial seat-with-neighbors layout) rcs)]
    ;;(doseq [seat seats] (println seat))
    (loop [seats seats new-layout layout] 
      ;(println (count seats) "seats empty?" (empty? seats))
      (if (empty? seats)
        new-layout
        (let [seat (first seats)
              seat-val (:seat seat)
              neighbors (:neighbors seat)
              num-occupied-neighbors (get (frequencies neighbors) \# 0)]
          ;;(println "seat=" seat "=L?" (= \L (:seat seat)) "=#?" (= \# (:seat seat)) "freqs" (frequencies (:neighbors seat)))
          (cond
            (and (= \L seat-val)(= 0 num-occupied-neighbors))
            (recur (rest seats) (assoc-in new-layout (:rc seat) \#))

            (and (= \# seat-val)(<= 4 num-occupied-neighbors))
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
  (println (count (filter occupied? (flatten final-layout)))))
