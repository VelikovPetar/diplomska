package com.velikovp.diplomska.web.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    @RequestMapping("/home", method = [RequestMethod.GET])
    fun index(): String {
        return "Welcome to the main page!"
    }
}