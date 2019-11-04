package com.example.demo.service;

import com.example.demo.model.Transaction;
import org.springframework.batch.item.file.LineMapper;

import java.util.ArrayList;

public class TransactionLineMapper implements LineMapper<Transaction> {
    private String delegator;

    public TransactionLineMapper(String delegator) {
        this.delegator = delegator;
    }

    @Override
    public Transaction mapLine(String line, int lineNumber){
        String[] fields = line.split(",");
        ArrayList<String> fieldList = new ArrayList<>();

        for (int i = 0; i < fields.length; i++) {
            fieldList.add(fields[i]
                    .replaceAll("\\p{Punct}", "")
                    .replaceAll("orderId","")
                    .replaceAll("amount","")
                    .replaceAll("currency","")
                    .replaceAll("comment", ""));
        }

        Transaction transaction = new Transaction();
        transaction.setOrderid(Integer.parseInt(fieldList.get(0)));
        transaction.setResult("OK");
        transaction.setCurrency(fieldList.get(2));
        transaction.setComment(fieldList.get(3));
        transaction.setLine(lineNumber);
        transaction.setFilename(delegator);

        try{
            transaction.setAmount(Integer.parseInt(fieldList.get(1)));
        }
        catch (Exception e){
            transaction.setResult("Ошибка в исходном файле amount= " + fieldList.get(1));
        }

        return  transaction;
    }
}