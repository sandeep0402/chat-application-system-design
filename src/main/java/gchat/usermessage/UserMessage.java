package gchat.usermessage;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@DynamoDBTable(tableName = "usermessage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class
UserMessage {

    @DynamoDBHashKey(attributeName = "messageId")
    private String messageId;
    @DynamoDBAttribute
    private Long from;
    @DynamoDBAttribute
    private Long to;
    @DynamoDBAttribute
    private String content;
    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    private LocalDateTime dateTime;
    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    private LocalDateTime deliveryReceipt;

    static public class LocalDateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {

        @Override
        public String convert( final LocalDateTime time ) {

            return time.toString();
        }

        @Override
        public LocalDateTime unconvert( final String stringValue ) {

            return LocalDateTime.parse(stringValue);
        }
    }
}