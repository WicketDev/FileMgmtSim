package project1_cs330;

import java.util.*;

/**
 * @author Janet Gordon CS310 Nordstrom Project 1 
 * 		   This Line class allows user to edit a String line of text. The
 *         methods of this class include: displayMenu()- which is prompted from
 *         the constructor and displays after each function call, lists all
 *         user- available functions deleteSubstring() - deletes a specified
 *         substring of the line copy() - copies a specified substring of the
 *         line. showLine() - displays the line paste() - pastes the copied
 *         substring into the line at a specified index newSub() - allows user
 *         to enter a new substring into a specified index of line cut() -
 *         copies a specified substring and stores it into the strBuffer
 *         Also has non-user method, validate() and validate End() that
 *         validates the user's input for starting and ending index
 *         respectively.
 */

public class Line {
	// private class variables, creates a Scanner object
	private String ln;
	private int lineNumber;
	private String strBuffer;
	private int b, e;
	private StringBuilder sb;
	Scanner in = new Scanner(System.in);

	public Line(int lineNumber, String line) {
		/* Constructor for the Line class requires a line number and String data
		* instantiates a StringBuilder for editing and displays menu.
		*/
		lineNumber = this.lineNumber;
		ln = line;
		sb = new StringBuilder(ln);
		displayMenu();
	}

	public void displayMenu() {
		/*
		 * This method displays the menu options and allows the user to make
		 * choices of line editing functions using a switch statement. User
		 * remains inside the do-while loop until the quit (q) option is chosen.
		 * each call validates index points before the respective function is
		 * called.
		 */
		String choice;
		do {
			showLine();
			System.out.printf("   %-20s\n   %-20s\n   %-20s\n   %-20s\n   %-20s\n   %-20s\n   %s\n", "Show Line: s",
					"Copy to String Buffer: c", "Cut: t", "Paste from String Buffer: p", "Enter new Substring: e",
					"Delete Substring: d", "Quit Line: q");

			System.out.print("-> ");
			choice = in.nextLine();
			choice = choice.toLowerCase();

			switch (choice) {
			case "s":
				showLine();
				break;
			case "c":
				showLine();
				System.out.print("from ");
				b = validate();
				e = validateEnd(b);
				copy(b, e);
				break;
			case "t":
				showLine();
				System.out.print("from ");
				b = validate();
				e = validateEnd(b);
				cut(b, e);
				break;
			case "p":
				showLine();
				System.out.print("insert at ");
				b = validate();
				paste(b);
				break;
			case "e":
				showLine();
				System.out.print("insert at ");
				b = validate();
				newSub(b);
				break;
			case "d":
				showLine();
				System.out.print("from ");
				b = validate();
				e = validateEnd(b);
				deleteSubstring(b, e);
				break;
			case "q":
				System.out.println("Exiting: Edit Line.");
				break;
			default:
				System.out.println("Incorrect choice, please choose again.");
			}
		} while (!(choice.equals("q")));
	}

	public int validate() {
		/*
		 * validates user input to make sure an integer was entered within the
		 * bounds of the String line array. Continues asking until appropriate
		 * value is given.
		 */
		int ok = 0, begin;
		do {
			begin = 0;
			try {

				System.out.print("postion: ");
				String pos = in.nextLine();
				begin = Integer.parseInt(pos);
				ok = 1;
			} catch (NumberFormatException e) {
				System.out.println("Error: That was not a valid line number.");
				ok = 0;
			}
			if (begin < 0 || begin > ln.length()) {
				System.out.println("Error: line number out of bounds, try again.\n");
				ok = 0;
			}
		} while (ok == 0);
		return begin;
	}

	public int validateEnd(int start) {
		/*
		 * validates the ending index to make sure it is an integer after the
		 * beginning index value and the length() of the String line array.
		 * Continues asking, giving error messages until appropriate value is
		 * given.
		 */
		int ok = 0, end;
		do {
			end = 0;
			try {
				System.out.print("to postion: ");
				String pos = in.nextLine();
				end = Integer.parseInt(pos);
				ok = 1;
			} catch (NumberFormatException e) {
				System.out.println("Error: That was not a valid line number.");
				ok = 0;
			}
			if (end < start || end > ln.length()) {
				System.out.println("Error: line number out of bounds, try again.\n");
				ok = 0;
			}
		} while (ok == 0);
		return end;
	}

	public void showLine() {
		/* displays the line in its current state with a scale to help user find
		 * appropriate index for editing
		 */
		for (int i = 0; i <= ln.length() + 4; i += 5)
			System.out.printf("%-5d", i);
		System.out.print("\n");
		for (int i = 0; i <= ln.length() - 1; i += 10)
			System.out.print("|----+----");
		System.out.print("\n");
		System.out.printf("%s\n\n", ln);
	}

	public String newLine() {
		// Method that returns the String line in its current state.
		return ln;
	}

	public void cut(int begin, int end) {
		/*
		 * method that copies a specified substring and stores it into the
		 * strBuffer by calling the copy() method and deletes the substring from
		 * the String line by calling the deleteSubstring() method.
		 */
		copy(begin, end);
		deleteSubstring(begin, end);
	}

	public void copy(int begin, int end) {
		/*
		 * Lets the user know what substring they have chosen and copies the
		 * specified substring into the strBuffer.
		 */
		System.out.println("copied: ");
		showUnderline(begin, end);
		strBuffer = ln.substring(begin, end + 1);
	}

	public void showUnderline(int begin, int end) {
		// Method that displays the line with specified indexes "underlined"

		for (int i = 0; i <= ln.length() + 4; i += 5)// prints every 5 indexes
														// of line
			System.out.printf("%-5d", i);
		System.out.print("\n");
		for (int i = 0; i <= ln.length(); i += 10)// prints once every 10
													// indexes of line
			System.out.print("|----+----");
		System.out.print("\n");
		System.out.printf("%s\n", ln);
		for (int i = 0; i < begin; i++)
			System.out.print(" ");
		for (int i = begin; i <= end; i++)
			System.out.print("^");
		System.out.print("\n");

	}

	public void paste(int begin) {
		// Method that pastes the copied substring into a specified index

		StringBuilder newl = new StringBuilder(ln).insert(begin, strBuffer);
		ln = newl.toString();
	}

	public void newSub(int begin) {
		/* Method that allows the user to type a new substring to be entered at
		 * a specified index
		 */

		sb = new StringBuilder(ln);
		System.out.println("text: ");
		String sub = in.nextLine();
		System.out.println(sub);
		sb.insert(begin, sub);
		ln = sb.toString();
	}

	public void deleteSubstring(int begin, int end) {
		/* Method that deletes a substring from one specified index to another,
		* requires confirmation.
		*/

		sb = new StringBuilder(ln);
		int ok = 0;
		System.out.println("delete: ");
		showUnderline(begin, end);
		do {
			System.out.print("y/n: ");
			String answer = in.nextLine();
			answer = answer.toLowerCase();
			if (!answer.equals("y") && !answer.equals("n")) {
				System.out.println("invalid answer, try again.");
				break;
			}
			if (answer.equals("n")) {
				ok = 1;
			}
			if (answer.equals("y")) {
				ln = sb.delete(begin, end + 1).toString();
				ok = 1;
			}
		} while (ok == 0);

	}

}
