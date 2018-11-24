package com.company;

import java.util.ArrayList;

public class DataStruct2<E> {

    private E data;
    private ArrayList<String> shares;


    public DataStruct2(E data)
    {

        this.data=data;
        this.shares= new ArrayList<>();

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

