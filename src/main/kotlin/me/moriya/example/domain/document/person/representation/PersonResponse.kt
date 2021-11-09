package me.moriya.example.domain.document.person.representation

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PersonResponse(
    val id: String?,
    val firstname: String?,
    val surname: String?,
    val cpfCnpj: String?
) {
}