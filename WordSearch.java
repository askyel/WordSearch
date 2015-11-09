// Group Members: Ariel Levy, Roz Joyce

//NOTE: Since this is an interactive version, it doesn't display the
//		version of the board with dashes. If you would like to see
//		the answer board, please uncomment line 201

import java.util.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class WordSearch {
	
	private String[][] grid;
	private ArrayList<String> words;
	private ArrayList<String> poss;
	
	public WordSearch() {
		// standard constructor
		grid = new String[10][10];
		variableSet();
	}
	
	public WordSearch(int r, int c) {
		// custom constructor
		grid = new String[r][c];
		variableSet();
	}
	
	public void variableSet() {
		// set instance variables
		for (int i=0; i < grid.length; i++)
			for (int j=0; j < grid[i].length; j++)
				grid[i][j] = "-";
		words = new ArrayList();
		poss = loadDictionary();
	}
	
	public ArrayList<String> loadDictionary() {
		// load words from wordlist.txt
		String s = "zzz";
	    ArrayList<String> dictionary = new ArrayList<String>();
 
	    try {
			FileReader f = new FileReader("wordlist.txt");
		    BufferedReader b = new BufferedReader(f);
			while( s != null ) {
		         s = b.readLine();
	        	 if ( s != null && s.length() >= 3)
	             	dictionary.add(s.toUpperCase());
	        }
		 }
		 catch (IOException e) {}
		 return dictionary;
	}
	
	public String toString() {
		return makeString(grid);
	}
	
	public String makeString(String[][] aGrid) {
		// displays grid with row/col indices and words w/ indices
		String s = "     ";
		int rowDig = (""+(aGrid.length-1)).length();
		int colDig = (""+(aGrid[0].length-1)).length();
		for (int k=0; k < rowDig-1; k++)
			s += " ";
		// column indices
		for (int i=0; i < aGrid[0].length; i++) {
			s += String.format("%0"+ colDig + "d", i) + " ";
		}
		s += "\n\n";
		// rows
		for (int i=0; i < aGrid.length; i++){
			s += String.format("%0"+ rowDig + "d", i) + "    ";
			for (int j=0; j < aGrid[i].length; j++) {
				s += aGrid[i][j] + " ";
				for (int k=0; k < colDig-1; k++)
					s += " ";
			}
			s += "\n";
		}
		s += "\n";
		// word list
		for (int i=0; i < words.size(); i++)
			s += i + ") " + words.get(i) + "\n";
		return s;
	} 
	
	public boolean addWordH(int row, int col, String s) {
		if (row >= 0 && row < grid.length)
			if (col >= 0 && col <= grid[row].length-s.length()) {
				for (int i=0; i < s.length(); i++) {
					String curGrid = grid[row][col+i];
					String curWord = "" + s.charAt(i);
					// System.out.println(curGrid + " " + curWord);
					if (!curGrid.equals("-"))
						if (!curGrid.equals(curWord))
							return false;
				}
				for (int i=0; i < s.length(); i++)
					grid[row][col+i] = "" + s.charAt(i);
				words.add(s);
				return true;
			}	
		return false;
	}
	
	public boolean addWordV(int row, int col, String s) {
		if (col >= 0 && col < grid[0].length)
			// System.out.println(row);
			if (row >= 0 && row <= grid.length-s.length()) {
				for (int i=0; i < s.length(); i++) {
					String curGrid = grid[row+i][col];
					String curWord = "" + s.charAt(i);
					if (!curGrid.equals("-"))
						if (!curGrid.equals(curWord))
							return false;
				}
				for (int i=0; i < s.length(); i++)
					grid[row+i][col] = "" + s.charAt(i);											
				words.add(s);
				return true;
			}
		return false;
	}
	
	public boolean addWordD(int row, int col, String s) {
		// check northeast and southeast
		if (col >= 0 && col <= grid[0].length-s.length())
			if (row >= 0 && row <= grid.length-s.length()) {
				for (int i=0; i < s.length(); i++) {
					String curGrid = grid[row+i][col+i];
					String curWord = "" + s.charAt(i);
					if (!curGrid.equals("-"))
						if (!curGrid.equals(curWord))
							return false;
				}
				for (int i=0; i < s.length(); i++)
					grid[row+i][col+i] = "" + s.charAt(i);											
				words.add(s);
				return true;
			}
		return false;		
	}
	
	public void fillGrid() {
		// fills remainder of grid w/ random characters
		Random r = new Random();
		String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		for (int i=0; i < grid.length; i++)
			for (int j=0; j < grid[i].length; j++) {
				if (grid[i][j] == "-")
					grid[i][j] = alphabet[ r.nextInt(alphabet.length) ];
			}
	}
	
	public void addWords(int n) {
		// adds n number of words to grid
		Random rand = new Random();
		int numWords = 0;
		while (numWords < n) {
			String s = poss.get( rand.nextInt(poss.size()) );
			// check if s already in words
			for (int i=0; i < words.size(); i++)
				if (s.equals( words.get(i) )) { // in words
					s = poss.get( rand.nextInt(poss.size()) );
					i = 0;
				}
			// check if s fits (methods in random order)
			int r = rand.nextInt(grid.length);
			int c = rand.nextInt(grid[0].length);
			ArrayList<Character> addTests = new ArrayList(Arrays.asList('H','V','D'));
			for (int i=0; i < 3; i++) {
				int test = rand.nextInt(addTests.size());
				if (addTests.get(test) == 'H')
					if (addWordH(r, c, s)){
						numWords++;
						i = 3;
					}
					else
						addTests.remove(test);
				else if (addTests.get(test) == 'V')
					if (addWordV(r, c, s)){
						numWords++;
						i = 3;
					}
					else
						addTests.remove(test);
				else if (addTests.get(test) == 'D')
					if (addWordD(r, c, s)){
						numWords++;
						i = 3;
					}
					else
						addTests.remove(test);
			}
		}
		//System.out.println(this); // --> uncomment here to see answers!
	}
	
	// EXTRA FUNSIES
	
	/*
		FOREGROUND: 30+i
		BACKGROUND: 40+i
		0 BLACK
		1 RED	
		2 GREEN
		3 YELLOW
		4 BLUE
		5 MAGENTA
		6 CYAN
		7 WHITE
	*/
	
	public static String color(int text, String s) {
		// colors text of terminal output of s
		return "\u001B[" + text + "m" + s + "\u001B[0m";
	}
	
	public static String color(int text, int back, String s) {
		// colors text and background of terminal output of s
		return "\u001B[" + text + ";" + back + "m" + s + "\u001B[0m";
	}
	
	public boolean findWordH(int wi, int row, int col) {
		// tests if word is horizontal at that position
		String word = words.get(wi);
		if (word.charAt(1) == '[') {
			System.out.println(color(37,42,"==> Already Found"));
			return false;
		}
		if (row >= 0 && row < grid.length)
			if (col >= 0 && col <= grid[row].length-word.length()) {
				for (int i=0; i < word.length(); i++) {
					String inGrid = grid[row][col+i];
					if (!inGrid.equals(""+word.charAt(i))) {
						if (inGrid.length()>1) {
							if (inGrid.charAt(5)!=word.charAt(i)) {
								System.out.println(color(37,42,"==> Incorrect"));
								return false;
							}
						 }
						else {
							System.out.println(color(37,42,"==> Incorrect"));
							return false;
						}	
					}
				}
				for (int i=0; i < word.length(); i++)
					grid[row][col+i] = color(31, ""+word.charAt(i));
				words.set(wi, color(31, word));
				System.out.println(color(37,42,"==> Correct"));
				return true;
			}	
		System.out.println(color(37,42,"==> Invalid Answer"));
		return false;
	}
	
	public boolean findWordV(int wi, int row, int col) {
		// tests if word is vertical at that position
		String word = words.get(wi);
		System.out.println(word);
		if (word.charAt(1) == '[') {
			System.out.println(color(37,42,"==> Already Found"));
			return false;
		}
		if (col >= 0 && col < grid[0].length)
			if (row >= 0 && row <= grid.length-word.length()) {
				for (int i=0; i < word.length(); i++) {
					String inGrid = grid[row+i][col];
					if (!inGrid.equals(""+word.charAt(i))) {
						if (inGrid.length()>1) {
							if (inGrid.charAt(5)!=word.charAt(i)) {
								System.out.println(color(37,42,"==> Incorrect"));
								return false;
							}
						 }
						else {
							System.out.println(color(37,42,"==> Incorrect"));
							return false;
						}	
					}
				}
				for (int i=0; i < word.length(); i++)
					grid[row+i][col] = color(33, ""+word.charAt(i));
				words.set(wi, color(33, word));
				System.out.println(color(37,42,"==> Correct"));
				return true;
			}	
		System.out.println(color(37,42,"==> Invalid Answer"));
		return false;
	}
	
	public boolean findWordD(int wi, int row, int col) {
		// tests if word is diagonal at that position
		String word = words.get(wi);
		if (word.charAt(1) == '[') {
			System.out.println(color(37,42,"==> Already Found"));
			return false;
		}
		if (col >= 0 && col <= grid[0].length-word.length())
			if (row >= 0 && row <= grid.length-word.length()) {
				for (int i=0; i < word.length(); i++) {
					String inGrid = grid[row+i][col+i];
					if (!inGrid.equals(""+word.charAt(i))) {
						if (inGrid.length()>1) {
							if (inGrid.charAt(5)!=word.charAt(i)) {
								System.out.println(color(37,42,"==> Incorrect"));
								return false;
							}
						 }
						else {
							System.out.println(color(37,42,"==> Incorrect"));
							return false;
						}	
					}
				}
				for (int i=0; i < word.length(); i++)
					grid[row+i][col+i] = color(36, ""+word.charAt(i));
				words.set(wi, color(36, word));
				System.out.println(color(37,42,"==> Correct"));
				return true;
			}	
		System.out.println(color(37,42,"==> Invalid Answer"));
		return false;
	}
	
	public boolean findWord() {
		// collect user info to test if answer is correct
		Scanner scan = new Scanner(System.in);
		System.out.print(color(37,45,"Enter word index: "));
		while (!scan.hasNextInt()) {
			System.out.println("Not an integer. Try again... ");
			System.out.print(color(37,45,"Enter word index: "));
			scan.next();
		}
		int wi = scan.nextInt();
		if (wi < 0 || wi >= words.size()) {
			System.out.println(color(37,42,"==> Invalid Word Index"));
			return false;
		}
		System.out.print(color(37,45,"Hint or answer (H/<anything>)? "));
		char s = scan.next().charAt(0);
		if (s == 'H' || s == 'h') {
			hint(wi);
		}
		else {
			// row
			System.out.print(color(37,45,"Enter row: "));
			while (!scan.hasNextInt()) {
				System.out.println("Not an integer. Try again... ");
				System.out.print(color(37,45,"Enter row: "));
				scan.next();
			}
			int r = scan.nextInt();
			// col
			System.out.print(color(37,45,"Enter column: "));
			while (!scan.hasNextInt()) {
				System.out.println("Not an integer. Try again... ");
				System.out.print(color(37,45,"Enter column: "));
				scan.next();
			}
			int c = scan.nextInt();
			// dir
			System.out.print(color(37,45,"Enter direction (H/V/D): "));
			char d = scan.next().charAt(0);
			// test if user info is correct
			if (wi < 0 || wi >= words.size())
				System.out.println(color(37,42,"==> Invalid Word Index"));
			else if (d == 'H') {
				if (findWordH(wi, r, c))
					return true;
			}	
			else if (d == 'V') {
				if (findWordV(wi, r, c))
					return true;
			}	
			else if (d == 'D') {
				if (findWordD(wi, r, c))
					return true;
			}
			else {
				System.out.println(color(37,42,"==> Invalid Direction"));
			}
		}
	
		return false;
	}
	
	public double findWords() {
		// times how long it takes to find all words
		long start = System.nanoTime();
		int numFound = 0;
		while (words.size() > numFound) {
			if (findWord()) 
				numFound++;
			System.out.println("\n" + this);
		}
		long end = System.nanoTime();
		double time = (1.0 * end - start)/1000000000;
		System.out.println(time + " seconds");
		return time;
	}
	
	public void hint(int a){
		// highlights 1/2 of possible letters in green, including actual answer
		String word = words.get(a);
        if (word.charAt(1) == '[') {
                System.out.println(color(37,42,"==> Already Found"));
                return;
        }
		else {
			String bb = ""+word.charAt(0);
			String[][] hintGrid = new String[grid.length][grid[0].length];
			Random rr = new Random();
			for (int i=0; i < hintGrid.length; i++)
				for (int j=0; j < hintGrid.length; j++) {
					hintGrid[i][j] = grid[i][j];
					if (bb.equals(hintGrid[i][j])) {
						if (!isHh(word,i,j) && !isVh(word,i,j) && !isDh(word,i,j)) {
							if (rr.nextDouble() >= 0.5)
								hintGrid[i][j] = color(32,bb);
						}
						else
							hintGrid[i][j] = color(32,bb);
					}
				}
			System.out.println( makeString(hintGrid) );
		}
	}
	
	public boolean isHh(String word, int row, int col) {
		// tests if word is horizontal at position
        if (row >= 0 && row < grid.length)
                if (col >= 0 && col <= grid[row].length-word.length()) {
                        for (int i=0; i < word.length(); i++) {
                                if (!grid[row][col+i].equals(""+word.charAt(i))) {
                                        return false;
                                }
                        }                                
                        return true;
                }
        return false;
    }
    
    public boolean isVh(String word, int row, int col) {
        // tests if word is vertical at position
		if (col >= 0 && col < grid[0].length)
                if (row >= 0 && row <= grid.length-word.length()) {
                        for (int i=0; i < word.length(); i++) {
                                if (!grid[row+i][col].equals(""+word.charAt(i))) {
                                        
                                        return false;
                                }
                        }
                        return true;
                }
        return false;
    }
    
    public boolean isDh(String word, int row, int col) {
		// tests if word is diagonal at position
        if (col >= 0 && col <= grid[0].length-word.length())
                if (row >= 0 && row <= grid.length-word.length()) {
                        for (int i=0; i < word.length(); i++) {
                                if (!grid[row+i][col+i].equals(""+word.charAt(i))) {
                                        
                                        return false;
                                }
                        }
                        
                        return true;
                }
        return false;
    }
	
	// GAME PLAY
	
	public static void play() {
		// plays full game, w/ multiple rounds
		System.out.println("WORDSEARCH");
		boolean play = true;
		double[] bestTimes = new double[3];
		while (play) {
			int level = chooseLevel();
			WordSearch ws = makeGame(level);
			double time = ws.findWords();
			if (level != 0)
				testTime(time, level, bestTimes);
			printTimes(bestTimes);
			play = playAgain();
		}
		System.out.println(color(37,42,"Goodbye!"));
		System.exit(0);
	}
	
	public static int chooseLevel() {
		// choose level of difficulty of game
		Scanner scan = new Scanner(System.in);
		System.out.print(color(37,45,"Select level 1, 2, or 3 (0 for custom): "));
		while (!scan.hasNextInt()) {
			System.out.println("Not an integer. Try again... ");
			System.out.print(color(37,45,"Select level 1, 2, or 3 (0 for custom): "));
			scan.next();
		}
		int level = scan.nextInt();
		if (level < 0 || level > 3) {
			System.out.println(color(37,42,"==> Invalid response. Try again..."));
			level = chooseLevel();
		} 
		return level;
	}
	
	public static int[] chooseCustom() {
		// set custom dim and # of words
		Scanner scan = new Scanner(System.in);
		System.out.print(color(37,45,"Number of rows: "));
		int r = scan.nextInt();
		System.out.print(color(37,45,"Number of columns: "));
		int c = scan.nextInt();
		System.out.print(color(37,45,"Number of words: "));
		int w = scan.nextInt();
		int[] props = {r, c, w};
		return props;
	}
	
	public static WordSearch makeGame(int level) {
		// makes WordSearch based on level
		WordSearch ws = new WordSearch();
		if (level == 0) {
			int[] props = chooseCustom();
			ws = new WordSearch(props[0],props[1]);
			ws.addWords(props[2]);
		}
		else if (level == 1) {
			ws = new WordSearch();
			ws.addWords(10);
		}
		else if (level == 2) {
			ws = new WordSearch(20,20);
			ws.addWords(15);
		}
		else if (level == 3){
			ws = new WordSearch(30,30);
			ws.addWords(20);
		}
		ws.fillGrid();
		System.out.println(ws);
		return ws;
	}
	
	public static void testTime(double time, int level, double[] bestTimes){
		// tests if time of current game is better than best previous time
		double best = bestTimes[level-1];
		if (best == 0.0 || time < best) {
		 	bestTimes[level-1] = time;
		}
	}
	
	public static void printTimes(double[] bestTimes) {
		// prints time statistics
		System.out.println("Best Times (custom boards not included)");
		for (int i=0; i < bestTimes.length; i++) {
			String s = "\tLevel " + (i+1) + ": ";
			double best = bestTimes[i];
			if (best == 0.0)
				s += "--";
			else
				s += best;
			System.out.println(s + " seconds");
		}
	}
	
	public static boolean playAgain() {
		// asks if user wants to play again
		Scanner scan = new Scanner(System.in);
		System.out.print(color(37,45,"Play again (Y/N)? "));
		char again = Character.toUpperCase(scan.next().charAt(0));
		if (again != 'Y' && again != 'N') {
			System.out.println (color(37,42,"==> Invalid response. Try again..."));
			playAgain();
		}
		if (again == 'Y')
			return true;
		else
			return false;
	}
	
	public static void main(String[] args) {
		
		play();
		
		/*
		WordSearch ws = new WordSearch();
	
		//working horizontal words
		ws.addWordH(0, 0, "hello");
        ws.addWordH(2, 4, "batman");
        ws.addWordH(5, 1, "apple");
		
		//Horizontal index error checking
        ws.addWordH(-2, 4, "joker");
        ws.addWordH(10, 4, "unicorn");  
        ws.addWordH(3, -1, "cowboys");
        ws.addWordH(5, 8, "dogs");

        //horizontal collision checking
        ws.addWordH(5, 3, "plow");
        ws.addWordH(2, 0, "neato");
        
        //working vertical words
        ws.addWordV(1, 0, "nice");
        ws.addWordV(4, 9, "yankee");
        ws.addWordV(4, 4, "old");
        
        //Verical index error checking
        ws.addWordV(-2, 4, "joker");
        ws.addWordV(7, 4, "unicorn");   
        ws.addWordV(3, -1, "cowboys");
        ws.addWordV(5, 20, "dogs");
        
		
        //vertical collision checking
        ws.addWordV(0, 4, "ores");
        ws.addWordV(4, 9, "goober");

		//working diagonal words
        ws.addWordD(7, 0,  "cat");
        ws.addWordD(0, 0, "home");
        ws.addWordD(0, 3, "loam");

        //Diagonal index error checking
        ws.addWordD(-2, 0,  "cat");
        ws.addWordD(3, -1,  "whelm");
        ws.addWordD(7, 7,  "after");    

        //Diagonal collision checking
        ws.addWordD(0, 4, "ores");
        ws.addWordD(4, 4, "oats");
		
		System.out.println( ws );
		//ws.fillGrid();
		ws.findWords();
			
		//Color checking
		System.out.println(color(33,"hi"));
		System.out.println(color(33,45,"hi"));
		*/
	}
	
}

