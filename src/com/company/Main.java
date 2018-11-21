package com.company;

import com.company.Exception.DuplicateUserException;
import com.company.Exception.UserNotFoundException;
import com.company.Exception.WrongPasswordException;

import java.util.ArrayList;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) {

        //Dati ipotizzati di tipo  Arraylist<string>
        ArrayList<String> dt1 = new ArrayList<String>();
        dt1.add("Stringa 1");
        dt1.add("Stringa 2");
        dt1.add("Stringa 3");
        ArrayList<String> dt2 = new ArrayList<String>();
        dt2.add("Stringa 4");
        dt2.add("Stringa 5");
        ArrayList<String> dt3 = new ArrayList<String>();
        dt3.add("Stringa 6");
        dt3.add("Stringa 7");
        dt3.add("Stringa 8");
        dt3.add("Stringa 9");
        ArrayList<String> dt4 = new ArrayList<String>();
        dt4.add("Stringa 10");


        //Implementazione 1
        //MySecureDataContainer<ArrayList<String>> securecontainer = new MySecureDataContainer<>();
        //Implementazione 2 (commentata)
        MySecureDataContainer2<ArrayList<String>> securecontainer= new MySecureDataContainer2<>();

        System.out.println("#### TEST ####");

        //Crezione utenti
        try
        {
            securecontainer.createUser("luca", "12345678");
            securecontainer.createUser("marco", "87654321");
            securecontainer.createUser("william", "13794650");
            securecontainer.createUser("lory", "172839465");
            securecontainer.createUser("giovanni", "97314652");
            System.out.println("CREATEUSER eseguita correttamente");
        }
        catch(DuplicateUserException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }
        //Inserimento dati
        try
        {
            securecontainer.put("luca", "12345678",dt1);
            securecontainer.put("luca", "12345678",dt2);
            securecontainer.put("marco", "87654321",dt1);
            securecontainer.put("william", "13794650",dt1);
            securecontainer.put("william", "13794650",dt3);
            securecontainer.put("lory", "172839465",dt2);
            securecontainer.put("lory", "172839465",dt3);
            System.out.println("PUT eseguita correttamente");
        }
        catch(UserNotFoundException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }
        catch(WrongPasswordException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }


        //Copia di un dato
        try
        {
            securecontainer.copy("luca", "12345678",dt1);
            System.out.println("COPY eseguita correttamente");
        }
        catch(UserNotFoundException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }
        catch(WrongPasswordException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }

        //eliminare un dato
        try
        {

            securecontainer.remove("luca", "12345678",dt1);
            System.out.println("Remove eseguita correttamente");
        }
        catch(UserNotFoundException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }
        catch(WrongPasswordException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }

        //share di un dato
        try
        {
            securecontainer.share("luca", "12345678","giovanni",dt1);
            System.out.println("Share eseguita correttamente");
        }
        catch(UserNotFoundException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }
        catch(WrongPasswordException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }


        //giovanni fa la get del dato
        try
        {
            securecontainer.get("giovanni", "97314652",dt1);
            System.out.println("GET eseguita correttamente");
        }
        catch(UserNotFoundException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }
        catch(WrongPasswordException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }
        //giovanni fa la getSize
        try
        {
            securecontainer.getSize("giovanni", "97314652");
            System.out.println("GETSIZE eseguita correttamente");
        }
        catch(UserNotFoundException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }
        catch(WrongPasswordException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }

        //rimozione user giovanni
        try
        {
            securecontainer.RemoveUser("giovanni", "97314652");
            System.out.println("REMOVEUSER eseguita correttamente");
        }
        catch(UserNotFoundException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }
        catch(WrongPasswordException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }
        //get iterator luca
        try
        {
            securecontainer.getIterator("luca", "12345678");
            System.out.println("GETITERATOR eseguita correttamente");
        }
        catch(UserNotFoundException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }
        catch(WrongPasswordException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }

        //#### TEST ECCEZIONI ####
        System.out.println("");
        System.out.println("#### TEST ECCEZIONI ####");
        System.out.println("");


        //Crezione utenti eccezione utente gia esistente
        try
        {
            System.out.println("Aggiungo un utente già esistente");
            securecontainer.createUser("luca", "12345678");
        }
        catch(DuplicateUserException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }

        //rimozione user non esistente
        try
        {
            System.out.println("Rimuovo un utente non esistente");
            securecontainer.RemoveUser("alfredo", "45689518");
        }
        catch(UserNotFoundException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }
        catch(WrongPasswordException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }

        //eliminare un dato di luca con password sbagliata
        try
        {
            System.out.println("Elimino un dato di luca con password sbagliata");
            securecontainer.remove("luca", "passwordno",dt1);
        }
        catch(UserNotFoundException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }
        catch(WrongPasswordException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }

        //condivido un dato di luca con un utente non esistente
        try
        {
            System.out.println("Condivido un dato di luca con un utente non esistente");
            securecontainer.share("luca", "12345678","usernot",dt1);
        }
        catch(UserNotFoundException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }
        catch(WrongPasswordException ex)
        {
            System.out.println("Eccezione catturata " + ex.getMessage());
        }







    }
}
