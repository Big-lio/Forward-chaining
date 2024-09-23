import java.io.*;
import java.util.*;

public class StringObject {
    private String str;
    ArrayList<StringObject> arrayList = new ArrayList<>();
    boolean lacking=false;
    boolean lacking_printed_already=false;

    public StringObject(String str) {
        this.str = str;
    }

    public boolean equals(String otherString) {
        // If the provided string is null or the lengths are different, return false
        if (otherString == null || otherString.length() != str.length()) {
            return false;
        }
        
        // Check each character of the string
        for (int i = 0; i < str.length(); i++) {
            if (otherString.charAt(i) != str.charAt(i)) {
                return false;
            }
        }
        
        return true;
    }

    public void addTo(StringObject str){
        arrayList.add(str);
    }

    public void removeFrom(StringObject str){
        arrayList.remove(str);
    }

    public boolean leadsTo(StringObject str){
        return arrayList.contains(str);
    }

    public  ArrayList<StringObject> getList(){
        return arrayList;
    }

    public String toString() {
        return str;
    }


    
}