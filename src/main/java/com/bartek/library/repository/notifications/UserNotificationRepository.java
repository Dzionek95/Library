package com.bartek.library.repository.notifications;

import com.bartek.library.model.notifications.UserNotification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserNotificationRepository extends CrudRepository<UserNotification, Long> {

    @Query(value = "SELECT * FROM ACCOUNTS, USER_NOTIFICATION " +
            "WHERE ACCOUNTS.ID=USER_NOTIFICATION.ACCOUNT_ID  " +
            "AND USER_NOTIFICATION.ACCOUNT_ID = ?1 " +
            "ORDER BY USER_NOTIFICATION.TIME_OF_CREATION_OF_NOTIFICATION", nativeQuery = true)
    List<UserNotification> findAllNotificationsForUser(long id);

    @Query(value = "SELECT * FROM ACCOUNTS, USER_NOTIFICATION " +
            "WHERE ACCOUNTS.ID=USER_NOTIFICATION.ACCOUNT_ID  " +
            "AND USER_NOTIFICATION.ACCOUNT_ID = ?1 " +
            "AND USER_NOTIFICATION.NOTIFICATION_TYPE = 0" +
            "ORDER BY USER_NOTIFICATION.TIME_OF_CREATION_OF_NOTIFICATION", nativeQuery = true)
    UserNotification findPunishmentNotificationForUser(long id);

    @Query(value = "SELECT * FROM ACCOUNTS, USER_NOTIFICATION " +
            "WHERE ACCOUNTS.ID=USER_NOTIFICATION.ACCOUNT_ID  " +
            "AND USER_NOTIFICATION.ACCOUNT_ID = ?1 " +
            "AND USER_NOTIFICATION.NOTIFICATION_TYPE = 1" +
            "ORDER BY USER_NOTIFICATION.TIME_OF_CREATION_OF_NOTIFICATION", nativeQuery = true)
    List<UserNotification> findBookAviabilityNotificationsForUser(long id);

}
