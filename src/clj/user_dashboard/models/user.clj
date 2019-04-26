(ns user-dashboard.models.user
  (:require [struct.core :as st]
            [buddy.hashers :as hashers]
            [user-dashboard.db.core :as db]
            [mount.core :as mount]))

(def user-schema
  {:first_name [[st/required :message "First name is a required field"]
                [st/min-count 2 :message "First name must be at least 2 characters"]
                [st/max-count 32 :message "First name must be less than 32 characters"]
                st/string]    
   :last_name  [[st/required :message "Last name is a required field"]
                [st/min-count 2 :message "Last name must be at least 2 characters"]
                [st/max-count 32 :message "Last name must be less than 32 characters"]
                st/string]  
   :username   [[st/required :message "Username is required"]
                [st/min-count 8 :message "Username must be at least 8 characters"]
                [st/max-count 32 :message "Username must be less that 32 characters"]
                st/string]  
   :password   [[st/required :message "Password is a required field"]
                [st/min-count 8 :message "Password must be at least 8 characters"]
                [st/max-count 32 :message "Password must be less than 32 characters"]
                st/string]  
   :confirmPW  [[st/required :message "Password confirmation is required"]
                [st/identical-to :password :message "Password must match confirmation"]
                st/string]})

(defn clean [user]
  (-> user
      (update :created_at #(.toDate %))
      (update :updated_at #(.toDate %))
      (dissoc :password :confirmPW)))

(defn validate-login [params]
  (if-let [user (db/query :get-user-by-username (apply hash-map (find params :username)))]
    (if (hashers/check (:password params) (:password user))
      {:user (clean user)}
      {:errors {:server "Invalid credentials"}})
    {:errors {:server "Invalid credentials"}}))

(defn validate-registration [params]
  (if-let [user (db/get-user-by-username (apply hash-map (find params :username)))]
    {:errors {:server "Username already exists, please login"}}
    (if-let [errors (first (st/validate params user-schema))]
      {:errors errors}
      (-> (assoc params :password (hashers/encrypt (:password params)))
          ((fn [map]
              (println map)
              map))
          (#(db/query :create-user! %))
          (merge params)
          (clean)
          ((fn [user] {:user user}))))))

(defn get-users-and-partition [session]
  (->>  (db/query :get-users)
        (filter #(not= (get-in session [:user :id]) (:id %)))
        (map clean)))

(defn get-user-by-id [params]  
  (-> (db/query :get-user (update params :id #(Integer. %)))
      (clean)))

(defn get-user-by-username [username]
  (-> (db/get-user-by-username {:username username})))

(defn delete-user [params]
  (db/delete-user!  {:id (Integer. params)}))
