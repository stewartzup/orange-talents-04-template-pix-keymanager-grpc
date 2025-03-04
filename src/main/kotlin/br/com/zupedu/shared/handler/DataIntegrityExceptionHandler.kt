package br.com.zupedu.shared.handler

import br.com.zupedu.shared.handler.ExceptionHandler
import br.com.zupedu.shared.handler.ExceptionHandler.StatusWithDetails
import io.grpc.Status
import io.micronaut.context.MessageSource
import io.micronaut.context.MessageSource.MessageContext
import org.hibernate.exception.ConstraintViolationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataIntegrityExceptionHandler(@Inject var messageSource: MessageSource) : ExceptionHandler<ConstraintViolationException> {

    override fun handle(e: ConstraintViolationException): StatusWithDetails {

        val constraintName = e.constraintName
        if (constraintName.isNullOrBlank()) {
            return internalServerError(e)
        }

        val message = messageSource.getMessage("data.integrity.error.$constraintName", MessageContext.DEFAULT)
        return message
            .map { alreadyExistsError(it, e) } // TODO: dealing with many types of constraint errors
            .orElse(internalServerError(e))
    }

    override fun supports(e: Exception): Boolean {
        return e is ConstraintViolationException
    }

    private fun alreadyExistsError(message: String?, e: ConstraintViolationException) =
        StatusWithDetails(Status.ALREADY_EXISTS
            .withDescription(message)
            .withCause(e))

    private fun internalServerError(e: ConstraintViolationException) =
        StatusWithDetails(Status.INTERNAL
            .withDescription("Unexpected internal server error")
            .withCause(e))
}