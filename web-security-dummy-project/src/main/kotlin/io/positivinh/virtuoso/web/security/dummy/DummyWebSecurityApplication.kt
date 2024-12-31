package io.positivinh.virtuoso.web.security.dummy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DummyWebSecurityApplication {
}

fun main(args: Array<String>) {

    runApplication<DummyWebSecurityApplication>(*args)
}