(defrecord Tile [id cells edges])

(def tile-size 10)

(defn rows
  [xs]
  (partition tile-size xs))

(defn print-tile
  [{:keys [id cells]}]
  (println "-----" id)
  (doseq [row (rows cells)]
    (doseq [col row]
      (print col))
    (println)))

(defn row
  [n xs]
  (take tile-size (drop (* n tile-size) xs)))

(defn col
  [n xs]
  (take-nth tile-size (drop n xs)))

(defn col-indices
  [xs n]
  (take-nth tile-size (drop n (range (count xs)))))

(defn rotate-cells
  [cells]
  (reduce
    (fn [acc [k v]]
      (assoc acc k v))
    cells
    (map vector
      (mapcat (partial col-indices cells) (range tile-size))
      (mapcat reverse (rows cells)))))

(defn edges
  [cells]
  {:top (row 0 cells)
   :left (col 0 cells)
   :right (col 9 cells)
   :bottom (row 9 cells)})

(defn rotate-tile
  [{:keys [id cells]}]
  (let [rotated (rotate-cells cells)]
    (->Tile id rotated (edges rotated))))

(defn flip-tile
  [{:keys [id cells]}]
  (let [flipped (flatten (reverse (rows cells)))]
    (->Tile id flipped (edges flipped))))

(defn parse-tile
  [input]
  (let [[h & t] (.split input "\n")
        id (Integer/parseInt (re-find #"\d+" h))
        cells (vec (mapcat seq t))]
    (->Tile id cells (edges cells))))

(defn all-positions
  [tile]
  (let [rotated (take 4 (iterate rotate-tile tile))]
     (concat rotated (map flip-tile rotated))))

(defn top-adjacent?
  [t0 t1]
  (= (-> t0 :edges :top)(-> t1 :edges :bottom)))

(defn bottom-adjacent?
  [t0 t1]
  (= (-> t0 :edges :bottom)(-> t1 :edges :top)))

(defn left-adjacent?
  [t0 t1]
  (= (-> t0 :edges :left)(-> t1 :edges :right)))

(defn right-adjacent?
  [t0 t1]
  (= (-> t0 :edges :right)(-> t1 :edges :left)))

(defn adjacent?
  [t0 t1]
  (or
    (top-adjacent? t0 t1)
    (bottom-adjacent? t0 t1)
    (left-adjacent? t0 t1)
    (right-adjacent? t0 t1)))

(defn adjacent-tiles
  [tile-map tile-id]
  (let [firstpos (first (get tile-map tile-id))
        all-others (flatten
                     (vals 
                       (remove 
                         (fn [[k v]]
                           (= k tile-id)) 
                         tile-map)))]
    (count (filter (partial adjacent? firstpos) all-others))))

(time
  (let [tile-defs (.split (slurp "input-day20.txt") "\n\n")
        tiles (map parse-tile tile-defs)
        tile-map
        (into {} (map 
                   (fn [tile] (vector (:id tile)(all-positions tile)))
                   tiles))]
    (time
      (println 
        (reduce *
          (filter
            #(= 2 (adjacent-tiles tile-map %))
            (keys tile-map))))))) 
