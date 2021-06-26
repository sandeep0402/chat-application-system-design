package gchat.uuid;

import org.springframework.stereotype.Service;

import java.util.PriorityQueue;
import java.util.UUID;

@Service
public class UuidService {
    PriorityQueue<String> uuidQueue = new PriorityQueue<>();

    public String getNextUuid(){
        if(uuidQueue.size() <= 1){
            fetchUuidBatchFromCenterService();
        }
        return uuidQueue.poll();
    }

    private void fetchUuidBatchFromCenterService(){
        for(int i=0; i<50; i++){
            uuidQueue.add(UUID.randomUUID().toString());
        }
    }
}
