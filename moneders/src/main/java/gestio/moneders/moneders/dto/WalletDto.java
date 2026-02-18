package gestio.moneders.moneders.dto;

import java.time.LocalDate;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WalletDto {
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


    public String toDisplayString(Function<Long, String> personaNameResolver) {
        String respGestioName = personaNameResolver.apply(responsableGestio);
        String respName = personaNameResolver.apply(responsable);

        return "Wallet{" +
                "id=" + id +
                ", responsableGestio=" + respGestioName + " (" + responsableGestio + ")" +
                ", responsable=" + respName + " (" + responsable + ")" +
                ", dinersInicals=" + dinersInicals +
                ", dinersJustificats=" + dinersJustificats +
                ", dinersFinals=" + dinersFinals +
                ", dataEntrega=" + dataEntrega +
                ", dataLimit=" + dataLimit +
                ", concepte='" + concepte + '\'' +
                ", retornat=" + retornat +
                '}';
    }
}