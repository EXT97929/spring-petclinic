/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/owners/{ownerId}")
class PetController {

  private final OwnerRepository owners;

  @GetMapping
  public Collection<Pet> getPets(@PathVariable("ownerId") int ownerId) {
    return this.owners
        .findById(ownerId)
        .map(Owner::getPets)
        .orElseThrow(() -> new IllegalArgumentException("Owner ID not found: " + ownerId));
  }

  @GetMapping("/{petId}")
  public Pet getPet(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId) {
    return this.owners
        .findById(ownerId)
        .map(owner -> owner.getPet(petId))
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "Owner ID or Pet ID not found: " + ownerId + ", " + petId));
  }

  @PostMapping
  public ResponseEntity<Void> addPet(@PathVariable("ownerId") int ownerId, @RequestBody Pet pet) {
    Owner owner = getOwner(ownerId);
    owner.addPet(pet);
    this.owners.save(owner);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/{petId}")
  public ResponseEntity<Void> updatePet(
      @PathVariable("ownerId") int ownerId,
      @PathVariable("petId") int petId,
      @RequestBody Pet pet) {

    Owner owner = getOwner(ownerId);
    Pet existingPet = owner.getPet(petId);
    if (existingPet == null) {
      throw new IllegalArgumentException("Pet ID not found: " + petId);
    }
    existingPet.setName(pet.getName());
    existingPet.setBirthDate(pet.getBirthDate());
    existingPet.setType(pet.getType());
    this.owners.save(owner);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private Owner getOwner(int ownerId) {
    return this.owners
        .findById(ownerId)
        .orElseThrow(() -> new IllegalArgumentException("Owner ID not found: " + ownerId));
  }
}
