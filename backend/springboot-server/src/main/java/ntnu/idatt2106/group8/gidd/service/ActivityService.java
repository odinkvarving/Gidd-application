package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.compositeentities.AccountActivity;
import ntnu.idatt2106.group8.gidd.model.compositeentities.ids.AccountActivityId;
import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.ActivityType;
import ntnu.idatt2106.group8.gidd.model.entities.Equipment;
import ntnu.idatt2106.group8.gidd.repository.AccountActivityRepository;
import ntnu.idatt2106.group8.gidd.repository.AccountRepository;
import ntnu.idatt2106.group8.gidd.repository.ActivityRepository;
import ntnu.idatt2106.group8.gidd.repository.ActivityTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Odin Kvarving
 */

@Service
public class ActivityService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountActivityRepository accountActivityRepository;

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Autowired
    private AccountService accountService;

    public List<Activity> getAllActivities() {
        List<Activity> activities = new ArrayList<>();
        try {
            activityRepository.findAll().forEach(activities::add);
        } catch (DataAccessException e) {
            log.info("Activities not found");
        }
        return activities;
    }

    public Optional<Activity> getActivity(int id) {
        try {
            return activityRepository.findById(id);
        } catch (DataAccessException e) {
            log.info("Activity not found");
        }
        return Optional.empty();
    }

    public Activity addActivity(Activity activity) {
        log.info(activity.toString());
        log.info(activity.getCreator().toString());
        try {
            ActivityType activityType = activityTypeRepository.findActivityTypeByType(activity.getActivityType().getType());
            activity.setActivityType(activityType);
            Activity result = activityRepository.save(activity);
            addParticipantToActivity(result.getId(), activity.getCreator().getId());
            return result;
        } catch (DataAccessException e) {
            e.printStackTrace();
            log.info("Could not add activity");
        }
        return null;
    }

    public Activity updateActivity(int id, Activity activity) {
        try {
            return activityRepository.save(activity);
        } catch (DataAccessException e) {
            log.info("Could not update activity");
        }
        return null;
    }

    public void deleteActivity(int id) {
        try {
            activityRepository.deleteById(id);
        } catch (DataAccessException e) {
            log.info("Could not delete activity");
        }
    }

    public List<Activity> getActivitiesByType(String type) {
        List<Activity> activities = new ArrayList<>();
        List<Activity> byType = new ArrayList<>();
        try {
            activityRepository.findAll().forEach(activities::add);
            for (Activity a : activities) {
                if (a.getActivityType().getType().equals(type)) {
                    byType.add(a);
                }
            }
            return byType;
        } catch (DataAccessException e) {
            log.info("Could not find any activities of this type");
        }
        return null;
    }

    public String getActivityType(int id) {
        Optional<Activity> activity;
        try {
            activity = activityRepository.findById(id);
            if (activity.isPresent()) {
                return activity.get().getActivityType().getType();
            }
        } catch (DataAccessException e) {
            log.info("Could not find the activity type for this activity");
        }
        return null;
    }

    public Set<Equipment> getActivityEquipment(int id) {
        Optional<Activity> activity;
        try {
            activity = activityRepository.findById(id);
            if (activity.isPresent()) {
                return activity.get().getEquipment();
            }
        } catch (DataAccessException e) {
            log.info("Could not find any equipment for this activity");
        }
        return null;
    }

    public Optional<AccountActivity> addParticipantToActivity(int activityId, int participantId) {
        boolean wasQueued = false;

        try {

            if (accountService.accountExistsById(participantId) && this.activityRepository.existsById(activityId)) {
                AccountActivity accountActivityToAdd;
                int activitySize = this.activityRepository
                        .findById(activityId)
                        .orElseThrow(NoSuchElementException::new)
                        .getMaxParticipants();
                Set<AccountActivity> allAccountActivities = this.accountActivityRepository.findByActivityId(activityId);
                if (allAccountActivities.size() < activitySize) { //there was space in the activity!
                    accountActivityToAdd = new AccountActivity(participantId, activityId, 0);
                } else if (allAccountActivities.size() == activitySize) {                         //there was not space in the activity, the account must be queued!
                    accountActivityToAdd = new AccountActivity(participantId, activityId, 1);
                    wasQueued = true;
                } else {
                    int highestQueuePosition =
                            allAccountActivities.stream()
                                    .filter(accountActivity -> accountActivity.getQueuePosition() > 0)
                                    .max(Comparator.comparing(AccountActivity::getQueuePosition))
                                    .orElseThrow(NoSuchElementException::new).getQueuePosition();
                    accountActivityToAdd = new AccountActivity(participantId, activityId, highestQueuePosition + 1);
                    wasQueued = true;
                }
                return Optional.of(this.accountActivityRepository.save(accountActivityToAdd));
            } else {
                throw new NoSuchElementException("Either the given account id or the given activity " +
                        "id was not found in the database");
            }
        } catch (DataAccessException e) {
            log.info("Could not add account to activity");
        }
        return Optional.empty();
    }

    public List<Account> getAllAccountsInActivity(int id) {
        List<Account> participatingAccounts = new ArrayList<>();
        try {
            List<AccountActivity> list = accountActivityRepository.findByActivityId(id)
                    .stream()
                    .filter(accountActivity -> accountActivity.getQueuePosition() == 0)
                    .collect(Collectors.toList());
            for (AccountActivity a : list) {
                participatingAccounts.add(accountRepository.findById(a.getAccountId()).orElseThrow(NoSuchElementException::new));
            }
            return participatingAccounts;
        } catch (NoSuchElementException e) {
            log.info("Could not find any participants in this activity");
        }
        return null;
    }

    public int countAllAccountsInActivity(int id){
        List<Account> participatingAccounts = new ArrayList<>();
        try{
            List<AccountActivity> list = accountActivityRepository.findByActivityId(id)
                    .stream()
                    .filter(accountActivity -> accountActivity.getQueuePosition() == 0)
                    .collect(Collectors.toList());
            return list.size();
        }catch (NoSuchElementException e){
            log.info("Could not find any participants in this activity");
        }
        return 0;
    }

    public boolean checkIfAccountIsInActivity(int activityId, int accountId) {
        try {
            return accountActivityRepository.findById(new AccountActivityId(accountId,activityId)).isPresent();
        } catch (DataAccessException e) {
            log.info("Could not find this account in this activity");
        }
        return false;
    }

    public List<Account> getAllAccountsInQueue(int id) {
        List<Account> queueAccounts = new ArrayList<>();
        try {
            List<AccountActivity> list = accountActivityRepository.findByActivityId(id)
                    .stream()
                    .filter(accountActivity -> accountActivity.getQueuePosition() > 0)
                    .collect(Collectors.toList());
            for (AccountActivity a : list) {
                queueAccounts.add(accountRepository.findById(a.getAccountId()).orElseThrow(NoSuchElementException::new));
            }
            return queueAccounts;
        } catch (NoSuchElementException e) {
            log.info("Could not find any participants in this activity");
        }
        return queueAccounts;
    }
}