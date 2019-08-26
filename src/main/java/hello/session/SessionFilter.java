package hello.session;

import static hello.Application.HITS;
import static hello.session.HazelcastConfiguration.SESSIONS;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;

@Component
public class SessionFilter implements Filter {

    @Autowired
    HazelcastInstance hazelcastInstance;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain)
            throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();

        Integer hazelcastSessionHints = (Integer) hazelcastInstance.getMap(SESSIONS).get(session.getId());
        if (hazelcastSessionHints == null) {
            Integer hints = (Integer) session.getAttribute(HITS);
            hazelcastInstance.getMap(SESSIONS).put(session.getId(), hints != null ? hints : 0);
        }

        filterchain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterconfig) throws ServletException {
    }
}