package org.example.petservice.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.List;
import java.util.Map;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    public Response toResponse(ConstraintViolationException exception) {
        List<String> errors = exception.getConstraintViolations()
                .stream()
                .map(violation -> violation.getMessage())
                .toList();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("errors", errors))
                .build();
    }
}
