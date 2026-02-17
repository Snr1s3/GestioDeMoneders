package gestio.moneders.moneders.controller;

import gestio.moneders.moneders.dto.WalletDto;
import gestio.moneders.moneders.service.WalletService;
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

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletDto create(@RequestBody WalletDto walletDto) {
        return walletService.create(walletDto);
    }

    @GetMapping
    public List<WalletDto> findAll() {
        return walletService.findAll();
    }

    @GetMapping("/{id}")
    public WalletDto findById(@PathVariable Long id) {
        return walletService.findById(id);
    }

    @PutMapping("/{id}")
    public WalletDto update(@PathVariable Long id, @RequestBody WalletDto walletDto) {
        return walletService.update(id, walletDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        walletService.delete(id);
    }
}
