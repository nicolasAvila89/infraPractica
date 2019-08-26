package hello;

import java.text.NumberFormat;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
    final NumberFormat format = NumberFormat.getInstance();

    final Logger LOGGER = Logger.getLogger(UsersController.class.getName());

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
        LOGGER.info(String.format("%s-%s-UserID:%s,Markers:%s",System.getenv("ENV_NAME"),"SAVE",userId,marcadores));
        return htmlLike;
    }


    @RequestMapping("/users/{userId}")
    public String get(@PathVariable String userId, HttpSession httpSession) {
        Integer markers = sessionService.get(userId);
        if( markers == null ){
            LOGGER.info(String.format("%s-UserID:%s-NotFound",System.getenv("ENV_NAME"),userId));
            return "El usuario " + userId +  " no existe en el sistema";
        } else {
            LOGGER.info(String.format("%s-UserID:%s,Markers:%s",System.getenv("ENV_NAME"),userId,markers.toString()));
            return "Cantidad de marcadores comprados " + markers.toString();
        }
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
