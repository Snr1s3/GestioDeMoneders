package gestio.moneders.moneders.service;

import org.springframework.stereotype.Service;
import gestio.moneders.moneders.dto.PersonaDto;
import gestio.moneders.moneders.mapper.PersonaMapper;
import gestio.moneders.moneders.model.Persona;
import gestio.moneders.moneders.repository.PersonaXlsxRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonaServiceImpl implements PersonaService {

    private final PersonaXlsxRepository personaRepository;
    private final PersonaMapper personaMapper;

    public PersonaServiceImpl( PersonaXlsxRepository personaRepository, PersonaMapper personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    @Override
    public Mono<PersonaDto> save(PersonaDto personaDto) {
        return Mono.fromCallable(() -> {
            Persona saved = personaRepository.save(personaMapper.toEntity(personaDto));
            return personaMapper.toDto(saved);
        });
    }

    @Override
    public Flux<PersonaDto> findAll() {
        return Flux.fromIterable(personaRepository.findAll())
                   .map(personaMapper::toDto);
    }

    @Override
    public Mono<PersonaDto> findById(Long id) {
        return Mono.justOrEmpty(personaRepository.findById(id))
                   .map(personaMapper::toDto);
    }

    @Override
    public Mono<PersonaDto> updateById(Long id, PersonaDto personaDto) {
        Persona entity = personaMapper.toEntity(personaDto);
        return Mono.justOrEmpty(personaRepository.updateById(id, entity))
                   .map(personaMapper::toDto);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return Mono.fromRunnable(() -> personaRepository.deleteById(id));
    }
}
