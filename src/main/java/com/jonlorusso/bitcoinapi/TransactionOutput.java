package com.jonlorusso.bitcoinapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionOutput {
    private String txHash;

    private boolean spent;
    private String addr;
    private long value;
    private int n;

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    @JsonIgnore
    public boolean isSpent() {
        return spent;
    }

    @JsonProperty
    public void setSpent(boolean spent) {
        this.spent = spent;
    }

    @JsonIgnore
    public String getAddr() {
        return addr;
    }

    @JsonProperty
    public void setAddr(String addr) {
        this.addr = addr;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @JsonProperty("output_idx")
    public int getN() {
        return n;
    }

    @JsonProperty("n")
    public void setN(int n) {
        this.n = n;
    }
}
