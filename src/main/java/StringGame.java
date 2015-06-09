/**
 * Ryan Zhen
 * 2015-04-16
 * ICS3U Period 2/4
 * String game assignment
 * Anagrams
 * The player is given 6 letters at the beginning of the game, and is asked how many rounds they wish to play. They are to spell as many words
 * as possible using their letters. The goal is to get as many points as possible in one round. Points are assigned based on word length.
 * Submitted to Ms Strelkovska
 */

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

// Creates class
public class StringGame {
    // Creates method to generate letters
    private static String generate(String givenLetters, String vowels, String consonants) {
        for (int i = 0; i < 4; i++) {
            givenLetters += consonants.charAt((int) (Math.random() * 21));
        }
        for (int i = 0; i < 2; i++) {
            givenLetters += vowels.charAt((int) (Math.random() * 5));
        }
        return givenLetters;
    }

    private static void clearScreen() {
        for(int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

    // Creates main function
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        // Creates lists for storing valid words and used words
        List<String> words = new ArrayList<>();
        List<String> alreadyUsed = new ArrayList<>();
        // The playAgain variable, in case the player wants to play multiple times
        boolean playAgain = true;
        // Imports words from 52 dictionary files
        for (int i = 1; i <= 52; i++) {
            String fileName = i + ".txt";
            Scanner files = new Scanner(new File(fileName));
            while (files.hasNext()) {
                words.add(files.next().replaceAll(",", " ").trim());
            }
            files.close();
        }
        while (playAgain) {
            // Generates given letters to the user
            String givenLetters = "";
            String vowels = "AEIOU";
            String consonants = "BCDFGHJKLMNPQRSTVWXYZ";
            // Calls previously described method
            givenLetters = generate(givenLetters, vowels, consonants);
            // Welcomes user and asks user for number of rounds
            System.out.println("Welcome to Anagrams!\n");
            System.out.print("How many rounds do you want this to last? ");
            int rounds = input.nextInt();
            // Starts counting the number of points accumulated and words given to the program
            int userWords = 0;
            int points = 0;
            clearScreen();
            System.out.println();
            // Starts game, lasts the amount of rounds that the player specifies
            for (int i = 0; i < rounds; i++) {
                // Printed if player on final round of game to inform them
                if (i == rounds - 1) {
                    System.out.println("This is the final round.");
                    System.out.print("Your letters for this round are: ");
                }
                // Printed if not on the final round
                else {
                    if (givenLetters.length() != 0) {
                        System.out.print("Your letters for this round are: ");
                    }
                    // Teaches player how to get new letters
                    else {
                        System.out.println("You have no letters. Type in '!' for more letters.");
                    }
                }
                // Prints letters given to the user
                for (int j = 0; j < givenLetters.length(); j++) {
                    System.out.print(givenLetters.charAt(j) + ", ");
                }
                System.out.println();
                // Gives user current game status
                System.out.println("You have guessed " + userWords + " words so far.");
                System.out.println("You have " + points + " points.");
                System.out.println("You have " + (rounds - i) + " tries left. Try to spell a word with these letters, or enter [!] to get new letters.");
                System.out.print("Your word: ");
                String userGuess = input.next().trim().toLowerCase();

                System.out.println();

                // Gives user new letters if they don't have enough
                if (userGuess.trim().equals("!")) {
                    if (givenLetters.length() < 8) {
                        givenLetters = generate(givenLetters, vowels, consonants);
                    } else {
                        System.out.println("You have enough letters already.");
                        i--;
                    }
                }
                // Checks input
                else if (words.contains(userGuess)) {
                    userGuess = userGuess.toUpperCase();

                    if (!alreadyUsed.contains(userGuess)) {
                        boolean invalid = false;
                        // Looks over current letters and checks how many times each one appears
                        for (char letter : givenLetters.toUpperCase().toCharArray()) {
                            // Number of appearances in user's word
                            int count = 0;
                            for (int j = 0; j < userGuess.length(); j++) {
                                if (userGuess.charAt(j) == letter) count++;
                                int countGiven = 0;
                                for (int k = 0; k < givenLetters.length(); k++) {
                                    // Compares number of appearances in word to number in given letters
                                    if (givenLetters.charAt(k) == userGuess.charAt(j)) {
                                        countGiven++;
                                    }
                                }
                                // Determine if word not valid
                                if (count > countGiven) {
                                    invalid = true;
                                    break;
                                }
                            }
                        }
                        // If word is invalid, tell user
                        if (invalid) {
                            System.out.println("You can't use that.");
                        }
                        // Congratulate user on spelling a good word
                        else {
                            points += userGuess.length();
                            for (char letter : userGuess.toCharArray())
                                givenLetters = givenLetters.replaceFirst(letter + "", "");
                            alreadyUsed.add(userGuess);
                            System.out.println("Good job! Here's " + userGuess.length() + " points!");
                            userWords++;
                        }
                    }
                }
                // Informs user of invalid words and their reasons if not explained before
                else {
                    System.out.print("I'm sorry, ");
                    if (alreadyUsed.contains(userGuess)) {
                        System.out.println("this word has already been used.");
                    } else {
                        System.out.println("this word does not exist in our dictionary.");
                    }
                }

                clearScreen();
            }
            // Tells user how they did
            System.out.println("During this game, you scored " + points + " points and created " + userWords + " words!\n\n\n");
            boolean askLoop = true;
            input.nextLine();
            while (askLoop) {
                System.out.print("Would you like to do another round? [Y/N] ");
                String choice = input.nextLine().toUpperCase().trim();
                if (choice.equals("Y")) {
                    playAgain = true;
                    askLoop = false;
                } else if (choice.equals("N")) {
                    System.out.println("Thank you for playing. Goodbye.");
                    playAgain = false;
                    askLoop = false;
                }

                clearScreen();
            }
        }
    }
}