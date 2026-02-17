package gestio.moneders.moneders.model;

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
public class Wallet {
    private Long id;
    private Persona responsableGestio;
    private Persona responsable;
    private float dinersInicals;
    private float dinersJustificats;
    private float dinersFinals;
    private float dataEntrega;
    private float dataLimit;
    private String concepte;
    private boolean retornat;
}
