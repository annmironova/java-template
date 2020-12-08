package edu.spbu.matrix;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.IOException;

/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix {
  public double[][] M;
  public int height, width;

  public DenseMatrix(String fileName) {
    try {
      Scanner data = new Scanner(new File(fileName));
      double[] buf;
      String[] row;
      ArrayList<double[]> a = new ArrayList<>();

      while (data.hasNextLine()) {
        row = data.nextLine().split(" ");
        buf = new double[row.length];
        for (int i = 0; i < buf.length; i++) {
          buf[i] = Double.parseDouble(row[i]);
        }
        a.add(buf);
      }

      double[][] result = a.toArray(new double[a.size()][]);

      M = result;
      this.height = result.length;
      this.width = result[0].length;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public DenseMatrix(double[][] mat) {
    M = mat;
    height = mat.length;
    width = mat[0].length;
  }


  public void ToString(double[][] res, int h, int w) {

    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        builder.append(res[i][j]).append(" ");
      }
      builder.append("\n");
    }
    System.out.println(builder.toString());
    try {
      FileWriter writer = new FileWriter("result.txt", false);
      writer.write(builder.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Matrix mul(Matrix o) {
    try {
      if (o instanceof DenseMatrix) {
          return mul((DenseMatrix) o);
      }
      else if (o instanceof SparseMatrix) {
        return mul((SparseMatrix) o);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  private DenseMatrix mul(DenseMatrix DM) throws Exception {
    if (width != DM.height) throw new Exception("The number of columns of the 1st matrix must " +
            "be the same as the number of rows of the 2d matrix");
    double[][] res = new double[height][DM.width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < DM.width; j++) {
        for (int l = 0; l < DM.height; l++) {
          res[i][j] += M[i][l] * DM.M[l][j];
        }
      }
    }
    // ToString(res, res.length, res[0].length);
    return new DenseMatrix(res);
  }

  private SparseMatrix mul(SparseMatrix SM) throws Exception{
    if (width != SM.height) throw new Exception("The number of columns of the 1st matrix must " +
            "be the same as the number of rows of the 2d matrix");
    HashMap<Point, Double> res = new HashMap<>();
    for (Point key : SM.M.keySet()) {
      for (int i = 0; i < height; i++) {
        if (M[i][key.x] != 0) {
          Point a = new Point(i, key.y);
          if (res.containsKey(a)) {
            res.put(a, res.get(a) + M[i][key.x] * SM.M.get(key));
          } else {
            res.put(a, M[i][key.x] * SM.M.get(key));
          }
        }
      }
    }
    return new SparseMatrix(res, height, SM.width);
  }

  class myThread implements Runnable {
    Thread t;
    int firstRow, lastRow;
    DenseMatrix D, res;

    myThread (int firstRow, int lastRow, DenseMatrix D, DenseMatrix res) {
      this.firstRow = firstRow;
      this.lastRow = lastRow;
      this.D = D;
      this.res = res;
      t = new Thread(this);
      t.start();
    }

    @Override public void run() {
      for (int i = firstRow; i < lastRow; i++) {
        for (int j = 0; j < D.width; j++) {
          for (int l = 0; l < D.height; l++) {
            res.M[i][j] += M[i][l] * D.M[l][j];
          }
        }
      }
    }
  }


  @Override
  public Matrix dmul(Matrix o) throws Exception {
    if (o instanceof DenseMatrix) {
      DenseMatrix D = (DenseMatrix) o;
      if (width != D.height) throw new Exception("The number of columns of the 1st matrix must " +
              "be the same as the number of rows of the 2d matrix");
      DenseMatrix res = new DenseMatrix(new double[height][D.width]);
      int step = height / 4;
      int accuracy = height - step * 4;

      myThread t0 = new myThread(0, step, D, res);
      myThread t1 = new myThread(step, 2 * step, D, res);
      myThread t2 = new myThread(2 * step, 3 * step, D, res);
      myThread t3 = new myThread(3 * step, 4 * step + accuracy, D, res);
        try {
          t0.t.join();
          t1.t.join();
          t2.t.join();
          t3.t.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
    return res;
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof DenseMatrix) {
      DenseMatrix DM = (DenseMatrix) o;
      if ((width == DM.width) && (height == DM.height)) {
        for (int i = 0; i < height; i++) {
          for (int j = 0; j < width; j++) {
            if (DM.M[i][j] != M[i][j]) {
              return false;
            }
          }
        }
      } else {
        return false;
      }
      return true;
    }

    if (o instanceof SparseMatrix) {
      SparseMatrix SM = (SparseMatrix) o;
      if ((width == SM.width) && (height == SM.height)) {
        int count1 = 0, count2 = 0;
        for (int i = 0; i < height; i++) {
          for (int j = 0; j < width; j++) {
            if (M[i][j] != 0) {
              count1++;
            }
          }
        }
        if (SM.M.size() == count1) {
          for (Point key : SM.M.keySet()) {
            if (M[key.x][key.y] != SM.M.get(key)) {
              return false;
            }
            else {
              count2++;
            }
          }
        }
        return (count1 == count2);
      }
    }
    return false;
  }
}
