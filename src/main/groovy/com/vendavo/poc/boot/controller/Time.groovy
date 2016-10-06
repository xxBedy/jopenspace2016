package com.vendavo.poc.boot.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller()
class Time {
    @RequestMapping("/currentTime")
    @ResponseBody
    String currentTime() {
        return new Date().toString();
    }
}
