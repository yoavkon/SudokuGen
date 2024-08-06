import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;

public class Sudoku
{
    public static void main(String[] args)
    {
        int[][] sudokuGrid = 
        {
            {5, 3, 4, 6, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };
        PrintGrid(sudokuGrid);
        System.out.println("Grid is valid: " + IsGridValid(sudokuGrid));
    }

    // Temporary function
    public static boolean IsGridValid(int[][] grid)
    {
        for (int row = 0; row < grid[0].length; row++)
        {
            for (int col = 0; col < grid.length; col++)
            {
                if (!IsCellValid(row, col, grid, grid[row][col]))
                    return false;
            }
        }
        return true;
    }

    public static boolean IsCellValid(int row, int col, int[][] grid, int n)
    {
        // Special case, 0 represents empty value
        if (n == 0) return true;

        // Check all columns
        for (int i = 0; i < grid[0].length; i++)
            if (grid[row][i] == n && i != col)
                return false;
        
        // Check all rows
        for (int i = 0; i < grid.length; i++)
            if (grid[i][col] == n && i != row)
                return false;

        // Check all 3x3 subgrids
        int boxRow = row - row%3;
        int boxCol = col - col%3;
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (grid[i+boxRow][j+boxCol] == n && (i+boxRow) != row && (j+boxCol) != col)
                {
                    return false;
                }
            }
        }

        return true;
    }

    // This method prints the Sudoku grid
    public static void PrintGrid(int[][] grid)
    {
        for (int row = 0; row < grid[0].length; row++)
        {
            for (int col = 0; col < grid.length; col++)
            {
                // Print cell
                System.out.print(grid[row][col] + " ");

                // Add a vertical divider
                if ((col + 1) % 3 == 0 && (col + 1) % 9 != 0)
                    System.out.print("| ");
            }
            // Add a new line every row
            System.out.println();
            
            // Add a horitontal divider every 3 rows
            if ((row+1) % 3 == 0 && (row+1) != 9)
                System.out.println("------+-------+------");
        }
    }

}
