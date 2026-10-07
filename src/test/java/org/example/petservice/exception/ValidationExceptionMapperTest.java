package org.example.petservice.exception;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationExceptionMapperTest {
    @Test
    void shouldReturnBadRequestWithValidationErrors() {
        ValidationExceptionMapper mapper =
                new ValidationExceptionMapper();

        ConstraintViolation<?> violation =
                mock(ConstraintViolation.class);

        when(violation.getMessage())
                .thenReturn("Name is required");

        ConstraintViolationException exception =
                new ConstraintViolationException(Set.of(violation));

        Map<String, List<String>> entity;
        try (Response response = mapper.toResponse(exception)) {

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

            entity = (Map<String, List<String>>) response.getEntity();
        }

        assertTrue(entity.get("errors").contains("Name is required"));
    }

    @Test
    void shouldReturnMultipleValidationErrors() {
        ValidationExceptionMapper mapper =
                new ValidationExceptionMapper();

        ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
        ConstraintViolation<?> violation2 = mock(ConstraintViolation.class);

        when(violation1.getMessage())
                .thenReturn("Name is required");

        when(violation2.getMessage())
                .thenReturn("Hunger level must not exceed 100");

        ConstraintViolationException exception =
                new ConstraintViolationException(
                        Set.of(violation1, violation2)
                );

        Map<String, List<String>> entity;
        try (Response response = mapper.toResponse(exception)) {

            entity = (Map<String, List<String>>) response.getEntity();
        }

        assertTrue(entity.get("errors").contains("Name is required"));
        assertTrue(entity.get("errors").contains("Hunger level must not exceed 100"));
    }

}
