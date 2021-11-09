package me.moriya.example.resource

import me.moriya.example.domain.service.PersonService
import org.apache.commons.lang3.StringUtils
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.coRouter

@Component
class PersonController(private val personService: PersonService) {

    val empty = StringUtils.EMPTY

    @Bean
    fun create() = coRouter {

        "/person".nest {

            POST(empty, personService::create)
            GET(empty, personService::read)

            "/{id}".nest {
                GET(empty, personService::findById)
                PUT(empty, personService::update)
                DELETE(empty, personService::delete)
            }

        }

    }

}
