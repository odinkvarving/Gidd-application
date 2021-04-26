package ntnu.idatt2106.group8.gidd.service;


import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.Notification;
import ntnu.idatt2106.group8.gidd.repository.AccountRepository;
import ntnu.idatt2106.group8.gidd.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AccountRepository accountRepository;


    public List<Notification> getAccountsNotifications(int account_id){
        Iterable<Notification> itNotifications = notificationRepository.findNotificationByAccountId(account_id);
        List<Notification> notifications = new ArrayList<>();

        itNotifications.forEach(notifications::add);

        return notifications;
    }
    public boolean sendNotification(Notification notification){
        Notification result = notificationRepository.save(notification);
        return result.equals(notification);
    }


    public boolean updateNotification(Notification notification){
        Optional<Notification> notificationToUpdate = notificationRepository.findById(notification.getId());
        Notification result;

        if(notificationToUpdate.isPresent()){
            notificationToUpdate.get().setSeen(true);
            result = notificationRepository.save(notificationToUpdate.get());
            return true;
        }else{
            return false;
        }
    }
}
