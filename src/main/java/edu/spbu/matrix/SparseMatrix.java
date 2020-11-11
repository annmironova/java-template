package edu.spbu.matrix;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.io.IOException;
import java.util.Scanner;

/**
 * Разряженная матрица
 */

public class SparseMatrix implements Matrix {
  public HashMap<Point, Double> M;
  public int height, width;

  public SparseMatrix(String fileName) {
    try {
      Scanner data = new Scanner(new File(fileName));
      M = new HashMap<>();
      String[] buf;
      int h = 0, w = 0;
      double a;
      while (data.hasNextLine()) {
        buf = data.nextLine().split(" ");
        w = buf.length;
        for (int i = 0; i < w; i++) {
          a = Double.parseDouble(buf[i]);
          if (a != 0) {
            M.put(new Point(h, i), a);
          }
        }
        h++;
      }
      height = h;
      width = w;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public SparseMatrix(HashMap<Point, Double> M, int height, int width) {
    this.M = M;
    this.height = height;
    this.width = width;
  }

  @Override
  public Matrix mul(Matrix o) {
    try {
      if (o instanceof DenseMatrix) {
        return mul((DenseMatrix) o);
      }
      if (o instanceof SparseMatrix) {
        return mul((SparseMatrix) o);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public void ToString(HashMap<Point, Double> res, int h, int w) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        Point a = new Point(i, j);
        if (res.containsKey(a)) {
          builder.append(res.get(a)).append(" ");
        } else {
          builder.append("0").append(" ");
        }
      }
      builder.append("\n");
    }
    System.out.println(builder.toString());
  }

  private SparseMatrix mul(SparseMatrix SM) throws Exception {
    if (width != SM.height) throw new Exception("The number of columns of the 1st matrix must " +
            "be the same as the number of rows of the 2d matrix");
    HashMap<Point, Double> res = new HashMap<>();
    for (Point key : M.keySet()) {
      for (int i = 0; i < height; i++) {
        Point a = new Point(key.y, i);
        if (SM.M.containsKey(a)) {
          Point b = new Point(key.x, i);
          if (res.containsKey(b)) {
            res.put(b, res.get(b) + M.get(key) * SM.M.get(a));
          } else {
            res.put(b, M.get(key) * SM.M.get(a));
          }
        }
      }
    }
    ToString(res, height, SM.width);
    return new SparseMatrix(res, height, SM.width);
  }

  private DenseMatrix mul(DenseMatrix DM) throws Exception {
      if (width != DM.height) throw new Exception("The number of columns of the 1st matrix must " +
              "be the same as the number of rows of the 2d matrix");
    double[][] res = new double[height][DM.width];
    for (Point key : M.keySet()) {
      for (int i = 0; i < height; i++) {
        if (DM.M[key.y][i] != 0) {
            res[key.x][i] += M.get(key) * DM.M[key.y][i];
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
  @Override
  public Matrix dmul(Matrix o) {
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof SparseMatrix) {
      SparseMatrix M2 = (SparseMatrix) o;
      if (height == M2.height || width == M2.width) {
        int count1 = 0, count2 = 0;
        if (M.size() == M2.M.size()) {
          for (Point key : M.keySet()) {
            count1++;
            if (M2.M.containsKey(key)) {
              double a = M.get(key);
              double b = M2.M.get(key);
              if (a == b) {
                count2++;
              }
            }
          }
          return (count1 == count2);
        }
      }
      return false;
    }

    else if (o instanceof DenseMatrix) {
      DenseMatrix M2 = (DenseMatrix) o;
      if (height == M2.height || width == M2.width) {
        int count1 = 0, count2 = 0;
        for (int i = 0; i < M2.height; i++) {
          for (int j = 0; j < M2.width; j++) {
            if (M2.M[i][j] != 0) {
              count1++;
            }
          }
        }
        if (M.size() == count1) {
          for (Point key : M.keySet()) {
            if (M2.M[key.x][key.y] == M.get(key)) {
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
