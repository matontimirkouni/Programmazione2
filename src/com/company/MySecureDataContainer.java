package com.company;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MySecureDataContainer <E> implements SecureDataContainer<E> {
    //FA

    //IR

    private List<User> users;

    public MySecureDataContainer()
    {
        users= new ArrayList<>();
    }


    public void createUser(String Id, String passw) throws NullPointerException,DuplicateUserException
    {
        if ((Id == null) || (passw== null)) throw new NullPointerException();
        User user = new User(Id,passw);

        for(User u:users)
            if(u.getId().equals(Id))
                throw new DuplicateUserException();
        users.add(user);
    }


    public void RemoveUser(String Id, String passw) throws NullPointerException {

    }


    public E remove(String Owner, String passw, E data) throws NullPointerException {
        return null;
    }


    public boolean put(String Owner, String passw, E data) throws NullPointerException {
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
}
