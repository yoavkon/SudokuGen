import java.util.Random;
import java.util.ArrayList;

public class Sudoku
{
    static final int MAX_DIGIT = 9;
    static final int SIZE = 9;     // the grid is 9x9
    static final int SUB_SIZE = 3; // the subgrids are 3x3
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.err.println("not enough arguments");
            System.out.println("Usage: [solve / generate]");
        }
        else if (args[0].equals("generate"))
        {
            int[][] sudoku_grid = new int[SIZE][SIZE];

            // generate the full grid
            System.out.println("generating..");
            GenerateGrid(sudoku_grid);
            PrintGrid(sudoku_grid);
            System.out.println();

            // remove a number of cells
            // (the more you remove, the more difficult it gets)
            System.out.println("removing 40 cells..");
            RemoveCells(40, sudoku_grid);
            PrintGrid(sudoku_grid);
            System.out.println();

            System.out.println("solving again just for fun..");
            SolveGrid(sudoku_grid, 0, 0, true);
            PrintGrid(sudoku_grid);
        }
        else if (args[0].equals("solve"))
        {
            int[][] sudoku_grid = 
            {
                {2, 0, 0, 0, 0, 3, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 3, 0, 0, 2, 0, 8, 6, 4},
                {3, 0, 0, 4, 0, 0, 1, 0, 0},
                {0, 0, 5, 0, 9, 0, 7, 0, 0},
                {0, 4, 0, 7, 0, 0, 0, 0, 5},
                {5, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 6, 2, 0, 0, 7, 0, 0, 0},
                {7, 0, 0, 1, 0, 9, 0, 0, 0}
            };
            
            PrintGrid(sudoku_grid);
            System.out.println("Solving..");
            if (!IsGridValid(sudoku_grid)) System.out.println("Unsolvable");
            if (IsUniquelySolvable(sudoku_grid))
            {
                SolveGrid(sudoku_grid, 0, 0, true);
                System.out.println("Success!");
                PrintGrid(sudoku_grid);
            }
            else if (SolveGrid(sudoku_grid, 0, 0, true))
            {
               System.out.println("More than one possible solution :(");
               System.out.println("ascending order:");
               PrintGrid(sudoku_grid);
            }
        }
        else System.out.println("Usage: [solve / generate]");
    }

    public static void RemoveCells(int num_remove, int[][] grid)
    {
        // base case: no more cells to remove
        if (num_remove == 0) return;

        Random rnd = new Random();
        int row, col;

        do
        {
            row = rnd.nextInt(SIZE);
            col = rnd.nextInt(SIZE);
        }
        while (grid[row][col] == 0);

        // remove cell
        int temp = grid[row][col];
        grid[row][col] = 0;

        if (IsUniquelySolvable(grid))
        {
            RemoveCells(num_remove - 1, grid);
            return;
        }

        // backtrack;
        grid[row][col] = temp;
        RemoveCells(num_remove, grid);
    }

    public static boolean GenerateGrid(int[][] grid)
    {
        for (int row = 0; row < SIZE; row++)
        {
            for (int col = 0; col < SIZE; col++)
            {
                if (grid[row][col] == 0)
                {
                    Random rand = new Random();

                    // array of random numbers from 1-9
                    int[] random_nums = rand.ints(1, MAX_DIGIT+1).distinct().limit(SIZE).toArray();

                    for (int num : random_nums)
                    {
                        grid[row][col] = num;
                        if (IsCellValid(row, col, grid))
                        {
                            // backtrack
                            if (GenerateGrid(grid))
                                return true;
                        }
                        grid[row][col] = 0;
                    }

                    return false;
                }
            }
        }
        return true;
    }

    // This method solves the entire Sudoku grid
    // and returns true if it was successful (solvable)
    public static boolean SolveGrid(int[][] grid, int row, int col, boolean asc)
    {
        // base case: reached last cell
        if (row == SIZE - 1 && col == SIZE)
        {
            return true;
        }

        // go to next row every column
        if (col == SIZE)
        {
            // initialize column and increment row
            col = 0;
            row++;
        }

        if (grid[row][col] != 0)
            return SolveGrid(grid, row, col+1, asc);

        // check every number (backtracking)
        // either in ascending order or descending order
        // (later used to determine if the grid is uniquely solvable)
        int i = 0;
        boolean cond = false;
        if (asc)
        {
            i = 1;
            cond = (i <= MAX_DIGIT);
        }
        else
        {
            i = MAX_DIGIT;
            cond = (i >= 1);
        }
        while (cond)
        {
            grid[row][col] = i;
            if (IsCellValid(row, col, grid))
            {
                // Try to solve
                if (SolveGrid(grid, row, col+1, asc))
                    return true;
            }
            
            // backtrack
            grid[row][col] = 0;
            
            if (asc) {
                i++;
                cond = (i <= MAX_DIGIT);
            }
            else {
                i--;
                cond = (i >= 1);
            }
        }

        return false;
    }

    // This method determines whether the
    // puzzle has only one unique solution
    public static boolean IsUniquelySolvable(int[][] grid)
    {
        // Copy the grid
        int[][] solution1 = new int[SIZE][SIZE]; // ascending
        int[][] solution2 = new int[SIZE][SIZE]; // descending
        for (int i = 0; i < SIZE; i++)
        {
            solution1[i] = grid[i].clone();
            solution2[i] = grid[i].clone();
        }
        
        // Solve the Sudoku grid once in ascending order
        // and once in descending order and compare the two
        if (SolveGrid(solution1, 0, 0, true) && SolveGrid(solution2, 0, 0, false))
        {
            if (java.util.Arrays.deepEquals(solution1, solution2))
                return true;
        }

        return false;
    }

    public static boolean IsCellValid(int row, int col, int[][] grid)
    {
        int n = grid[row][col];

        // Special case, 0 represents empty value
        if (n == 0) return true;

        // Check all columns
        for (int i = 0; i < SIZE; i++)
            if (grid[row][i] == n && i != col)
                return false;
        
        // Check all rows
        for (int i = 0; i < SIZE; i++)
            if (grid[i][col] == n && i != row)
                return false;

        // Check all 3x3 subgrids
        int boxRow = row - (row % SUB_SIZE);
        int boxCol = col - (col % SUB_SIZE);
        for (int i = 0; i < SUB_SIZE; i++)
        {
            for (int j = 0; j < SUB_SIZE; j++)
            {
                if (grid[i+boxRow][j+boxCol] == n && (i+boxRow) != row && (j+boxCol) != col)
                {
                    return false;
                }
            }
        }

        return true;
    }

    // Temporary function, inefficient
    public static boolean IsGridValid(int[][] grid)
    {
        for (int row = 0; row < SIZE; row++)
        {
            for (int col = 0; col < SIZE; col++)
            {
                if (!IsCellValid(row, col, grid))
                    return false;
            }
        }
        return true;
    }

    // This method prints the Sudoku grid
    public static void PrintGrid(int[][] grid)
    {
        for (int row = 0; row < SIZE; row++)
        {
            for (int col = 0; col < SIZE; col++)
            {
                // Print cell
                if (grid[row][col] == 0)
                    System.out.print("# ");
                else
                    System.out.print(grid[row][col] + " ");

                // Add a vertical divider
                if ((col + 1) % SUB_SIZE == 0 && (col + 1) % SIZE != 0)
                    System.out.print("| ");
            }
            // Add a new line every row
            System.out.println();
            
            // Add a horizontal divider every 3 rows
            if ((row+1) % SUB_SIZE == 0 && (row+1) != SIZE)
                System.out.println("------+-------+------");
        }
    }
}
