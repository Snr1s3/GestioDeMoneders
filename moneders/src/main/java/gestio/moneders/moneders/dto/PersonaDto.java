package gestio.moneders.moneders.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PersonaDto {
    private Long id;
    private String nom;
    private String grup;
    private boolean membreGestio;
}
