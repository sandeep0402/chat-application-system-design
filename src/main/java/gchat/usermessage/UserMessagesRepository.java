package gchat.usermessage;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface UserMessagesRepository extends CrudRepository<UserMessage, String> {

    List<UserMessage> findAllByToAndDeliveryReceiptIsNull(Long to);
}