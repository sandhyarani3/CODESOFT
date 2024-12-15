import java.util.*;

class Main {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        Random r = new Random();
        boolean continueGame = true; 
        int totalScore = 0; 

        System.out.println("Welcome to the Number Guessing Game!");
        System.out.println("You have 5 attempts to guess a number between 1 and 100.");

        while (continueGame) {
            int randomNumber = r.nextInt(100) + 1; 
            int attemptsLeft = 5; 
            System.out.println("\nStarting a new game! Good luck!");

            while (attemptsLeft > 0) {
                System.out.print("Enter your guess: ");
                int userGuess = sc.nextInt();

                int difference = Math.abs(userGuess - randomNumber); 

                if (userGuess == randomNumber) {
                    System.out.println("🎉 Your guess is correct! 🎉");
                    totalScore += attemptsLeft * 10; 
                    break;
                } else if (difference <= 10) {
                    System.out.println("🔥 You're very close! Try again.");
                } else if (userGuess < randomNumber) {
                    System.out.println("Too small! Try again.");
                } else {
                    System.out.println("Too large! Try again.");
                }

                attemptsLeft--;
                if (attemptsLeft > 0) {
                    System.out.println("Attempts remaining: " + attemptsLeft);
                } else {
                    System.out.println("Sorry, you've used all your attempts. The number was: " + randomNumber);
                }
            }

            System.out.println("\nDo you want to continue? Enter Yes/No:");
            sc.nextLine(); // Consume leftover newline
            String response = sc.nextLine();

            if (response.equalsIgnoreCase("Yes")) {
                System.out.println("Starting a new game...");
            } else {
                continueGame = false;
                System.out.println("Game over!");
            }
        }

        System.out.println("YOUR TOTAL SCORE: " + totalScore);
        sc.close();
    }
}
