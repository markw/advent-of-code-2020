(defn lines
  [filename]
  (.split (slurp filename) "\n"))

(defn rule?
  [s]
  (.contains s ":"))

(defn message?
  [s]
  (let [ch (first s)]
    (or (= \a ch)(= \b ch))))

(defn parse-rule
  [rule]
  (let [[k s] (.split rule ":")]
    (condp = (.trim s)
      "\"a\"" [k "a"]
      "\"b\"" [k "b"]
      (vector k (.trim s)))))

(defn tokenize-rule
  [s]
  (vec (filter seq (.split s " "))))


(defn add-leaf
  [m leaf]
  (if (empty? m)
    leaf
    (reduce-kv
      (fn f [m k v]
        (if (nil? v)
          (assoc m k leaf)
          (assoc m k (reduce-kv f {} v))))
      {}
      m)))

(defn add-letter
  [tree letter]
  (add-leaf tree {letter nil}))

(defn deep-merge-with
  [f & maps]
  (apply
    (fn deep-merge [& maps]
      (if (every? map? maps)
        (apply merge-with deep-merge maps)
        (apply f maps)))
    maps))

(defn merge-maps
  [& maps]
  (apply deep-merge-with conj maps))

(defn build-rule
  [rules id]
  (loop [[t & ts] (tokenize-rule (get rules id))
         tree {}
         other {}]
    ;(when (nil? t)
    ;  (prn "id=" id "t=" t "tree" tree "other" other)
    (case t
     nil (merge-maps tree other)
     "a" (recur ts (add-letter tree :a) other)
     "b" (recur ts (add-letter tree :b) other)
     "|" (recur ts {} tree)
     (recur ts (add-leaf tree (build-rule rules t)) other))))

(defn expand-rule
  [rules id]
  (if (= "|" id)
    id
    (let [rule (get rules id)]
      (condp = rule
        "a" rule
        "b" rule
        (str "("
             (apply str (mapv (partial expand-rule rules) (tokenize-rule rule)))
             ")")))))

(defn matches?
  [m s]
  (cond
    (empty? m)(empty? s)
    (empty? s)(empty? m)
    :default
    (let [k (keyword (str (first s)))]
      (if (contains? m k)
        (recur (k m)(rest s))
        false))))

(defn matches-start?
  [m s]
  (loop [m m s s acc 0]
    (if (empty? m)
      acc
      (let [k (keyword (str (first s)))]
        (when (contains? m k)
          (recur (k m)(rest s)(inc acc)))))))

(defn print-rule
  ([rule] (print-rule rule ""))
  ([rule prefix]
   (when (some? rule)
    (apply
      str
      (concat
        (str prefix (:value rule) "\n")
        (print-rule (first  (:children rule))(str prefix "-"))
        (print-rule (second (:children rule))(str prefix "-")))))))

(defn walk2
  [f node]
  (apply vector (f node)(mapv #(walk2 f %)(:children node))))

;(expand-rule rule-map "112")
;(println (print-rule (build-rule rule-map "44a")))
;(println (print-rule (build-rule rule-map "112")))

;(defn print-value
;  [node]
;  (let [v (:value node)]
;    (if (keyword? v) v (keyword (str v)))))

;(walk2 print-value (build-rule rule-map "112"))
;(walk2 print-value (build-rule rule-map "44a"))

;(expand-rule rule-map "44")
;(expand-rule rule-map "77")
;(expand-rule rule-map "5")
;(expand-rule rule-map "112")
;(expand-rule rule-map "72")


(defn matches-rule?
  [rule word]
  (loop [s word acc 0]
    (if (empty? s)
      acc
      (if-let [len (matches-start? rule s)]
        (recur (subs s len)(+ acc len))
        acc))))

(defn matches-rule0?
  "the hacky solution that is reasonably fast, returns the correct answer,  but is not generalized"
  [rule42 rule31 word]
  (let [len42 (matches-rule? rule42 word)
        len31 (matches-rule? rule31 (subs word len42))
        result (and
                 (= (count word)(+ len42 len31))
                 (> len42 0)
                 (> len31 0)
                 (> len42 len31))]
    (printf "%2d %2d %2d %5s %s\n" (count word) len42 len31 result word)
    result))

(time
  (let [input (lines "input-day19.txt")
        messages (filter message? input)
        rules (filter rule? input)
        rule-map (into {} (map parse-rule rules))
        rule42 (build-rule rule-map "42")
        rule31 (build-rule rule-map "31")]

    (time
      (println "matching words" 
        (count
          (filter
            (partial matches-rule0? rule42 rule31)
            messages))))))

(comment "-------------------------Tests-------------------------")

(def rule-map
  (let [input (lines "input-day19.txt")
        rules (filter rule? input)]
    (into {} (map parse-rule rules))))

(defn assert-matches
  [rule s]
  (assert (matches? rule s) s))

(defn assert-not-matches
  [rule s]
  (assert (not (matches? rule s)) s))

(let [rule (build-rule rule-map "44")]
  (assert-matches rule "bba")
  (assert-matches rule "baa")
  (assert-matches rule "abb")
  (assert-matches rule "aab")
  (assert-not-matches rule "aaa")
  (assert-not-matches rule "bbb")
  (assert-not-matches rule "bbaa"))

(let [rule (build-rule rule-map "118")]
  (assert-matches rule "bbb")
  (assert-matches rule "bba")
  (assert-matches rule "aab")
  (assert-not-matches rule "aaa")
  (assert-not-matches rule "bab")
  (assert-not-matches rule "bbaa"))

;(let [rule (build-rule rule-map "0")]
;  (assert-matches rule "aaaaaaaaabbbaaaaabaaabab")
;  (assert-matches rule "aaaaabababaabaaaabaaaabb")
;  (assert-matches rule "aaaaabbbbabbaabaabaabbaa")
;  (assert-matches rule "aaaaabbbbabbbbabbaaaaaba"))
