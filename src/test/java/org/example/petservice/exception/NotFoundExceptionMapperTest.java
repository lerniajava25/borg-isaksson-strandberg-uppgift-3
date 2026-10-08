package org.example.petservice.exception;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotFoundExceptionMapperTest {
    @Test
    void shouldReturnNotFoundResponse() {
        NotFoundExceptionMapper mapper = new NotFoundExceptionMapper();
        NotFoundException exception = new NotFoundException("Pet not found");
        Map<String, String> entity;
        try (Response response = mapper.toResponse(exception)) {

            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

            entity = (Map<String, String>) response.getEntity();
        }

        assertEquals("Pet not found", entity.get("error"));
    }
}
