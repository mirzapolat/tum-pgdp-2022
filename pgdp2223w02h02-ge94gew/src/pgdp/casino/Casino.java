package pgdp.casino;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Casino {

	public static void penguBlackJack() {

		// Here is a card deck for your games :)
		// Remember for testing you can use seeds:
		// CardDeck deck = CardDeck.getDeck(420);
		CardDeck deck = CardDeck.getDeck();
		int tokens = 1000;
		boolean continueplaying = true;
		System.out.println("Welcome to Pengu-BlackJack!");

		while (continueplaying) {
			System.out.println("(1) Start a game or (2) exit");

			int a = readInt();
			while (a != 1 && a != 2) {
				System.out.println("What?!");
				System.out.println("(1) Start a game or (2) exit");
				a = readInt();
			}

			if (a == 2) {
				System.out.println("Your final balance: " + tokens);
				if (tokens > 1000) System.out.println("Wohooo! Ez profit! :D");
				else System.out.println("That's very very sad :(");
				System.out.println("Thank you for playing. See you next time.");
				return;
			}

			System.out.println("Your current balance: " + tokens);
			System.out.println("How much do you want to bet?");

			int bet = readInt();
			while (bet < 1 || bet > tokens) {
				System.out.println("How much do you want to bet?");
				bet = readInt();
			}

			int playerCardCount = 0;
			int playerscore = 0;

			System.out.println("Player cards:");

			int x = deck.drawCard();
			playerCardCount++;
			playerscore += x;
			System.out.println(playerCardCount + " : " + x);

			boolean con = true;
			loop:
			while (con && playerscore <= 20) {
				x = deck.drawCard();
				playerCardCount++;
				playerscore += x;
				System.out.println(playerCardCount + " : " + x);

				System.out.println("Current standing: " + playerscore);
				if (playerscore > 20) break loop;

				System.out.println("(1) draw another card or (2) stay");
				int con1 = readInt();
				while (con1 != 1 && con1 != 2) {
					System.out.println("What?!");
					System.out.println("(1) draw another card or (2) stay");
					con1 = readInt();
				}

				con = con1 == 1;
			}

			if (playerscore == 21) {
				System.out.println("Blackjack! You won " + bet * 2 + " tokens.");
				tokens += bet * 2;
			} else if (playerscore > 21) {
				System.out.println("You lost " + bet + " tokens.");
				tokens -= bet;
			} else {
				System.out.println("Dealer cards:");

				int dealerscore = 0;
				int dealerturns = 0;

				while (dealerscore <= playerscore) {
					x = deck.drawCard();
					dealerturns++;
					dealerscore += x;
					System.out.println(dealerturns + " : " + x);
				}

				System.out.println("Dealer: " + dealerscore);
				if (dealerscore > 21) {
					System.out.println("You won " + bet + " tokens.");
					tokens += bet;
				} else {
					System.out.println("Dealer wins. You lost " + bet + " tokens.");
					tokens -= bet;
				}
			}

			if (tokens < 1) {
				System.out.println("Sorry, you are broke. Better Luck next time.");
				System.out.println("Your final balance: " + tokens);
				System.out.println("That's very very sad :(");
				System.out.println("Thank you for playing. See you next time.");
				continueplaying = false;
			}
		}
	}

	public static String readString() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			return br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int readInt() {
		while (true) {
			String input = readString();
			try {
				return Integer.parseInt(input);
			} catch (Exception e) {
				System.out.println("This was not a valid number, please try again.");
			}
		}
	}

	public static void main(String[] args) {
		penguBlackJack();
	}

}
