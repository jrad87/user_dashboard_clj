(ns user-dashboard.routes.home
  (:require [user-dashboard.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page [{:keys [session]}]
  (let [date {:date (new java.util.Date)}]
    (layout/render
     "home.html" date)))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" request (home-page request))
  (GET "/about" [] (about-page)))

