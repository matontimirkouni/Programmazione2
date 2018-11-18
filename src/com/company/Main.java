package com.company;

import java.util.ArrayList;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
	// write your code here

        ArrayList<User> ut = new ArrayList<>();
        User tmp = new User("orga","frocio");
        User tmp2 = new User("orga2","frocio2");
        ut.add(tmp);
        ut.add(tmp2);

        ArrayList<User> ut2 = new ArrayList<>();
        ut2.add(tmp2);



        MySecureDataContainer<ArrayList<User>> c = new MySecureDataContainer<>();
        MySecureDataContainer2<ArrayList<User>> c2 = new MySecureDataContainer2<>();
       try {
           c.createUser("lello", "marco");
           c.createUser("lello2", "marco");
           c.createUser("fi", "marco");
           c.put("lello","marco",ut);
           c.put("lello2","marco",ut);
           c.put("lello2","marco",ut);
           c.copy("lello","marco",ut);
           c.put("lello","marco", ut2);
           c.share("lello","marco","fi",ut2);



           c2.createUser("lello", "marco");
           c2.createUser("lello2", "marco");
           c2.createUser("fi", "marco");
           c2.put("lello","marco",ut);
           c2.put("lello2","marco",ut);
           c2.put("lello2","marco",ut);
           c2.copy("lello","marco",ut);
           c2.put("lello","marco", ut2);
           c2.share("lello","marco","fi",ut2);



           ArrayList<User> t =c.get("lello2","marco",ut2);

           ArrayList<User> t2 =c2.get("lello2","marco",ut2);



           //c.put("lello2","marco","wuoo2");
           //c.put("lello2","marco","wuoo3");

           //c.share("lello","marco","lello2",ut);
           //ut.remove(0);
           c2.createUser("nemp","uci");
           ArrayList<User> pp= c.get("lello","marco",ut);
          // System.out.println(c.getSize("lello","marco"));
           c.RemoveUser("lello","marco");

           //System.out.println(c.getSize("lello2","marco"));

           c.createUser("lello3", "marco");
          //System.out.println(c.getSize("lello","marco"));

           //ut.clear();
           //System.out.println(c.getSize("lello","marco"));

           Iterator<ArrayList<User>> it = c.getIterator("lello","marco");
           Iterator<ArrayList<User>> it2 = c2.getIterator("lello","marco");



           //it.remove();

           System.out.println("lol");
       }
       catch (Exception ex)
       {
            System.out.println(ex);
       }

    }
}
