package com.akivaliaho.AkiTestingClientCode;

/**
 * Created by vagrant on 3/1/17.
 */
public class BeerService {
    public String noArgsString;

    public BeerService(String noArgsString) {
        this.noArgsString = noArgsString;
    }

    public String getNoArgsString() {
        return noArgsString;
    }

    public void setNoArgsString(String noArgsString) {
        this.noArgsString = noArgsString;
    }
}
