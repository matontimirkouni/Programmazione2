package com.company;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MySecureDataContainer <E> implements SecureDataContainer<E> {
    //FA

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


    public void createUser(String Id, String passw) throws NullPointerException,DuplicateUserException
    {
        if ((Id == null) || (passw== null)) throw new NullPointerException();
        if(checkUserExitence(Id)) throw  new DuplicateUserException();

        User user = new User(Id,passw);
        users.add(user);
    }


    public void RemoveUser(String Id, String passw) throws NullPointerException {

    }


    public E remove(String Owner, String passw, E data) throws NullPointerException,UserNotFoundException {
        if ((Owner == null) || (passw== null)) throw new NullPointerException();
        if(checkUserExitence(Owner)) throw  new UserNotFoundException();
        datacollec.remove(getDatabyOwner(Owner));

        return data;
    }


    // Inserisce il valore del dato nella collezione
    // se vengono rispettati i controlli di identità
    public boolean put(String Owner, String passw, E data) throws NullPointerException,UserNotFoundException {

        if((Owner == null) || (passw == null) ||  (data == null)) throw new NullPointerException();
        if(!checkUserExitence(Owner)) throw  new UserNotFoundException();

        DataStruct<E> tmp= new DataStruct<>(Owner,data);
        datacollec.add(tmp);


        return false;
    }

    public int getSize(String Owner, String passw) {
        return 0;
    }


    public E get(String Owner, String passw, E data) throws NullPointerException {
        return null;
    }


    public void copy(String Owner, String passw, E data) throws NullPointerException {

    }

    @Override
    public void share(String Owner, String passw, String Other, E data) throws NullPointerException {

    }

    @Override
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
