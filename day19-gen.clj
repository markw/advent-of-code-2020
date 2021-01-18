(set! *warn-on-reflection* true)
(defrecord Rule [id ch subrules])

(defn parse-int
  [s]
  (Integer/parseInt s))

(defn ->rule
  [id ch subrules]
  (->Rule (parse-int id) ch subrules))

(def ch-rule-template "(f%s [^String [h & t :as s]] (when (= \\%c h) (list t)))\n")

(def rule-template "(f%s [^String s] %s)\n")

(defn rule-fn
  [{:keys [id ch subrules]}]
  (if ch
    (format ch-rule-template id ch)
    (let [rs (map
               (fn [[h & t]]
                 (let [mapcats (apply str (map #(str "(mapcat f" % ")") t))]
                  (format "(->> s f%s %s)" h mapcats)))
               subrules)]
      (if (= 1 (count rs))
        (format rule-template id (apply str rs))
        (format rule-template id (str "(concat " (apply str rs)")"))))))

(def rules-template "
(defmacro trace
  [fname v expr]
  `(let [_# (println ~fname ~v \"<-\")
         r# ~expr]
     (println ~fname ~v \"->\" r#)
     r#))

(letfn [
  (validate [^String s] (some empty? (f0 s)))
  %s]
  (println (time (count (filter validate (list %s))))))")

(defn tokenize
  [^String s]
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
  [^String s]
  (let [[id ss](.split s ": ")]
    (case ss
      "\"a\"" (->rule id \a nil)
      "\"b\"" (->rule id \b nil)
      (->rule id nil (parse-subrules ss)))))

(defn parse-rules
  [rules]
  (sort-by :id (mapv parse-rule rules)))

(defn lines
  [filename]
  (.split (slurp filename) "\n"))

(defn rule?
  [^String s]
  (.contains s ":"))

(defn message?
  [[c]]
  (or (= \a c)(= \b c)))


;;(def rules
;;  ["0: 1 2"
;;   "1: 4 | 4 1"
;;   "2: 5 4 | 4 5"
;;   "4: \"a\""
;;   "5: \"b\""])
;;
;;(def messages ["aaaaab"])

;;(spit "rules.clj" (format rules-template 
;;                          (apply str (map rule-fn (parse-rules rules)))
;;                          (apply str (map prn-str messages))))

(let [input (lines "./input-day19.txt")
      rules (parse-rules (filter rule? input))
      messages (filter message? input)]
  (spit "rules.clj" (format rules-template 
                            (apply str (map rule-fn rules))
                            (apply str (map prn-str messages)))))

(load-file "rules.clj")

(let [input (lines "./input-day19.txt")
      rules (mapv
              (fn [rule]
                (condp = (:id rule)
                  8  (->Rule 8 nil [[42][42 8]])
                  11 (->Rule 11 nil [[42 31][42 11 31]])
                  rule))
              (parse-rules (filter rule? input)))
      messages (filter message? input)]
  (spit "rules.clj" (format rules-template 
                            (apply str (map rule-fn rules))
                            (apply str (map prn-str messages)))))
(load-file "rules.clj")

;;(defmacro debug
;;  [name v expr]
;;  `(let [_# (println ~name ~v "<-")
;;         r# ~expr]
;;     (println ~name ~v "->" r#)
;;     r#))
