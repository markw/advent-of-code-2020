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

(defn fix-precedence
  "adjusts the parse tree to give higher precedence to addition. Takes trees
  in the form [[a * b] + c ] and returns [a * [b + c]]. Other forms are returned as is."
  [expr]
  (let [[t0 op t1] expr]
    (if (and 
          (coll? t0) 
          (= 3 (count t0)) 
          (= \+ op) 
          (= \* (t0 1)))
      (conj (pop t0) 
            (assoc expr 0 (last t0)))
      expr)))

(defn parse
  [s] 
  (loop [s s expr [] stack []]
    ;(prn "expr" expr)
    (if (= 3 (count expr))
      (recur s (vector (fix-precedence expr)) stack)
      (let [ch (token (first s))]
        (condp = ch
          nil (evaluate expr)
          \space (recur (rest s) expr stack)
          LPAREN (recur (rest s) [] (conj stack expr))
          RPAREN (recur (rest s) (conj (last stack) expr) (pop stack))
          (recur (rest s) (conj expr ch) stack))))))

;(fix-precedence [[2 \+ 3] \* 4])
;(fix-precedence [[2 \* 3] \+ 4])
;(fix-precedence [[[2 \* 3] \* 4] \+ 5])

;(parse "2 + 3 * 4")
;(parse "2 * 3 + 4")
;(parse "1 + 2 * 3 + 4 * 5 + 6")
;(parse "1 + (2 * 3) + (4 * (5 + 6))")
;(parse "2 * 3 + (4 * 5)")
;(parse "5 + (8 * 3 + 9 + 3 * 4 * 3)")
;(parse "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")
;(parse "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")

(def input (lines "input-day18.txt"))

(prn (reduce + (map parse input)))
