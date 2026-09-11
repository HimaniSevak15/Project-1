import java.util.Random;
import java.util.Scanner;

public class game {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        Random r = new Random();

        System.out.print("Enter the Name: ");
        String name = s.next();

        int continueGame;

        // Continue game loop
        do {

            System.out.println("\n================================");
            System.out.println("      Number Guessing Game");
            System.out.println("================================");

            System.out.println("Enter Your Name: " + name);

            System.out.println("\nChoose Difficulty:");
            System.out.println("1. Easy");
            System.out.println("2. Medium");
            System.out.println("3. Hard");

            System.out.print("Enter the choice: ");
            int choice = s.nextInt();

            int randomNumber = 0;
            int num = 0;
            int timeLimit = 0;

            // Set random number and timer according to difficulty
            if (choice == 1) {

                System.out.println("\nEasy Level");
                System.out.println("Guess the Number between 1 to 100");

                randomNumber = r.nextInt(100) + 1;
                timeLimit = 60;

            }
            else if (choice == 2) {

                System.out.println("\nMedium Level");
                System.out.println("Guess the Number between 100 to 200");

                randomNumber = r.nextInt(101) + 100;
                timeLimit = 45;

            }
            else if (choice == 3) {

                System.out.println("\nHard Level");
                System.out.println("Guess the Number between 300 to 400");

                randomNumber = r.nextInt(101) + 300;
                timeLimit = 30;

            }
            else {

                System.out.println("Invalid Choice!");

                System.out.println("\nDo you want to continue?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                System.out.print("Enter choice: ");

                continueGame = s.nextInt();

                if (continueGame == 2) {
                    break;
                }

                continue;
            }

            // Multiple Chances
            int maxAttempts = 10;
            int attempts = 0;
            boolean correct = false;

            System.out.println("\nYou have " + maxAttempts + " chances.");
            System.out.println("You have " + timeLimit + " seconds.");

            // Start timer
            long startTime = System.currentTimeMillis();

            while (attempts < maxAttempts) {

                // Calculate elapsed time
                long currentTime = System.currentTimeMillis();
                long elapsedTime =
                        (currentTime - startTime) / 1000;

                // Check if time is over
                if (elapsedTime >= timeLimit) {

                    System.out.println("\nTime's Up!");
                    System.out.println("You ran out of time.");
                    break;
                }

                long remainingTime = timeLimit - elapsedTime;

                System.out.println("\n----------------------------");
                System.out.println("Time Remaining: "
                        + remainingTime + " seconds");

                attempts++;

                System.out.println("Chance "
                        + attempts + "/" + maxAttempts);

                System.out.print("Enter The Number: ");
                num = s.nextInt();

                // Check timer after user enters number
                currentTime = System.currentTimeMillis();

                elapsedTime =
                        (currentTime - startTime) / 1000;

                if (elapsedTime >= timeLimit) {

                    System.out.println("\nTime's Up!");
                    System.out.println("Game Over!");
                    break;
                }

                // Check user's answer
                if (num == randomNumber) {

                    System.out.println(
                            "\nCongratulations " + name + "!");

                    System.out.println(
                            "You guessed the correct number.");

                    System.out.println(
                            "Number of attempts: " + attempts);

                    System.out.println(
                            "Time used: " + elapsedTime
                            + " seconds");

                    correct = true;
                    break;

                }
                else if (num < randomNumber) {

                    System.out.println("Too Low!");
                    System.out.println(
                            "Try a higher number.");

                }
                else {

                    System.out.println("Too High!");
                    System.out.println(
                            "Try a lower number.");
                }

                System.out.println("Chances remaining: "
                        + (maxAttempts - attempts));
            }

            // Game Over
            if (correct == false) {

                System.out.println("\n================================");
                System.out.println("          GAME OVER");
                System.out.println("================================");

                System.out.println(
                        "The correct number was: "
                        + randomNumber);
            }

            // Continue or Exit
            System.out.println("\n================================");
            System.out.println("Do you want to play again?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.print("Enter your choice: ");

            continueGame = s.nextInt();

        } while (continueGame == 1);

        System.out.println("\n================================");
        System.out.println("Thank you for playing, "
                + name + "!");
        System.out.println("Game Ended.");
        System.out.println("================================");

        s.close();
    }
}