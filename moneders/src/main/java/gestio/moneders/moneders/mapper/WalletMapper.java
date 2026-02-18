package gestio.moneders.moneders.mapper;

import gestio.moneders.moneders.dto.WalletDto;
import gestio.moneders.moneders.model.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public WalletDto toDto(Wallet wallet) {
        return new WalletDto(
            wallet.getId(),
            wallet.getResponsableGestio(),
            wallet.getResponsable(),
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
            walletDto.getId(),
            walletDto.getResponsableGestio(),
            walletDto.getResponsable(),
            walletDto.getDinersInicals(),
            walletDto.getDinersJustificats(),
            walletDto.getDinersFinals(),
            walletDto.getDataEntrega(),
            walletDto.getDataLimit(),
            walletDto.getConcepte(),
            walletDto.isRetornat()
        );
    }
}
