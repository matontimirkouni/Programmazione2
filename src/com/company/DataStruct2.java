package com.company;

import java.util.ArrayList;

public class DataStruct2<E> {
    //Overview: classe di supporto per la gestione dei dati utente
    //Typical Element: <data,{share.get(0)....share.get(shares.size()-1)}
    //IR: data != null && shares != null
    //forall i. 0<=i< shares.size() => share.get(i) != null

    private E data;
    private ArrayList<String> shares;

    public DataStruct2(E data) throws NullPointerException
    {
        if(data== null) throw new NullPointerException();
        this.data=data;
        this.shares= new ArrayList<>();
    }
    /**
     @REQUIRES: data != null
     @EFFECTS: Inizializza this.data e this.shares ai valori passati dal chiamante
     @THROWS: NullPointerException se owner == null || data == null
     **/
    
    public ArrayList<String> getShares() {
        return shares;
    }
    /**
     @EFFECTS: Ritorna la lista degli share
     **/

    public E getData() {
        return data;
    }
    /**
     @EFFECTS: Ritorna il dato 'data'
     **/

    public void addShare(String Id) throws NullPointerException
    {
        if(Id == null) throw new NullPointerException();
        shares.add(Id);
    }
    /**
     @REQUIRES: id != null
     @EFFECTS: Aggiunge Id alla lista degli share
     @THROWS: NullPointerException se id == null
     **/
}

