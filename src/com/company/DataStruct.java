package com.company;

import java.util.ArrayList;

public class DataStruct<E> {

    private String owner;
    private E data;
    private ArrayList<String> shares;


    public DataStruct(String owner, E data)
    {
        this.owner=owner;
        this.data=data;
        this.shares= new ArrayList<>();
    }

    public String getOwner() {
        return owner;
    }

    public ArrayList<String> getShares() {
        return shares;
    }

    public E getData() {
        return data;
    }

    public void addShare(String Id)
    {
      shares.add(Id);
    }
}
