(defn assert-equal
  [expected actual]
  (assert (= expected actual) (str "expected: " expected " actual: " actual)))

(defn between?
  [[a b] n]
  (and (<= a n)(<= n b)))

(defn parse-range
  [s]
  (mapv #(Integer. %)(.split s "-")))

(defn parse-ranges
  [ss]
  (mapv parse-range ss))

(defn validator-fn
  [[r0 r1]]
  (fn [n]
    (or (between? r0 n)
        (between? r1 n))))

(defn s-to-validator
  [s]
  (let [[f-name, f-ranges] (.split s ":")
        fn (-> f-ranges
            (.trim)
            (.split " ")
            ((juxt first last))
            (parse-ranges)
            (validator-fn))]
    [f-name fn]))

(defn invalid-for-all?
  [validators n]
  (every? false? (validators n)))

(defn valid-for-all?
  [validators n]
  (every? true? (validators n)))

(defn valid-ticket?
  [validators ticket]
  (zero? (count (filter (partial invalid-for-all? validators) ticket))))

(defn valid-tickets
  [validators tickets]
  (filter (partial valid-ticket? validators) tickets))

(defn field-position-candidates
  [field-validators valid-tickets num-fields]
  (reduce
    (fn [m ndx]
      (let [field-vals (map #(nth % ndx) valid-tickets)]
        (loop [validators field-validators field-names #{}]
          (let [[f-name fn] (first validators)]
            (cond
              (empty? validators) (assoc m ndx field-names) 
          
              (every? true? (map fn field-vals))
              (recur (rest validators)(conj field-names f-name))
              
              :default
              (recur (rest validators) field-names))))))
    {}
    (range num-fields)))

(defn field-positions
  [candidates]
  (loop [candidates candidates positions {}]
    (let [has-one? (fn [[k v]] (= 1 (count v)))
          one (first (filter has-one? candidates))]
      (if (nil? one)
        positions
        (recur (reduce-kv 
                 (fn [m k v] 
                   (assoc m k (set (remove (val one) v)))) 
                 {} 
                 candidates)
               (assoc positions (first (val one))(key one)))))))

(let [input (.split (slurp "input-day16.txt") "\n")
      field-input (filter #(.contains % " or ") input)
      ticket-input (filter #(.contains % ",") input)
      tickets (map (fn [s]
                     (map #(Integer. %) (.split s ","))) 
                   ticket-input)
      your-ticket (first tickets)
      nearby-tickets (rest tickets)
      field-validators (into {} (map s-to-validator field-input))
      validators (apply juxt (vals field-validators))
      valid-nearby-tickets (valid-tickets validators nearby-tickets)
      field-positions (field-positions 
                       (field-position-candidates 
                         field-validators 
                         valid-nearby-tickets 
                         (count field-input)))
      departure-indexes (map second
                         (filter 
                           (fn [[k v]] (.startsWith k "departure"))
                           field-positions))]

    (assert-equal 622670335901
                  (reduce * 
                          (map #(nth your-ticket %) 
                               departure-indexes))))
