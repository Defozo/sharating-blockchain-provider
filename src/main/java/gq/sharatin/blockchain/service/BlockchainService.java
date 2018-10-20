package gq.sharatin.blockchain.service;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Service
public class BlockchainService {

    public String sendDataToBlockchain(String data) throws ExecutionException, InterruptedException {
        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
        byte[] digest = digestSHA3.digest(data.getBytes());
        System.out.println(Hex.toHexString(digest));
        return sendHexToBlockchain(Hex.toHexString(digest));
    }

    public String sendHexToBlockchain(String hex) throws ExecutionException, InterruptedException {
        Web3j web3 = Web3j.build(new HttpService("https://ropsten.infura.io/v3/2376b03822ce47d5b2c8c914348f1f1f"));
        Credentials credentials = Credentials.create(System.getenv("PRIVATE_KEY"));
        EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(
                "0x453e0cc25E4Cb3D20CC99ef79a2F352bAFAC55c7", DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce, BigInteger.valueOf(100000L), BigInteger.valueOf(100000L), "0x453e0cc25E4Cb3D20CC99ef79a2F352bAFAC55c7", hex);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
        return ethSendTransaction.getTransactionHash();
    }

}
