package org.example.petservice.service;

import org.example.petservice.dto.PetDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@ApplicationScoped
public class PetService {

    private final Map<Long, PetDTO> petStorage = new ConcurrentHashMap<>();
    
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    private final ReentrantLock lock = new ReentrantLock();

    public PetDTO createPet(PetDTO petDTO) {
        Long newId = idGenerator.getAndIncrement();
        petDTO.setId(newId);
        petStorage.put(newId, petDTO);
        return petDTO;
    }

    public List<PetDTO> getAllPets() {
        return new ArrayList<>(petStorage.values());
    }

    public Optional<PetDTO> getPetById(Long id) {
        return Optional.ofNullable(petStorage.get(id));
    }

    public Optional<PetDTO> feedPet(Long id) {
        lock.lock();
        try {
            PetDTO pet = petStorage.get(id);
            if (pet == null) {
                return Optional.empty();
            }
            int newHunger = Math.max(0, pet.getHungerLevel() - 15);
            pet.setHungerLevel(newHunger);
            return Optional.of(pet);
        } finally {
            lock.unlock();
        }
    }

    public Optional<PetDTO> playWithPet(Long id) {
        lock.lock();
        try {
            PetDTO pet = petStorage.get(id);
            if (pet == null) {
                return Optional.empty();
            }
            int newHappiness = Math.min(100, pet.getHappiness() + 15);
            pet.setHappiness(newHappiness);
            return Optional.of(pet);
        } finally {
            lock.unlock();
        }
    }

    public boolean deletePet(Long id) {
        return petStorage.remove(id) != null;
    }
}
