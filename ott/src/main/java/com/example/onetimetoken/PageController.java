package com.example.onetimetoken;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/index"})
    public String home() {
        return "index";
    }


    @GetMapping("/ott/sent")
    public String ottSent() {
        return "sent";
    }


    @GetMapping("/home")
    public String loggedIn() {
        return "loggedin";
    }
}