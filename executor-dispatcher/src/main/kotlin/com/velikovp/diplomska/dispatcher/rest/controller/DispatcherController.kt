package com.velikovp.diplomska.dispatcher.rest.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class DispatcherController {

    @Value("\${jwt.token.secret}")
    lateinit var secretKey: String

    /**
     *
     * TODO: replace <out Any> with correct return type.
     */
    @RequestMapping("execute", method = [RequestMethod.GET])
    fun dispatchExecution(): String {
        return "Execute: OK"
    }
}