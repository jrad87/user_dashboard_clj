(ns user-dashboard.models.message
  (:require [struct.core :as st]
            [conman.core :as conman]
            [user-dashboard.db.core :as db]))


(def message-schema
  {:text [[st/required :message "Message text is required"]
          [st/max-count 512 :message "Message text must not contain more than 512 characters"]
          st/string]
   :message_to [[st/integer :message "User id must be an integer"]]
   :message_from [[st/integer :message "User id must be an integer"]]})

(defn send-message! [session params]
  (db/query :send-message! {:text (:text params)
                            :to (bigint (:id params))
                            :from (bigint (get-in session [:user :id]))}))

(defn get-messages-to-viewed-user [params]
  (db/query :get-messages-to (update params :id  #(Integer. %))))

(defn get-messages-to-current-user [session]
  (println (get-in session [:user :id]))
  (println (db/query :get-messages-to {:id (get-in session [:user :id])})))

(defn validate-delete-message! [session params]
  (if (= (:user_id params) (get-in session [:user :id]))
    (db/query :delete-message! (find params :id))
    nil))
