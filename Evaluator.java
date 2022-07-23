import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

interface IExpressionEvaluator {
  
/**
* Takes a symbolic/numeric infix expression as input and converts it to
* postfix notation. There is no assumption on spaces between terms or the
* length of the term (e.g., two digits symbolic or numeric term)
*
* @param expression infix expression
* @return postfix expression
*/
  
public String infixToPostfix(String expression);
  
  
/**
* Evaluate a postfix numeric expression, with a single space separator
* @param expression postfix expression
* @return the expression evaluated value
*/
  
public int evaluate(String expression);

}

interface IStack {
  
  /*** Removes the element at the top of stack and returnsthat element.
  * @return top of stack element, or through exception if empty
  */
  
  public Object pop();
  
  /*** Get the element at the top of stack without removing it from stack.
  * @return top of stack element, or through exception if empty
  */
  
  public Object peek();
  
  /*** Pushes an item onto the top of this stack.
  * @param object to insert*
  */
  
  public void push(Object element);
  
  /*** Tests if this stack is empty
  * @return true if stack empty
  */
  public boolean isEmpty();
  
  public int size();
}


class MyStack implements IStack {
    Node head = null;
    int size = 0;
    class Node{
        Object value;
        Node next;
        Node(Object d,Node n){
            value = d;
            next = n;
        }
        public String toString(){
            return this.value + "";
        }
    }
    public void push(Object element){
        Node newNode = new Node(element,head);
        head = newNode;
        size++;
    }
    public Object pop(){
        if(head != null){
            Node temp = head;
            head = head.next;
            size--;
            return temp.value;
        }else
            return null;
    }
    public Object peek(){
        if(head != null)
            return head.value;
        else
            return null;
    }
    public boolean isEmpty(){
        if (head == null)
            return true;
        return false;
    }
    public int size(){
        return size;
    } 
    
    public void printlist(MyStack list){
        if(list.size() > 1){
            System.out.print("[");
            while(list.peek() != null && list.size() > 1){
                System.out.print(list.pop() + ", ");
            }
            System.out.print(list.pop() + "");
            System.out.print("]");
        }else if(list.size == 1){
            System.out.print("[" + list.pop() + "]");
        }else{
            System.out.print("["+"]");
        }
    }
}




public class Evaluator implements IExpressionEvaluator{
    
    static int pio(char c){
        switch (c){
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }
    
    
    public String infixToPostfix(String expression){
        MyStack postfix = new MyStack();
        String postfixstr = "";
        Object temp;
                
        expression = expression.replaceAll("--","+");

        for(int i = 0;i < expression.length();i++){
            if((expression.charAt(i)>96 && expression.charAt(i)<100) || (expression.charAt(i)>64 && expression.charAt(i)<91) || 
               (expression.charAt(i)>47 && expression.charAt(i)<58))
               postfixstr += expression.charAt(i);
            else if(expression.charAt(i) == '(')
                postfix.push(expression.charAt(i));
            else if(expression.charAt(i) == ')'){
                while(!postfix.isEmpty() && !postfix.peek().equals('(')){
                    postfixstr += postfix.pop();
                }
                if(postfix.isEmpty())
                    return "";
                postfix.pop();
            }else if(expression.charAt(i) == '+'){
                if(i > 0 && pio(expression.charAt(i-1)) == -1){
                    while(!postfix.isEmpty() &&  pio(expression.charAt(i)) <= pio((Character)postfix.peek())){
                        postfixstr += postfix.pop();
                    }
                    postfix.push('+');
                }
            }else{
                if(i == expression.length() -1)
                    return "";
                else{
                    while(!postfix.isEmpty() &&  pio(expression.charAt(i)) <= pio((Character)postfix.peek())){
                        postfixstr += postfix.pop();
                    }
                    postfix.push(expression.charAt(i));
                }
            }
            
        }
        while (!postfix.isEmpty())
            postfixstr += postfix.pop();
            
        return postfixstr;
    }
    
    
    static int[] nums = new int[3];
    final static  int maxInt = 2147483647;
    
    public int evaluate(String expression){
        MyStack temp = new MyStack();
        int num1 = 0, num2 = 0;
        if(expression.equals(""))
            return maxInt;
        
        for(int i=0;i<expression.length();i++){
            if(expression.charAt(i)>96 && expression.charAt(i)<100)
                temp.push(nums[expression.charAt(i) - 97]);
            else if(expression.charAt(i)>64 && expression.charAt(i)<91)
                temp.push(nums[expression.charAt(i) - 65]);
            else if(expression.charAt(i)>47 && expression.charAt(i)<58)
                temp.push(expression.charAt(i) - 48);
            else{
                if(temp.isEmpty())
                    return maxInt;
                else    
                    num2 = (int)temp.pop();
                if(temp.isEmpty()){
                    if(expression.charAt(i) != 45)
                        return maxInt;
                    else
                        num1 = 0;
                }
                else
                    num1 = (int)temp.pop();
                switch(expression.charAt(i)){
                    case 43:
                        
                        temp.push(num1 + num2);
                        break;
                    case 42:
                        
                        temp.push(num1 * num2);
                        break;
                    case 94:
                        
                        temp.push((int)Math.pow(num1,num2));
                        break;
                    case 45:
                        
                        temp.push(num1 - num2);
                        break;
                    case 47:
                        temp.push((int)num1/num2);
                        break;

                }
            }
                
        }
        
        return (int)temp.pop();
    }
    
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String infix = sc.nextLine();
        String aline;
        String bline;
        String cline;
        try{
            aline = sc.nextLine();
            bline = sc.nextLine();
            cline = sc.nextLine();
        }catch (NoSuchElementException e){
            aline = "a=0";
            bline = "b=0";
            cline = "c=0";
        }      
        if(aline.length() < 3 || bline.length() < 3 || cline.length() < 3 ){
            System.out.println("Error");
            return;
        }
        nums[0] = Integer.parseInt(aline.split("=")[1]);
        nums[1] = Integer.parseInt(bline.split("=")[1]);
        nums[2] = Integer.parseInt(cline.split("=")[1]);
        
        String postfix = new Evaluator().infixToPostfix(infix);
        int temp = new Evaluator().evaluate(postfix);
        if(temp == maxInt){
            System.out.println("Error");
            return;
        }else{
            System.out.println(postfix);
            System.out.println(temp);
        }
    }
}
