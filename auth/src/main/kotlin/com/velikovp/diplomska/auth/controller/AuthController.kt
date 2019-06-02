package com.velikovp.diplomska.auth.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController {

    @RequestMapping("/auth", method = [RequestMethod.GET])
    fun authenticate(): String {
        return "secret"
    }
}