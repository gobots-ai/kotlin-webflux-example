package me.moriya.example.infra.exception

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ResponseStatusException

@Component
class CustomAttributes : DefaultErrorAttributes() {

    override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions): Map<String, Any> {
        val errorAttributesMap = super.getErrorAttributes(request, options)
        val throwable = getError(request)
        if (throwable is ResponseStatusException) {
            errorAttributesMap["message"] = throwable.reason
            // errorAttributesMap["developerMessage"] = throwable.reason
            return errorAttributesMap
        }
        return errorAttributesMap
    }

}