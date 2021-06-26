package gchat.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageResource {
    private final MessageSource messageSource;

    public String getMessage(String key){
        return getMessage(key,null);
    }

    public String getMessage(String key, String[] params){
        return messageSource.getMessage(key,params, Locale.getDefault());
    }

}

