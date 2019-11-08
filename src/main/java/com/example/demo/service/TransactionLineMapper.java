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
                    .replaceAll("comment", "")
                    .replaceAll("]","")
                    .replaceAll("\\[",""));
        }

        String resultError = " ";
        Transaction transaction = new Transaction();

        try{
            transaction.setOrderid(Integer.parseInt(fieldList.get(0).trim()));
        }
        catch (Exception e){
            resultError += " Ошибка в исходном файле orderId = " + fieldList.get(0);
        }

        try{
            transaction.setCurrency(fieldList.get(2));
        }
        catch (Exception e){
            transaction.setCurrency("Not currency");
            resultError += " Ошибка в исходном файле currency = Not currency ";
        }

        try{
            transaction.setComment(fieldList.get(3));
        }
        catch (Exception e){
            transaction.setComment("Not comment");
            resultError += " Ошибка в исходном файле comment = Not comment ";
        }

        transaction.setLine(lineNumber);

        File file = new File(delegator);
        transaction.setFilename(file.getName());

        try{
            transaction.setAmount(Integer.parseInt(fieldList.get(1).trim()));
        }
        catch (Exception e){
            if(fieldList.size() >= 2){
                resultError += " Ошибка в исходном файле amount = " + fieldList.get(1);
            }
            else {
                resultError += " Ошибка в исходном файле amount = Not amount ";
            }
        }

        if(resultError.equals(" ")){
            transaction.setResult("OK");
        }
        else {
            transaction.setResult(resultError);
        }

        return  transaction;
    }
}