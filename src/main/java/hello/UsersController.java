package hello;

import java.text.NumberFormat;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.core.HazelcastInstance;

@RestController
public class UsersController {
    final NumberFormat format = NumberFormat.getInstance();

    final Logger LOGGER = Logger.getLogger(UsersController.class.getName());

    @Autowired
    public SessionService sessionService;

    @RequestMapping("/markers/purchase")
    public String save() {
        Integer markers = sessionService.getMarkers();

        markers = markers != null ? markers + 1 : 1;

        sessionService.saveMarker(markers);

        String htmlLike = "<!DOCTYPE html>"
                + "<html>"
                + "<body>"
                + "Cantidad de marcadores comprados " + markers
                + " <br> "
                + getFreeMem();
        LOGGER.info(String.format("%s-%s,Markers:%s", System.getenv("ENV_NAME"), "SAVE", markers));
        return htmlLike;
    }


    @RequestMapping("/markers")
    public String get() {
        Integer markers = sessionService.getMarkers();
        LOGGER.info(String.format("%s,Markers:%s", System.getenv("ENV_NAME"), markers.toString()));
        return "Cantidad de marcadores comprados " + markers.toString();
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
