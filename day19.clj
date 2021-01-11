(defrecord Rule [ch subrules])

(defn parse-int
  [s]
  (Integer/parseInt s))

(defn tokenize
  [s]
  (filterv seq (.split s " ")))

(defn parse-subrules
  [s]
  (loop [subrule [] subrules [] [h & t] (tokenize s)]
    (if (nil? h)
      (conj subrules subrule)
      (if (= "|" h)
        (recur [] (conj subrules subrule) t)
        (recur (conj subrule (parse-int h)) subrules t)))))

(defn parse-rule
  [s]
  (let [[id ss](.split s ": ")]
    (vector (parse-int id)
            (case ss
              "\"a\"" (->Rule \a nil)
              "\"b\"" (->Rule \b nil)
              (->Rule nil (parse-subrules ss))))))

(defn parse-rules
  [rules]
  (into {} (mapv parse-rule rules)))

(def rules-input
  ["0: 4 1 5"
   "1: 2 3 | 3 2"
   "2: 4 4 | 5 5"
   "3: 4 5 | 5 4"
   "4: \"a\""
   "5: \"b\""])

(defn match-start
  [rules {:keys [ch subrules] :as rule} [h & t :as s]]
  ;(println "ch=" ch "subrules=" subrules "h=" h "t=" t)
  (if (some? ch)
    (when (= ch h) [t])
    (mapcat
      (fn [subrule]
        ;(println "reduce" [s] subrule)
        (reduce
          (fn [acc rule-id]
            ;(println rule-id acc)
            (if (empty? acc)
              (reduced '())
              (let [rule (get rules rule-id)]
                (mapcat
                  (partial match-start rules rule)
                  acc))))
          [s]
          subrule))
      subrules)))

(defn matches?
  [rules rule s]
  (let [result (match-start rules rule s)]
    (spit "results.txt" (format "%6s %s\n" (boolean (some nil? result)) s) :append true)
    (some nil? result)))

(defn assert-matches
  [rules rule s]
  (assert (matches? rules rule s)(str s " did not match."))
  "OK")

;;(let [rules (parse-rules rules-input)
;;      rule (get rules 0)]
;;  (assert-matches rules rule "ababbb")
;;  (assert-matches rules rule "aaabab")
;;  (assert-matches rules rule "abbbab")
;;  (assert-matches rules rule "aaaabb")
;;  (assert-matches rules rule "abbbab"))

(defn lines
  [filename]
  (.split (slurp filename) "\n"))

(defn rule?
  [s]
  (.contains s ":"))

(defn message?
  [[c]]
  (or (= \a c)(= \b c)))

(time
  (let [input (lines "./input-day19.txt")
        rules (parse-rules (filter rule? input))
        messages (filter message? input)]
      (println 
        "part 1"
        (count
          (filter
            (partial matches? rules (get rules 0))
            messages)))))

(time
 (let [input (lines "./input-day19.txt")
       rules (reduce 
               (fn [m [k v]] (assoc m k v))
               (parse-rules (filter rule? input))
               (list 
                 (vector 8 (->Rule nil [[42][42 8]]))  
                 (vector 11 (->Rule nil [[42 31][42 11 31]]))))
       messages (filter message? input)]
     (println 
       "part 2"
       (count
         (filter
           (partial matches? rules (get rules 0))
           messages)))))
