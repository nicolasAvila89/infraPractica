package hello;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;

import javax.servlet.http.HttpSession;

@Service
public class SessionService {

    @Autowired
    HttpSession session;

    List<byte[]> memoryFiller = new ArrayList<>();

    private static String MARKERS="MARKERS";
    private static String HITS="HITS";

    public Integer addHit(){
        Integer hit= (Integer) session.getAttribute(HITS);
        if (hit==null){
            hit=0;
        }else{
            hit++;
        }
        session.putValue(HITS, hit);
        return hit;
    }

    public void saveMarker(Integer value) {
        memoryFill();
        session.putValue(MARKERS, value);
    }

    public Integer getMarkers() {
        Integer result = (Integer) session.getAttribute(MARKERS);
        if (result==null) return 0;
        return result;
    }

    private void memoryFill() {
        for (int i = 0; i < 100; i++) {
            byte bytes[] = new byte[1048576];
            memoryFiller.add(bytes);
        }
    }

    public HttpSession getSession(){
        return this.session;
    }

}
