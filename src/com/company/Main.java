package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        MySecureDataContainer<String> c = new MySecureDataContainer<>();
       try {
           c.createUser("lello", "marco");
           c.createUser("lello2", "marco");
           c.put("lello22","marco","wuoo");

         //  c.createUser("lello", "marco");
       }
       catch (Exception ex)
       {
            System.out.println("Eccezione");
       }
    }
}
