(ns user-dashboard.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [user-dashboard.layout :refer [error-page]]
            [user-dashboard.routes.home :refer [home-routes]]
            [user-dashboard.routes.auth :refer [auth-routes]]
            [user-dashboard.routes.dashboard :refer [dashboard-routes]]
            [compojure.route :as route]
            [user-dashboard.env :refer [defaults]]
            [mount.core :as mount]
            [user-dashboard.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats)
        (wrap-routes middleware/wrap-require-not-logged-in))
    (-> #'auth-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats)
        (wrap-routes middleware/wrap-require-not-logged-in))
    (-> #'dashboard-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats)
        (wrap-routes middleware/wrap-require-logged-in))
    (route/not-found
     (:body
      (error-page {:status 404
                   :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
