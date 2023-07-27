package com.wowguild.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    @GetMapping("/{path:[^\\.]+}")
    public String index() {
        return "forward:/";
    }

    @GetMapping(value = "/edit/{id}")
    public String index2(@PathVariable("id") long id){

        return "forward:/";
    }

}
