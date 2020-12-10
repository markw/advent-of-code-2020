(defn group [strs]
  (loop [ss strs current [] groups []]
    (let [s (first ss)]
      (if (empty? ss)
        (conj groups current)
        (if (empty? s)
          (recur (rest ss) [] (conj groups current))
          (recur (rest ss) (conj current s) groups))))))

(defn to-map
  [ss]
  (into {}
    (mapv 
      (fn [s] (into [] (.split s ":")))
      (.split (String/join " " ss) " "))))

(defn valid-passport?
  [passport]
  (let [missing-keys (filter
                       (complement (partial contains? passport)) 
                       ["byr","iyr","eyr","hgt","hcl","ecl","pid","cid"])]
    (or (empty? missing-keys)(= ["cid"] missing-keys))))
    
(let [passports
      (mapv to-map 
            (group (.split (slurp "input-day4.txt") "\n")))]
  (println (count (filter valid-passport? passports))))
