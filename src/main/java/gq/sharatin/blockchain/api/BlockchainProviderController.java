package gq.sharatin.blockchain.api;

import gq.sharatin.blockchain.service.BlockchainService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
public class BlockchainProviderController {

    private final BlockchainService blockchainService;

    public BlockchainProviderController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @PostMapping("/blockchain/")
    public String sendToBlockchain(@RequestBody String data) throws ExecutionException, InterruptedException {
        return blockchainService.sendDataToBlockchain(data);
    }
}