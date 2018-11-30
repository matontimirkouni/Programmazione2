package com.company;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class User {
    //Overview: classe di supporto per la gestione degli  utenti
    //Typical Element: <id,password>
    //IR: id!= null && password != null

    private String id;
    private String password;

    public User(String id,String Password)
    {
        this.id = id;
        this.password = getHash(Password);
    }

    //Ritorna id utente
    public String getId() {
        return id;
    }

    //Metodo controllo identità
    public boolean checkPassword(String password)
    {
        String tmp = getHash(password);
        if(tmp.equals(this.password))
            return true;
        return false;
    }

    public String getPassword() {
        return password;
    }

    private String getHash(String str)
    {
        if(str==null) throw new NullPointerException();
        MessageDigest msgD;
        try {
            msgD = MessageDigest.getInstance("SHA-256");
            msgD.update(str.getBytes());
            return new String(msgD.digest());
        }
        catch (NoSuchAlgorithmException ex)
        {
            System.out.println("Eccezione algoritmo");
        }
        return "none";

    }
}
