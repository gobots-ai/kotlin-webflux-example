package me.moriya.example.domain.vo

import com.google.common.base.Preconditions
import org.apache.commons.lang3.StringUtils
import java.util.*

data class Name private constructor(
    val firstname: String,
    val surname: String
)  {
    companion object {
        fun of(firstname: String, surname: String): Name {
            Preconditions.checkArgument(StringUtils.isNotBlank(firstname), "Firstname cannot be null or empty")
            Preconditions.checkArgument(StringUtils.isNotBlank(surname), "Surname cannot be null or empty")

            return Name(firstname, surname)
        }

        fun of(name: String): Name {
            Preconditions.checkArgument(StringUtils.isNotBlank(name), "Name cannot be null or empty")
            val value = name.split(" ")
            return Name(value[0], value[1])
        }
    }

    override fun toString(): String {
        return String.format("%s %s", firstname, surname)
    }

}