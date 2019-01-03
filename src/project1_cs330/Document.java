package project1_cs330;

import java.util.*;

import project1_cs330.DblLList.DNode;

import java.io.*;

/**
 * @author Janet Gordon CS310 Nordstrom Project 1 
 * 		   This Document class acts as a line editor, instantiation creates a 
 * 		   doubly linked list for String storage. The methods of this class include: 
 *         chooseMenu()- which is prompted from the constructor or from choosing 'm', 
 *         lists all user-available functions 
 *         deleteLine() - deletes a specified line
 *         loadFile() - loads a specified file if available 
 *         deleteRange() - deletes specified range of lines 
 *         showAll() - displays all lines in list 
 *         copyRange() - copies a range of lines 
 *         showLine() - displays a specified line 
 *         pasteLines() - pastes lines that were copied to a specified location in list 
 *         showRange() - displays a range of lines
 *         writeFile() - writes to a specified file, if available, or the input file, if used 
 *         newLine() - allows user to enter a new line into a specified location of list 
 *         editLine() - calls the Line Class to do various line edits
 * 
 *         Also has non-user method, validate() that validates the user's input
 */

public class Document {

	// private variables and instantiations of linked lists and iterator
	private int lines;
	private Scanner in = new Scanner(System.in);
	private DblLList<String> list = new DblLList<String>();
	private Iterator<String> iter = list.iterator();
	private DblLList<String> buffer = new DblLList<String>();
	private String filename = null;//allows writeFile() to use readFile() file

	// Constructor for Document class, calls menu() and initializes counter
	public Document() {
		lines = 0;
		menu();
	}

	public void chooseMenu() {
		/*
		 * This method allows the user to make choices of line editor functions
		 *  using a switch statement. User remains inside the do-while loop until
		 *   quit (q) or write and quit (wq) option is chosen. Also, checks to make 
		 *   sure list is not empty for some functions that would require the list 
		 *   to have lines available.
		 */
		String choice;
		do {
			System.out.print("-> ");
			choice = in.nextLine();
			choice = choice.toLowerCase();

			switch (choice) {
			case "m":
				menu();
				break;
			case "l":
				loadFile();
				break;
			case "sa":
				showAll();
				break;
			case "sl":
				showLine();
				break;
			case "sr":
				showRange();
				break;
			case "nl":
				newLine();
				break;
			case "el":
				if (list.isEmpty()) {
					System.out.println("List is empty.");
					break;
				}
				editLine();
				break;
			case "dl":
				if (list.isEmpty()) {
					System.out.println("List is empty.");
					break;
				}
				deleteLine();
				break;
			case "dr":
				if (list.isEmpty()) {
					System.out.println("List is empty.");
					break;
				}
				deleteRange();
				break;
			case "cr":
				if (list.isEmpty()) {
					System.out.println("List is empty.");
					break;
				}
				copyRange();
				break;
			case "pl":
				if (list.isEmpty()) {
					System.out.println("List is empty.");
					break;
				}
				pasteLines();
				break;
			case "w":
				if (list.isEmpty()) {
					System.out.println("List is empty.");
					break;
				}
				writeFile();
				break;
			case "q":
				System.out.println("Goodbye.");
				break;
			case "wq":
				writeFile();
				System.out.println("Goodbye.");
				break;
			default:
				System.out.println("Incorrect choice, please choose again.");
			}
		} while (!(choice.equals("q") || choice.equals("wq")));
	}

	
	private void menu() {
		//displays menu and calls the chooseMenu() method
		
		System.out.printf(
				"\n   %-16s%-20s\n   %-16s%-20s\n   %-16s%-20s\n   %-16s%-20s\n   %-16s%-20s\n   %-16s%-20s\n   %-16s%-20s\n",
				"Menu: m", "Delete Line: dl", "Load File: l", "Delete Range: dr", "Show All: sa", "Copy Range: cr",
				"Show Line: sl", "Paste Lines: pl", "Show Range: sr", "Write to File: w", "New Line: nl", "Quit: q",
				"Edit Line: el", "Write and Quit: wq");
		chooseMenu();
	}

	private void writeFile() {
		/*
		 * This method checks to see if an input file has already been used, if
		 * so, it writes into that file. If not, prompts user for a filename,
		 * and catches exceptions if file not found or opened. Uses PrintWriter
		 * to write line by line into opened file and closes once done.
		 * 
		 */
		PrintWriter fOut = null;
		File file = null;

		try {
			if (filename == null) {
				System.out.println("write to file: ");
				String outfile = in.nextLine();
				file = new File(outfile);
			}
			if (!(filename == null))
				file = new File(filename);

			fOut = new PrintWriter(new FileWriter(file));
			while (iter.hasNext())//uses iterator to traverse list
				fOut.println(iter.next());
			System.out.println("File saved");

		} catch (IOException e) {
			// catches IO errors and allows user to try again
			System.out.print("Unable to open file. try again: (y/n); ");
			String answer = in.nextLine();
			if (answer.equals("y"))
				writeFile();
		} finally {
			if (fOut != null)//closes if fOut exists
				fOut.close();
		}

	}

	private void pasteLines() {
		/*
		 * This method pastes all lines in the buffer after a specified
		 * line number. The buffer remains unchanged. All bounds are checked via
		 * validate(). lines(counter) is incremented appropriately.
		 */
		System.out.print("paste after ");
		int ln = validate(0, lines);
		int j = ln;
		System.out.println("pasting after: ");
		list.display(ln);
		for (int i = 1; i <= buffer.size(); i++) {
			list.insert(buffer.getData(i), j);
			lines++;
			j++;
		}

	}

	private void copyRange() {
		/*
		 * This method clears the buffer and copies a user-specified range of
		 * lines from the list into the buffer. line numbers are validated by
		 * validate(). The lines copied, are displayed to user.
		 * 
		 */
		int j = 0;
		System.out.print("from ");
		int b = validate(1, lines);
		System.out.print("to ");
		int e = validate(b, lines);
		buffer.clear();
		System.out.println("copied:");
		for (int i = b; i <= e; i++) {
			buffer.insert(list.getData(i), j);
			buffer.display(j + 1);
			j++;
		}

	}

	private void deleteRange() {
		/*
		 * This method deleted a specified and validated range of lines. lines
		 * is decremented appropriately.
		 */
		System.out.print("beginning ");
		int b = validate(1, lines);//beginning index
		System.out.print("ending ");
		int e = validate(b, lines);//ending index
		System.out.println("deleting: ");
		for (int i = b; i <= e; i++) {
			list.display(i);
			list.remove(i);
			lines--;
		}
		System.out.println("Lines deleted.");//user confirmation

	}

	private void deleteLine() {
		/*
		 * This method deletes a single, specified and validated line and
		 * decrements lines.
		 */

		int d = validate(1, lines);
		System.out.printf("deleteing: %s", list.getData(d));
		list.remove(d);
		lines--;

	}

	private String editLine() {
		/*
		 * This method instantiates a new Line Class object so that it may be
		 * edited. See Line Class for menu options on edits. Returns the new
		 * altered line and replaces the old one.
		 */
		String newLine;
		int index = validate(1, lines);//line to be edited
		Line line = new Line(index, list.getData(index));
		newLine = line.newLine();
		list.remove(index);
		list.insert(newLine, index - 1);
		return newLine;
	}

	private void newLine() {
		/*
		 * This method creates a new line of String and enters at a specified
		 * validated location in list if not empty, otherwise just enters into
		 * first position. Increments lines for each line entered.
		 */

		int ok = 0;
		int lnum = 0;
		DNode current;

		// if list not empty, obtains the line number to insert after and prints that line
		if (lines > 0) {
			System.out.print("insert after ");
			lnum = validate(0, lines);
			current = list.getHead();
			for (int i = 0; i < lnum; i++)
				current = current.next;
			System.out.printf("inserting after: \n%s\n", current.getData());

		}
		/*
		 * while loop allows multiple consecutive lines to be added at a time.
		 * exits loop when user chooses 'n'. increments both lines and lnum
		 * counters for insert location.
		 */
		while (ok == 0) {
			System.out.print("type line? (y/n): ");
			String answer = in.nextLine();
			answer = answer.toLowerCase();
			if (!answer.equals("y") && !answer.equals("n"))
				System.out.println("Invalid input.Try again.");
			if (answer.equals("y")) {
				System.out.printf("%d:", lnum + 1);
				String line = in.nextLine();
				list.insert(line, lnum);
				lines++;
				lnum++;
			}

			if (answer.equals("n"))
				ok = 1;
		}

	}

	private int validate(int low, int high) {
		/*
		 * This method validates user input for line number requests used
		 * throughout program. User remains inside do-while loop until
		 * appropriate values are given for requested functions. It checks index
		 * bounds (given as parameters) as well as whether an integer was given.
		 * 
		 */

		int ok = 0, k = 0;

		do {
			k = 0;
			try {

				System.out.print("line number: ");
				String ln = in.nextLine();
				k = Integer.parseInt(ln);
				ok = 1;
			} catch (NumberFormatException e) {//thrown if integer not entered
				System.out.println("Error: That was not a valid line number.");
				ok = 0;
			}
			if (k < low || k > high) {
				System.out.println("Error: line number out of bounds.\n");
				ok = 0;
			}
		} while (ok == 0);
		return k;
	}

	private void showRange() {
		/*
		 * This method displays a user-specified and validated range of lines in
		 * list or list is empty if that is so.
		 * 
		 */
		if (!list.isEmpty()) {
			System.out.print("beginning ");
			int b = validate(1, lines);
			System.out.print("ending ");
			int e = validate(b, lines);

			for (int i = b; i <= e; i++)
				list.display(i);
		} else
			System.out.println("List is empty.");

	}

	private void showLine() {
		/*
		 * This method displays a single user-specified and validated line. If
		 * list is empty, tells the user and exits the function.
		 */

		if (!list.isEmpty()) {
			int line = validate(1, lines);
			list.display(line);
		} else
			System.out.println("List is empty.");
	}

	private void showAll() {
		/*
		 * This method checks to see if list is empty, if so, displays that to
		 * user and exits. Otherwise, displays entire list.
		 */

		if (list.isEmpty())
			System.out.println("List is empty.");
		else
			for (int i = 1; i <= lines; i++)
				list.display(i);
	}

	private void loadFile() {
		/*
		 * This method requests a file name from user and checks to see if file
		 * exists, if so and is able to open, opens file, reads the contents
		 * with Scanner(). The list is cleared and lines counter set to zero.
		 * Each line from file is added to list from file and lines are incremented.
		 *  Closes file once complete. A try-catch block is used to catch errors, the user is
		 * notified, and the method is exited. **A full path to file is required.**
		 * 
		 */

		String line = new String();

		System.out.println("File name: ");
		filename = in.nextLine();
		File file = new File(filename);
		Scanner fIn = null;
		try {
			fIn = new Scanner(new FileInputStream(file));
			list.clear();
			lines = 0;
			while (fIn.hasNextLine()) {
				line = fIn.nextLine();
				list.insert(line, lines);
				lines++;
			}
		} catch (IOException e) {
			System.out.print("Unable to open file. Check file path is accurate. Try again?: (y/n); ");
			String answer = in.nextLine();
			if (answer.equals("y"))
				loadFile();
		} finally {
			if (fIn != null)
				fIn.close();
		}
	}

}