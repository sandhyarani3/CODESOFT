import java.util.*;
class Main{
    public static void  main(String args[]){
        Scanner sc=new Scanner(System.in);
        Random r=new Random();
        int n=r.getRandom(1)+100;
        System.out.println("Guess the number");
        int usern=sc.nextInt();
        int count=5,score=0;
        Boolean bool=true;
        while(bool){
        while(count>=0 && n!=usern){
            if(n==usern){System.out.println("Your Guess was correct");score++;}
            else if(n-50>=usern){
                System.out.println("Your Guess was to small");
            }
            else if(n+50<=usern){
                System.out.println("Your Guess was to large");
            }
            else{
                System.out.println("Hey..Your are near to Success...!,Try again");
            }
            count--;
        }
        System.out.println("Do You want to continue?Enter Yes/No");
        String str=sc.nextLine();
        if(str.equals("Yes"))bool=true;
        else bool=false;
        }
        System.out.println("Game over");
        System.out.println("YOUR SCORE:"+score);
    }
}