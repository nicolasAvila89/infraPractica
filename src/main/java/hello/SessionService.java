package hello;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class SessionService {
    List<byte[]> memoryFiller = new ArrayList<>();
    Map<String, Integer> sessionContainer = new HashMap<>();

    public void save(String user, Integer value){
        for (int i = 0; i < 100; i++) {
            byte bytes[] = new byte[1048576];
            memoryFiller.add(bytes);
        }
        sessionContainer.put(user, value);
    }

    public Integer get(String user){
        return sessionContainer.get(user);
    }
}
