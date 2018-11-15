package com.company;


import java.util.*;

public class MySecureDataContainer <E> implements SecureDataContainer<E> {
    //FA <Users,datacollec> dove
    //users={users.get(0).......users.get(users.size-1)}
    //datacollec={datacollec.get(0).....datacollec.get(datacollec.size - 1)}

    //IR: users != null && datacollec != null
    //    forall i. 0<=i < users.size() => users.get(i) != null
    //    forall i,j. 0<=i,j< users.size() i!= j => users.get(i) != users.get(j)
    //    forall i. 0<=i < datacollec.size() => datacollec.get(i) != null

    private List<User> users;
    private List<DataStruct<E>> datacollec;


    public MySecureDataContainer()
    {
        users= new ArrayList<>();
        datacollec= new ArrayList<>();
    }

    // Crea l’identità un nuovo utente della collezione
    public void createUser(String Id, String passw) throws DuplicateUserException
    {
        if ((Id == null) || (passw== null)) throw new NullPointerException();
        if(checkUserExitence(Id)) throw  new DuplicateUserException();

        User user = new User(Id,passw);
        users.add(user);
    }
    /**
     @REQUIRES: Id != null && passw != null
     @MODIFIES: this.users
     @EFFECTS: crea un nuovo utente nella collezione se non già esistente
     @THROWS: NullPointerException se id == null || passw == null
              DuplicateUserException se user.id già presente nella collezione (checkUserExitence(Id) = True)
     **/


    // Rimuove l’utente dalla collezione
    public void RemoveUser(String Id, String passw)  throws UserNotFoundException,WrongPasswordException {
        if ((Id == null) || (passw== null)) throw new NullPointerException();
        if(!checkUserExitence(Id)) throw  new UserNotFoundException();
        User u = getUserbyId(Id);
        if(!u.checkPassword(passw)) throw new WrongPasswordException();

        //Rimuovo l'utente dalle autorizzazioni in altri file
        for(DataStruct d:datacollec)
            if(d.getShares().contains(Id))
                d.getShares().remove(Id);

        //Rimuovo i dati di cui l'utente è proprietario
        for(int i=0;i< datacollec.size();i++)
        {
            DataStruct d = datacollec.get(i);
            if(d.getOwner().equals(Id)) {
                datacollec.remove(d);
                i--;
            }
        }
        //Rimuovo utente
        users.remove(getUserbyId(Id));

    }

    /**
     @REQUIRES: Id != null && passw != null
     @MODIFIES: this.users && this.datacollec
     @EFFECTS: se l'utente è presente nella collezione rimuove i dati a lui associati ovvero quelli di cui è proprietario
              ed i riferimenti a lui nei file condivisi da altri utenti, per finite elimina l'utente dalla lista users
     @THROWS: NullPointerException se id == null || passw == null
              UserNotFoundException (checked) se l'utente non è presente (checkUserExitence(Id)=False)
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità (checkPassword(passw)=False)
     **/


    public E remove(String Owner, String passw, E data) throws UserNotFoundException,WrongPasswordException {
        if ((Owner == null) || (passw== null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException();
        User u = getUserbyId(Owner);
        if(!u.checkPassword(passw)) throw new WrongPasswordException();

        E tmp=null;
        for(DataStruct d:datacollec)
        {
            if((d.getOwner().equals(Owner)) && (d.getData().equals(data))){
                tmp = (E) d.getData();
                datacollec.remove(d);
                return tmp;
            }
        }

        return tmp;
    }

    /**Attenzione da implementare se data è presente**/
    // Inserisce il valore del dato nella collezione
    // se vengono rispettati i controlli di identità
    public boolean put(String Owner, String passw, E data) throws UserNotFoundException,WrongPasswordException {
        if((Owner == null) || (passw == null) ||  (data == null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException();
        User u = getUserbyId(Owner);
        if(!u.checkPassword(passw)) throw new WrongPasswordException();


        DataStruct<E> tmp= new DataStruct<>(Owner,data);
        datacollec.add(tmp);
        return true;
    }
    /**
     @REQUIRES: Owner != null && passw != null && data != null
     @MODIFIES: this.datacollec
     @EFFECTS: Superati i controlli di identità, se 'data' non è presente nella collezione viene inserito ed attribuito
               ad Owner, la funzione restituisce : true
               Se 'data' è presente nella collezione viene restituito false
     @THROWS: NullPointerException se owner == null || passw == null || data== null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/


    // Restituisce il numero degli elementi di un utente presenti nella
    // collezione
    public int getSize(String Owner, String passw) throws UserNotFoundException,WrongPasswordException{
        if((Owner == null) || (passw == null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException();
        User u = getUserbyId(Owner);
        if(!u.checkPassword(passw)) throw new WrongPasswordException();

        int size=0;
        for(DataStruct d:datacollec)
        {
            if(d.getOwner().equals(Owner))
                size++;
        }
        return size;
    }
    /**
     @REQUIRES: Owner != null && passw != null
     @EFFECTS: Superati i controlli di identità,restituisce size ovvero il numero degli elementi di utente presenti
               nella collezione
     @THROWS: NullPointerException se Owner == null || passw == null
              UserNotFoundException (checked) se l'utente non è presente (checkUserExitence(Id)=False)
     **/


    /**Da decidere se 'data' puo essere duplicato -> nella copy di fatto vengono creati duplicati **/
    // Ottiene una copia del valore del dato nella collezione
    // se vengono rispettati i controlli di identità
    public E get(String Owner, String passw, E data) throws NullPointerException,UserNotFoundException,WrongPasswordException{
        if ((Owner == null) || (passw== null) || (data== null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException();
        User u = getUserbyId(Owner);
        if(!u.checkPassword(passw)) throw new WrongPasswordException();

        E tmp=null;
        for(DataStruct d:datacollec)
        {
            if(d.getData().equals(data)) {
                tmp = (E) d.getData();
                return tmp;
            }
        }

        return tmp;
    }
    /**
     @REQUIRES: Owner != null && passw != null && data != null
     @EFFECTS: Superati i controlli di identità restituisce il riferimento a'data'
     @THROWS: NullPointerException se owner == null || passw == null || data== null
              UserNotFoundException (checked) se Owner non è presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/


    // Crea una copia del dato nella collezione
    // se vengono rispettati i controlli di identità
    public void copy(String Owner, String passw, E data) throws NullPointerException {

    }
    /**
     @REQUIRES: Owner != null && passw != null && data != null
     @MODIFIES: this
     @EFFECTS: copia un  dato nella collezione
     @THROWS: NullPointerException se owner == null || passw == null || data== null
     UserNotFoundException (checked) se Owner non è presente
     WrongPasswordException (checked) se non vengono rispettati i controlli di identità
     **/


    /**Da capire cosa fare**/
    // Condivide il dato nella collezione con un altro utente
    // se vengono rispettati i controlli di identità
    public void share(String Owner, String passw, String Other, E data) throws UserNotFoundException,WrongPasswordException,DuplicateUserException {
        if((Owner == null) ||(Other == null) || (passw == null) || (data == null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException();
        if(!checkUserExitence(Other)) throw  new UserNotFoundException();

        User u = getUserbyId(Owner);
        if(!u.checkPassword(passw)) throw new WrongPasswordException();

        if(getDataObject(Owner,data).getShares().contains(Other)) throw new DuplicateUserException();


        getDataObject(Owner,data).addShare(Other);


    }
    /**
     @REQUIRES: Owner != null && passw != null &&  Other != null && data != null
     @MODIFIES: this.datacollec
     @EFFECTS: Superati i controlli di identità condivide un dato della collezione ovvero aggiunge 'Other' alla lista
               'Shares'
     @THROWS: NullPointerException se owner == null || passw == null || data== null || Other == nul
              UserNotFoundException (checked) se Owner oppure Other non sono presente
              WrongPasswordException (checked) se non vengono rispettati i controlli di identità
              DuplicateUserException se Other è già autorizzato alla visione di data
     **/


    // Restituisce un iteratore (senza remove) che genera tutti i dati
    // dell’utente in ordine arbitrario
    // se vengono rispettati i controlli di identità
    public Iterator<E> getIterator(String Owner, String passw) throws UserNotFoundException,WrongPasswordException {
        if((Owner == null))  throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException();
        User u = getUserbyId(Owner);
        if(!u.checkPassword(passw)) throw new WrongPasswordException();

        ArrayList<E> ls = new ArrayList<>();
        for(DataStruct d:datacollec)
            if((d.getOwner().equals(Owner)) || (d.getShares().contains(Owner)))
                ls.add((E)d.getData());

        //Collection<E> mycollection = Collections.unmodifiableCollection(ls);
        Iterator<E> it = ls.iterator();
        return it;
    }
    /**
     @REQUIRES: Owner != null && passw != null
     @EFFECTS: Superati i controlli di identità restituisce un iteratore contenente i dati dell utente 'Owner'
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
    //Ottiene l'oggetto DataStruct dato Owner e data
    private DataStruct getDataObject(String Owner,E data)
    {
        for(DataStruct d:datacollec)
            if(d.getOwner().equals(Owner) && d.getData().equals(data))
                return d;
        return null;
    }


}





