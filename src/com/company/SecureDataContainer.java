package com.company;

import java.util.Iterator;

public interface SecureDataContainer<E> {
    //Overview : tipo di dato modificabile che rappresenta una collezione di dati di tipo E e di utenti proprietari
    //Typical Element: {<users(0),data(0)>......<users(size-1),data(size-1)>}


    // Crea l’identità un nuovo utente della collezione
    //
    public void createUser(String Id, String passw) throws NullPointerException,DuplicateUserException;
    /**
    @REQUIRES: Id != null && passw != null
    @MODIFIES: this
    @EFFECTS: crea un nuovo utente nella collezione
    @THROWS: NullPointerException se id == null || passw == null
             DuplicateUserException se user.id già presente
     **/

    // Rimuove l’utente dalla collezione
    public void RemoveUser(String Id, String passw) throws  NullPointerException,UserNotFoundException,WrongPasswordException;
    /**
    @REQUIRES: Id != null && passw != null
    @MODIFIES: this
    @EFFECTS: rimuove dalla collazione se presente l'utente <id,passw>
    @THROWS: NullPointerException se id == null || passw == null
             UserNotFoundException (checked) se l'utente non è presente
             WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/

    // Restituisce il numero degli elementi di un utente presenti nella
    // collezione
    public int getSize(String Owner, String passw) throws  NullPointerException,UserNotFoundException,WrongPasswordException;
    /**
     @REQUIRES: Owner != null && passw != null
     @EFFECTS: restituisce size
     @THROWS: NullPointerException se Owner == null || passw == null
              UserNotFoundException (checked) se l'utente non è presente
     **/

    // Inserisce il valore del dato nella collezione
    // se vengono rispettati i controlli di identità
    public boolean put(String Owner, String passw, E data) throws  NullPointerException,UserNotFoundException,WrongPasswordException;
    /**
     @REQUIRES: Owner != null && passw != null && data != null
     @MODIFIES: this
     @EFFECTS: inserisce dato nella collezione, ritorna true se l'inserimento va a buon fine altrimenti false
     @THROWS: NullPointerException se owner == null || passw == null || data== null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/

    // Ottiene una copia del valore del dato nella collezione
    // se vengono rispettati i controlli di identità
    public E get(String Owner, String passw, E data) throws  NullPointerException,UserNotFoundException,WrongPasswordException;
    /**
     @REQUIRES: Owner != null && passw != null && data != null
     @EFFECTS: restituisce una copia del valore data
     @THROWS: NullPointerException se owner == null || passw == null || data== null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/
    // Rimuove il dato nella collezione
    // se vengono rispettati i controlli di identità
    public E remove(String Owner, String passw, E data) throws  NullPointerException,UserNotFoundException,WrongPasswordException;
    /**
     @REQUIRES: Owner != null && passw != null && data != null
     @MODIFIES: this
     @EFFECTS: rimuove il dato nella collezione
     @THROWS: NullPointerException se owner == null || passw == null || data== null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/

    // Crea una copia del dato nella collezione
    // se vengono rispettati i controlli di identità
    public void copy(String Owner, String passw, E data) throws  NullPointerException,UserNotFoundException,WrongPasswordException;
    /**
     @REQUIRES: Owner != null && passw != null && data != null
     @MODIFIES: this
     @EFFECTS: copia un  dato nella collezione
     @THROWS: NullPointerException se owner == null || passw == null || data== null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/

    // Condivide il dato nella collezione con un altro utente
    // se vengono rispettati i controlli di identità
    public void share(String Owner, String passw, String Other, E data)throws  NullPointerException,UserNotFoundException,WrongPasswordException,DuplicateUserException;
    /**
     @REQUIRES: Owner != null && passw != null &&  Other != null && data != null
     @MODIFIES: this
     @EFFECTS: condivide un dato nella collezione
     @THROWS: NullPointerException se owner == null || passw == null || data== null || Other == nul
              UserNotFoundException (checked) se Owner oppure Other non sono presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
              DuplicateUserException se Other è già autorizzato alla visione di data
     **/

    // restituisce un iteratore (senza remove) che genera tutti i dati
    // dell’utente in ordine arbitrario
    // se vengono rispettati i controlli di identità
    public Iterator<E> getIterator(String Owner, String passw) throws  NullPointerException,UserNotFoundException,WrongPasswordException;
    /**
     @REQUIRES: Owner != null && passw != null
     @EFFECTS: restituisce iteratore contenente  dati di un utente
     @THROWS: NullPointerException se owner == null || passw == null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità

     **/

}


class UserNotFoundException extends Exception
{
    public  UserNotFoundException()
    {
        super();
    }
    public  UserNotFoundException(String s)
    {
        super(s);
    }
}
class WrongPasswordException extends Exception
{
    public  WrongPasswordException()
    {
        super();
    }
    public  WrongPasswordException(String s)
    {
        super(s);
    }
}
class DuplicateUserException extends Exception
{
    public  DuplicateUserException()
    {
        super();
    }
    public  DuplicateUserException(String s)
    {
        super(s);
    }
}