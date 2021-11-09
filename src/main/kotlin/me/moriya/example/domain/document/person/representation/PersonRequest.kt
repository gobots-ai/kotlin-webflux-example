package me.moriya.example.domain.document.person.representation

data class PersonRequest(
    val firstname: String,
    val surname: String,
    val cpfCnpj: String? = null
)