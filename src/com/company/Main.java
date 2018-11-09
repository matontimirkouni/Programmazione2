package com.company;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	// write your code here

        ArrayList<User> ut = new ArrayList<>();
        User tmp = new User("orga","frocio");
        User tmp2 = new User("orga2","frocio2");
        ut.add(tmp);
        ut.add(tmp2);

        MySecureDataContainer<ArrayList<User>> c = new MySecureDataContainer<>();
       try {
           c.createUser("lello", "marco");
           c.createUser("lello2", "marco");
           c.put("lello","marco",ut);
           //c.put("lello2","marco","wuoo2");
           //c.put("lello2","marco","wuoo3");

           //c.share("lello","marco","lello2",ut);
           ut.remove(0);

           ArrayList<User> pp= c.get("lello","marco",ut);
          // System.out.println(c.getSize("lello","marco"));
           c.remove("lello2","marco",ut);
           //System.out.println(c.getSize("lello2","marco"));

         //  c.createUser("lello", "marco");
           System.out.println(c.getSize("lello","marco"));
       }
       catch (Exception ex)
       {
            System.out.println(ex);
       }
    }
}
