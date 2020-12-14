;; application of Chinese Remainder Theorem described at
;; https://www.dave4math.com/mathematics/chinese-remainder-theorem/
;;
;; I use the arbitrary variable names h,i,j,k because the variables name on the
;; above website don't seem to match the typical equation form ax = b (mod m)
;; that I see everywhere (although here a is always 1).  Here's how they are
;; calulated for the following example system of congruences:
;;
;;  0 (mod 17)
;; -2 (mod 13)
;; -3 (mod 19)
;;
;;  h    i   j                  k                           (i)(j)(k)
;;  17   0   4199 / 17 = 247    247 k = 1 (mod 17), k = 2       0
;;  13  -2   4199 / 13 = 323    323 k = 1 (mod 13), k = 6   -3876
;;  19  -3   4199 / 19 = 221    221 k = 1 (mod 19), k = 8   -5304
;;                                                          -----
;;                                                          -9180
;;
;; so x = -9180 (mod (17)(13)(19))
;;      = -9180 (mod 4199)
;;
;; the first positive integer that satisfies the resulting congruence = (3)(4199) - 9180 = 3417.
;; 

(defn parse-int 
  [s] 
  (if (= "x" s) s (Integer. s)))

(defn congruences
  "parse congruences from string representation"
  [s]
  (println s)
  (filter 
    (comp number? first)
    (map
      (fn [s n] [(parse-int s) (- n)])
      (.split s ",")
      (range))))

(defn h
  [congruences]
  (map first congruences))

(defn i
  [congruences]
  (map second congruences))

(defn j
  [congruences]
  (let [m (reduce * (h congruences))]
    (map #(/ m %) (map first congruences))))

(defn k
  [congruences]
  (map
    (fn [h j]
      ;(println "h" h "j" j)
      (first 
        (filter 
          (fn [n]
            (= 1 (mod (* n j) h)))
          (range))))
    (h congruences)
    (j congruences)))

(defn b
  [is js ks]
  (reduce + 
          (map 
            (fn [ijk]
              (reduce * ijk))
            (apply map vector [is js ks]))))

(defn first-positive
  [b m]
  (println "b" b "m" m)
  (first 
    (drop-while 
      (partial > 0)
      (map 
        (fn [n]
          (+ b (* n m)))
        (range)))))

(defn solve
  [input]
  (let [[h i j k] ((juxt h i j k) (congruences input))]
    (println "h" h)
    (println "i" i)
    (println "j" j)
    (println "k" k)
    (first-positive 
      (b i j k)
      (reduce * h))))

;(println (solve "17,x,13,19"))
;(println (solve "1789,37,47,1889"))
(println (solve "17,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,983,x,29,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,19,x,x,x,23,x,x,x,x,x,x,x,397,x,x,x,x,x,37,x,x,x,x,x,x,13"))

      
