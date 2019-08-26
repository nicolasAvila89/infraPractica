package hello;

import static hello.session.HazelcastConfiguration.MARKERS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;

@Service
public class SessionService {

    @Autowired
    HazelcastInstance hazelcastInstance;

    List<byte[]> memoryFiller = new ArrayList<>();
    Map<String, Integer> sessionContainer = new HashMap<>();

    public void save(String user, Integer value, boolean persistent) {
        memoryFill();
        if (persistent) {
            hazelcastInstance.getMap(MARKERS).put(user, value);
        }

        sessionContainer.put(user, value);
    }

    public Integer get(String user, boolean persistent) {
        if (persistent) {
            return (Integer) hazelcastInstance.getMap(MARKERS).get(user);
        }

        return sessionContainer.get(user);
    }

    private void memoryFill() {
        for (int i = 0; i < 100; i++) {
            byte bytes[] = new byte[1048576];
            memoryFiller.add(bytes);
        }
    }

}
