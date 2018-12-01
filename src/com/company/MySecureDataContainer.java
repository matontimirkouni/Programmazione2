package com.company;
import java.util.*;
import com.company.Exception.*;

public class MySecureDataContainer <E> implements SecureDataContainer<E> {
    //FA <Users,Passwords,Datacollection> dove
    //Users={users.get(i).getId() | 0 <= i < users.size()}
    //Passwords={users.get(i).getPassword() | 0 <= i < users.size()}
    //Datacollection={<datacollection.get(i)>  | 0<= i < datacollection.size()}

    //IR: users != null && datacollection != null
    //    forall i. 0 <= i < users.size() => users.get(i) != null
    //    forall i,j. 0<= i,j< users.size() i != j => users.get(i).getId() != users.get(j).getId()
    //    forall i. 0<= i < datacollection.size() => datacollection.get(i) != null

    private List<User> users;
    private List<DataStruct<E>> datacollection;


    public MySecureDataContainer()
    {
        users= new ArrayList<>();
        datacollection= new ArrayList<>();
    }

    // Crea l’identità un nuovo utente della collezione
    public void createUser(String Id, String passw) throws DuplicateUserException
    {
        if ((Id == null) || (passw== null)) throw new NullPointerException();
        if(checkUserExitence(Id)) throw  new DuplicateUserException("Duplicated user");

        User user = new User(Id,passw);
        users.add(user);
    }
    /**
     @REQUIRES: Id != null && passw != null
     @MODIFIES: this.users
     @EFFECTS: Crea un nuovo utente nella collezione se non è già esistente
     @THROWS: NullPointerException se id == null || passw == null
              DuplicateUserException se user.id già presente nella collezione (checkUserExitence(Id) = True)
     **/


    // Rimuove l’utente dalla collezione
    public void RemoveUser(String Id, String passw)  throws UserNotFoundException,WrongPasswordException {
        if ((Id == null) || (passw== null)) throw new NullPointerException();
        if(!checkUserExitence(Id)) throw  new UserNotFoundException("User not found");
        User u = getUserbyId(Id);
        if(!u.checkPassword(passw)) throw new WrongPasswordException("Wrong password");

        //Rimuovo l'utente dalle autorizzazioni in altri file
        for(DataStruct d:datacollection)
            if(d.getShares().contains(Id))
                d.getShares().remove(Id);

        //Rimuovo i dati di cui l'utente è proprietario
        for(int i=0;i< datacollection.size();i++)
        {
            DataStruct d = datacollection.get(i);
            if(d.getOwner().equals(Id)) {
                datacollection.remove(d);
                i--;
            }
        }
        //Rimuovo utente
        users.remove(getUserbyId(Id));
    }

    /**
     @REQUIRES: Id != null && passw != null
     @MODIFIES: this.users && this.datacollection
     @EFFECTS: Se l'utente è presente nella collezione rimuove i dati a lui associati ovvero quelli di cui è proprietario
              ed i riferimenti a lui nei file condivisi da altri utenti, per finire elimina l'utente dalla lista users
     @THROWS: NullPointerException se id == null || passw == null
              UserNotFoundException (checked) se l'utente non è presente (checkUserExitence(Id)=False)
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità (checkPassword(passw)=False)
     **/



    // Rimuove il dato nella collezione
    // se vengono rispettati i controlli di identità
    public E remove(String Owner, String passw, E data) throws UserNotFoundException,WrongPasswordException {
        if ((Owner == null) || (passw== null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException("User not found");
        User u = getUserbyId(Owner);
        if(!u.checkPassword(passw)) throw new WrongPasswordException("Wrong password");

        E tmp=null;
        for(int i=0;i< datacollection.size();i++)
        {
            DataStruct<E> d = datacollection.get(i);
            if((d.getOwner().equals(Owner)) && (d.getData().equals(data))){
                tmp =  d.getData();
                datacollection.remove(d);
                i--; //Decremento l'indice poichè la lista si è ricompattata dopo la remove
            }
        }


        return tmp;
    }
    /**
     @REQUIRES: Owner != null && passw != null && data != null
     @MODIFIES: this.datacollection
     @EFFECTS: Se il dato non è presente ritorna Null
               Se il dato è presente viene conservato in 'tmp' prima di essere rimosso dalla collezione e
               ritornato al chiamante
               Se il dato è presente im molteplice copia verranno rimosse tutte e la funzione restituisce l'ultima rimossa
               NOTA 'data' viene rimosso solo se Owner è il proprietario
     @THROWS: NullPointerException se owner == null || passw == null || data== null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/


    // Inserisce il valore del dato nella collezione
    // se vengono rispettati i controlli di identità
    public boolean put(String Owner, String passw, E data) throws UserNotFoundException,WrongPasswordException {
        if((Owner == null) || (passw == null) ||  (data == null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException("User not found");
        User u = getUserbyId(Owner);
        if(!u.checkPassword(passw)) throw new WrongPasswordException("Wrong password");


        DataStruct<E> tmp= new DataStruct<>(Owner,data);
        return datacollection.add(tmp);
    }
    /**
     @REQUIRES: Owner != null && passw != null && data != null
     @MODIFIES: this.datacollection
     @EFFECTS: Superati i controlli di identità, se 'data' non è presente nella collezione viene inserito ed attribuito
               ad Owner, la funzione restituisce : true

     @THROWS: NullPointerException se owner == null || passw == null || data== null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/


    // Restituisce il numero degli elementi di un utente presenti nella
    // collezione
    public int getSize(String Owner, String passw) throws UserNotFoundException,WrongPasswordException{
        if((Owner == null) || (passw == null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException("User not found");
        User u = getUserbyId(Owner);
        if(!u.checkPassword(passw)) throw new WrongPasswordException("Wrong password");

        int size=0;
        for(DataStruct d:datacollection)
        {
            if(d.getOwner().equals(Owner) || d.getShares().contains(Owner) )
                size++;
        }
        return size;
    }
    /**
     @REQUIRES: Owner != null && passw != null
     @EFFECTS: Superati i controlli di identità,restituisce size ovvero il numero degli elementi di un utente presenti
     nella collezione (solo quelli di cui è proprietario)
     @THROWS: NullPointerException se Owner == null || passw == null
              UserNotFoundException (checked) se l'utente non è presente (checkUserExitence(Id)=False
              WrongPasswordException se i controlli di indentità non sono rispettati

     **/



    // Ottiene una copia del valore del dato nella collezione
    // se vengono rispettati i controlli di identità
    public E get(String Owner, String passw, E data) throws NullPointerException,UserNotFoundException,WrongPasswordException{
        if ((Owner == null) || (passw== null) || (data== null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException("User not found");
        User u = getUserbyId(Owner);
        if(!u.checkPassword(passw)) throw new WrongPasswordException("Wrong password");

        E tmp=null;
        for(DataStruct<E> d:datacollection)
        {
            if(d.getData().equals(data) && (d.getOwner().equals(Owner) || d.getShares().contains(Owner))) {
                tmp = d.getData();
            }
        }

        return tmp;
    }
    /**
     @REQUIRES: Owner != null && passw != null && data != null
     @EFFECTS: Superati i controlli di identità restituisce il riferimento a'data'
               Se 'data' è presente in molteplice copia restituisce l'ultimo trovato
               Se 'data' non è presente ritorna null
     @THROWS: NullPointerException se owner == null || passw == null || data== null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/


    // Crea una copia del dato nella collezione
    // se vengono rispettati i controlli di identità
    public void copy(String Owner, String passw, E data) throws NullPointerException,UserNotFoundException,WrongPasswordException{
        if ((Owner == null) || (passw== null) || (data== null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException("User not found");
        User u = getUserbyId(Owner);
        if(!u.checkPassword(passw)) throw new WrongPasswordException("Wrong password");

        this.put(Owner,passw,data);
    }
    /**
     @REQUIRES: Owner != null && passw != null && data != null
     @MODIFIES: this
     @EFFECTS: Crea una copia di 'data' nella collezione se data non è presente viene inserito
     @THROWS: NullPointerException se owner == null || passw == null || data== null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/



    // Condivide il dato nella collezione con un altro utente
    // se vengono rispettati i controlli di identità
    public void share(String Owner, String passw, String Other, E data) throws UserNotFoundException,WrongPasswordException {
        if((Owner == null) ||(Other == null) || (passw == null) || (data == null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException("User not found");
        if(!checkUserExitence(Other)) throw  new UserNotFoundException("User not found");

        User u = getUserbyId(Owner);
        if(!u.checkPassword(passw)) throw new WrongPasswordException("Wrong password");


        for(int i=0;i < datacollection.size();i++) {
            DataStruct<E> d = datacollection.get(i);
            if (d.getOwner().equals(Owner) && d.getData().equals(data)) {
                if (d.getShares().contains(Other))
                    return;
                d.addShare(Other);
            }
        }


    }
    /**
     @REQUIRES: Owner != null && passw != null &&  Other != null && data != null
     @MODIFIES: this.datacollection
     @EFFECTS: Superati i controlli di identità condivide un dato della collezione ovvero aggiunge 'Other' alla lista
               'Shares'
               Se 'data' è presente in molteplice copia, la condivisione verrà fatta per ogni singola copia
     @THROWS: NullPointerException se owner == null || passw == null || data== null || Other == nul
              UserNotFoundException (checked) se Owner oppure Other non sono presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/


    // Restituisce un iteratore (senza remove) che genera tutti i dati
    // dell’utente in ordine arbitrario
    // se vengono rispettati i controlli di identità
    public Iterator<E> getIterator(String Owner, String passw) throws UserNotFoundException,WrongPasswordException {
        if((Owner == null))  throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException("User not found");
        User u = getUserbyId(Owner);
        if(!u.checkPassword(passw)) throw new WrongPasswordException("Wrong password");

        ArrayList<E> ls = new ArrayList<>();
        for(DataStruct d:datacollection)
            if((d.getOwner().equals(Owner)) || (d.getShares().contains(Owner)))
                ls.add((E)d.getData());


        return Collections.unmodifiableList(ls).iterator();
    }
    /**
     @REQUIRES: Owner != null && passw != null
     @EFFECTS: Superati i controlli di identità restituisce un iteratore contenente i dati dell utente 'Owner' e i file a lui condivisi
     @THROWS: NullPointerException se owner == null || passw == null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità

     **/


    // ******** Metodi Ausiliari*********//

    //Controlla che l'utente è presente nella lista users
    private boolean checkUserExitence(String Owner)
    {
        for(User u:users)
            if(u.getId().equals(Owner)) return true;
        return false;
    }

    //Ottiene l'oggetto User dato l'Id
    private User getUserbyId(String Id)
    {
        for(int i =0; i< users.size();i++)
            if(users.get(i).getId().equals(Id))
                return users.get(i);
        return null;
    }



}





