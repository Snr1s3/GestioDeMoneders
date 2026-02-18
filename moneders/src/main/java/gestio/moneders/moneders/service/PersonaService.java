package gestio.moneders.moneders.service;

import gestio.moneders.moneders.dto.PersonaDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface PersonaService {

    Mono<PersonaDto> save(PersonaDto personaDto);

    Flux<PersonaDto> findAll();

    Mono<PersonaDto> findById(Long id);

    Mono<PersonaDto> updateById(Long id, PersonaDto personaDto);

    Mono<Void> deleteById(Long id);
}
