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
      double[] buf = {};
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
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public DenseMatrix(double[][] mat) {
    this.M = mat;
    this.height = mat.length;
    this.width = mat[0].length;
  }

  public String ToString(double[][] res, int h, int w)
  {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        builder.append(res[i][j]).append(" ");
      }
      builder.append("\n");
    }
    System.out.println(builder.toString());
    return builder.toString();
  }

  @Override
  public Matrix mul(Matrix o)
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
        for (int l = 0; l < DM.height; l++) {
          res[i][j] += M[i][l] * DM.M[l][j];
        }
      }
    }
    ToString(res, res.length, res[0].length);
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
      else {return false;}
      return true;
    }
    return false;
  }

}
