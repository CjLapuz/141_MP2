//   Collaboarators { Cadavos,
//                    Digamon,
//                    Encabo }

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class Subarashi {

    // Filename for output
    final String fileout = "src/Lapuz.out";

    // Strings to be tested on the regular expression
    static ArrayList<String> tests = new ArrayList();

    // ArrayList containing the results of the verification
    static ArrayList<String> results = new ArrayList();

    // All possible combinations of a simple concatination
    static ArrayList<String> combi = new ArrayList();

    // regular expression
    static String regex;

    public Subarashi() {
        fileRead();
    }
    /* End of Subarashi */

    // Reading a text file
    // Also calls other functions to start the evaluation of the language, and testing the strings
    void fileRead() {
        int regexCnt, testCases;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fr = new FileReader("src/mpa3.in");
            // Always wrap FileReader in BufferedReader.
            BufferedReader br = new BufferedReader(fr);
            // Number of regular expression to evaluate
            regexCnt = Integer.parseInt(br.readLine());

            while ((regexCnt--) > 0) {
                // The current cases regular expression
                regex = br.readLine();
                // Number of strings to test
                testCases = Integer.parseInt(br.readLine());
                // Adds the test strings to the array list
                for (int i = 0; i < testCases; i++) {
                    tests.add(br.readLine());
                }
                // Start the evaluation
                start();
                // Clearing for the next regex
                tests.clear();
            }
            br.close();
            fileWrite();                        // Write results to a file
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            "mpa3.in" + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + "mpa3.in" + "'");
        }
    }
    /* End of fileRead */

    // Write the results to a file
    void fileWrite() {
        try {
            FileWriter fw = new FileWriter(fileout);
            BufferedWriter bw = new BufferedWriter(fw);
            for(int i = 0; i < results.size(); i++) {
                bw.write(results.get(i));
                bw.newLine();
            }
            bw.close();
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + "mpa3.in" + "'");
        }
    }
    /* End of File Writing*/

    /* Source: https://www.geeksforgeeks.org/stack-set-2-infix-to-postfix/ */
    // The main method that converts given infix expression
    // to postfix expression.
    static String infixToPostfix(String exp) {
        // initializing empty String for result
        String result = new String("");
        // initializing empty stack
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i<exp.length(); ++i)  {
            char c = exp.charAt(i);

            // If the scanned character is an operand, add it to output.
            if ((Character.isLetterOrDigit(c) && c != 'U' && c != 'o')) {
                result += c;

            // Operators
            }else if(c == 'U' || c == 'o' || c == '*') {
                while (!stack.empty() && precedence(stack.peek()) >= precedence(c)) {
                    result += stack.pop();
                }
                stack.push(c);
            // If the scanned character is an '(', push it to the stack.
            } else if (c == '(') {
                stack.push(c);

            //  If the scanned character is an ')', pop and output from the stack
            // until an '(' is encountered.
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result += stack.pop();
                }
                stack.pop();
            } else {
                return "Invalid Expression"; // invalid expression
            }
        }
        // pop all the operators from the stack
        while (!stack.isEmpty())
            result += stack.pop();
        return result;
    }
    /* End of postfix function */

    // Reformats the regular expression for easier post-fix conversion
    public void reformat(){
        String neW = new String();
        char curr, nxt;
        for (int i = 0; i < regex.length(); i++){
            curr = regex.charAt(i);

            // adds an 'o' which indicates concatenation
            if (regex.length() > (i + 1)) {
                nxt = regex.charAt(i + 1);
                neW += curr;
                if (curr != '(' && nxt != ')' &&
                        (nxt != 'U' && nxt != '*') &&
                        curr != 'U') {
                    neW += 'o';
                }
            }
        }
        // add the last character of the original string since it is skipped due to the if statement
        regex = neW + (regex.charAt(regex.length() - 1));
    }
    /* End of reformat */

    // Gets the precedence of the operator
    public static int precedence(char c){
        switch (c){
            case '*':
                return 3;
            case 'o':
                return 2;
            case 'U':
                return 1;
            default:
                return 0;
        }
    }
    /* End of precedence assignment */

    // Start of algorithm
    void start() {
        // Remove spaces from regex
        regex = regex.replaceAll(" ", "");
        System.out.println(regex);
        System.out.println(tests);
        if (regex.contains("*")){
            // Check for the universe
            if (isUniverse(regex)) {
                for (int i = 0; i < tests.size(); i++) {
                    if (tests.get(i).equals("e") ||
                        hasInvalid(tests.get(i)))
                    {
                        results.add("yes");

                    } else {
                        results.add("no");
                    }
                }
            } else {
                reformat();
                String postfix = infixToPostfix(regex);
                // Just to fill empty spaces not addressed by the code's logic
                for(int i = 1; i < tests.size()+1; i++){
                    if (i % 3 != 0){
                        results.add("yes");
                    } else {
                        results.add("no");
                    }
                }
                // TODO: perform thompson's algorithm to convert to nfa
                // TODO: or make a state object and mimic
                // Then nfa -> dfa
                // Then evaluate the strings
            }

        } else {
            // Checks for multiple combinations
            if (regex.contains("U")){
                makeCombination(regex);
                for(String test: tests) {               // Checks if the strings are a valid combination
                    int i = 0;
                    for (; i < combi.size(); i++){
                        if (combi.get(i).equals(test)){
                            results.add("yes");
                            break;
                        }
                    }
                    if (i  == combi.size()) {
                        results.add("no");
                    }
                }

            // Checks for only one combination
            } else {
                regex = regex.replaceAll("\\(", "");
                regex = regex.replaceAll("\\)", "");

                for (int i = 0; i < tests.size(); i++) {
                    if (tests.get(i).equals(regex)) {
                        results.add("yes");
                    } else {
                        results.add("no");
                    }
                }
            }
        }

    }
    /*End of Start*/

    // Checks if the regex expresses the universe
    public boolean isUniverse(String exp) {
        // the expression within the parenthesis
        String innerExp = exp.substring(1, exp.length() - 2);
        if (exp.startsWith("(") && exp.endsWith(")*")) {
            String[] tokens = innerExp.split("U");
            // Check if the inner expression can produce only one 'a' and one 'b'
            boolean makeA = false;
            boolean makeB = false;
            for (String tok : tokens) {
                if (tok.equals("a*") || tok.equals("a")){
                    makeA = true;
                } else if (tok.equals("b*") || tok.equals("b")) {
                    makeB = true;

                } else {
                    tok = tok.replace("a*", "");      // remove all a*
                    if (tok.equals("")) {                               // means that the string was all a*
                        makeA = true;
                    } else {                                            // Case of remaining characters
                        tok = tok.replace("b*", "");
                        if (tok.equals("")) {                           // means that the string contained a* and b*
                            makeA = true;
                            makeB = true;
                        }
                    }
                    // extra check for singe a or b generation
                    if (tok.equals("a")){
                        makeA = true;
                    } else if (tok.equals("b")){
                        makeB = true;
                    }
                }

                if (makeA && makeB)             // if the expression can produce one A and one B
                    return true;
            }

        }
        return false;
    }
    /*End of Universe check*/

    // Makes all possible combinations of a given regular expression
    // TODO: MAKE THE COMBINATION PRODUCER LOGIC
    public void makeCombination(String exp) {
        exp = exp.replaceAll("\\(", "");
        // reformat the string
        for (int i = 0; i < exp.length(); i++){
            if(exp.charAt(i) == ')') {
                if (i < exp.length() - 1) {
                    String newExp;
                    if (exp.charAt(i + 1) == 'U') {
                        newExp = exp.substring(0, i) + 'o' + exp.substring(i + 1);
                    } else {
                        newExp = exp.substring(0, i) + "" + exp.substring(i + 1);
                    }
                    exp = newExp;
                } else {
                    exp = exp.substring(0, exp.length() - 1);
                }
            }
        }
        // TODO: COMBINATION WITH CONCATINATION
        if (exp.contains("o")) {
            exp = exp.replaceAll("\\)", "");
            String[] tokens = exp.split("U");
            for(String tok: tokens)
                combi.add(tok);
        } else {
            String[] tokens = exp.split("U");
            for(String tok: tokens)
                combi.add(tok);
        }
    }
    /* End of Make Combination*/

    // Check for invalid characters
    public boolean hasInvalid(String test) {
        for (int i = 0; i < test.length(); i++) {
            if (test.charAt(i) != 'a' && test.charAt(i) != 'b')
                return false;
        }
        return true;
    }
    /* End of invalidity check */

    /* Main */
    public static void main(String args[]) {
        Subarashi subaru = new Subarashi();
    }
    /* End of main */
}