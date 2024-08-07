public class Sudoku
{
    static final int MAX_DIGIT = 9;
    public static void main(String[] args)
    {
        int[][] sudoku_grid = 
        {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 6, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
//        int[][] sudoku_grid = 
//        {
//            {5, 3, 0, 0, 7, 0, 0, 0, 0},
//            {6, 0, 0, 1, 9, 5, 0, 0, 0},
//            {0, 9, 8, 0, 0, 0, 0, 6, 0},
//            {8, 0, 0, 0, 6, 0, 4, 2, 3},
//            {4, 0, 0, 8, 0, 3, 7, 9, 1},
//            {7, 0, 0, 0, 2, 0, 0, 0, 6},
//            {0, 6, 0, 0, 0, 0, 2, 8, 0},
//            {0, 0, 0, 4, 1, 9, 0, 0, 5},
//            {0, 0, 0, 0, 8, 0, 0, 7, 9}
//        };
//        int[][] sudoku_grid = 
//        {
//            {5, 3, 4, 6, 7, 8, 9, 1, 2},
//            {6, 7, 2, 1, 9, 5, 3, 4, 8},
//            {1, 9, 8, 3, 4, 2, 5, 6, 7},
//            {8, 5, 9, 7, 6, 1, 4, 2, 3},
//            {4, 2, 6, 8, 5, 3, 7, 9, 1},
//            {7, 1, 3, 9, 2, 4, 8, 5, 6},
//            {9, 6, 1, 5, 3, 7, 2, 8, 4},
//            {2, 8, 7, 4, 1, 9, 6, 3, 5},
//            {3, 4, 5, 2, 8, 6, 1, 7, 9}
//        };
        PrintGrid(sudoku_grid);
        System.out.println("Solving..");
        if (SolveGrid(sudoku_grid, 0, 0, true))
            System.out.println("Success!");
        else 
            System.out.println(":(");
        PrintGrid(sudoku_grid);
        System.out.println("Grid is valid: " + IsGridValid(sudoku_grid));
    }

    // Temporary function
    public static boolean IsGridValid(int[][] grid)
    {
        for (int row = 0; row < grid[0].length; row++)
        {
            for (int col = 0; col < grid.length; col++)
            {
                if (!IsCellValid(row, col, grid))
                    return false;
            }
        }
        return true;
    }

    // This method solves the entire Sudoku grid
    // and returns true if it was successful (solvable)
    public static boolean SolveGrid(int[][] grid, int row, int col, boolean asc)
    {
        // base case: reached last cell
        if (row == grid[0].length - 1 && col == grid.length)
        {
            return true;
        }

        // go to next row every column
        if (col == grid.length)
        {
            // initialize column
            col = 0;
            row++;
        }

        if (grid[row][col] != 0)
            return SolveGrid(grid, row, col+1, asc);

        // check every number (backtracking)
        // either in ascending order or descending
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
            
            // backtrack (go back to previous copy)
            grid[row][col] = 0;
            
            if (asc) {
                i++;
                cond = (i <= MAX_DIGIT);
            }
            else {
                i--;
                cond = (i >= 1);
                System.out.println(i);
                System.out.println(cond);
            }
        }

        return false;
    }

    public static boolean IsCellValid(int row, int col, int[][] grid)
    {
        int n = grid[row][col];

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
