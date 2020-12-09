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
  
(defn num-contained-bags
  [color number bags]
  ;;(println (str color number))
  (if (empty? bags)
    number
    (let [bag (first 
                (filter 
                  #(= color (:color %1)) 
                  bags))]
      (reduce + 
              (map 
                (fn [bag]
                  (+ (:count bag)
                     (* (:count bag)
                        (num-contained-bags (:color bag)(:count bag) bags)))) 
                (:contained bag)))))) 

(let [bags (map line-to-bag (lines "input-day7.txt"))]
  (println (num-contained-bags "shiny gold" 0 bags)))
