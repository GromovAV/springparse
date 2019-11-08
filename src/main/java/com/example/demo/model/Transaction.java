package com.example.demo.model;

@SuppressWarnings("restriction")
public class Transaction{
    private int orderid = 0;
    private int amount = 0;
    private String currency;
    private String comment;
    private String filename;
    private int line;
    private String result;

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String toString() {
        if(amount == 0){
            return "{id: " + orderid + ", comment: " + comment  +
                    ", filename: " + filename + ", line: " + line + ", result: " + result + "}";
        }
        else if(orderid == 0){
            return "{" + "amount: " + amount + ", comment: " + comment  +
                    ", filename: " + filename + ", line: " + line + ", result: " + result + "}";
        }
        else {
            return "{id: " + orderid + ", amount: " + amount + ", comment: " + comment  +
                    ", filename: " + filename + ", line: " + line + ", result: " + result + "}";
        }
    }
}