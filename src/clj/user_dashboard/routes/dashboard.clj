(ns user-dashboard.routes.dashboard
  (:require [user-dashboard.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [user-dashboard.models.user :as User]
            [user-dashboard.models.message :as Message]))

(defn dashboard [{:keys [session]}]
  (let  [users (User/get-users-and-partition session)]    
    (layout/render "dashboard.html" {:users  users
                                     :current-user (:user session)})))

(defn view-profile [{:keys [session params]}]
  (println (Message/get-messages-to-viewed-user params))
  (layout/render "user.html" {:user (User/get-user-by-id params)
                              :current-user (:user session)
                              :messages (Message/get-messages-to-viewed-user params)
                              :true true}))

(defn send-message! [{:keys [session params]}]
  (Message/send-message! session params)
  (response/found (str "/users/" (:id params))))

(defn delete-user! [id]
  (do (User/delete-user id)
      (response/found "/dashboard")))

(defn delete-message! [{:keys [session params]}]
  (Message/validate-delete-message! session params)
  (response/found "/dashboard"))

(defn logout [{:keys [session]}]
  (-> (response/found "/")
      (assoc :session (dissoc session :identity :user))))

(defroutes dashboard-routes
  (GET "/dashboard" [] dashboard)
  (GET "/users/:id" [] view-profile)
  (POST "/users/:id/delete" [id] (delete-user! id))
  (POST "/users/:id/message" [] send-message!)
  (POST "/messages/:id/delete" [])
  (GET "/logout" [] logout))
