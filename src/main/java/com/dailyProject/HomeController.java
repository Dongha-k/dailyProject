package com.dailyProject;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String home(){
        return "index";
    }

    @RequestMapping("/login")
    public String login(@RequestParam(defaultValue = "false") Boolean error,
                        Model model){
        if(error){
            model.addAttribute("errorMessage", "아이디나 패스워드가 잘못되었습니다.");
        }
        return "login";
    }
}
