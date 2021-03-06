import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

class ParaNotTransposed {

  class PNT {
    int cores = Runtime.getRuntime().availableProcessors();
    int n;
    double[][] a, b, c;

    PNT(double[][] a, double[][] b, int n) {
      this.a = a;
      this.b = b;
      this.n = n;
      c = new double[n][n];
    }

    class Worker implements Runnable {
      int id, start, end;

      Worker(int id, int start, int end) {
        this.id = id;
        this.start = start;
        this.end = end;
      }

      public void run() {
        // double[][] temp = new double[n][n]; //temporary matrice to store results until single synchronization below

        for (int i = start; i < end; i++) { //each thread will calculate a specified range of rows
          for (int j = 0; j < n; j++) { //including every element in that row (all columns)
            for (int k = 0; k < n; k++) { //since neither matrice is transposed, we multiply rows by columns
              // temp[i][j] += a[i][k] * b[k][j]; //<--did not use this approach
              c[i][j] += a[i][k] * b[k][j];
            }
          }
        }

        // for (int i = start; i < end; i++) { //synchronizing the all results once, to minimize false sharing
        //   for (int j = 0; j < n; j++) {
        //     c[i][j] = temp[i][j];
        //   }
        // }

      }

    }

    public double[][] parallel() {
      int rows = n / cores; //how many rows each thread is responsible for
      Thread[] threads = new Thread[cores];

      for (int i = 0; i < cores - 1; i++) {
        threads[i] = new Thread(new Worker(i, i*rows, (i+1) * rows));
      }

      threads[cores - 1] = new Thread(new Worker(cores - 1, (cores - 1) * rows, n)); //incase "rows" does not "add up"

      for (Thread t : threads) { t.start(); }

      try {
        for (Thread t : threads) { t.join(); }
      } catch (Exception e) {
        e.printStackTrace();
      }

      return c;
    }


  }


  public static void main(String args[]) {
    int seed, n;
    seed = n = 0;

    try {
      seed = Integer.valueOf(args[0]);
      n = Integer.valueOf(args[1]);
    } catch (Exception e) {
      System.out.println("Please run the program with both seed and n as arguments.");
      System.exit(0);
    }

    double[][] a = Oblig2Precode.generateMatrixA(seed,n);
    double[][] b = Oblig2Precode.generateMatrixB(seed,n);

    double[] runtimes = new double[7];
    double[][] c = null;
    for (int i = 0; i < 7; i++) {
      PNT pnt = new ParaNotTransposed().new PNT(a,b,n);
      long start = System.nanoTime();
      c = pnt.parallel();
      long end = System.nanoTime();
      runtimes[i] = ((end-start)/1000000.0);
      // System.out.println(runtimes[i]);
    }
    Arrays.sort(runtimes);

    compareToSeqNT(a,b,c,n);

    System.out.println("ParaNotTransposed median execution time was: " + runtimes[3] + " milliseconds.");

    Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.PARA_NOT_TRANSPOSED, c);
  }



/////// Additional methods: //////

  public static void compareToSeqNT(double[][] a, double[][] b, double[][] c, int n) { //method for comparing result to SeqNotTransposed result, since Arrays.equals() will not be accurate for double-arrays.
    double[][] seqNotTransComparison = SeqNotTransposed.multiplyMatrices(a,b,n); //using SeqNotTransposed multiplication method, where I use a[i][k] instead
    for (int i = 0 ; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (round(seqNotTransComparison[i][j],2) != round(c[i][j],2)) {
          System.out.println("Something went wrong: ParaNotTransposed matrix multiplication result was not identical to SeqNotTransposed result.");
          System.out.println("Index [" + i + "][" + j + "] " + round(seqNotTransComparison[i][j],2) + " != " + round(c[i][j],2));
          System.exit(0);
        }
      }
    }
    System.out.println("Success: ParaNotTransposed matrix multiplication result was identical to SeqNotTransposed result!");
  }

  public static double round(double value, int places) { //method for rounding double, used for comparison with SeqNotTransposed
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

}
