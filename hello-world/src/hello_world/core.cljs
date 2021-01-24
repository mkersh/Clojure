(ns ^:figwheel-hooks hello-world.core
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]))

(println "This text is printed from src/hello_world/core.cljs. Go ahead and edit it and see reloading in action.")

(defn multiply [a b] (* a b))

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Poker for Fun" :current-bet 2 :pot [50 100 2]
:players [{:name "Mark" :tokens 49}]
}))

(defn get-app-element []
  (gdom/getElement "app"))

(defn get-pot-amount [i]
(let [pot-amount (get (:pot @app-state) i)]
  (if pot-amount pot-amount 0))
)

(defn show-pots []
  [:dev.pots
  (for [i (range 5)]
    (let [pot-amount (get-pot-amount i)]
      (when (> pot-amount 0) [:div.pot "Pot:" pot-amount])))
  ]) 

(defn get-player [i]
  (get (:players app-state) i))
  
(defn show-players []
  [:dev.players
  "Players:"
   (for [i (range 5)]
     (let [player (get-player i)]
       [:div.pot (:name player) (:tokens player) ]))])

(defn show-common-cards []
  (for [_ (range 5)]
    [:img {:src "images/card1.png" :height 200}]))

(defn show-your-tokens []
  [:div.topLeft [:div.tokens "Tokens: 50"]
  [:div.tokens "Current Bet: " (:current-bet @app-state)  [:br] 
  "Raise:" [:input.raise {:type "number"}]]
  ]
  
  )

(defn show-your-actions []
  [:div.actions-list.topRight
   [:div.action "Fold"]  ;; to discard your hand and forfeit the current pot
   [:div.action "Bet"] ;; the opening bet of a betting round 
   [:div.action "Check"] ;; betting zero
   [:div.action "Call"] ;; to match a bet or a raise
   [:div.action "Raise"] ;; to increase the size of the current bet.
   ])

(defn show-your-cards []
  (for [_ (range 2)]
    [:img {:src "images/card1.png" :height 200}]))

(defn hello-world []
  [:div
   [:h1 (:text @app-state)]
   [:h3 "Edit this in src/hello_world/core.cljs and watch it change!"]
   [:div.board
    [:div.cardsAll "All Cards" [:br] (show-common-cards) (show-pots)]
    [:div.cardsYour "Your Cards" [:br] 
    (show-your-cards) (show-your-tokens)
    (show-your-actions)]]
   ])

(defn mount [el]
  (rdom/render [hello-world] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

;;(swap! app-state assoc :text "This is a Test")

(comment

(swap! app-state assoc :text "This is a test!!!!")
)
