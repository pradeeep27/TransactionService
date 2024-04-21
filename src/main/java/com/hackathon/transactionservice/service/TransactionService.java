package com.hackathon.transactionservice.service;

import com.hackathon.transactionservice.model.Account;
import org.springframework.stereotype.Service;


@Service
public interface TransactionService {

    boolean createAccount(Account account);

    Double getBalance(String accountNumber);

    Double depositAmount(String accountNumber, double amount);

    Double withdrawAmount(String accountNumber, double amount);

    Account getAccount(String accountNumber);

    boolean thirdPartyTransfer(String debitAccountNumber, String creditAccountNumber, double amount);
}
