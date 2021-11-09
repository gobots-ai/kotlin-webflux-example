package me.moriya.example.infra.exception

import kotlinx.coroutines.coroutineScope
import org.apache.commons.lang3.StringUtils
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import java.util.*

@Component
@Order(-2)
class GlobalExceptionHandler(
    errorAttributes: ErrorAttributes?,
    resourceProperties: WebProperties.Resources?,
    applicationContext: ApplicationContext?,
    codecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(errorAttributes, resourceProperties, applicationContext) {

    init {
        setMessageWriters(codecConfigurer.writers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes): RouterFunction<ServerResponse> =
        RouterFunctions.route(RequestPredicates.all()) {
                request: ServerRequest -> formatErrorResponse(request)
        }

    private fun formatErrorResponse(request: ServerRequest): Mono<ServerResponse> {
        val query = request.queryParam("trace")
        val errorAttributeOptions = if(query.orElse("false").toBoolean())
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE)
            else
                ErrorAttributeOptions.defaults()

        val errorAttributesMap = getErrorAttributes(request, errorAttributeOptions)
        val status = Optional.ofNullable(errorAttributesMap["status"]).orElse(HttpStatus.BAD_REQUEST.value()) as Int
        return ServerResponse
            .status(status)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(errorAttributesMap))
    }

}