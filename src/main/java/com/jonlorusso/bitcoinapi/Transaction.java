package com.jonlorusso.bitcoinapi;

import java.util.List;

public class Transaction {

    private String subjectAddress;

    private String hash;
    private List<TransactionOutput> out;

    public String getHash() {
        return hash;
    }

    public List<TransactionOutput> getOut() {
        return out;
    }

    public String getSubjectAddress() {
        return subjectAddress;
    }

    public void setSubjectAddress(String subjectAddress) {
        this.subjectAddress = subjectAddress;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setOut(List<TransactionOutput> out) {
        this.out = out;
    }

}
