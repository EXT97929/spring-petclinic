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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/owners/{ownerId}/pets/{petId}/visits")
class VisitController {

  private final OwnerRepository ownersRepository;

  @PostMapping
  public ResponseEntity<Void> processNewVisitForm(
      @PathVariable int ownerId,
      @PathVariable int petId,
      @Valid Visit visit,
      BindingResult result) {

    if (result.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    Owner owner =
        this.ownersRepository
            .findById(ownerId)
            .orElseThrow(() -> new IllegalArgumentException("Owner ID not found: " + ownerId));

    if (owner.getPet(petId) == null) {
      throw new IllegalArgumentException("Pet ID not found: " + petId);
    }

    owner.addVisit(petId, visit);
    this.ownersRepository.save(owner);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
