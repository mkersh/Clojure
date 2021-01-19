(ns sinewave.core
  (:require [reagent.core :as reagent :refer [atom]]
  [reagentdemo.intro :as demo]
  ))

(enable-console-print!)

(defonce app-state (atom {:text "Hello Chestnut!"
                          :body "this is the body"}))

(defn greeting []
  [:h1 (:text @app-state)])

(defn setBody []
  [:div (:body @app-state)])

(defn render []
  (reagent/render [greeting] (js/document.getElementById "app"))
  (reagent/render [setBody] (js/document.getElementById "app2"))
  (reagent/render [demo/main] (js/document.getElementById "app3"))
  )

(.log js/console "hello clojurescript")
(swap! app-state assoc :body "BODY Beautiful 225555")

(def canvas (.getElementById js/document "myCanvas"))
(def ctx (.getContext canvas "2d"))

;; Clear canvas before doing anything else
(.clearRect ctx 0 0 (.-width canvas) (.-height canvas))

(def interval js/Rx.Observable.interval)
(def time0 (interval 10))


(defn test1 [n] (-> time0
    (.take n)
    (.subscribe (fn [n]

                  (.log js/console n)))))

(defn deg-to-rad [n]
  (* (/ Math/PI 180) n))
(defn sine-coord [x]
  (let [sin (Math/sin (deg-to-rad x))
        y (- 100 (* sin 90))]
    {:x x
     :y y
     :sin sin}))

(def sine-wave
  (.map time0 sine-coord))

(defn test2 [n]
  (-> sine-wave
      (.take n)
      (.subscribe (fn [xysin]
                    (.log js/console (str xysin))))))

(defn fill-rect [x y colour]
  (set! (.-fillStyle ctx) colour)
  (.fillRect ctx x y 2 2))

(defn test3 [n color offset]
  (-> sine-wave
      (.take n)
      (.subscribe (fn [{:keys [x y]}]

                    ;;(fill-rect x y color)
                    (fill-rect x (+ y offset) color)
                    
                    ))))


(test3 650 "red" 0)
(test3 650 "blue" 50)
(test3 650 "orange" 100)
(test3 650 "pink" 150)
(test3 650 "black" 200)


(comment

;; [1] After starting the REPL
;;     Instructions - https://github.com/mkersh/Clojure/blob/main/sinewave/README.md

;; From a termine start the repl
;; lein repl
;; lein cljsbuild once
(go) 
(cljs-repl)
(reagentdemo.intro/essential-api)

;; [2] Then loan browser - http://localhost:10555/

;; [After this you can then change the state]
  ;;(ns sinewave.core)
  ;;  
  (swap! app-state assoc :text "Interactivity FTW")
  (swap! app-state assoc :body "BODY Beautiful")
  
(test3 600)  
  
  
  )
