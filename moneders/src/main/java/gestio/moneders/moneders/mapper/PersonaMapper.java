package gestio.moneders.moneders.mapper;

import gestio.moneders.moneders.dto.PersonaDto;
import gestio.moneders.moneders.model.Persona;
import org.springframework.stereotype.Component;

@Component
public class PersonaMapper {

    public PersonaDto toDto(Persona persona) {
        return new PersonaDto(
                persona.getId(),
                persona.getNom(),
                persona.getGrup(),
                persona.isMembreGestio()
        );
    }

    public Persona toEntity(PersonaDto personaDto) {
        return new Persona(
                personaDto.getId(),
                personaDto.getNom(),
                personaDto.getGrup(),
                personaDto.isMembreGestio()
        );
    }
}
