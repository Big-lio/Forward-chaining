import java.io.*;
import java.util.*;

public class fc1 {
    public static void main(String[] args) {
        try {

            // reading and storing data from the file
            
            System.out.print("Enter the file name: ");
            Scanner scanner0 = new Scanner(System.in);
            String fileName = scanner0.nextLine();
            File file = new File(fileName);
            scanner0.close();

            Scanner scanner = new Scanner(file);
            ArrayList<String> rules = new ArrayList<>();
            ArrayList<String> facts = new ArrayList<>();
            String goal = "";
            String str;

            // reading the useless information
            boolean end=false;

            while (end == false && scanner.hasNextLine()) {
                str = scanner.nextLine();
                String[] line = str.split(" ");
                
                for (int i=0; i<line.length; i++){
                    if (line[i].equals("Rules")){
                        end=true;
                        break;
                    }
                    
                }
            }
            
            // reading and storing the rules
            boolean end2=false;

            while (end2 == false && scanner.hasNextLine()) {
                str = scanner.nextLine();
                String[] line = str.split(" ");
                
                for (int i=0; i<line.length; i++){
                    if (line[i].equals("2)")){
                        end2=true;
                        break;
                    }
                    if (line[i].equals("//")){
                        rules.add(line[i]);
                        break;
                    }
                    else
                    {
                        rules.add(line[i]);
                    }
                    
                }
            }

            

            // reading and storing the facts

            boolean end3=false;

            while (end3 == false && scanner.hasNextLine()) {
                str = scanner.nextLine();
                String[] line = str.split(" ");
                
                for (int i=0; i<line.length; i++){
                    if (line[i].equals("3)")){
                        end3=true;
                        break;
                    }
                    else
                    {
                        facts.add(line[i]);
                    }
                    
                }
            }

            // reading and storing the goal
            goal = scanner.nextLine();

            scanner.close();

            // we read the rules' table an we store all the "To" edges 
            
            StringObject to = new StringObject("");   //the finishing edge

            StringObject[] neighborsTable = new StringObject[rules.size()]; // it is used as a helper to store the neighbors of its edge
                                                                        // after the neighbors of 1 edge are stored then we go the next
            int neighbors=0;

            ArrayList<StringObject> tosFinal = new ArrayList<>(); // second parts of the rules
            

            // we do the process described above for the first "To" edge because before i there is no "//"

            int j = 1;
            StringObject to1 = new StringObject(rules.get(0));
            tosFinal.add(to1);
            boolean f = false;
            while (!f){
                if (j<rules.size()-1){
                    StringObject current0 = new StringObject(rules.get(j));
                    if (!(current0.equals("//"))) {
                        to1.addTo(current0);
                        j++;
                    }
                    else{
                        f=true;
                    }
                }
            }

           // we do the process described above for the rest "To" edges

            for (int i = j; i < rules.size(); i++) {

                StringObject current = new StringObject(rules.get(i));

                if (current.equals("//")) {
                    if (i<rules.size()-1){
                        StringObject nextTo = new StringObject(rules.get(i+1));
                        to = nextTo;
                        tosFinal.add(nextTo);
                        
                    }
                    
                }

                if (!(current.equals("//"))) {
                    neighborsTable[neighbors]=current;
                    neighbors++;
                    
                }

                if (i<rules.size()-1){
                    StringObject next = new StringObject(rules.get(i+1));
                    if ((next.equals("//"))) {
                        
                        for (int l=1; l<neighbors; l++){
                            if (!(to.equals(neighborsTable[l]))){
                                to.addTo(neighborsTable[l]);
                            }
                            
                        }
                        neighbors=0;
                    }
                }
                
                
            }

            // for each "To" edge we store its neighbors that we call "From" edges

            ArrayList<ArrayList<StringObject>> fromsFinal = new ArrayList<>(); // first parts of the rules

            for (int b=0; b<tosFinal.size(); b++ ){
                fromsFinal.add(tosFinal.get(b).getList());
            }

            // printing the data as needed

            System.out.println("PART 1. Data");
            System.out.println();
            System.out.println("  1) Rules");
            System.out.println();

            // we print the rules as needed and we also store them as strings for easier printing later

            int R=1; // rule number
            String rule=""; // the whole rule as a string
            String [] rulesTable = new String[rules.size()]; // whole rules as a Strings, wiil be used just for printing

            for (int b=0; b<tosFinal.size(); b++ ){

                rule+=("    R" + R + ": ");
                System.out.print("    R" + R + ": ");

                    for (int a = 0 ; a < tosFinal.get(b).getList().size(); a++){

                        if (a>=1){
                            rule+=(", ");
                            System.out.print(", ");   
                        }
                        rule+=(tosFinal.get(b).getList().get(a).toString());
            
                        System.out.print(tosFinal.get(b).getList().get(a).toString());
                        
                    }
                System.out.print( " -> " + tosFinal.get(b).toString() + " ");
                rule+=( " -> " + tosFinal.get(b).toString() + " ");
                rulesTable[R-1]=rule;
                System.out.println();

                R++;
                rule="";
            }

            // printing the facts and the goal

            System.out.println();
            
            System.out.print("  2) Facts: ");
            for (int a=0; a<facts.size();a++){
                System.out.print(facts.get(a));
                if (a!=facts.size()-1){
                    System.out.print(", ");
                }
                else{
                    System.out.print(".");
                }
            }
            System.out.println();
            System.out.println();
            System.out.print("  3) Goal: ");
            System.out.println(goal + ".");

            System.out.println();
            System.out.println("PART 2. Trace");


            // forward chaining implementation
            ArrayList<String> path = new ArrayList<>();
            ArrayList<StringObject> newFacts = new ArrayList<>();
            boolean finished=false;
            boolean goalInFacts=false;
            String achieved="";
            int iteration=1;
            int fsize= facts.size();
            int ruleFail=0;

            // for case -1
            ArrayList<String> originalFacts = new ArrayList<>();
            for (int a=0; a<facts.size();a++){
                originalFacts.add(facts.get(a));
            }
            
            
            while (!finished) {
                // 0  case

                if (facts.contains (goal)) {
                    goalInFacts=true;
                    finished=true;
                    break;
                }

                System.out.println();
                System.out.println("  ITERATION " + iteration);
                ruleFail=0;

                for (int a=0; a<tosFinal.size(); a++){

                    // 1st case

                    int count=0; // it counts how many elements of the "To's" list are facts 
                    String notContained = ""; // it stores the edge that is not a fact

                    for (int b=0; b<tosFinal.get(a).getList().size(); b++){

                        if (facts.contains (tosFinal.get(a).getList().get(b).toString())) {
                            count++;
                        }
                        else{
                            notContained = tosFinal.get(a).getList().get(b).toString();
                        }
                    }

                    if (tosFinal.get(a).lacking_printed_already==true ){
                        System.out.println( rulesTable[a]+  "skip, because flag2 raised.");
                        ruleFail++;
                    }

                    // 2nd  case

                    else if (originalFacts.contains (tosFinal.get(a).toString()) && tosFinal.get(a).lacking==true) {
                        System.out.println( rulesTable[a]+  "not applied, because RHS in facts. Raise flag2.");
                        ruleFail++;
                        tosFinal.get(a).lacking_printed_already=true;
                    }

                    //3rd case
                    else if (count == tosFinal.get(a).getList().size() && facts.contains(tosFinal.get(a).toString())  ){
                        System.out.println( rulesTable[a]+  "skip, because flag1 raised.");
                        ruleFail++;
                    }

                    


                    // 4th case
                    
                    else if (count == tosFinal.get(a).getList().size() && !(facts.contains(tosFinal.get(a).toString())) ){
                        facts.add(tosFinal.get(a).toString());
                        newFacts.add(tosFinal.get(a));
                        path.add("R" + (a+1));
                        System.out.print(rulesTable[a]+ " apply. Raise flag1. Facts ");
                        for (int b=0; b<fsize;b++){
                            System.out.print(facts.get(b) + " ");
                        }

                        System.out.print(" and  ");

                        for (int c=0; c<newFacts.size();c++){
                            System.out.print(newFacts.get(c) + " ");
                        }
                        System.out.println();
                        break;

                    }

                    // 5th case

                   else if (count != tosFinal.get(a).getList().size()){
                        System.out.println(rulesTable[a] + "not applied, because of lacking " + notContained);
                        ruleFail++;
                        tosFinal.get(a).lacking=true;
                    }   
                }
                iteration++;
                if (facts.contains(goal)){
                    finished=true;
                    achieved="achieved.";
                    System.out.println("    Goal achieved.");
                }
                else if (ruleFail==tosFinal.size()){
                        finished=true;
                        achieved=" not achieved.";
                        System.out.println("    Goal not achieved.");
                }
                
            }

            // printing results
            System.out.println();
            System.out.println("PART 3. Results");
            System.out.println("  1) Goal " + goal + " " + achieved);
            if (achieved.equals("achieved.")){
                System.out.print("  2) Path "); 
                for (int d=0; d<path.size();d++){
                    if (d!=path.size()-1){
                        System.out.print(path.get(d) + ", ");
                    }
                    else{
                        System.out.print(path.get(d) + ".");
                    }
                }
            }
            else{
                System.out.print("  2) No path found. ");
            }

            if (goalInFacts){
                System.out.println(" Goal "  + goal + " in facts. Empty path.");
            }
            

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}