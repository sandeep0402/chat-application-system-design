package gchat.lastseen;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface LastSeenRepository extends CrudRepository<LastSeen, String> {
}