package me.moriya.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinWebfluxExampleApplication

fun main(args: Array<String>) {
	runApplication<KotlinWebfluxExampleApplication>(*args)
}
