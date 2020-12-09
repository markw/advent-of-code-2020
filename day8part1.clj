(defn read-lines 
  [filename] 
  (vec (.split (slurp filename) "\n")))

(defn execute
  ([program] 
   (execute program 0 0 #{} []))
  ([program accum index indexes executed]
   ;;(println "acc" accum "indx" index indexes executed)
   (cond 
     (contains? indexes index)
     {:finished false :index index :accum accum :statements executed}

     (> index (count program))
     {:finished true :index index :accum accum :statements executed}

     :default
     (let [line (nth program index)
           tokens (.split line " ")
           op (first tokens)
           arg (Integer. (second tokens))
           executed (conj executed line)
           indexes (conj indexes index)]
       (condp = op
         "acc" (execute program (+ accum arg) (inc index) indexes executed)
         "jmp" (execute program accum (+ index arg) indexes executed)
         "nop" (execute program accum (inc index) indexes executed))))))

(println (execute (read-lines "input-day8.txt")))
  
