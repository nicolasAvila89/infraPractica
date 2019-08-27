package hello;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.session.StandardSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;

import javax.servlet.http.HttpSession;

@Service
@PropertySource("classpath:app.properties")

public class SessionService {

    @Autowired
    HttpSession session;

    @Value("${sessionEnabled}")
    private boolean sessionEnabled;


    List<byte[]> memoryFiller = new ArrayList<>();

    private static String MARKERS = "MARKERS";
    private static String HITS = "HITS";

    public Integer addHit() {
        Integer hit = (Integer) getSession().getAttribute(HITS);
        if (hit == null) {
            hit = 0;
        } else {
            hit++;
        }
        getSession().putValue(HITS, hit);
        return hit;
    }

    public void saveMarker(Integer value) {
        memoryFill();
        getSession().putValue(MARKERS, value);
    }

    public Integer getMarkers() {
        Integer result = (Integer) session.getAttribute(MARKERS);
        if (result == null) return 0;
        return result;
    }

    private void memoryFill() {
        if (sessionEnabled){
            for (int i = 0; i < 100; i++) {
                byte bytes[] = new byte[1048576];
                memoryFiller.add(bytes);
            }
        }
    }

    public HttpSession getSession() {
        if (!sessionEnabled){
            this.session.invalidate();
        }
        return this.session;
    }
}
