/* Desafio baseado em https://www.hackerrank.com/challenges/bomber-man */

import java.util.Scanner;

public class Bomberman {
	
	static final Scanner s = new Scanner(System.in);

	public static void main(String[] args) {
		int r = s.nextInt();
		int c = s.nextInt();
		int n = s.nextInt();

		boolean [][] grid = readGrid(r, c);
		// bombas em posicoes "aleatorias"
		System.out.println(grid[2][2]);
		printGrid(grid);
	}

	public static void explodeGrid(boolean [][] grid) {

	}

	public static void explodeBomb(boolean [][] grid, int x, int y) {

	}	

	// completar
	public static void fillWithBombs() {}

	public static boolean[][] readGrid(int r, int c) {
		boolean [][] grid = new boolean[r][c];
		for ( int i = 0; i < r; i++ ) {
			char [] row = s.nextLine().toCharArray();
			for ( int j = 0; j < row.length; j++ )
				if ( row[j] == '0' )
					grid[i][j] = true;
				else
					grid[i][j] = false;
		}

		return grid;
	}

	// implementada
	public static void printGrid(boolean [][] grid) {
		for ( int i = 0; i < grid.length; i++ ) {
			for ( int j = 0; j < grid[0].length; j++ ) {
				if ( grid[i][j] )
					System.out.print( "0" );
				else
					System.out.print( "." );
			}
			System.out.println();
		}
	}
}
