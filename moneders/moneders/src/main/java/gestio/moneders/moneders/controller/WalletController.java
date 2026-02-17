package gestio.moneders.moneders.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gestio.moneders.moneders.dto.WalletDto;
import gestio.moneders.moneders.service.WalletService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    @Autowired
    private final WalletService walletService;

    

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<WalletDto> create(@RequestBody WalletDto walletDto) {
        return walletService.create(walletDto);
    }

    @GetMapping
    public Flux<WalletDto> findAll() {
        return walletService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<WalletDto> findById(@PathVariable Long id) {
        return walletService.findById(id);
    }

    @PutMapping("/{id}")
    public Mono<WalletDto> update(@PathVariable Long id, @RequestBody WalletDto walletDto) {
        return walletService.update(id, walletDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        walletService.delete(id);
    }
}
