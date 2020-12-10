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

(defn int-between?
  [x y s]
  (let [n (try (Integer. s)
               (catch Exception e 0))]
    (and (<= x n)
         (>= y n))))

(defn valid-height?
  [s]
  (let [matcher (re-matcher #"([0-9]{2,3}+)(cm|in)" s)]
    (if (.matches matcher)
      (condp = (.group matcher 2)
        "cm" (int-between? 150 193 (.group matcher 1))
        "in" (int-between?  59  76 (.group matcher 1))))))

(defn valid-passport?
  [passport]
  (and
    (int-between? 1920 2002 (get passport "byr"))
    (int-between? 2010 2020 (get passport "iyr"))
    (int-between? 2020 2030 (get passport "eyr"))
    (some #{(get passport "ecl")} ["amb","blu","brn","gry","grn","hzl","oth"])
    (re-matches #"#[0-9a-f]{6}+" (get passport "hcl" ""))
    (re-matches #"\d{9}+" (get passport "pid" ""))
    (valid-height? (get passport "hgt" ""))))
    
(let [passports
      (mapv to-map 
            (group (.split (slurp "input-day4.txt") "\n")))]
  (println (count (filter valid-passport? passports))))


