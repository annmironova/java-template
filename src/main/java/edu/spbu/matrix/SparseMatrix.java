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
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public SparseMatrix(HashMap<Point, Double> M, int height, int width) {
    this.M = M;
    this.height = height;
    this.width = width;
  }

  @Override
  public Matrix mul(Matrix o) {
    if (o instanceof DenseMatrix) {
      return mul((DenseMatrix) o);
    }
    if (o instanceof SparseMatrix) {
      return mul((SparseMatrix) o);
    } else {
      return null;
    }
  }

  private SparseMatrix mul(SparseMatrix SM) {
    HashMap<Point, Double> res = new HashMap<>();
    for (Point key : M.keySet()) {
      for (int i = 0; i < height; i++) {
        Point a = new Point(key.y, i);
        if (SM.M.containsKey(a)) {
          Point b = new Point(key.x, i);
          if (res.containsKey(b)) {
            double value = res.get(b) + M.get(key) * SM.M.get(a);
            res.put(b, value);
          } else {
            double value = M.get(key) * SM.M.get(a);
            res.put(b, value);
          }
        }
      }
    }
    return new SparseMatrix(res, this.height, SM.width);
  }

  private DenseMatrix mul(DenseMatrix DM) {
    double[][] res = new double[height][DM.width];
    for (Point key : M.keySet()) {
      for (int i = 0; i < height; i++) {
        if (DM.M[key.y][i] != 0) {
          if (res[key.x][i] != 0) {
            res[key.x][i] += M.get(key) * DM.M[key.y][i];
          } else {
            res[key.x][i] = M.get(key) * DM.M[key.y][i];
          }
        }
      }
    }
    return new DenseMatrix(res);
  }

  public SparseMatrix transposeSM() {
    HashMap<Point, Double> transposed = new HashMap<>();
    for (Point key : M.keySet()) {
      transposed.put(new Point(key.y, key.x), M.get(key));
    }
    return new SparseMatrix(transposed, height, width);
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
      if (height != M2.height || width != M2.width) {
        return false;
      }
      int count1 = 0, count2 = 0;
      if (M.size() != M2.M.size()) {
        return false;
      } else {
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
        if (count1 == count2) {
          return true;
        }
      }
      return false;
    }


    else if (o instanceof DenseMatrix) {
      DenseMatrix M2 = (DenseMatrix) o;
      if (height != M2.height || width != M2.width) {
        return false;
      }
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
      if (count1 == count2) {
        return true;
      }
      return false;
    }
    return false;
  }
}
