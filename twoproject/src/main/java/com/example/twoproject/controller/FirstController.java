package com.example.twoproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //컨트롤러 선언
public class FirstController {
    @GetMapping("/bye")
    public String seeYouNext(Model model){
        model.addAttribute("nickname","홍길동");
        return "goodbye";
    }
    @GetMapping("/hi")
    public String niceMeetYou(Model model){
        model.addAttribute("username","홍길동");
        return "greetings";
    }
}