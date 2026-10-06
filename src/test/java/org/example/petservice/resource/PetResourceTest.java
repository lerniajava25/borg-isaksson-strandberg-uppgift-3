package org.example.petservice.resource;

import org.example.petservice.service.PetService;
import org.junit.jupiter.api.BeforeEach;

import org.example.petservice.dto.PetDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;

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

        PetDTO createdPet = petResource.createPet(pet);

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
        PetDTO createdPet =
                petResource.createPet(new PetDTO(null, "Bosse", "Dog", 50, 50));

        PetDTO foundPet = petResource.getPetById(createdPet.getId());

        assertEquals("Bosse", foundPet.getName());
    }

    @Test
    void shouldFeedPet() {
        PetDTO createdPet =
                petResource.createPet(new PetDTO(null, "Bosse", "Dog", 50, 50));

        PetDTO fedPet = petResource.feedPet(createdPet.getId());

        assertEquals(35, fedPet.getHungerLevel());
    }

    @Test
    void shouldPlayWithPet() {
        PetDTO createdPet =
                petResource.createPet(new PetDTO(null, "Bosse", "Dog", 50, 50));

        PetDTO playedPet = petResource.playWithPet(createdPet.getId());

        assertEquals(65, playedPet.getHappiness());
    }

    @Test
    void shouldDeletePet() {
        PetDTO createdPet =
                petResource.createPet(new PetDTO(null, "Bosse", "Dog", 50, 50));

        petResource.deletePet(createdPet.getId());

        assertEquals(0, petResource.getAllPets().size());
    }

}



