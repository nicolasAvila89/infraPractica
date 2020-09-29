package hello.web;

import hello.Application;
import hello.SessionService;
import hello.model.Message;
import hello.model.OutputMessage;
import hello.model.StressThread;
import hello.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.security.provider.DSAPrivateKey;
import sun.security.rsa.RSAPrivateCrtKeyImpl;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.LongAdder;
import java.util.logging.Logger;

/**
 * User: Diego Dakszewicz - diego@nubing.net
 * Date: 02/09/19 11:16
 */
@Controller
public class WebController {

    @Autowired
    private SessionService sessionService;

    final Logger LOGGER = Logger.getLogger(Application.class.getName());


    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

    @GetMapping("/responsive")
    public String responsive() {
        return "responsive";
    }

    @GetMapping("/materials")
    public String materials(Model model) {
        model.addAttribute("user", new User());
        return "materials";
    }


    @PostMapping("/addUser")
    public String addUserSubmit(Model model, @ModelAttribute User user) {
        model.addAttribute("user", user);
        return "userCreated";
    }

    @GetMapping("/simpleChat")
    public String chat() {
        return "chat";
    }

    @GetMapping("/vueChat")
    public String vueChat() {
        return "vueChat";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message) throws Exception {
        return new OutputMessage(message.getFrom(), message.getText());
    }

    @GetMapping("/")
    public String infra(Model model, HttpServletRequest request) {
        Runtime runtime = Runtime.getRuntime();

        Integer hits = sessionService.addHit();
        HttpSession session = sessionService.getSession();
        String server = System.getenv("ENV_NAME");
        //Si voy por la otra session obtengo un proxy
        String sessionType = request.getSession().getClass().getSimpleName();
        if (server == null) {
            server = "Local";
        }
        LOGGER.info(server + " hits was " + hits + " session id " + session.getId() + " session type " + sessionType);

        model.addAttribute("serverName", server);
        model.addAttribute("hits", hits);
        model.addAttribute("sessionId", session.getId());
        model.addAttribute("sessionType", sessionType);
        model.addAttribute("free", getMBRepresent(runtime.freeMemory()));
        model.addAttribute("allocated", getMBRepresent(runtime.totalMemory()));
        model.addAttribute("max", getMBRepresent(runtime.maxMemory()));
        model.addAttribute("total", getMBRepresent(runtime.freeMemory() + (runtime.maxMemory() - runtime.totalMemory())));
        return "infra";
    }

    @GetMapping("/leak")
    public String leak(Model model, HttpServletRequest request) {
        sessionService.memoryFill();
        return infra(model, request);
    }

    @GetMapping("/stress")
    public String stress(Model model, HttpServletRequest request) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        LongAdder counter = new LongAdder();
        StressThread stress = new StressThread(counter);
        Thread t = new Thread(stress);
        t.start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                stress.stop();
            }
        },60000);
        return infra(model, request);
    }

    private String getMBRepresent(long memory) {
        NumberFormat format = NumberFormat.getInstance();
        long mb = 1024 * 1024;
        String mega = " MB";
        return format.format(memory / mb) + mega;
    }

}
