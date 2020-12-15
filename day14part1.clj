(def input ["mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X"
            "mem[8] = 11"
            "mem[7] = 101"
            "mem[8] = 0"])

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
  (re-find #"\d+" s))

(defn pad-with-zero
  [s]
  (str (apply str (repeat (- 36 (count s)) "0")) s))

(defn to-binary
  [n]
  (pad-with-zero (Integer/toBinaryString n)))

(defn apply-mask
  [mask n]
  (let [applied (reduce 
                 (fn [acc [a b]]
                   (condp = a
                     \1 (str acc "1")
                     \0 (str acc "0")
                     (str acc (str b))))
                 ""
                 (apply map vector [mask (to-binary n)]))]
    (Long/parseLong applied 2)))
    

(defn exec
  [state s]
  (let [[cmd arg] (parse-instruction s)]
    (if (mask? s)
      (assoc state :mask arg)
      (assoc-in state [:memory (mem-address cmd)] (apply-mask (:mask state) (Long. arg))))))

(defn solve
  [input]
  (let [state {:memory {} :mask ""}]
    (reduce + (vals (:memory (reduce exec state input))))))

(def input (parse-input "input-day14.txt"))
(println (solve input))
