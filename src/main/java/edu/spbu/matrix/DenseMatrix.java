package edu.spbu.matrix;

import java.io.File;
import java.util.ArrayList;
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
      Double[] buf = {};
      String[] row;
      ArrayList<Double[]> a = new ArrayList<Double[]>();

      while (data.hasNextLine()) {
        row = data.nextLine().split(" ");
        buf = new Double[row.length];
        for (int i = 0; i < buf.length; i++) {
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

  public DenseMatrix(double[][] mat) {
    this.M = mat;
    this.height = mat.length;
    this.width = mat[0].length;
  }

  @Override public Matrix mul(Matrix o)
  {
    if (o instanceof DenseMatrix) {
      return mul((DenseMatrix) o);
    }
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

  @Override public boolean equals(Object o) {
    if (o instanceof DenseMatrix) {
      DenseMatrix DM = (DenseMatrix)o;
      if ((width == DM.width) && (height == DM.height)) {
        for (int i = 0; i < height; i++) {
          for (int j = 0; j < width; j++) {
            if (DM.M[i][j] != M[i][j]) {
              return false;
            }
          }
        }
      }
      return true;
    }
    return false;
  }

}
