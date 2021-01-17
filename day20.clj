(defn rows
  [xs]
  (vec (partition (int (Math/sqrt (count xs))) xs)))

(defn print-matrix
  [a]
  (doseq [row (rows a)]
    (doseq [col row]
      (printf "%3s" col))
    (println)))

(defn row-size
  [xs]
  (int (Math/sqrt (count xs))))

(defn row
  [n xs]
  (let [size (row-size xs)]
    (take size (drop (* n size) xs))))

(defn col
  [n xs]
  (take-nth (row-size xs) (drop n xs)))

(defn col-indices
  [xs n]
  (let [indices (range (count xs))]
    (take-nth (row-size xs) (drop n indices))))

(defn rotate
  [xs]
  (let [size (row-size xs)]
    (reduce 
      (fn [acc [k v]]
        (assoc acc k v))
      xs
      (map vector
        (mapcat (partial col-indices xs) (range size))
        (mapcat reverse (rows xs))))))

(print-matrix (rotate (vec (range 100))))
