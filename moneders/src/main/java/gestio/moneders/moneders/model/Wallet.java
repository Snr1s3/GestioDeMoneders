package gestio.moneders.moneders.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Wallet {
    private Long id;
    private Long responsableGestio;
    private Long responsable;
    private Double dinersInicals;
    private Double dinersJustificats;
    private Double dinersFinals;
    private LocalDate dataEntrega;
    private LocalDate dataLimit;
    private String concepte;
    private boolean retornat;

}
