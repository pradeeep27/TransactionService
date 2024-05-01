package com.hackathon.transactionservice.controller;


import com.hackathon.transactionservice.model.Account;
import com.hackathon.transactionservice.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Validated
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;


    @GetMapping("/getBalance")
    public ResponseEntity<String> getBalance(@RequestParam("accountNumber") String accountNumber ) {
        Double balance = transactionService.getBalance(accountNumber);
        if(balance == null) {
            return new ResponseEntity<>("Not a valid account number", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("balance found is: " +balance, HttpStatus.OK);
    }

    @GetMapping("/getAccountDetails")
    public ResponseEntity<Account> getAccountDetails(@RequestParam("accountNumber") String accountNumber) {
        Account accountDetails = transactionService.getAccount(accountNumber);
        if(accountDetails == null) {
            return new ResponseEntity<>(new Account(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accountDetails, HttpStatus.OK);
    }

    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount(@RequestBody @Valid Account account) {
        boolean isSuccessful = transactionService.createAccount(account);

        if (isSuccessful) {
            return new ResponseEntity<>("Account created successfully ", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Account already exists with same account number "+ account.getAccountNumber(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam @Valid @NotBlank String accountNumber, @RequestParam @Valid @Positive double amount) {

        Double isDepositSuccessful = transactionService.depositAmount(accountNumber, amount);
        if(isDepositSuccessful == null) {
            return new ResponseEntity<>("Not a valid account for deposit", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Updated balance is: " +isDepositSuccessful, HttpStatus.OK);
    }

    @PutMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam @Valid @NotBlank String accountNumber, @RequestParam @Valid @Positive double amount) {

        Double isWithdrawSuccessful = transactionService.withdrawAmount(accountNumber, amount);
        if(isWithdrawSuccessful == null) {
            return new ResponseEntity<>("invalid account or insufficient balance", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Updated balance is: " +isWithdrawSuccessful, HttpStatus.OK);
    }

    @PostMapping("/thirdPartyTransfer")
    public ResponseEntity<String> thirdPartyTransfer(@RequestParam @Valid @NotBlank String debitAccountNumber, @RequestParam @Valid @NotBlank String creditAccountNumber, @RequestParam @Valid @Positive double amount) {
        boolean isTransferSuccessful = transactionService.thirdPartyTransfer(debitAccountNumber,creditAccountNumber,amount);
        if(isTransferSuccessful) {
            return new ResponseEntity<>("Third party transfer successfully ", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Third party transfer failed", HttpStatus.BAD_REQUEST);
    }

}
