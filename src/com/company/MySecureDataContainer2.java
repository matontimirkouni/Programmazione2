package com.company;

import java.util.Iterator;

public class MySecureDataContainer2 <E> implements SecureDataContainer<E>{
    //FA

    //IR:


    public MySecureDataContainer2()
    {

    }

    public void createUser(String Id, String passw) throws NullPointerException {

    }


    public void RemoveUser(String Id, String passw) throws NullPointerException {

    }


    public int getSize(String Owner, String passw) throws NullPointerException {
        return 0;
    }


    public boolean put(String Owner, String passw, E data) throws NullPointerException {
        return false;
    }


    public E get(String Owner, String passw, E data) throws NullPointerException {
        return null;
    }


    public void share(String Owner, String passw, String Other, E data) throws NullPointerException {

    }


    public E remove(String Owner, String passw, E data) throws NullPointerException {
        return null;
    }


    public void copy(String Owner, String passw, E data) throws NullPointerException {

    }


    public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException {
        return null;
    }
}
