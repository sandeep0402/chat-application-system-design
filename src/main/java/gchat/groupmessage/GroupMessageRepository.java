package gchat.groupmessage;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface GroupMessageRepository extends CrudRepository<GroupMessage, String> {
}