(ns simple-parser.core
  (:gen-class))

(use 'clojure.pprint)

(def sentence1 '("the" "dog" "bit" "John" "."))
(def sentence2 '("Andie" "saw" "Steve" "."))
(def sentence3 '("the" "doctor" "sent" "for" "the" "patient" "arrived" "."))

(def lexicon1
  {"the" '((NP / N))
   "dog" '((N))
   "bit" '(((S \\ NP) / NP))
   "John" '((NP))})

(def lexicon2
  {"Andie" '((NP) (S / (S \\ NP)))
   "saw" '((N) ((S \\ NP) / NP))
   "Steve" '((NP) (S / (S \\ NP)))})

(def lexicon2vision
  {"Andie" '((NP) (S / (S \\ NP)))
   "saw" '((N))
   "Steve" '((NP) (S / (S \\ NP)))})

(def lexicon3
  {"the" '((NP / N) ((S / (S \\ NP)) / N))
   "doctor" '((N) (N / (N \\ N)))
   "flowers" '((N) (N / (N \\ N)))
   "patient" '((N) (N / (N \\ N)))
   "sent" '(((S \\ NP) / PP) ((N \\ N) / PP))
   "for" '((PP / NP))
   "arrived" '((S \\ NP))})

(defn getLexicon
  [number vision]
  (if (and (= vision 1) (= number 2))
    lexicon2vision
    (cond
     (= number 1) lexicon1
     (= number 2) lexicon2
     (= number 3) lexicon3)))

(defn getSentence
  [number]
  (cond
     (= number 1) sentence1
     (= number 2) sentence2
     (= number 3) sentence3))

(defn complex?
  [lexical]
  (= (count lexical) 3))

(defn forward?
  [lexical]
  (and (complex? lexical) (= (second lexical) '/)))

(defn backward?
  [lexical]
  (and (complex? lexical) (= (second lexical) \\)))

(defn s?
  [stack]
  (= stack '((S))))

(defn apply-ac-f
  [alpha beta]
  (let [result (first alpha)]
    (if (list? result)
      result
      (list result))))

(defn ac-f?
  [alpha beta]
  (and (complex? alpha) (forward? alpha) (= (last alpha) (last beta))))

(defn apply-ac-b
  [beta alpha]
  (let [result (first alpha)]
    (if (list? result)
      result
      (list result))))

(defn ac-b?
  [beta alpha]
  (and (complex? alpha) (backward? alpha) (= (last alpha) (last beta))))

(defn apply-cc-f
  [alpha beta]
  (list (first alpha) '/ (last beta)))

(defn cc-f?
  [alpha beta]
  (and (complex? alpha) (forward? alpha) (= (last alpha) (first beta))))

(defn apply-cc-b
  [beta alpha]
  (list (first alpha) \\ (last beta)))

(defn cc-b?
  [beta alpha]
  (and (complex? alpha) (backward? alpha) (= (last alpha) (first beta))))

(defn update-stacks
  [workspace input]
   (for [stack workspace
        entry input]
     (cons entry stack)))

(defn reduce-stack
  [stack]
  (let [beta (first stack)
        alpha (second stack)]
    (if (> (count stack) 1)
      (cond
       (ac-f? alpha beta) (cons (apply-ac-f alpha beta) (drop 2 stack))
       (ac-b? alpha beta) (cons (apply-ac-b alpha beta) (drop 2 stack))
       (cc-f? alpha beta) (cons (apply-cc-f alpha beta) (drop 2 stack))
       (cc-b? alpha beta) (cons (apply-cc-b alpha beta) (drop 2 stack))))))

(defn just-reduce
  [workspace]
  (let [final (filter #(not (nil? %))
                             (for [stack workspace]
                               (reduce-stack stack)))]
    (let [combined (concat workspace final)]
      (pprint combined)
      (println (count combined))
      combined)))

(defn shift-reduce
  [workspace input]
  (let [stacks (update-stacks workspace input)]
    (let [new-stacks (filter #(not (nil? %))
                             (for [stack stacks]
                               (reduce-stack stack)))]
      (let [combined (concat stacks new-stacks)]
        (pprint combined)
        combined))))

(defn -main
  [& [example vision]]
  (let [lexicon (getLexicon (Integer/parseInt example) (Integer/parseInt vision))
        sentence (getSentence (Integer/parseInt example))]
    (loop [input sentence
           workspace '(())]
      (println "===========")
      (cond
       (= (first input) ".") (println (filter #(s? %) (just-reduce workspace)))
       :else (recur (next input)
                    (shift-reduce workspace (lexicon (first input))))))))
