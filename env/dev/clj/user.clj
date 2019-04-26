(ns user
  (:require [mount.core :as mount]
            user-dashboard.core))

(defn start []
  (mount/start-without #'user-dashboard.core/repl-server))

(defn stop []
  (mount/stop-except #'user-dashboard.core/repl-server))

(defn restart []
  (stop)
  (start))


