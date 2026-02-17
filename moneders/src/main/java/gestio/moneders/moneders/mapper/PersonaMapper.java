package gestio.moneders.moneders.mapper;

import gestio.moneders.moneders.dto.PersonaDto;
import gestio.moneders.moneders.model.Persona;
import org.springframework.stereotype.Component;

@Component
public class PersonaMapper {

    public PersonaDto toDto(Persona persona) {
        if (persona == null) {
            return null;
        }

        return new PersonaDto(
                persona.getId(),
                persona.getNom(),
                persona.getGrup(),
                persona.isMembreGestio()
        );
    }

    public Persona toEntity(PersonaDto personaDto) {
        if (personaDto == null) {
            return null;
        }

        return Persona.builder()
                .id(personaDto.id())
                .nom(personaDto.nom())
                .grup(personaDto.grup())
                .membreGestio(personaDto.membreGestio())
                .build();
    }
}
