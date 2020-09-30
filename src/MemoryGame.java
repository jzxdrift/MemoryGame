import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class MemoryGame
{
	private static final int MIN_PLAYERS = 1;
	private static final int MAX_PLAYERS = 8;
	private static final int ROWS = 5;
	private static final int COLS = 8;
	private static final char BACK = '*'; 		//back side of a card
	private static final char BLANK = ' ';		//blank space for empty cards
	
	private Card[][] card; 						//matrix of cards
	private Player[] player;					//array of players
	private Random numGen; 						//random number generator
	private BufferedReader reader;
	
	public MemoryGame()
	{
		card = new Card[ROWS][COLS];
		numGen = new Random();
		reader = new BufferedReader(new InputStreamReader(System.in));
		createGrid();
		shuffleCards();
	}
	
	//creating grid
	public void createGrid()
	{
		char front = 'A';
		
		for(int i = 0; i < ROWS; i ++)
		{
			for(int j = (COLS - 1); j >= 0; j -= 2)	  //starting from 7 to exclude "out of bounds" error
			{
				card[i][j] = new Card(front, BACK);
				card[i][j - 1] = new Card(front, BACK);
				front++;
			}
		}
	}
	
	//shuffling cards
	public void shuffleCards()
	{
		for(int i = 0; i < ROWS; i++)
		{
			for(int j = 0; j < COLS; j++)
			{
				int randomRow = numGen.nextInt(ROWS); 		//random numbers from 0 to 5 exclusively
				int randomCol = numGen.nextInt(COLS); 		//random numbers from 0 to 8 exclusively
				
				Card temp = card[i][j]; 					//simple swap using temporary card
				card[i][j] = card[randomRow][randomCol];
				card[randomRow][randomCol] = temp;
			}
		}
	}
	
	//showing grid
	public void showGrid()
	{
		for(int i = 0; i < ROWS; i++)
		{
			for(int j = 0; j < COLS; j++)
			{
				card[i][j].showCard();
			
				if(j == (COLS - 1)) 				//go to next line when there are 8 elements in a row
					System.out.print("\n");
			}
		}
	}
	
	//comparing cards
	public void compareCards(Card card1, Card card2, int num)			//"num" is current player's number
			throws NumberFormatException, IOException, InterruptedException
	{
		if(card1.getFrontSide() == card2.getFrontSide())
		{
			card1.setFrontSide(BLANK); 						//removing cards from board
			card1.setBackSide(BLANK);
			card2.setFrontSide(BLANK);
			card2.setBackSide(BLANK);
			player[num].updateScore();					//current player's score is incremented
			
			if(checkForSolvedGame() == false)			//keep playing until all cards are matched
			{
				Thread.sleep(5000);						//5 seconds delay before cards are hidden again
				System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				
				System.out.println(player[num].getName() + "'s turn");
				getInput(num);							//current player continues to play
			}
			
			else
			{
				Thread.sleep(5000);
				System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				
				System.out.println("Game is solved");
				showScore();								//prints players' scores
				
				Thread.sleep(5000);							//5 seconds delay before program terminates
				System.exit(0);								//terminate the program
			}
		}
		
		else
		{
			card1.changeCardStatus(); 						//hide cards again
			card2.changeCardStatus();
			
			Thread.sleep(5000);
			System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			
			changeTurn(num);				//current player changes (passing current player's number)		
		}
	}
	
	//creating players
	public void createPlayers()
			throws NumberFormatException, IOException, InterruptedException
	{
		int number;
		String name;
		
		do
		{
			System.out.print("Enter number of players (1-8): ");
			number = Integer.parseInt(reader.readLine());
		} while(number < MIN_PLAYERS || number > MAX_PLAYERS);
		
		player = new Player[number];		//using number of players as an array size
		
		int count = 0;
		
		while(count < number)				
		{
			System.out.print("Enter player's name: ");
			name = reader.readLine();
			player[count] = new Player(name);			//assigning names to players
			count++;
		}
		
		//choosing random player to start the game
		int playerNum = numGen.nextInt(number);			//random number from 0 to "number" exclusively
		
		System.out.println("\n" + player[playerNum].getName() + "'s turn");
		getInput(playerNum);										//passing current player's number
	}
	
	//changing players' turns
	public void changeTurn(int changeNum)				//receiving current player's number
			throws NumberFormatException, IOException, InterruptedException
	{
		int num = ++changeNum;			//incrementing current player's number to let next player play
		
		if(num == player.length)
		{
			num = 0;					//if previous player was last it the list, then next is first
			
			System.out.println(player[num].getName() + "'s turn");
			getInput(num);
		}
		
		else							//if previous player was not last it the list, then next is next
		{
			System.out.println(player[num].getName() + "'s turn");
			getInput(num);
		}
	}
	
	//showing score
	public void showScore()
	{
		for(int i = 0; i < player.length; i++)
		{
			System.out.println(player[i].getName() + "'s score is " + player[i].getScore());
		}
	}
	
	//getting input from players
	public void getInput(int num)
			throws NumberFormatException, IOException, InterruptedException
	{
		boolean status;
		int input;
		int row1;
		int col1;
		int row2;
		int col2;
		
		//first input
		do
		{
			status = true;
			showGrid();
			System.out.println();
			
			System.out.print("Select a card: ");
			input = Integer.parseInt(reader.readLine());	//converting string to integer
			row1 = input / 10;								//obtain first digit
			col1 = input % 10;								//obtain second digit
	
			if(checkWrongInput(row1, col1) == true)			//checking input
				status = false;
		} while(status == false);							//repeat if there are any input errors
			
		card[row1][col1].changeCardStatus();				//make card visible
		
		//second input
		do
		{
			status = true;
			showGrid();
			System.out.println();
			
			System.out.print("Select another card: ");
			input = Integer.parseInt(reader.readLine());
			row2 = input / 10;
			col2 = input % 10;
	
			if(checkWrongInput(row2, col2) == true)
				status = false;
		} while(status == false);

		card[row2][col2].changeCardStatus();
		
		showGrid();								//shows two opened cards
		
		//check if two cards are the same and passing "num" argument as current player's number
		compareCards(card[row1][col1], card[row2][col2], num);
	}
	
	//checking for invalid input
	public boolean checkWrongInput(int row, int col)
	{	
		if(row >= ROWS || col >= COLS || row < 0 || col < 0)		//check if input is out of bounds
		{	
			System.out.println("Number out of bounds! Try again\n");
			return true;
		}
		
		//check if card is empty (guessed already)
		else if(card[row][col].getFrontSide() == BLANK && card[row][col].getBackSide() == BLANK)
		{	
			System.out.println("Empty element! Try again\n");
			return true;
		}
		
		else
			return false;
	}
	
	//checking if game is solved
	public boolean checkForSolvedGame()
	{
		int count = 0;
		
		for(int i = 0; i < ROWS; i++)
		{
			for(int j = 0; j < COLS; j++)
			{
				if(card[i][j].getFrontSide() == BLANK && card[i][j].getBackSide() == BLANK)
					count++;						//increment if card is empty
			}
		}
		
		if(count == ROWS * COLS)					//counter should be 40 (total number of elements)
			return true;							//if all cards are empty, game is solved
		else
			return false;
	}
	
	//starting actual game
	public void startGame()
			throws InterruptedException, NumberFormatException, IOException
	{
		createPlayers();
	}
	
	//main method
	public static void main(String[] args)
			throws InterruptedException, NumberFormatException, IOException
	{
		MemoryGame game = new MemoryGame();					//most elements of the game are created
		game.startGame();									//actual game starts
	}
}