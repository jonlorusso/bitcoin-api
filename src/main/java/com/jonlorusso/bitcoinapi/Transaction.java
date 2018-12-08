package com.jonlorusso.bitcoinapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class Transaction {
    private String hash;
    private List<TransactionOutput> out;

    public String getHash() {
        return hash;
    }

    public List<TransactionOutput> getOut() {
        return out;
    }
}
