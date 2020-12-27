(defn lines
  [filename]
  (.split (slurp filename) "\n"))

(defn rule?
  [s]
  (.contains s ":"))

(defn message?
  [s]
  (or (.startsWith s "a")
      (.startsWith s "b")))

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

(defn add-tree
  [t t']
  (if (empty? t)
    t'
    (clojure.walk/walk
      (fn f [[k v]]
        (if (empty? v)
          [k t']
          [k (clojure.walk/walk f identity v)]))
      identity
      t)))

(defn deep-merge-with
  [f & maps]
  (apply
    (fn m [& maps]
      (if (every? map? maps)
        (apply merge-with m maps)
        (apply f maps)))
    maps))

(defn mapify
  [s]
  ;(prn "mapify" s)
  (loop [tokens s tree {} stack [] branches []]
    (if (empty? tokens)
      tree
      (let [ch (first tokens)]
        ;(println "ch" ch "branches" branches "stack" stack "tree" tree)
        (condp = ch
          \( (recur (rest tokens) {} (conj stack {:tree tree :branches branches}) [])

          \) (recur (rest tokens)
                    (add-tree
                      (:tree (peek stack))
                      (apply deep-merge-with conj (conj branches tree)))
                    (pop stack)
                    (:branches (peek stack)))

          \| (recur (rest tokens) {} stack (conj branches tree))

          (let [k (keyword (str ch))]
            (recur (rest tokens)
                   (add-tree tree {k {}})
                   stack
                   branches)))))))

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

(time
  (let [input (lines "input-day19.txt")
        rules (filter rule? input)
        rule-map (into {} (map parse-rule rules))
        messages (filter message? input)
        rule0 (expand-rule rule-map "0")]
    (prn
      (count
        (filter
          (partial matches? (mapify rule0))
          messages)))))

(comment "tests------------------------------------------------")

(defn assert-equal
  [expected actual]
  (assert (= expected actual) (str "expected: " expected " actual: " actual)))

(defn assert-matches
  [rule s]
  (assert (matches? rule s) s))

(defn assert-not-matches
  [rule s]
  (assert (not (matches? rule s)) s))

(let [rule (mapify "(ab|ba)")]
  (assert-matches rule "ab")
  (assert-matches rule "ba")
  (assert-not-matches rule "bb")
  (assert-not-matches rule "aa"))

(let [rule (mapify "((ab|ba)(aa|bb))")]
  (assert-matches rule "abaa")
  (assert-matches rule "abbb")
  (assert-matches rule "baaa")
  (assert-matches rule "babb")
  (assert-not-matches rule "aaaa")
  (assert-not-matches rule "aabb")
  (assert-not-matches rule "abab")
  (assert-not-matches rule "abba"))

(let [rule (mapify "(a(ab|ba)(aa|bb)b)")]
  (assert-matches rule "aabaab")
  (assert-matches rule "aabbbb")
  (assert-matches rule "abaaab")
  (assert-matches rule "ababbb")
  (assert-not-matches rule "aaaaab")
  (assert-not-matches rule "aaabbb")
  (assert-not-matches rule "aababb")
  (assert-not-matches rule "aabbab"))

(let [rule (mapify "(a(ab|ba)")]
  (assert-matches rule "aab")
  (assert-matches rule "aba")
  (assert-not-matches rule "aaa")
  (assert-not-matches rule "bab")
  (assert-not-matches rule "bba")
  (assert-not-matches rule "abb"))

(let [rule (mapify "(a(ab|ba)|b(aa|bb))")]
  (assert-matches rule "aab")
  (assert-matches rule "bbb")
  (assert-matches rule "aba")
  (assert-matches rule "baa")
  (assert-matches rule "bbb")
  (assert-not-matches rule "aaa")
  (assert-not-matches rule "bab")
  (assert-not-matches rule "bba")
  (assert-not-matches rule "abb"))

(let [rule (mapify "(a(bb|(b|a)a)a)")]
  (assert-matches rule "abba")
  (assert-matches rule "abaa")
  (assert-matches rule "aaaa")
  (assert-not-matches rule "abbb"))

(let [rule (mapify "((a|b)(ab))")]
  (assert-matches rule "aab")
  (assert-matches rule "bab")
  (assert-not-matches rule "aaa")
  (assert-not-matches rule "ab"))

(let [rule (mapify "((ab)b|(ba|ab)a)")]
  (assert-matches rule  "aba")
  (assert-matches rule  "baa")
  (assert-matches rule  "abb")
  (assert-not-matches rule "bab")
  (assert-not-matches rule "aab"))

(let [rule (mapify "(b((ab)b|(ba|ab)a))")]
  (assert-matches rule  "baba")
  (assert-matches rule  "bbaa")
  (assert-matches rule  "babb"))

(let [rule (mapify "(a((ab|aa)a|(ba)b)|b((ab)b|(ba|ab)a))")]
  (assert-matches rule  "abab")
  (assert-matches rule  "aaba")
  (assert-matches rule  "baba")
  (assert-matches rule  "babb"))

(let [input (lines "input-day19-sample.txt")
      rules (filter rule? input)
      messages (filter message? input)
      rule-map (into {} (map parse-rule rules))
      rule0 (mapify (expand-rule rule-map "0"))
      rule1 (mapify (expand-rule rule-map "1"))]

  (assert-matches rule1 "aaab")
  (assert-matches rule1 "aaba")
  (assert-matches rule1 "bbab")
  (assert-matches rule1 "bbba")
  (assert-matches rule1 "abaa")
  (assert-matches rule1 "abbb")
  (assert-matches rule1 "baaa")
  (assert-matches rule1 "babb")

  (assert-matches rule0 "ababbb")
  (assert-matches rule0 "abbbab")
  (assert-not-matches rule0 "aaabbb")
  (assert-not-matches rule0 "bababa")
  (assert-not-matches rule0 "aaaabbb"))

(deep-merge-with conj {:a {:b {:a {}}}, :b {:a {:a {}}}} {:a {:b {:b {}}}})
