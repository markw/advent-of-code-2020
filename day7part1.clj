(defn tokenize [line] (.split line " "))

(defn lines [filename] (.split (slurp filename) "\n"))

(defn bag
  [color cnt contained]
  {:color color :count cnt :contained contained})

(defn make-color [tokens index]
   (str (nth tokens index) " " (nth tokens (inc index))))

(defn make-contained-bags
  [tokens]
  (loop [tokens tokens bags []]
    (if (empty? tokens)
      bags
      (let [color (make-color tokens 1)
            number (Integer/parseInt (nth tokens 0))]
        (recur (drop 4 tokens)
               (conj bags (bag color number [])))))))

(defn line-to-bag
  [line]
  (let [tokens (tokenize line)
        color (make-color tokens 0)
        s (nth tokens 4)
        cnt (if (= "no" s) 0 (Integer/parseInt s))
        contained (if (zero? cnt) [] (make-contained-bags (drop 4 tokens)))]
      (bag color cnt contained)))

(defn find-bag-in-bags-map
  [bags-map bag]
  ((keyword (:color bag)) bags-map))

(defn contains-shiny-gold?
  [bags-map bag]
  (if (some #{"shiny gold"} (map :color (:contained bag)))
    bag
    (let [contained-bags (map (partial find-bag-in-bags-map bags-map) (:contained bag))]
      (first (filter (partial contains-shiny-gold? bags-map) contained-bags)))))
  


(let [bags (mapv line-to-bag (lines "input-day7.txt"))
      bags-map (into {} (mapv 
                          (fn [bag] [(keyword (:color bag)) bag])
                          bags))]
  (println (count (filter (partial contains-shiny-gold? bags-map) bags))))
