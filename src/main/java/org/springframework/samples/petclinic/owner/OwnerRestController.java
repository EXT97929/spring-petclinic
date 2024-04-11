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

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/owners")
public class OwnerRestController {

  private final OwnerRepository ownerRepository;

  @GetMapping
  public List<Owner> getAllOwners() {
    return ownerRepository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Owner> getOwnerById(@PathVariable Integer id) {
    return ownerRepository
        .findById(id)
        .map(owner -> new ResponseEntity<>(owner, HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  public ResponseEntity<Owner> createOwner(@RequestBody Owner owner) {
    return new ResponseEntity<>(ownerRepository.save(owner), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Owner> updateOwner(@PathVariable Integer id, @RequestBody Owner owner) {
    return ownerRepository
        .findById(id)
        .map(
            existingOwner -> {
              existingOwner.setFirstName(owner.getFirstName());
              existingOwner.setLastName(owner.getLastName());
              existingOwner.setAddress(owner.getAddress());
              existingOwner.setCity(owner.getCity());
              existingOwner.setTelephone(owner.getTelephone());
              ownerRepository.save(existingOwner);
              return new ResponseEntity<>(existingOwner, HttpStatus.OK);
            })
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOwner(@PathVariable Integer id) {
    return ownerRepository
        .findById(id)
        .map(
            owner -> {
              ownerRepository.delete(owner);
              return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            })
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}
