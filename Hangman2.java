/*
* Hangman2.java
* Author: Emily Ockerman
* Submission Date: October 17, 2018
*
* Purpose: to play a lovely game of Hangman, with a slight rule modification
* for coding ease (hence the "2.0").
*
* Statement of Academic Honesty:
*
* The following code represents my own work. I have neither
* received nor given inappropriate assistance. I have not copied
* or modified code from any source other than the course webpage
* CSCI 1301: Project 1 Page 3
* or the course textbook. I recognize that any unauthorized
* assistance or plagiarism will be handled in accordance with
* the University of Georgia's Academic Honesty Policy and the
* policies of this course. I recognize that my work is based
* on an assignment created by the Department of Computer
* Science at the University of Georgia. Any publishing
* or posting of source code for this project is strictly
* prohibited unless you have written consent from the Department
* of Computer Science at the University of Georgia.
*/

import java.util.Scanner;

public class Hangman2 
{
	public static final boolean testingMode = false;
	
	public static void main(String[] args) 
	{
		Scanner input = new Scanner(System.in);
		
		//VARIABLE INITIALIZATION AND DECLARATION
		String playAgain = "", numSpacesAllowed = "", spacesToCheck = "", guessLetter = "";
		int numGamesAllowed = 20;
		double roundScore = 0, totalScore = 0;
		boolean wonGame = false, play = true;
		
		//LOOP WHILE PLAYER STILL WANTS TO PLAY AND HAVENT PLAYED 20+ GAMES
		while (play && numGamesAllowed > 0)
		{
			//CALLS SECRET WORD FROM RANDOMWORD.JAVA
			String secretWord = RandomWord.newWord();
			int length = secretWord.length();
			
			//IF TESTING MODE IS ON THEN SHOWS SECRET WORD
			if (testingMode)
			{
				System.out.println("The secret word is: " + secretWord);
			}
			
			//CREATES STRING MADE OUT OF - THAT WILL ACT AS HIDDEN WORD
			String secretLengthHidden = "";
			for (int w = 0; w < length; w++)
			{
				secretLengthHidden = secretLengthHidden + "-";
			}
			System.out.println("The secret word is: " + secretLengthHidden);
			
			//PROMPTS FOR # OF SPACES ALLOWED
			System.out.println("Enter the number of spaces allowed");
			numSpacesAllowed = input.nextLine();
			
			//HANDLES INVALID INPUT OF SPACES ALLOWED
			while (Character.isLetter(numSpacesAllowed.charAt(0)) && Character.getNumericValue(numSpacesAllowed.charAt(0)) > length || Character.getNumericValue(numSpacesAllowed.charAt(0)) <= 0)
			{
				System.out.println("Your input is not valid. Try again.");
				System.out.println("Enter the number of spaces allowed");
				numSpacesAllowed = input.nextLine();
			}
				int spacesAllowed = Character.getNumericValue(numSpacesAllowed.charAt(0));
				
			//LOOPS GAME UNTIL WIN OR LOSE
			wonGame = false;
			int guesses = 20;
			while (guesses > 0 && !wonGame)
			{
				//PROMPTS FOR LETTER GUESS
				System.out.print("Please enter the letter you want to guess: ");
				guessLetter = input.nextLine().trim();
				//PROMPTS FOR SPACES TO CHECK
				System.out.println("Please enter the spaces you want to check (separated by spaces):");
				spacesToCheck = input.nextLine();
				
				//CHECKS FOR VALID INPUT FOR SPACES TO CHECK
				boolean validInput = false;
				while (!validInput)
				{
					//CHECKING FOR #_#_#
					int count = 0;
					for (int i = 0; i < spacesToCheck.length(); i++)
					{
						if (i%2 == 0 && Character.isDigit(spacesToCheck.charAt(i)) && (Character.getNumericValue(spacesToCheck.charAt(i)) <= length) && (Character.getNumericValue(spacesToCheck.charAt(i)) >= 0))
						{
							count ++; 
						}
						if (i%2 == 1 && spacesToCheck.charAt(i) == ' ')
						{
							count++;
						}
					}
					
					//IF ALL OF THE VALIDITY MEASURES ARE MET, JUMPS OUTTA LOOP BC BOOLEAN IS NOW TRUE
					if (count == spacesToCheck.length() && (spacesToCheck.length() == ((spacesAllowed * 2) - 1)) && (Character.isLetter(guessLetter.charAt(0))))
					{
						validInput = true;
					}
					//IF INPUT IS SOMEHOW INVALID
					else
					{
						System.out.println("Your input is not valid. Try again.");
						System.out.println("Guesses remaining: " + guesses);
						System.out.print("Please enter the letter you want to guess: ");
						guessLetter = input.nextLine().trim();
						System.out.println("Please enter the spaces you want to check (separated by spaces):");
						spacesToCheck = input.nextLine();
					}
				}
			
				//CHECK TO SEE IF LETTER IS IN THE SPACES GUESSED
				boolean guessRight = false;
				String firstHalf = "", secondHalf = "";
				
				//LOOPS THROUGH SPACESTOCHECK STRING
				for (int w = 0; w < spacesToCheck.length(); w++)
				{
					//IF CHARACTER IS NUMBER
					if (Character.isDigit(spacesToCheck.charAt(w)))
					{
						//IF THAT NUMERIC VALUE MATCHES INDEX VALUE OF THE GUESS LETTER IN THE SECRET WORD
						if (secretWord.charAt(Character.getNumericValue(spacesToCheck.charAt(w))) == guessLetter.charAt(0))
						{
							//REPLACES - WITH THE GUESSED LETTER
							guessRight = true;
							firstHalf = secretLengthHidden.substring(0, Character.getNumericValue(spacesToCheck.charAt(w)));
							secondHalf = secretLengthHidden.substring(Character.getNumericValue(spacesToCheck.charAt(w))+1, length);
							secretLengthHidden = firstHalf + guessLetter.charAt(0) + secondHalf;
						}
					}
				}
				
				//IF LETTER WAS FOUND
				if (guessRight)
				{
					System.out.println("Your guess is in the word!");
					System.out.println("The updated word is: " + secretLengthHidden);
					System.out.println("Guesses remaining: " + guesses);
				}
				
				//IF LETTER WASNT FOUND
				if (!guessRight)
				{
					System.out.println("Your letter was not found in the spaces provided.");
					guesses--;
					System.out.println("Guesses remaining: " + guesses);
				}
			
				//IF ALL OF THE LETTERS HAVE BEEN UNCOVERED
				int letterCount = 0;
				for (int w = 0; w < length; w++)
				{
					if (Character.isLetter(secretLengthHidden.charAt(w)))
					{
						letterCount++;	
					}	
				}
				
				//IF PLAYER WINS
				if (letterCount == length)
				{
					wonGame = true;
					System.out.println("Congrats! You've guessed the word.");
					roundScore = ((guesses*10)/((double)(spacesAllowed)));
					System.out.printf("Score for this round: %1.0f\n", roundScore);
					totalScore = totalScore + roundScore;
					System.out.printf("Total score: %1.0f\n", totalScore);
					System.out.println("Would you like to play again? Yes(y) or no(n)");
					playAgain = input.nextLine();
				}
				
				//IF PLAYER LOSES
				if (guesses == 0)
				{
					System.out.println("You have failed to guess the word :(");
					roundScore = ((guesses*10)/((double)(spacesAllowed)));
					System.out.printf("Score for this round: %1.0f\n", roundScore);
					totalScore = totalScore + roundScore;
					System.out.printf("Total score: %1.0f\n", totalScore);
					System.out.println("Would you like to play again? Yes(y) or no(n)");
					playAgain = input.nextLine();
				}
			}
			
			//HANDLING Y/N FOR PLAY AGAIN
			boolean invalidAgainCommand = true;
			if (playAgain.equalsIgnoreCase("y"))
			{
				numGamesAllowed--;
				invalidAgainCommand = false;
				play = true;
			}
				
			if (playAgain.equalsIgnoreCase("n"))
			{
				System.out.println("It was fun playing with you. See you next time!");
				System.exit(0);
			}
				
			//INVALID COMMAND INPUT
			if (invalidAgainCommand)
			{
				System.out.println("Your input is not valid. Try again.");
				System.out.println("Would you like to play again? Yes(y) or no(n)");
				playAgain = input.nextLine();
			}
			
			//TOO MUCH HANGMAN ENTHUSIASM
			if (numGamesAllowed == 0)
			{
				System.out.println("Sorry, my word bank has run dry. Switch to a more advanced program to continue playing Hangman!");
				System.exit(0);
			}
		}		
	}
}

