package hello;

import java.text.NumberFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

    final NumberFormat format = NumberFormat.getInstance();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RequestMapping("/")
    public String home() {
        Runtime runtime = Runtime.getRuntime();

        final long maxMemory = runtime.maxMemory();
        final long allocatedMemory = runtime.totalMemory();
        final long freeMemory = runtime.freeMemory();
        final long mb = 1024 * 1024;
        final String mega = " MB";

        final String stringBuilder = "<!DOCTYPE html>"
                    + "<html>"
                    + "<body>"
                    + "<br>========================== Application Memory Info =========================="
                    + "<br>Free memory: " + format.format(freeMemory / mb) + mega
                    + "<br>Allocated memory: " + format.format(allocatedMemory / mb) + mega
                    + "<br>Max memory: " + format.format(maxMemory / mb) + mega
                    + "<br>Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / mb) + mega
                    + "<br>=================================================================\n"
                    + "</body>"
                    + "</html>";
        return stringBuilder;
    }


}
