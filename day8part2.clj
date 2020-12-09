(defn read-lines 
  [filename] 
  (vec (.split (slurp filename) "\n")))

(defn execute
  ([program] 
   (execute program 0 0 #{} []))
  ([program accum index indexes executed]
   (cond 
     (contains? indexes index)  {:finished false :index index :accum accum :statements executed}
     (>= index (count program)) {:finished true  :index index :accum accum :statements executed}
     :default
     (let [line (nth program index)
           tokens (.split line " ")
           arg (Integer. (second tokens))
           executed (conj executed line)
           indexes (conj indexes index)]
       (condp = (first tokens)
         "acc" (execute program (+ accum arg) (inc index) indexes executed)
         "jmp" (execute program accum (+ index arg) indexes executed)
         "nop" (execute program accum (inc index) indexes executed))))))

(defn replace-first-jmp-or-nop
  [program start]
  (loop [n (inc start)]
    (let [line (nth program n)
          subst (fn [old new] {:index n :program (assoc program n (.replace line old new))})]
      (condp = (.substring line 0 3)
        "jmp" (subst "jmp" "nop")
        "nop" (subst "nop" "jmp")
        (recur (inc n))))))

(let [program (read-lines "input-day8.txt")
      ctx {:index -1 :program program}]
  (loop [ctx ctx]
    (let [result (execute (:program ctx))]
      ;;(println p result)
      (if (:finished result)
        (println "accum =" (:accum result))
        (recur (replace-first-jmp-or-nop program (:index ctx)))))))
