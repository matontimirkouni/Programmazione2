package com.company;

import java.security.cert.TrustAnchor;
import java.util.*;
import com.company.Exception.*;

public class MySecureDataContainer2 <E> implements SecureDataContainer<E>{
    //FA

    //IR:

    private Hashtable<String,List<E>> datacollection;
    private Hashtable<String,String> users;


    public MySecureDataContainer2()
    {
        users= new Hashtable<>();
        datacollection=new Hashtable<>();
    }

    // Crea l’identità un nuovo utente della collezione
    public void createUser(String Id, String passw) throws NullPointerException,DuplicateUserException{
        if((Id == null) || (passw == null)) throw new NullPointerException();
        if(users.containsKey(Id)) throw  new DuplicateUserException();

        users.put(Id,passw);
    }
    /**
     @REQUIRES: Id != null && passw != null
     @MODIFIES: this
     @EFFECTS: crea un nuovo utente nella collezione
     @THROWS: NullPointerException se id == null || passw == null
              DuplicateUserException se user.id già presente
     **/


    public void RemoveUser(String Id, String passw) throws NullPointerException {

    }

    // Restituisce il numero degli elementi di un utente presenti nella
    // collezione
    public int getSize(String Owner, String passw) throws NullPointerException,UserNotFoundException ,WrongPasswordException{
        if((Owner == null) || (passw == null)) throw new NullPointerException();
        if(!users.containsKey(Owner)) throw  new UserNotFoundException();
        if(!checkUserPassword(Owner,passw)) throw new WrongPasswordException();

        return datacollection.get(Owner).size();
    }
    /**
     @REQUIRES: Owner != null && passw != null
     @EFFECTS: Superati i controlli di identità,restituisce size ovvero il numero degli elementi di utente presenti
     nella collezione
     @THROWS: NullPointerException se Owner == null || passw == null
              UserNotFoundException (checked) se l'utente non è presente (checkUserExitence(Id)=False)
              WrongPasswordException se i controlli di indentità non sono rispettati
     **/

    // Inserisce il valore del dato nella collezione
    // se vengono rispettati i controlli di identità
    public boolean put(String Owner, String passw, E data) throws NullPointerException,UserNotFoundException,WrongPasswordException{
        if((Owner == null) || (passw == null) ||  (data == null)) throw new NullPointerException();
        if(!users.containsKey(Owner)) throw  new UserNotFoundException();
        if(!checkUserPassword(Owner,passw)) throw new WrongPasswordException();


        List<E> dt= datacollection.get(Owner);
        if (dt==null)
        {
            dt=new ArrayList<>();
            datacollection.put(Owner,dt);
        }
        return dt.add(data);
    }
    /**
     @REQUIRES: Owner != null && passw != null && data != null
     @MODIFIES: this.datacollec
     @EFFECTS: Superati i controlli di identità, 'data'  viene inserito ed attribuito ad Owner, la funzione restituisce : true
                se l'operazione di inserimento va a buon fine

     @THROWS: NullPointerException se owner == null || passw == null || data== null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/


    public E get(String Owner, String passw, E data) throws NullPointerException {
        return null;
    }

    // Condivide il dato nella collezione con un altro utente
    // se vengono rispettati i controlli di identità
    public void share(String Owner, String passw, String Other, E data) throws NullPointerException,UserNotFoundException,WrongPasswordException,DuplicateUserException {
        if((Owner == null) || (passw == null) ||(Other== null) || (data==null)) throw new NullPointerException();
        if(!users.containsKey(Owner)) throw  new UserNotFoundException();
        if(!users.containsKey(Other)) throw  new UserNotFoundException();
        if(!checkUserPassword(Owner,passw)) throw new WrongPasswordException();

        String prvpassother= users.get(Other);
        this.put(Owner,prvpassother,data);
    }
    /**
     @REQUIRES: Owner != null && passw != null &&  Other != null && data != null
     @MODIFIES: this.datacollec
     @EFFECTS: Superati i controlli di identità condivide un dato della collezione ovvero aggiunge 'data' in Hash(Owner)
     @THROWS: NullPointerException se owner == null || passw == null || data== null || Other == nul
              UserNotFoundException (checked) se Owner oppure Other non sono presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
              DuplicateUserException se Other è già autorizzato alla visione di data
     **/


    public E remove(String Owner, String passw, E data) throws NullPointerException {
        return null;
    }

    // Crea una copia del dato nella collezione
    // se vengono rispettati i controlli di identità
    public void copy(String Owner, String passw, E data) throws NullPointerException,UserNotFoundException,WrongPasswordException{
        if((Owner == null) || (passw == null) ||(data==null)) throw new NullPointerException();
        if(!users.containsKey(Owner)) throw  new UserNotFoundException();
        if(!checkUserPassword(Owner,passw)) throw new WrongPasswordException();

        this.put(Owner,passw,data);
    }
    /**
     @REQUIRES: Owner != null && passw != null && data != null
     @MODIFIES: this
     @EFFECTS: Crea una copia di 'data' nella collezione
     @THROWS: NullPointerException se owner == null || passw == null || data== null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/

    // Restituisce un iteratore (senza remove) che genera tutti i dati
    // dell’utente in ordine arbitrario
    // se vengono rispettati i controlli di identità
    public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException,UserNotFoundException,WrongPasswordException {
        if((Owner == null) || (passw == null)) throw new NullPointerException();
        if(!users.containsKey(Owner)) throw  new UserNotFoundException();
        if(!checkUserPassword(Owner,passw)) throw new WrongPasswordException();


        Iterator<E> it = datacollection.get(Owner).iterator();
        return it;
    }
    /**
     @REQUIRES: Owner != null && passw != null
     @EFFECTS: Superati i controlli di identità restituisce un iteratore contenente i dati dell utente 'Owner'
     @THROWS: NullPointerException se owner == null || passw == null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità

     **/



    private boolean checkUserPassword(String Id,String Passw)
    {
        String pass= users.get(Id);
        if (pass.equals(Passw))
            return true;
        return false;
    }
}
