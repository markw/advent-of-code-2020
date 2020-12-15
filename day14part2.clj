(defn parse-input
  [filename]
  (.split (slurp filename) "\n"))

(defn parse-instruction
  [s]
  (vec (.split s " = ")))

(defn mask?
  [s]
  (.startsWith s "mask"))

(defn mem-address
  [s]
  (Long. (re-find #"\d+" s)))

(defn pad-with-zero
  [s]
  (str (apply str (repeat (- 36 (count s)) "0")) s))

(defn to-binary
  [n]
  (pad-with-zero (Integer/toBinaryString n)))

(defn pow2
  [n]
  (if (zero? n) 1 (* 2 (pow2 (dec n)))))

(defn replace-floaters
  [mask replacements]
  (loop [m mask r replacements accum ""]
    (if (empty? m) 
      accum
      (let [m-char (first m)]
        (if (= \X m-char)
          (recur (rest m) (rest r)(str accum (first r)))
          (recur (rest m) r       (str accum m-char)))))))

(defn apply-mask
  [mask n]
  (let [applied 
        (reduce 
         (fn [acc [a b]]
           (condp = a
             \1 (str acc "1")
             \0 (str acc (str b))
             (str acc (str a))))
         ""
         (apply map vector [mask (to-binary n)]))

        x-count 
        (count (filter #(= \X %) applied))
        
        combinations (map
                      (comp (partial drop (- 36 x-count)) to-binary)
                      (range (pow2 x-count)))
        
        addresses (map 
                    replace-floaters 
                    (repeat (pow2 x-count) applied) 
                    combinations)]

    (mapv #(Long/parseLong % 2) addresses)))

(defn exec
  [state s]
  (let [[cmd arg] (parse-instruction s)]
    (if (mask? s)
      (assoc state :mask arg)
      (let [addresses (apply-mask (:mask state) (mem-address cmd))]
        (reduce 
          (fn [state address]
            (assoc-in state [:memory address] (Long. arg)))
          state
          addresses)))))

(defn solve
  [input]
  (let [state {:memory {} :mask ""}]
    (reduce + (vals (:memory (reduce exec state input))))))

;(def input ["mask = 000000000000000000000000000000X1001X"
;            "mem[42] = 100"
;            "mask = 00000000000000000000000000000000X0XX"
;            "mem[26] = 1")))

(def input (parse-input "input-day14.txt"))

(println (solve input))
