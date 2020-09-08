package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Service
@PropertySource("classpath:app.properties")

public class SessionService {

    @Autowired
    HttpSession session;

    @Value("${sessionEnabled}")
    private boolean sessionEnabled;

    @Value("${leakEnabled}")
    private boolean leakEnabled;


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

    public void memoryFill() {
        if (sessionEnabled && leakEnabled) {
            for (int i = 0; i < 100; i++) {
                byte bytes[] = new byte[1048576];
                memoryFiller.add(bytes);
            }
        }
    }

    public HttpSession getSession() {
        if (!sessionEnabled) {
            this.session.invalidate();
            return new HttpSession() {
                @Override
                public long getCreationTime() {
                    return 0;
                }

                @Override
                public String getId() {
                    return null;
                }

                @Override
                public long getLastAccessedTime() {
                    return 0;
                }

                @Override
                public ServletContext getServletContext() {
                    return null;
                }

                @Override
                public void setMaxInactiveInterval(int i) {

                }

                @Override
                public int getMaxInactiveInterval() {
                    return 0;
                }

                @Override
                public HttpSessionContext getSessionContext() {
                    return null;
                }

                @Override
                public Object getAttribute(String s) {
                    return null;
                }

                @Override
                public Object getValue(String s) {
                    return null;
                }

                @Override
                public Enumeration<String> getAttributeNames() {
                    return null;
                }

                @Override
                public String[] getValueNames() {
                    return new String[0];
                }

                @Override
                public void setAttribute(String s, Object o) {

                }

                @Override
                public void putValue(String s, Object o) {

                }

                @Override
                public void removeAttribute(String s) {

                }

                @Override
                public void removeValue(String s) {

                }

                @Override
                public void invalidate() {

                }

                @Override
                public boolean isNew() {
                    return false;
                }
            };
        }
        return this.session;
    }
}
