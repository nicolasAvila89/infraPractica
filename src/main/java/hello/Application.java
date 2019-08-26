package hello;

import java.text.NumberFormat;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {
    public static final String USE_HAZELCAST = "true";

    final NumberFormat format = NumberFormat.getInstance();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    final Logger LOGGER = Logger.getLogger(Application.class.getName());

    @RequestMapping("/")
    public String home(HttpSession httpSession) {
        Integer hits = (Integer) httpSession.getAttribute("hits");

        LOGGER.info("index() called, hits was " + hits + " session id " + httpSession.getId());

        if (hits == null) {
            hits = 0;
        }

        httpSession.setAttribute("hits", ++hits);

        Runtime runtime = Runtime.getRuntime();

        final long maxMemory = runtime.maxMemory();
        final long allocatedMemory = runtime.totalMemory();
        final long freeMemory = runtime.freeMemory();
        final long mb = 1024 * 1024;
        final String mega = " MB";
        final String name = System.getenv("ENV_NAME");

        final String stringBuilder = "<!DOCTYPE html>"
                + "<html>"
                + "<body>"
                + "<br>========================== Application Memory Info - " + name + " =========================="
                + "<br>Free memory: " + format.format(freeMemory / mb) + mega
                + "<br>Allocated memory: " + format.format(allocatedMemory / mb) + mega
                + "<br>Max memory: " + format.format(maxMemory / mb) + mega
                + "<br>Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / mb) + mega
                + "<br>=================================================================\n"
                + "<br> hits: \n " + hits + " httpSession Id: " + httpSession.getId()
                + "<br>=================================================================\n"
                + "</body>"
                + "</html>";
        LOGGER.info(System.getenv("ENV_NAME") + "- free memory:" + freeMemory);
        return stringBuilder;
    }
}
