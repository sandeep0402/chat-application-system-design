package gchat.groupmessage;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import gchat.usermessage.UserMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@DynamoDBTable(tableName = "groupmessage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class GroupMessage {

    @DynamoDBHashKey(attributeName = "messageId")
    private String messageId;
    @DynamoDBAttribute
    private Long from;
    @DynamoDBAttribute
    private Long groupId;
    @DynamoDBAttribute
    private String content;
    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = UserMessage.LocalDateTimeConverter.class)
    private LocalDateTime dateTime;

}