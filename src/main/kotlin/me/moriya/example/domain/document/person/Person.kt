package me.moriya.example.domain.document.person

import me.moriya.example.domain.vo.Name
import me.yanaga.opes.CpfCnpj
import org.apache.commons.lang3.StringUtils
import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Person(
    @Id
    var id: String? = null,
    @NotNull
    var name: Name,
    var cpfCnpj: CpfCnpj
) {
    fun withName(firstname: String, surname: String) = apply {
        this.name = Name.of(firstname, surname)
    }

    fun withId(id: String?) = apply {
        this.id = id
    }

    fun withCpfCnpj(cpfCnpj: String?) = apply {
        if(StringUtils.isNotBlank(cpfCnpj))
            this.cpfCnpj = CpfCnpj.of(cpfCnpj)
    }

}