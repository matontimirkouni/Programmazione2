package com.company;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MySecureDataContainer <E> implements SecureDataContainer<E> {
    //FA <Users,datacollec> dove users={users.get(0).......users.get(size-1)}

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
        for(DataStruct d:datacollec)
            if(d.getOwner().equals(Id))
                datacollec.remove(d);
        //Rimuovo utente
        users.remove(getUserbyId(Id));

    }


    public E remove(String Owner, String passw, E data) throws UserNotFoundException {
        if ((Owner == null) || (passw== null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException();

        E tmp=null;
        for(DataStruct d:datacollec)
        {
            if(d.getData().equals(data)) {
                tmp = (E) d.getData();
                datacollec.remove(d);
                System.out.println("diocaml");
                return tmp;
            }
        }

        return tmp;
    }


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


    public E get(String Owner, String passw, E data) throws NullPointerException,UserNotFoundException {
        if ((Owner == null) || (passw== null) || (data== null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException();

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


    public void copy(String Owner, String passw, E data) throws NullPointerException {

    }

    //Aggiustare
    public void share(String Owner, String passw, String Other, E data) throws UserNotFoundException,WrongPasswordException,DuplicateUserException {
        if((Owner == null) ||(Other == null) || (passw == null) || (data == null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException();
        if(!checkUserExitence(Other)) throw  new UserNotFoundException();

        User u = getUserbyId(Owner);
        if(!u.checkPassword(passw)) throw new WrongPasswordException();

        if(getDatabyOwner(Owner).getShares().contains(Other)) throw new DuplicateUserException();


        getDatabyOwner(Owner).addShare(Other);


    }


    public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException {
        return null;
    }


    // ******** Metodi Ausiliari*********//



    private boolean checkUserExitence(String Owner)
    {
        for(User u:users)
            if(u.getId().equals(Owner)) return true;
        return false;
    }

    private User getUserbyId(String Id)
    {
        for(int i =0; i< users.size();i++)
            if(users.get(i).getId().equals(Id))
                return users.get(i);
        return null;
    }

    private DataStruct getDatabyOwner(String Owner)
    {
        for(DataStruct d:datacollec)
            if(d.getOwner().equals(Owner))
                return d;
        return null;
    }


}
