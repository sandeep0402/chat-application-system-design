package gchat.lastseen;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import gchat.usermessage.UserMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@DynamoDBTable(tableName = "lastseen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class LastSeen {

    @DynamoDBHashKey
    private String userId;
    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = UserMessage.LocalDateTimeConverter.class)
    private LocalDateTime lastSeenAt;

}