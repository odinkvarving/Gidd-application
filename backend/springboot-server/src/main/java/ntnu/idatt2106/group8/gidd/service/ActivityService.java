package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.compositeentities.AccountActivity;
import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import ntnu.idatt2106.group8.gidd.model.entities.ActivityType;
import ntnu.idatt2106.group8.gidd.model.entities.Equipment;
import ntnu.idatt2106.group8.gidd.repository.*;

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
    private ActivityRepo activityRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private AccountActivityRepo accountActivityRepo;

    @Autowired
    private ActivityTypeRepo activityTypeRepo;

    @Autowired
    private EquipmentRepo equipmentRepo;

    public List<Activity> getAllActivities() {
        List<Activity> activities = new ArrayList<>();
        try {
            activityRepo.findAll().forEach(activities::add);
        }catch (DataAccessException e) {
            log.info("Activities not found");
        }
        return activities;
    }

    public Optional<Activity> getActivity(int id) {
        try {
            return activityRepo.findById(id);
        }catch (DataAccessException e) {
            log.info("Activity not found");
        }
        return Optional.empty();
    }

    public Activity addActivity(Activity activity) {
        try {
            return activityRepo.save(activity);
        }catch (DataAccessException e) {
            log.info("Could not add activity");
        }
        return null;
    }

    public Activity updateActivity(int id, Activity activity) {
        try {
            return activityRepo.save(activity);
        }catch (DataAccessException e) {
            log.info("Could not update activity");
        }
        return null;
    }

    public void deleteActivity(int id) {
        try {
            activityRepo.deleteById(id);
        }catch (DataAccessException e) {
            log.info("Could not delete activity");
        }
    }

    public List<Activity> getActivitiesByType(String type) {
        List<Activity> activities = new ArrayList<>();
        List<Activity> byType = new ArrayList<>();
        try {
            activityRepo.findAll().forEach(activities::add);
            for (Activity a: activities) {
                if(a.getActivityType().getType().equals(type)) {
                    byType.add(a);
                }
            }
            return byType;
        }catch (DataAccessException e) {
            log.info("Could not find any activities of this type");
        }
        return null;
    }

    public String getActivityType(int id) {
        Optional<Activity> activity;
        try {
            activity = activityRepo.findById(id);
            if(activity.isPresent()) {
                return activity.get().getActivityType().getType();
            }
        }catch (DataAccessException e) {
            log.info("Could not find the activity type for this activity");
        }
        return null;
    }

    public Set<Equipment> getActivityEquipment(int id) {
        Optional<Activity> activity;
        try {
            activity = activityRepo.findById(id);
            if(activity.isPresent()) {
                return activity.get().getEquipment();
            }
        }catch (DataAccessException e) {
            log.info("Could not find any equipment for this activity");
        }
        return null;
    }

    public Optional<Account> addParticipantToActivity(int activityId, int participantId) {
        Optional<Activity> activity;
        Optional<Account> participant;
        Set<AccountActivity> participantList;
        AccountActivity addedParticipant;
        try {
            activity = activityRepo.findById(activityId);
            participant = accountRepo.findById(participantId);
            if(activity.isPresent() && participant.isPresent()) {
                participantList = accountActivityRepo.findByActivityId(activityId).stream()
                        .filter(accountActivity -> accountActivity.getQueuePosition()==0)
                        .collect(Collectors.toCollection(HashSet::new));

                int participatingSize = participantList.size();
                if(participatingSize < activity.get().getMaxParticipants()) {
                    log.info("There are " + participatingSize + " participants in this activity");
                    addedParticipant = new AccountActivity(participantId, activityId, 0);
                    accountActivityRepo.save(addedParticipant);
                    log.info("Added as participant to activity.");
                }else{
                    ArrayList<AccountActivity> queueList = accountActivityRepo.findByActivityId(activityId).stream()
                            .filter(accountActivity -> accountActivity.getQueuePosition() != 0)
                            .collect(Collectors.toCollection(ArrayList::new));
                    int queueSize = queueList.size();
                    addedParticipant = new AccountActivity(participant.get().getId(), activity.get().getId(), (queueSize + 1));
                    accountActivityRepo.save(addedParticipant);
                    log.info("The activity is full. Participant added to queue with position: " + (queueSize + 1));
                }
                return participant;
            }else {
                log.info("Could not find the specified user or activity");
            }
        }catch (DataAccessException e) {
            log.info("Could not add participant to activity");
        }
        return Optional.empty();
    }

    public List<Account> getAllAccountsInActivity(int id) {
        List<Account> participatingAccounts = new ArrayList<>();
        try {
            List<AccountActivity> list = accountActivityRepo.findByActivityId(id)
                    .stream()
                    .filter(accountActivity -> accountActivity.getQueuePosition() != 0)
                    .collect(Collectors.toList());
            for (AccountActivity a : list) {
                participatingAccounts.add(accountRepo.findById(a.getUserId()).orElseThrow(NoSuchElementException::new));
            }
            return participatingAccounts;
        }catch (NoSuchElementException e) {
            log.info("Could not find any participants in this activity");
        }
        return null;
    }

    public boolean checkIfAccountIsInActivity(int activityId, int accountId) {
        try {
            List<Integer> list = accountActivityRepo.findByActivityId(activityId)
                    .stream()
                    .map(AccountActivity::getUserId)
                    .collect(Collectors.toList());
            for(Integer i : list) {
                if(accountRepo.findById(i).isPresent() && accountRepo.findById(i).get().getId() == accountId) {
                    return true;
                }
            }
        }catch (DataAccessException e) {
            log.info("Could not find this account in this activity");
        }
        return false;
    }

    public List<Account> getAllAccountsInQueue(int id) {
        List<Account> queueAccounts = new ArrayList<>();
        try {
            List<AccountActivity> list = accountActivityRepo.findByActivityId(id)
                    .stream()
                    .filter(accountActivity -> accountActivity.getQueuePosition() == 0)
                    .collect(Collectors.toList());
            for (AccountActivity a : list) {
                queueAccounts.add(accountRepo.findById(a.getUserId()).orElseThrow(NoSuchElementException::new));
            }
            return queueAccounts;
        }catch (NoSuchElementException e) {
            log.info("Could not find any participants in this activity");
        }
        return null;
    }
}