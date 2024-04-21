package com.hackathon.transactionservice.service;

import com.hackathon.transactionservice.dao.AccountDao;
import com.hackathon.transactionservice.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    AccountDao accountDao;

    @Override
    public boolean createAccount(Account account) {
        if(!checkAccountExists(account.getAccountNumber())) {
            return accountDao.createAccount(account);
        }
        return false;
    }

    private boolean checkAccountExists(String accountNumber) {
        return Objects.nonNull(accountDao.getAccount(accountNumber));
    }

    @Override
    public Double getBalance(String accountNumber) {
        return accountDao.getBalance(accountNumber);
    }

    @Override
    public Double depositAmount(String accountNumber, double amount) {
        if(checkAccountExists(accountNumber)) {
            return accountDao.depositAmount(accountNumber, amount);
        }
        return null;
    }

    @Override
    public Double withdrawAmount(String accountNumber, double amount) {
        if(checkAccountExists(accountNumber)) {
            return accountDao.withdrawAmount(accountNumber, amount);
        }
        return null;
    }

    @Override
    public Account getAccount(String accountNumber) {
        return accountDao.getAccount(accountNumber);
    }

    @Override
    public boolean thirdPartyTransfer(String debitAccountNumber, String creditAccountNumber, double amount) {
        if(checkAccountExists(debitAccountNumber) && checkAccountExists(creditAccountNumber)) {
            return accountDao.thirdPartyTransfer(debitAccountNumber,creditAccountNumber,amount);
        }
        else{
            return false;
        }
    }
}
