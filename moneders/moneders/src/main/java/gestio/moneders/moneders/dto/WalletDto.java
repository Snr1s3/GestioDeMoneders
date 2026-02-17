package gestio.moneders.moneders.dto;

public record WalletDto(
        Long id,
        PersonaDto responsableGestio,
        PersonaDto responsable,
        float dinersInicals,
        float dinersJustificats,
        float dinersFinals,
        float dataEntrega,
        float dataLimit,
        String concepte,
        boolean retornat
) {
}