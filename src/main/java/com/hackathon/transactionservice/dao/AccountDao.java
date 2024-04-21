package com.hackathon.transactionservice.dao;

import com.hackathon.transactionservice.enums.Type;
import com.hackathon.transactionservice.constants.Constants;
import com.hackathon.transactionservice.model.Account;
import com.hackathon.transactionservice.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@Repository
public class AccountDao{

    @Autowired
    private MongoTemplate mongoTemplate;


    public boolean createAccount(Account account) {
        try {
            if(account!=null) {
                account.setCreatedAt(OffsetDateTime.now());
                mongoTemplate.save(account, Constants.ACCOUNT_COLLECTION);
                if(account.getBalance()>0){
                    updateTransactionHistory(account);
                }
                return true;
            }
        }
        catch (Exception e) {
            log.error("Error creating account {} with exception", account,e);
            return false;
        }
        return false;
    }

    private void updateTransactionHistory(Account account) {
        try {
          Transaction transaction = Transaction.builder().accountNumber(account.getAccountNumber())
                  .transactionAmount(account.getBalance())
                  .transactionDate(OffsetDateTime.now())
                  .transactionType(Type.CREDIT)
                  .build();
          mongoTemplate.save(transaction, Constants.TRANSACTION_COLLECTION);
        }catch (Exception e) {
            log.error("Error updating transaction history for accountNumber {} with exception", account.getAccountNumber(),e);
        }
    }



    public Account getAccount(String accountNumber) {
        return getAccountDetails(accountNumber).orElse(null);
    }

    private Optional<Account> getAccountDetails(String accountNumber) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and(Constants.ACCOUNT_NUMBER).is(accountNumber);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Account.class, Constants.ACCOUNT_COLLECTION).stream().findFirst();
    }

    public Double getBalance(String accountNumber) {
        if(getAccountDetails(accountNumber).isPresent()) {
            return getAccountDetails(accountNumber).get().getBalance();
        }
        return null;
    }

    public Double depositAmount(String accountNumber, double amount) {
        try {
            Double currentBalance = getBalance(accountNumber);
            if (currentBalance != null) {
                currentBalance += amount;
                updateTransactionHistory(accountNumber,amount,Type.CREDIT);
                return updateAccountBalance(accountNumber, currentBalance);
            }
        }catch (Exception e){
            log.error("Error deposit amount {} with exception", amount,e);
        }
        log.info("Unable to Deposit amount {}", amount);
        return null;

    }

    private void updateTransactionHistory(String accountNumber, double amount, Type type) {
        try {
            Transaction transaction = Transaction.builder()
                .accountNumber(accountNumber)
                .transactionAmount(amount)
                .transactionType(type)
                .transactionDate(OffsetDateTime.now())
                .build();
            mongoTemplate.save(transaction, Constants.TRANSACTION_COLLECTION);
        }catch (Exception e) {
            log.error("Error updating transaction history for accountNumber {} with exception", accountNumber,e);
        }
    }

    public Double withdrawAmount(String accountNumber, double amount) {
        try {
            Double currentBalance = getBalance(accountNumber);
            if (currentBalance != null && amount <= currentBalance) {
                currentBalance -= amount;
                updateTransactionHistory(accountNumber,amount,Type.DEBIT);
                return updateAccountBalance(accountNumber, currentBalance);
            }
        }catch (Exception e){
            log.error("Error deposit amount {} with exception", amount,e);
        }
        log.info("Unable to Withdraw amount {}", amount);
        return null;
    }

    private Double updateAccountBalance(String accountNumber, Double currentBalance) {
        Criteria criteria = Criteria.where(Constants.ACCOUNT_NUMBER).is(accountNumber);
        Query query = new Query(criteria);
        Update update = new Update();
        update.set(Constants.ACCOUNT_BALANCE, currentBalance);
        update.set(Constants.UPDATED_AT, OffsetDateTime.now());
        mongoTemplate.findAndModify(query, update, Account.class, Constants.ACCOUNT_COLLECTION);
        return currentBalance;
    }

    public boolean thirdPartyTransfer(String debitAccountNumber, String creditAccountNumber, double amount) {
        try{
            Double debitAccountBal = getBalance(debitAccountNumber);
            Double creditAccountBal = getBalance(creditAccountNumber);
            if (debitAccountBal != null && amount <= debitAccountBal && creditAccountBal != null) {
                debitAccountBal -= amount;
                //deduct amount from debtor
                updateTransactionHistory(debitAccountNumber,amount,Type.DEBIT);
                updateAccountBalance(debitAccountNumber, debitAccountBal);
                creditAccountBal += amount;
                //add amount tp creditor
                updateTransactionHistory(creditAccountNumber,amount,Type.CREDIT);
                updateAccountBalance(creditAccountNumber, creditAccountBal);
                return true;
            }
        }catch (Exception e){
            log.error("Error in third party transfer amount {} with exception", amount,e);
        }
        log.info("Unable to do third party transfer for amount {}", amount);
        return false;
    }
}
