package org.example.petservice.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.example.petservice.dto.PetDTO;
import org.example.petservice.service.PetService;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;

import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;

import java.util.List;

@Path("/pets")
public class PetResource {

    @Inject
    private PetService petService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PetDTO> getAllPets() {
        return petService.getAllPets();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PetDTO getPetById(@PathParam("id") Long id) {
        return petService.getPetById(id)
                .orElseThrow(() -> new NotFoundException("Pet not found"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PetDTO createPet(PetDTO petDTO) {
        return petService.createPet(petDTO);
    }

    @PUT
    @Path("/{id}/feed")
    @Produces(MediaType.APPLICATION_JSON)
    public PetDTO feedPet(@PathParam("id") Long id) {
        return petService.feedPet(id)
                .orElseThrow(() -> new NotFoundException("Pet not found"));
    }

    @PUT
    @Path("/{id}/play")
    @Produces(MediaType.APPLICATION_JSON)
    public PetDTO playWithPet(@PathParam("id") Long id) {
        return petService.playWithPet(id)
                .orElseThrow(() -> new NotFoundException("Pet not found"));
    }

    @DELETE
    @Path("/{id}")
    public void deletePet(@PathParam("id") Long id) {
        boolean deleted = petService.deletePet(id);

        if (!deleted) {
            throw new NotFoundException("Pet not found");
        }
    }

}



