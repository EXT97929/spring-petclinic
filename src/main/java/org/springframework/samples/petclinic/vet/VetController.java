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
package org.springframework.samples.petclinic.vet;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
class VetController {

  private final VetRepository vetRepository;

  @GetMapping("/vets")
  public ResponseEntity<List<Vet>> showVetList(@RequestParam(defaultValue = "1") int page) {
    Page<Vet> paginated = findPaginated(page);
    return new ResponseEntity<>(paginated.getContent(), HttpStatus.OK);
  }

  private Page<Vet> findPaginated(int page) {
    int pageSize = 5;
    Pageable pageable = PageRequest.of(page - 1, pageSize);
    return vetRepository.findAll(pageable);
  }
}
