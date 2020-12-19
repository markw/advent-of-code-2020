(defn lines 
  [filename]
  (.split (slurp filename) "\n"))

(defn digit?
  [ch]
  (Character/isDigit ch))

(defn token
  [ch]
  (cond
    (nil? ch) ch
    (digit? ch) (Integer. (str ch))
    :default ch))

(def LPAREN \()
(def RPAREN \))

(defn evaluate
  [expr]
  (cond
    (number? expr) expr
    (= 1 (count expr)) (evaluate (first expr))
    :default
    (let [[t0 op t1] expr
          x (if (coll? t0) (evaluate t0) t0)
          y (if (coll? t1) (evaluate t1) t1)]
      (if (= \+ op)
        (+ x y)
        (* x y)))))

(defn parse
  [s] 
  (loop [s s expr [] stack []]
    ;(prn "expr" expr)
    (if (= 3 (count expr))
      (recur s (vector (evaluate expr)) stack)
      (let [ch (token (first s))]
        (condp = ch
          nil (evaluate expr)
          \space (recur (rest s) expr stack)
          LPAREN (recur (rest s) [] (conj stack expr))
          RPAREN (recur (rest s) (conj (last stack) expr) (pop stack))
          (recur (rest s) (conj expr ch) stack))))))

(def input (lines "input-day18.txt"))

;(def input ["1 + (2 * 3) + (4 * (5 + 6))"
;            "5 + (8 * 3 + 9 + 3 * 4 * 3)"
;            "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"
;            "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))))))

(reduce + (mapv parse input))



