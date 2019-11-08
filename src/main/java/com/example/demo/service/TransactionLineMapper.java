package com.example.demo.service;

import com.example.demo.model.Transaction;
import org.springframework.batch.item.file.LineMapper;

import java.io.File;
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

        transaction.setResult("OK");

        try{
            transaction.setOrderid(Integer.parseInt(fieldList.get(0).trim()));
        }
        catch (Exception e){
            transaction.setResult("Ошибка в исходном файле orderId = " + fieldList.get(0));
        }

        try{
            transaction.setCurrency(fieldList.get(2));
        }
        catch (Exception e){
            transaction.setCurrency("Not currency");
            transaction.setResult("Ошибка в исходном файле currency = Not currency");
        }

        try{
            transaction.setComment(fieldList.get(3));
        }
        catch (Exception e){
            transaction.setComment("Not comment");
            transaction.setResult("Ошибка в исходном файле comment = Not comment");
        }

        transaction.setLine(lineNumber);

        File file = new File(delegator);
        transaction.setFilename(file.getName());

        try{
            transaction.setAmount(Integer.parseInt(fieldList.get(1).trim()));
        }
        catch (Exception e){
            if(fieldList.size() >= 2){
                transaction.setResult("Ошибка в исходном файле amount = " + fieldList.get(1));
            }
            else {
                transaction.setResult("Ошибка в исходном файле amount = Not amount");
            }
        }

        return  transaction;
    }
}