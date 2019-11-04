package com.example.demo.service;

import com.example.demo.model.Transaction;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import org.springframework.batch.item.file.LineMapper;

import java.util.Map;

public class TransactionJsonLineMapper implements LineMapper<Transaction> {
    private String delegator;

    public TransactionJsonLineMapper(String delegator) {
        this.delegator = delegator;
    }

    private MappingJsonFactory factory = new MappingJsonFactory();

    @Override
    public Transaction mapLine(String line, int lineNumber) throws Exception {
        JsonParser parser = factory.createJsonParser(line);
        Map transactionMap = parser.readValueAs(Map.class);

        Transaction transaction = new Transaction();
        transaction.setOrderid((int)transactionMap.get("orderId"));
        transaction.setCurrency(((String)transactionMap.get("currency")));
        transaction.setComment((String)transactionMap.get("comment"));
        transaction.setLine(lineNumber);
        transaction.setFilename(delegator);
        transaction.setResult("OK");

        try{
            transaction.setAmount((int)transactionMap.get("amount"));
        }
        catch (Exception e){
            transaction.setResult("Ошибка в исходном файле amount= " + transactionMap.get("amount"));
        }

        return transaction;
    }
}