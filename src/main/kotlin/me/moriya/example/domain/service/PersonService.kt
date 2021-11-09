package me.moriya.example.domain.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import me.moriya.example.domain.document.person.Person
import me.moriya.example.domain.document.person.representation.PersonRequest
import me.moriya.example.domain.document.person.representation.PersonResponse
import me.moriya.example.domain.repository.PersonRepository
import me.moriya.example.domain.vo.Name
import me.yanaga.opes.CpfCnpj
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.*
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
data class PersonService(private val personRepository: PersonRepository) {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    suspend fun create(request: ServerRequest): ServerResponse = coroutineScope {
        log.info { "Creating person " }

        request
            .body(BodyExtractors.toMono(PersonRequest::class.java))
            .flatMap {
                personRepository.save(
                    Person(name = Name.of(it.firstname, it.surname), cpfCnpj = CpfCnpj.of(it.cpfCnpj))
                )
            }.onErrorResume {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Error to create a person. Cause: ${it.message}")
            }.awaitSingle()

        ServerResponse
            .noContent()
            .buildAndAwait()
    }

    suspend fun read(request: ServerRequest): ServerResponse = coroutineScope {
        log.info { "Reading person " }
        ServerResponse
            .ok()
            .bodyAndAwait(
                personRepository.findAll()
                    .map {
                        PersonResponse(it.id, it.name.firstname, it.name.surname, it.cpfCnpj.toString())
                    }.switchIfEmpty {
                        throw ResponseStatusException(HttpStatus.NOT_FOUND, "People not found")
                    }.onErrorResume {
                        throw it
                    }.asFlow()
            )
    }

    suspend fun update(request: ServerRequest): ServerResponse = coroutineScope {
        log.info { "Updating person" }
        val id = request.pathVariable("id")
        val body = request.awaitBody<PersonRequest>()

        findBy(id)
            .flatMap {
                val person = Person(name = Name.of(body.firstname, body.surname), cpfCnpj = CpfCnpj.of(body.cpfCnpj))
                    .withId(it.id)

                    personRepository.save(person)
            }.onErrorResume {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro to update person with id $id. Cause ${it.message}")
            }.awaitSingle()

        ServerResponse
            .noContent()
            .buildAndAwait()
    }

    suspend fun delete(request: ServerRequest): ServerResponse = coroutineScope {
        val id = request.pathVariable("id")
        log.info { "Deleting person with $id" }

        findBy(id)
            .flatMap {
                it.id?.let { it1 -> personRepository.deleteById(it1) }
            }.onErrorResume {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Error to delete person with id $id. Cause ${it.message}")
            }.awaitSingle()

        ServerResponse
            .noContent()
            .buildAndAwait()
    }

    suspend fun findById(request: ServerRequest): ServerResponse = coroutineScope {
        ServerResponse
            .ok()
            .bodyAndAwait(
                findBy(request.pathVariable("id"))
                    .asFlow()
            )
    }


    private suspend fun findBy(id: String) : Mono<PersonResponse> = coroutineScope {
        personRepository.findById(id).map {
            PersonResponse(it.id, it.name.firstname, it.name.surname, it?.cpfCnpj.toString())
        }.switchIfEmpty {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Person with $id not found")
        }
    }

}