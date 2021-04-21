package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.compositeentities.AccountActivity;
import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.Equipment;
import ntnu.idatt2106.group8.gidd.repository.AccountActivityRepository;
import ntnu.idatt2106.group8.gidd.repository.AccountRepository;
import ntnu.idatt2106.group8.gidd.repository.ActivityRepository;
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
    private AccountService accountService;

    /**
     * Method for finding all registered activities
     * @return a list of all activities
     */
    public List<Activity> getAllActivities() {
        List<Activity> activities = new ArrayList<>();
        try {
            activityRepository.findAll().forEach(activities::add);
        } catch (DataAccessException e) {
            log.info("Activities not found");
        }
        return activities;
    }

    /**
     * Method for getting an activity with the specified id
     * @param id the id of the activity
     * @return the activity matching the id
     */
    public Optional<Activity> getActivity(int id) {
        try {
            return activityRepository.findById(id);
        } catch (DataAccessException e) {
            log.info("Activity not found");
        }
        return Optional.empty();
    }

    /**
     * Method for adding a new activity
     * @param activity the activity to be added
     * @return the activity that was added
     */
    public Activity addActivity(Activity activity) {
        try {
            return activityRepository.save(activity);
        } catch (DataAccessException e) {
            log.info("Could not add activity");
        }
        return null;
    }

    /**
     * Method for updating an activity
     * @param id the id of the activity to be updated
     * @param activity the updated version of the activity
     * @return the updated activity
     */
    public Activity updateActivity(int id, Activity activity) {
        try {
            return activityRepository.save(activity);
        } catch (DataAccessException e) {
            log.info("Could not update activity");
        }
        return null;
    }

    /**
     * Method for deleting an activity
     * @param id the id of the activity to be deleted
     */
    public void deleteActivity(int id) {
        try {
            activityRepository.deleteById(id);
        } catch (DataAccessException e) {
            log.info("Could not delete activity");
        }
    }

    /**
     * Method for finding activities with the specified Activity-type
     * @param type a String with the type you want to find activities by
     * @return a list of Activities with this Activity-type
     */
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

    /**
     * Method for finding the Activity-type of a specified Activity
     * @param id the id of the Activity
     * @return a String specifying which Activity-type the Activity is
     */
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

    /**
     * Method for finding the Equipment for a specified Activity
     * @param id the id of the Activity
     * @return a Set of Equipment
     */
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

    /**
     * Method for adding an account as a participant in the specified Activity
     * @param activityId the id of the Activity
     * @param participantId the id of the Account
     * @return the Account that was added to the Activity
     */
    public Optional<Account> addParticipantToActivity(int activityId, int participantId) {
        boolean wasQueued = false;

        try {

            if (accountService.accountExistsById(participantId) && this.activityRepository.existsById(activityId)) {
                AccountActivity accountActivityToAdd;
                int activitySize = this.activityRepository
                        .findById(activityId)
                        .orElseThrow(NoSuchElementException::new)
                        .getMaxParticipants();
                Set<AccountActivity> allAccountActivities = this.accountActivityRepository.findByActivityId(activityId);
                if (allAccountActivities.size() < activitySize) {
                    accountActivityToAdd = new AccountActivity(participantId, activityId, 0);
                } else if (allAccountActivities.size() == activitySize) {
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
                this.accountActivityRepository.save(accountActivityToAdd);
            } else {
                throw new NoSuchElementException("Either the given account id or the given activity " +
                        "id was not found in the database");
            }
        } catch (DataAccessException e) {
            log.info("Could not add account to activity");
        }
        return Optional.empty();
    }

    /**
     * Method for finding all Accounts registered to a specified Activity
     * @param id the id of the Activity
     * @return a list of Accounts registered to an Activity
     */
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

    /**
     * Method for checking if a specified Account is registered to a specific Activity
     * @param activityId the id of the activity
     * @param accountId the id of the Account
     * @return true or false
     */
    public boolean checkIfAccountIsInActivity(int activityId, int accountId) {
        try {
            List<Integer> list = accountActivityRepository.findByActivityId(activityId)
                    .stream()
                    .map(AccountActivity::getActivityId)
                    .collect(Collectors.toList());
            for (Integer i : list) {
                if (accountRepository.findById(i).isPresent() && accountRepository.findById(i).get().getId() == accountId) {
                    return true;
                }
            }
        } catch (DataAccessException e) {
            log.info("Could not find this account in this activity");
        }
        return false;
    }

    /**
     * Method for finding all Accounts in queue for a specific Activity
     * @param id the id of the Activity
     * @return a list of Accounts in queue for the Activity
     */
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

    /**
     * Method for cancelling a specific Activity
     * @param id the id of the Activity to be cancelled
     * @return true or false
     */
    public boolean cancelActivity(int id) {
        Optional<Activity> activity;
        List<AccountActivity> accountActivities;
        try {
            activity = activityRepository.findById(id);
            if(activity.isPresent()) {
                accountActivities = new ArrayList<>(accountActivityRepository.findByActivityId(activity.get().getId()));
                for (AccountActivity a : accountActivities) {
                    accountActivityRepository.delete(a);
                    return true;
                }
            }else{
                log.info("No activity with this ID registered");
                return false;
            }
        }catch (DataAccessException e) {
            log.info("Could not find the specified activity");
        }
        return false;
    }
}