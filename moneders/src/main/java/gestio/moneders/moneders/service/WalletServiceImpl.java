package gestio.moneders.moneders.service;

import org.springframework.stereotype.Service;
import gestio.moneders.moneders.dto.WalletDto;
import gestio.moneders.moneders.mapper.WalletMapper;
import gestio.moneders.moneders.model.Wallet;
import gestio.moneders.moneders.repository.WalletXlsxRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletXlsxRepository walletRepository;
    private final WalletMapper walletMapper;

    public WalletServiceImpl( WalletXlsxRepository walletRepository, WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    @Override
    public Mono<WalletDto> save(WalletDto walletDto) {
        return Mono.fromCallable(() -> {
            Wallet saved = walletRepository.save(walletMapper.toEntity(walletDto));
            return walletMapper.toDto(saved);
        });
    }

    @Override
    public Flux<WalletDto> findAll() {
        return Flux.fromIterable(walletRepository.findAll())
                   .map(walletMapper::toDto);
    }

    @Override
    public Mono<WalletDto> findById(Long id) {
        return Mono.justOrEmpty(walletRepository.findById(id))
                   .map(walletMapper::toDto);
    }

    @Override
    public Mono<WalletDto> updateById(Long id, WalletDto walletDto) {
        Wallet entity = walletMapper.toEntity(walletDto);
        return Mono.justOrEmpty(walletRepository.updateById(id, entity))
                   .map(walletMapper::toDto);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return Mono.fromRunnable(() -> walletRepository.deleteById(id));
    }
}
