(ns user-dashboard.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [user-dashboard.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[user_dashboard started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[user_dashboard has shut down successfully]=-"))
   :middleware wrap-dev})
