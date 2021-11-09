package me.moriya.example.domain.repository

import me.moriya.example.domain.document.person.Person
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface PersonRepository : ReactiveMongoRepository<Person, String> {
}