package gestio.moneders.moneders.service;

import gestio.moneders.moneders.dto.WalletDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface WalletService {

    Mono<WalletDto> save(WalletDto walletDto);

    Flux<WalletDto> findAll();

    Mono<WalletDto> findById(Long id);

    Mono<WalletDto> updateById(Long id, WalletDto walletDto);

    Mono<Void> deleteById(Long id);
}
