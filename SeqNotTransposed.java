import java.util.*;
import java.util.concurrent.CyclicBarrier;

class SeqNotTransposed {

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
      long start = System.nanoTime();
      c = multiplyMatrices(a,b,n);
      long end = System.nanoTime();
      runtimes[i] = ((end-start)/1000000.0);
      // System.out.println(runtimes[i]);
    }
    Arrays.sort(runtimes);

    System.out.println("SeqNotTransposed median execution time was: " + runtimes[3] + " milliseconds.");

    Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.SEQ_NOT_TRANSPOSED, c);
  }



/////// Additional methods: //////

  public static double[][] multiplyMatrices(double[][] a, double[][] b, int n) {
    double[][] c = new double[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        for (int k = 0; k < n; k++) { //since neither matrice is transposed, we multiply rows by columns
          c[i][j] += a[i][k] * b[k][j];
        }
      }
    }
    return c;
  }


}
