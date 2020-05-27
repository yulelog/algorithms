/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double Z_SCORE = 1.96;
    private final int gridSize;
    private final int numberOfTrials;
    private final double[] thresholdsArray;
    private final double mean;
    private final double stdDev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        gridSize = n;
        numberOfTrials = trials;
        thresholdsArray = getThresholdsArray();
        mean = mean();
        stdDev = stddev();
    }

    private double getPercolationThreshold() {
        Percolation grid = new Percolation(gridSize);
        while (!grid.percolates()) {
            int x = StdRandom.uniform(1, gridSize + 1);
            int y = StdRandom.uniform(1, gridSize + 1);
            grid.open(x, y);
        }
        return ((double) grid.numberOfOpenSites()) / (gridSize * gridSize);
    }

    private double[] getThresholdsArray() {
        double[] percolationThresholds = new double[numberOfTrials];
        for (int i = 0; i < numberOfTrials; i++) {
            percolationThresholds[i] = getPercolationThreshold();
        }
        return percolationThresholds;
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholdsArray);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholdsArray);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - (Z_SCORE * stdDev / Math.sqrt(numberOfTrials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + (Z_SCORE * stdDev / Math.sqrt(numberOfTrials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int gridSize = Integer.parseInt(args[0]);
        int numberOfTrials = Integer.parseInt(args[1]);
        PercolationStats p = new PercolationStats(gridSize, numberOfTrials);
        System.out.println("mean = \t" + p.mean);
        System.out.println("stddev = \t" + p.stdDev);
        System.out.println(
                "95% confidence interval = \t [" + p.confidenceLo() + ", " + p.confidenceHi()
                        + "]");
    }

}
