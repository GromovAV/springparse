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
        if(line.isEmpty()) return null;
        String[] fields = line.split(",");
        ArrayList<String> fieldList = new ArrayList<>();
        StringBuilder resultError = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            fieldList.add(fields[i]
                    .replaceAll("\\p{Punct}", "")
                    .replaceAll("orderId","")
                    .replaceAll("amount","")
                    .replaceAll("currency","")
                    .replaceAll("comment", ""));
        }

        Transaction transaction = new Transaction();

        try{
            transaction.setOrderid(Integer.parseInt(fieldList.get(0).trim()));
        }
        catch (Exception e){
            resultError.append("Ошибка orderId =" + fieldList.get(0));
        }

        try{
            transaction.setCurrency(fieldList.get(2));
        }
        catch (Exception e){
            transaction.setCurrency("Not currency");
            resultError.append("Ошибка currency = Not currency ");
        }

        try{
            transaction.setComment(fieldList.get(3));
        }
        catch (Exception e){
            transaction.setComment("Not comment");
            resultError.append("Ошибка comment = Not comment ");
        }

        transaction.setLine(lineNumber);

        File file = new File(delegator);
        transaction.setFilename(file.getName());

        try{
            transaction.setAmount(Integer.parseInt(fieldList.get(1).trim()));
        }
        catch (Exception e){
            if(fieldList.size() >= 2){
                resultError.append("Ошибка amount =" + fieldList.get(1));
            }
            else {
                resultError.append("Ошибка amount = Not amount ");
            }
        }

        if(resultError.length() == 0){
            transaction.setResult("OK");
        }
        else {
            transaction.setResult(resultError.toString());
        }

        return  transaction;
    }
}