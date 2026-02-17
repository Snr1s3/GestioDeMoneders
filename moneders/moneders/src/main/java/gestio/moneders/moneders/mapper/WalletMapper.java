package gestio.moneders.moneders.mapper;

import gestio.moneders.moneders.dto.WalletDto;
import gestio.moneders.moneders.model.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    private final PersonaMapper personaMapper;

    public WalletMapper(PersonaMapper personaMapper) {
        this.personaMapper = personaMapper;
    }

    public WalletDto toDto(Wallet wallet) {
        return new WalletDto(
                wallet.getId(),
                personaMapper.toDto(wallet.getResponsableGestio()),
                personaMapper.toDto(wallet.getResponsable()),
                wallet.getDinersInicals(),
                wallet.getDinersJustificats(),
                wallet.getDinersFinals(),
                wallet.getDataEntrega(),
                wallet.getDataLimit(),
                wallet.getConcepte(),
                wallet.isRetornat()
        );
    }

    public Wallet toEntity(WalletDto walletDto) {
        return new Wallet(
                walletDto.id(),
                personaMapper.toEntity(walletDto.responsableGestio()),
                personaMapper.toEntity(walletDto.responsable()),
                walletDto.dinersInicals(),
                walletDto.dinersJustificats(),
                walletDto.dinersFinals(),
                walletDto.dataEntrega(),
                walletDto.dataLimit(),
                walletDto.concepte(),
                walletDto.retornat()
        );
    }
}
