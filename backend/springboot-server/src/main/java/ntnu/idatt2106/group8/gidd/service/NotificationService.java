package ntnu.idatt2106.group8.gidd.service;


import ntnu.idatt2106.group8.gidd.model.entities.*;
import ntnu.idatt2106.group8.gidd.repository.AccountInfoRepository;
import ntnu.idatt2106.group8.gidd.repository.AccountRepository;
import ntnu.idatt2106.group8.gidd.repository.NotificationRepository;
import ntnu.idatt2106.group8.gidd.repository.NotificationSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    Logger logger = LoggerFactory.getLogger(NotificationService.class);
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private NotificationSettingsRepository notificationSettingsRepository;
    @Autowired
    private AccountInfoRepository accountInfoRepository;
    @Autowired
    private ActivityService activityService;

    public List<Notification> getAccountsNotifications(int account_id) {
        Iterable<Notification> itNotifications = notificationRepository.findNotificationByAccountId(account_id);
        List<Notification> notifications = new ArrayList<>();

        itNotifications.forEach(notifications::add);

        return notifications;
    }

    public boolean sendNotification(Notification notification) {
        try {
            Notification result = notificationRepository.save(notification);
            return result.equals(notification);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Could not save notifications. Something went wrong!");
            return false;
        }
    }


    public boolean updateNotification(Notification notification) {
        Optional<Notification> notificationToUpdate = notificationRepository.findById(notification.getId());
        Notification result;

        if (notificationToUpdate.isPresent()) {
            notificationToUpdate.get().setSeen(true);
            result = notificationRepository.save(notificationToUpdate.get());
            return true;
        } else {
            return false;
        }
    }

    public NotificationSettings getNotificationSettingsByAccountId(int accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isPresent()) {
            AccountInfo accountInfo = account.get().getAccountInfo();
            return accountInfo.getNotificationSettings();
        } else {
            return null;
        }
    }

    public boolean updateAccountsNotificationsSetting(int accountId, NotificationSettings notificationSettings) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isPresent()) {
            AccountInfo accountInfo = account.get().getAccountInfo();
            notificationSettings.setId(accountInfo.getNotificationSettings().getId());
            accountInfo.setNotificationSettings(notificationSettings);
            accountInfoRepository.save(accountInfo);
            logger.info("Comes here!!!!");
            return true;
        } else {
            return false;
        }
    }

    public boolean updateNotificationSettings(NotificationSettings notificationSettings) {
        return notificationSettingsRepository.save(notificationSettings).equals(notificationSettings);
    }

    public boolean sendNotificationToAllParticipants(int activityId, String msg) {
        List<Account> accounts = activityService.getAllAccountsInActivity(activityId);
        Activity activity = activityService.getActivity(activityId).orElse(null);
        if (activity != null) {
            if (accounts.size() != 0) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime now = LocalDateTime.now();

                accounts.stream().forEach(account -> {
                    String message = "Activity: \"" + activity.getTitle() + "\" " + msg;
                    String date = dtf.format(now);
                    Notification notification = new Notification(account, activityId, message, date, false);
                    sendNotification(notification);
                });
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteNotification(int notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notificationRepository.delete(notification);
            logger.info(notification + " \nwas deleted");
            return true;
        } else {
            logger.info("Notifcation was not found, could not delete");
            return false;
        }
    }
}
