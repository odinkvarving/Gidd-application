package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.ChatMessage;
import ntnu.idatt2106.group8.gidd.repository.ChatMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class to manage chat messages written by users on activities
 * @author Endré Hadzalic
 */
@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    /**
     * Adds a message to the chatfield in a activity.
     *
     * @param accountId  the id of the account that made the message.
     * @param activityId the id of the activity the message belongs to.
     * @param message    the message.
     */
    public void addMessageToActivity(int accountId, int activityId, String message) {
        logger.info("adding message from account: " + accountId + ", to activity: " + activityId + ", with message: \n" + message);
        this.chatMessageRepository.save(new ChatMessage(accountId, activityId, message));
    }

    /**
     * Method for finding all messages that is in a activity.
     *
     * @param activityId the id of the activity to find the messages to.
     * @return a list sorted by date with messages.
     */
    public List<ChatMessage> getMessagesInActivitySorted(int activityId) {
        logger.debug("Returned all messages for activity: " + activityId);
        return this.chatMessageRepository.findChatMessagesByActivityIdOrderByTimeStamp(activityId);
    }

    /**
     * Updates the message with the given id.
     *
     * @param id        the id of the chat message to update
     * @param message   the new state to set to the chat message
     */
    public void updateChatMessage(int id, String message) {
        try {
            ChatMessage foundMessage = this.chatMessageRepository.findById(id).orElseThrow(NoSuchElementException::new);
            foundMessage.setMessage(message);
            this.chatMessageRepository.save(foundMessage);
        } catch (NoSuchElementException nee) {
            logger.error("error did not find the chat-message that is to be updated", nee);
        }
    }

    /**
     * Deletes the chat message with the given id.
     *
     * @param id the chat message to delete
     */
    public void deleteChatMessage(int id) {
        this.chatMessageRepository.deleteById(id);
    }

    /**
     * Deletes all chat messages on the activity with the given id
     *
     * @param activityId the id of the activity
     */
    public void deleteAllMessagesByActivity(int activityId) {
        List<ChatMessage> messages = this.chatMessageRepository.findChatMessagesByActivityId(activityId);
        this.chatMessageRepository.deleteAll(messages);
    }

    /**
     * Deletes all chat messages written by the account with the given id
     *
     * @param accountId the id of the account
     */
    public void deleteAllMessagesByAccount(int accountId) {
        List<ChatMessage> messages = this.chatMessageRepository.findChatMessagesByAccountId(accountId);
        this.chatMessageRepository.deleteAll(messages);
    }
}
