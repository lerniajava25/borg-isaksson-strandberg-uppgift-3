
package org.example.petservice.resource;

import jakarta.ws.rs.core.Response;
import org.example.petservice.dto.PetDTO;
import org.example.petservice.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PetResourceTest {

    private PetResource petResource;

    @BeforeEach
    void setUp() throws Exception {
        petResource = new PetResource();
        PetService petService = new PetService();

        Field field = PetResource.class.getDeclaredField("petService");
        field.setAccessible(true);
        field.set(petResource, petService);
    }

    @Test
    void shouldCreatePet() {
        PetDTO pet = new PetDTO(null, "Bosse", "Dog", 50, 50);

        Response response = petResource.createPet(pet);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        PetDTO createdPet = (PetDTO) response.getEntity();

        assertNotNull(createdPet.getId());
        assertEquals("Bosse", createdPet.getName());
    }

    @Test
    void shouldGetAllPets() {
        petResource.createPet(new PetDTO(null, "Bosse", "Dog", 50, 50));
        petResource.createPet(new PetDTO(null, "Misse", "Cat", 40, 60));

        var pets = petResource.getAllPets();

        assertEquals(2, pets.size());
    }

    @Test
    void shouldGetPetById() {
        Response response =
                petResource.createPet(new PetDTO(null, "Bosse", "Dog", 50, 50));

        PetDTO createdPet = (PetDTO) response.getEntity();

        PetDTO foundPet = petResource.getPetById(createdPet.getId());

        assertEquals("Bosse", foundPet.getName());
    }

    @Test
    void shouldFeedPet() {
        Response response =
                petResource.createPet(new PetDTO(null, "Bosse", "Dog", 50, 50));

        PetDTO createdPet = (PetDTO) response.getEntity();

        PetDTO fedPet = petResource.feedPet(createdPet.getId());

        assertEquals(35, fedPet.getHungerLevel());
    }

    @Test
    void shouldPlayWithPet() {
        Response response =
                petResource.createPet(new PetDTO(null, "Bosse", "Dog", 50, 50));

        PetDTO createdPet = (PetDTO) response.getEntity();

        PetDTO playedPet = petResource.playWithPet(createdPet.getId());

        assertEquals(65, playedPet.getHappiness());
    }

    @Test
    void shouldDeletePet() {
        Response response =
                petResource.createPet(new PetDTO(null, "Bosse", "Dog", 50, 50));

        PetDTO createdPet = (PetDTO) response.getEntity();

        petResource.deletePet(createdPet.getId());

        assertEquals(0, petResource.getAllPets().size());
    }
}




