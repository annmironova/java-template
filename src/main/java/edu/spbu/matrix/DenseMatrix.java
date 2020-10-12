package edu.spbu.matrix;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix {
  public double[][] M;
  public int height, width;

  public DenseMatrix(String fileName) {
    try {
      Scanner data = new Scanner(new File(fileName));
      Double[] buf = {};
      String[] row;
      ArrayList<Double[]> a = new ArrayList<Double[]>();


      while (data.hasNextLine()) {
        row = data.nextLine().split(" ");
        buf = new Double[row.length];
        int w = buf.length;
        for (int i = 0; i < w; i++) {
          buf[i] = Double.parseDouble(row[i]);
        }
        a.add(buf);
      }

      double[][] result = new double[a.size()][buf.length];
      for (int i = 0; i < result.length; i++) {
        for (int j = 0; j < result[0].length; j++) {
          result[i][j] = a.get(i)[j];
        }
      }

      M = result;
      this.height = result.length;
      this.width = result[0].length;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public DenseMatrix(double[][] matr) {
    this.M = matr;
    this.height = matr.length;
    this.width = matr[0].length;
  }

  @Override public Matrix mul(Matrix o)
  {
    if (o instanceof DenseMatrix)
      return mul((DenseMatrix) o);
    else return null;
  }

  private DenseMatrix mul(DenseMatrix DM) {
    double[][] res = new double[height][DM.width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < DM.width; j++) {
        for (int k = 0; k < DM.height; k++) {
          res[i][j] += M[i][k] * DM.M[k][j];
        }
      }
    }
    return new DenseMatrix(res);
  }


  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   */
  @Override public Matrix dmul(Matrix o)
  {
    return null;
  }

  /**
   * спавнивает с обоими вариантами
   * @param o
   * @return
   */
  @Override public boolean equals(Object o) {
    return false;
  }

}
