// Author: Shachar Ron 203018254

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Sudoku {

	public static void main(String[] args) {

		int[][] board = readBoardFromFile("test3.txt");

		int domains [][][]=eliminateDomains(board);
		printBoard( domains,board);
		System.out.println(	solveSudoku( board));

	}



	// **************   Sudoku - Read Board From Input File   **************
	public static int[][] readBoardFromFile(String fileToRead){
		int[][] board = new int[9][9];
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToRead)); // change S1.txt to any file you like (S2.txt, ...)
			int row = 0;
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				for(int column = 0; column < line.length(); column++){
					char c = line.charAt(column);
					if(c == 'X')
						board[row][column] = 0;
					else board[row][column] = c - '0';
				}
				row++;
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return board;
	}



	// **************   Sudoku - Part1 (iterative)   **************

	//This function gets Sudoku table, row number and the key number.
	//The function returns if the key number is found in a row.
	public static boolean checkRow(int[][] board,int row,int key){
		for (int i=0; i<9; i++) {
			if(board[row][i]== key) {
				return false;
			}
		}
		return true;
	}

	//This function gets Sudoku table, column number and the key number.
	//The function returns if the key number is found in a column.
	public static boolean checkCol(int[][] board,int col, int key){
		for (int i=0; i<9; i++) {
			if(board[i][col]== key) {
				return false;
			}
		}
		return true;
	}

	//This function gets Sudoku table, column number, row number and the key number.
	//The function returns if the key number is found in a matrix 3*3.
	public static boolean checkMatrix(int[][] board,int row,int col,int key){

		int k=0;
		int l=0;

		if(row>=0 && row<=2) {
			k=0;
		}
		else if(row>=3 && row<=5) {
			k=3;
		}
		else {
			k=6;
		}

		if(col>=0 && col<=2) {
			l=0;
		}
		else if(col>=3 && col<=5) {
			l=3;
		}
		else {
			l=6;
		}

		for(int i=k ;i<k+3 ; i++) {
			for(int j=l;j<l+3 ; j++) {
				if(board[i][j]== key) {
					return false;
				}
			}
		}
		return true;
	}


	//This function gets a sudoku array that contains a partial insertion.
	//The function returns a three-dimensional array containing all numbers that can be placed
	//in any empty square in the original Sudoku array.

	public static int[][][] eliminateDomains(int[][] board){

		int [][][] domains= new int [9][9][9];
		int counter1=0; //Count the number of options each slot has per run.
		int counter2=0; //Checks whether the update options in the board are finished.

		while (counter2==0) {
			//Reset the 3D array at the beginning of every test.
			for (int i=0; i<9; i++) {
				for( int j=0; j<9; j++) {
					for(int k=0; k<9; k++) {
						domains[i][j][k]=0;
					}
				}
			}

			for (int i=0; i<9; i++) {
				for( int j=0; j<9; j++) {
					for(int key=1; key<10; key++) {
						if(board[i][j]==0) {
							if(checkRow(board,i,key)==true && checkCol(board,j,key)==true && checkMatrix(board, i,j,key)==true) 
								domains[i][j][key-1]=1;

						}
						else {
							domains[i][j][key-1]=0;

						}
					}// key

				}

			}
			int temp=0;
			for (int i=0; i<9; i++) {
				for( int j=0; j<9; j++) { 
					if(board [i][j]==0) {
						for (int k=0; k<9; k++) {
							if(domains[i][j][k]==1) {
								temp= k+1;
								counter1++;
							}

						}
						if(counter1==1) {
							board[i][j]=temp;
							counter2++;
						}
						counter1=0;
					}
				}
			}
			if(counter2==0) {
				return domains;
			}
			else {
				counter2=0;
			}
		}



		return domains; 

	}



	//This function gets a sudoku array and get three-dimensional array
	//containing all numbers that can be placed in any empty square in the original Sudoku array.
	// The function prints the Sudoku array and all the options for numbers that can be put in any slot on the Sudoku board.
	public static void printBoard(int[][][] domains, int[][] board){

		for(int i=0 ;i<9 ; i++) {
			if(i==3 || i==6) {
				System.out.println("---+---+---");

			}	
			for(int j=0;j<9 ; j++) {
				if(j==3 || j==6) {
					System.out.print("|");
				}
				System.out.print(board [i][j]);
			}

			System.out.println();
		}

		//Prints all values that can still be set.
		for(int i=0; i<9; i++) {
			for(int j=0 ;j<9 ; j++) {
				System.out.print(i+ ","+ j + " = ");
				if(board[i][j]==0) {
					for(int k=0; k<9; k++) {
						if(domains[i][j][k]!=0)
							System.out.print((k+1)+ "," );
					}
				}
				else {
					System.out.print(board[i][j]+ "," );
				}
				System.out.println();
			}
		}
	}


	// **************   Sudoku - Part2 (recursive)   **************

	//This function check if the sudoku table is full.
	//The function returns true if it is full and false if not.
	public static boolean isFull(int [][] board){
		for(int i=0; i<9; i++) {
			for(int j=0 ;j<9 ; j++) {
				if (board[i][j]==0) {
					return false;
				}
			}
		}
		return true;
	}

	//The function gets Sudoku table, Row, and Column.
	//The function returns the options of possible numbers that can be put in a specific slot.
	public static int [] possibilityArray(int [][] board, int row, int col){
		int [] possibilityArray= new int [9];
		for(int key=1; key<10; key++) {
			if(board[row][col]==0) {
				if(checkRow(board,row,key)==true && checkCol(board,col,key)==true && checkMatrix(board, row,col,key)==true) 
					possibilityArray[key-1]=1;
			}
			else {
				possibilityArray[key-1]=0;

			}
		}// key
		return possibilityArray;

	}

	//This function gets a sudoku array that contains a partial insertion.
	//The function returns true if there is a solution for the Sudoku table and false if not.
	public static boolean solveSudoku(int[][] board){

		int row=0;
		int col=0;
		boolean ans=false;
		int [] possibilityArray= new int [9];

		if (isFull(board)==true) {
			ans= true;
			return ans;
		}

		//find the first vacant spot.
		else {
			boolean isEmpty=true;
			for (int i=0; i<9 && isEmpty== true;i++) {
				for(int j=0; j<9 & isEmpty== true; j++) {
					if(board[i][j]==0) {
						row=i;
						col=j;
						isEmpty=false;
					}


				}
			}
		}
		//take all the option for specific slot.
		possibilityArray= possibilityArray(board,row, col);


		//recursive call
		for( int i=0; i< 9 ; i++) {
			if (possibilityArray[i]!=0) {
				board[row][col]=i+1;
				solveSudoku(board);
			}

			if (isFull(board)==false) {
				board[row][col]=0;
			}
			else {
				ans= true;
			}

		}

		return ans;
	}

}
