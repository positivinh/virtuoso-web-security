package io.positivinh.virtuoso.web.security.dummy.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/api/dummy")
class DummyController {

    @GetMapping
    fun dummy() = "Hello Dummy!"
}