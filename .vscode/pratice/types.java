package pratice;

import java.io.IOException;

public class types {
    public static void main(String[] args) throws IOException {
        
   type<Integer,Integer> example1=new type<Integer,Integer>(1,2);
   type<Integer,Integer> example2=new type<Integer,Integer>(2,3);
   type<String,Integer> example3=new type<String,Integer>("hi",2);
   type<String,Integer> example4=new type<String,Integer>("geunwoo",3);
 
   example1.compare(example2.getKey());
   example3.compare(example4.getKey());
    }
}