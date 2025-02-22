package io.positivinh.virtuoso.web.security.dummy.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dummy")
class DummyController {

    @GetMapping
    fun dummy() = "Hello Dummy!"

    @PreAuthorize("hasPermission(#name, 'read')")
    @GetMapping("/with-permission/{name}")
    fun withPermission(@PathVariable("name") name: String?) = "Permission has been evaluated $name"
}