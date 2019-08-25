package hello;

import java.text.NumberFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
    final NumberFormat format = NumberFormat.getInstance();

    @Autowired
    public SessionService sessionService;

    @RequestMapping("/users/{userId}/comprar")
    public String save(@PathVariable String userId) {
        Integer marcadores = sessionService.get(userId);

        marcadores = marcadores != null ? marcadores + 1 : 1;

        sessionService.save(userId, marcadores);

        String htmlLike = "<!DOCTYPE html>"
                + "<html>"
                + "<body>"
                + "Cantidad de marcadores comprados " + marcadores
                + " <br> "
                + getFreeMem();

        return htmlLike;
    }

    @RequestMapping("/users/{userId}")
    public String get(@PathVariable String userId) {
        return "Cantidad de marcadores comprados " + sessionService.get(userId).toString();
    }

    private String getFreeMem() {
        Runtime runtime = Runtime.getRuntime();
        final long maxMemory = runtime.maxMemory();
        final long allocatedMemory = runtime.totalMemory();
        final long freeMemory = runtime.freeMemory();
        final long mb = 1024 * 1024;
        final String mega = " MB";
        return "Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / mb) + mega;
    }

}
