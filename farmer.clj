(ns user)

;; This is a classic riddle. It has a few different names, but the core idea is
;; the same.
;;
;; A farmer wants to transport a fox, a chicken, and grain across a river. He
;; has room to fit one of these with him in his boat. If he leaves the fox and
;; the chicken alone together, the fox will eat the chicken. Likewise, if he
;; leaves the chicken alone with the grain, the chicken will eat the grain. How
;; can he get everything safely across?
;;
;; This is like a graph problem, we need to search through different states
;; until we come to a solution. At a high level, we want to:
;;  - check if the state is a solution
;;  - check if the state is valid
;;  - generate possible moves and continue the search
;;
;; We want to avoid cycles as this will cause our search to continue forever.
;; This can be done using two heuristics:
;;  1. don't allow the farmer to pick the same thing as the previous turn
;;  2. try the emtpy boat first
;; This didn't work! It's still possible to have long-running cycles. Seems
;; like you need to keep track of visited states in order to halt.

(defn farmer [[curr other west? :as state] path history]
  (cond
    (and west? (= #{:fox :chicken :grain} curr)) [path]
    (history state) []
    (#{#{:fox :chicken} #{:chicken :grain}} other) []
    :else (into
           (farmer [other curr (not west?)] (conj path nil) (conj history state))
           (mapcat
            #(farmer [(conj other %) (disj curr %) (not west?)] (conj path %) (conj history state))
            curr))))

(farmer [#{:fox :chicken :grain} #{} false] [] #{})
