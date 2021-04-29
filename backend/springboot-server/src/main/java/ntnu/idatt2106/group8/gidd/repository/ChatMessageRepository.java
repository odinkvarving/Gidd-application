package ntnu.idatt2106.group8.gidd.repository;

import ntnu.idatt2106.group8.gidd.model.entities.ChatMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Integer> {

    List<ChatMessage> findChatMessagesByActivityIdOrderByTimeStamp(int activityId);

    List<ChatMessage> findChatMessagesByActivityId(int activityId);

    List<ChatMessage> findChatMessagesByAccountIdOrderByTimeStamp(int accountId);

    List<ChatMessage> findChatMessagesByAccountId(int accountId);

}
