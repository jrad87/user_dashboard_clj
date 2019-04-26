(ns user-dashboard.routes.auth
  (:require [user-dashboard.layout :as layout]
            [compojure.core :refer [defroutes POST GET]]
            [ring.util.http-response :as response]
            [buddy.hashers :as hashers]
            [user-dashboard.db.core :as db]
            [user-dashboard.models.user :refer [validate-login
                                                validate-registration]]))

(defn render-login [{:keys [flash]}]
  (layout/render "login.html" flash))

(defn list-errors [errors]
  {:errors  (->>  (map (:errors errors)
                       [:server   :first_name :last_name
                        :username :password   :confirmPW])
                  (filter #(not= % nil)))})

(defn login-or-display-error [validation-result session redirect-to]
  (if (:errors validation-result)
    (-> (response/found  redirect-to)
        (assoc :flash (list-errors validation-result)))
    (-> (response/found "/dashboard")
        (assoc :session (assoc (merge session validation-result)
                          :identity "Success")))))

(defn login [{:keys [params session]}]
  (->  (validate-login params)
       (login-or-display-error session "/login")))

(defn render-registration [{:keys [flash]}]
  (layout/render "registration.html" flash))

(defn register [{:keys [params session]}]
  (-> (validate-registration params)
      (login-or-display-error session "/register")))

(defroutes auth-routes
  (GET "/login" request (render-login request))
  (POST "/login" request (login request))

  (GET "/register" request (render-registration request))
  (POST "/register" request (register request)))
