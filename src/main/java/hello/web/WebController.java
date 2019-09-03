package hello.web;

import hello.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * User: Diego Dakszewicz - diego@nubing.net
 * Date: 02/09/19 11:16
 */
@Controller
public class WebController {

    @GetMapping("/hello")
    public String hello(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
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
    public String addUserSubmit(Model model,@ModelAttribute User user) {
        model.addAttribute("user", user);
        return "userCreated";
    }


}
