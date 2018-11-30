package com.company;

import java.util.ArrayList;

public class DataStruct<E> {
    //Overview: classe di supporto per la gestione dei dati utente
    //Typical Element: <owner,data,{share.get(0)....share.get(shares.size()-1)}
    //IR: owner!= null && data != null && shares != null
    //forall i. 0<=i< shares.size() => share.get(i) != null

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
