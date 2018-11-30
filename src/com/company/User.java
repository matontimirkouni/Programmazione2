package com.company;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class User {
    //Overview: classe di supporto per la gestione degli  utenti
    //Typical Element: <id,password>
    //IR: id!= null && password != null

    private String id;
    private String password;

    public User(String id, String Password) throws NullPointerException {
        if(id == null || Password == null) throw  new NullPointerException();
        this.id = id;
        this.password = getHash(Password);
    }
    /**
     @REQUIRES: id != null && Password != null
     @EFFECTS: Inizializza this.id e this.password
     @THROWS: NullPointerException se id == null || Password == null
     **/

    //Ritorna id utente
    public String getId() {
        return id;
    }
    /**
     @EFFECTS: Restituisce id
     **/

    //Metodo controllo identità
    public boolean checkPassword(String password) throws NullPointerException {
        if (password == null) throw new NullPointerException();
        String tmp = getHash(password);
        if ( tmp != null && tmp.equals(this.password))
            return true;
        return false;
    }
    /**
     @REQUIRES: password != null
     @EFFECTS: Restituisce true se il controllo password va a buon fine, false altrimenti
     @THROWS: NullPointerException se password == null
     **/


    public String getPassword() {
        return password;
    }
    /**
     @EFFECTS: Restituisce password
     **/

    //Metodo che genera un Hash di una stringa
    private String getHash(String str) {
        if (str == null) throw new NullPointerException();
        //Classe che fornisce funzioni Hash One-Way
        MessageDigest msgD;
        try {
            //Ritorna oggetto di tipo MessageDigest che implementa l'algoritmo 'SHA-256'
            msgD = MessageDigest.getInstance("SHA-256");
            //Da impasto la stringa str al Digest
            msgD.update(str.getBytes());
            //Ritorna il risultato della funzione Hash completo di padding
            return new String(msgD.digest());
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Eccezione algoritmo");
        }
        return null;

    }
    /**
     @REQUIRES: str != null
     @EFFECTS: Restituisce un Hash di str se l'algoritmo 'SHA-256' non è presente ritorna  null
     @THROWS: NullPointerException se str == null
     **/
}
