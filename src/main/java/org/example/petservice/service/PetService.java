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

    // Trådsäker lagring i minnet
    private final Map<Long, PetDTO> petStorage = new ConcurrentHashMap<>();
    
    // Trådsäker räknare för unika ID-nummer
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    // Explicit lås för atomära uppdateringar (feed och play)
    private final ReentrantLock lock = new ReentrantLock();

    // Sparar ett nytt husdjur och tilldelar ett unikt ID
    public PetDTO createPet(PetDTO petDTO) {
        Long newId = idGenerator.getAndIncrement();
        petDTO.setId(newId);
        petStorage.put(newId, petDTO);
        return petDTO;
    }

    // Hämtar alla husdjur som en ny lista (trådsäkert snapshot)
    public List<PetDTO> getAllPets() {
        return new ArrayList<>(petStorage.values());
    }

    // Hämtar ett specifikt husdjur insvept i en Optional om det inte finns
    public Optional<PetDTO> getPetById(Long id) {
        return Optional.ofNullable(petStorage.get(id));
    }

    // Atomär uppdatering för att mata husdjuret (minskar hunger)
    public Optional<PetDTO> feedPet(Long id) {
        lock.lock();
        try {
            PetDTO pet = petStorage.get(id);
            if (pet == null) {
                return Optional.empty();
            }
            // Minskar hungern med 15, men stannar som lägst på 0
            int newHunger = Math.max(0, pet.getHungerLevel() - 15);
            pet.setHungerLevel(newHunger);
            return Optional.of(pet);
        } finally {
            lock.unlock();
        }
    }

    // Atomär uppdatering för att leka med husdjuret (ökar glädje)
    public Optional<PetDTO> playWithPet(Long id) {
        lock.lock();
        try {
            PetDTO pet = petStorage.get(id);
            if (pet == null) {
                return Optional.empty();
            }
            // Ökar glädjen med 15, men stannar som högst på 100
            int newHappiness = Math.min(100, pet.getHappiness() + 15);
            pet.setHappiness(newHappiness);
            return Optional.of(pet);
        } finally {
            lock.unlock();
        }
    }

    // Tar bort (släpper) ett husdjur baserat på ID
    public boolean deletePet(Long id) {
        return petStorage.remove(id) != null;
    }
}
