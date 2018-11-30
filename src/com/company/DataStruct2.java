package com.company;

import java.util.ArrayList;

public class DataStruct2<E> {
    //Overview: classe di supporto per la gestione dei dati utente
    //Typical Element: <data,{share.get(0)....share.get(shares.size()-1)}
    //IR: data != null && shares != null
    //forall i. 0<=i< shares.size() => share.get(i) != null

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

