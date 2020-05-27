/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int VIRTUAL_TOP = 0;
    private final int virtualBottom;
    private final int gridSize; // gridSize/width of the square grid
    private boolean[][] site; // site[row][col]
    private final WeightedQuickUnionUF uf1;
    private final WeightedQuickUnionUF uf2; // to solve backwash issue
    private int numberOfOpenSites = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        gridSize = n;
        site = new boolean[n][n];
        virtualBottom = n * n + 1;
        uf1 = new WeightedQuickUnionUF(n * n + 2);
        uf2 = new WeightedQuickUnionUF(n * n + 2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!inBound(row, col)) throw new IllegalArgumentException();

        if (!isOpen(row, col)) numberOfOpenSites++;
        site[row - 1][col - 1] = true;

        if (row == 1) { // connect the first row with virtual top
            uf1.union(siteIndex(row, col), VIRTUAL_TOP);
            uf2.union(siteIndex(row, col), VIRTUAL_TOP);
        }
        if (row == gridSize) { // connect the last row with virtual bottom
            uf1.union(siteIndex(row, col), virtualBottom);
        }

        // connect to the possible neighbours in the four directions
        if (row > 1 && isOpen(row - 1, col)) { // connect to the site above
            uf1.union(siteIndex(row, col), siteIndex(row - 1, col));
            uf2.union(siteIndex(row, col), siteIndex(row - 1, col));
        }
        if (row < gridSize && isOpen(row + 1, col)) { // connect to the site below
            uf1.union(siteIndex(row, col), siteIndex(row + 1, col));
            uf2.union(siteIndex(row, col), siteIndex(row + 1, col));
        }
        if (col > 1 && isOpen(row, col - 1)) { // connect to the site to the left
            uf1.union(siteIndex(row, col), siteIndex(row, col - 1));
            uf2.union(siteIndex(row, col), siteIndex(row, col - 1));
        }
        if (col < gridSize && isOpen(row, col + 1)) { // connect to the site to the right
            uf1.union(siteIndex(row, col), siteIndex(row, col + 1));
            uf2.union(siteIndex(row, col), siteIndex(row, col + 1));
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!inBound(row, col)) throw new IllegalArgumentException();
        return site[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!inBound(row, col)) throw new IllegalArgumentException();
        return uf2.find(siteIndex(row, col)) == uf2.find(VIRTUAL_TOP);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf1.find(virtualBottom) == uf1.find(VIRTUAL_TOP);
    }

    // return the index of the site
    private int siteIndex(int row, int col) {
        return (row - 1) * gridSize + col;
    }

    // return if the given index in in bound of the grid
    private boolean inBound(int row, int col) {
        return row > 0 && row <= gridSize && col > 0 && col <= gridSize;
    }
}
