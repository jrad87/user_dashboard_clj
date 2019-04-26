(ns user-dashboard.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[user_dashboard started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[user_dashboard has shut down successfully]=-"))
   :middleware identity})
