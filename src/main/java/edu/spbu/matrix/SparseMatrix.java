package edu.spbu.matrix;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.io.IOException;

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
  //  ToString(res, height, SM.width);
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

  class myThread implements Runnable {
    Thread t;
    SparseMatrix S;
    Point[] S1;
    HashMap<Point, Double> res;
    int start, end;

    myThread(SparseMatrix S, Point[] S1, HashMap<Point, Double> res, int start, int end) {
      this.S = S;
      this.S1 = S1;
      this.res = res;
      this.start = start;
      this.end = end;
      t = new Thread(this);
      t.start();
    }

    @Override
    public void run() {
      for (int k = start; k < end; k++) {
        Point key = S1[k];
        for (int i = 0; i < height; i++) {
          Point a = new Point(key.y, i);
          if (S.M.containsKey(a)) {
            Point b = new Point(key.x, i);
            addElement(key, a, b, S, res);
            }
          }
        }
      }
    }


  synchronized public void addElement (Point key, Point a, Point b, SparseMatrix S, HashMap<Point, Double> res) {
    if (res.containsKey(b)) {
      res.put(b, res.get(b) + M.get(key) * S.M.get(a));
    } else {
      res.put(b, M.get(key) * S.M.get(a));
    }
  }



  @Override
  public Matrix dmul(Matrix o) throws Exception {
    if (o instanceof SparseMatrix) {
      SparseMatrix S = (SparseMatrix) o;
      if (width != S.height) throw new Exception("The number of columns of the 1st matrix must " +
              "be the same as the number of rows of the 2d matrix");
      HashMap<Point, Double> res = new HashMap<>();
      int size = M.size();
      int proc = 4;
      if (size < proc) {
        proc = size;
      }
      int step = size / proc;
      int accuracy = size % proc;
      int i = 0, start = 0, end = step;
      myThread[] t = new myThread[proc];
      Point[] S1 = new Point[size];

      for (Point key: M.keySet()) {
        S1[i] = key;
        i++;
      }

      for (i = 0; i < proc - 1; i++) {
        t[i] = new myThread(S, S1, res, start, end);
        start += step;
        end += step;
      }
      t[proc - 1] = new myThread(S, S1, res, start, end + accuracy);

      try {
        for (i = 0; i < 4; i++) {
          t[i].t.join();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
     // ToString(res, height, S.width);
      return new SparseMatrix(res, height, S.width);
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof SparseMatrix) {
      SparseMatrix M2 = (SparseMatrix) o;
      if (height == M2.height || width == M2.width) {
        int count = 0;
        if (M.size() == M2.M.size()) {
          for (Point key : M.keySet()) {
            if (M2.M.containsKey(key)) {
              double a = M.get(key);
              double b = M2.M.get(key);
              if (a != b) {
                return false;
              }
              else {
                count++;
              }
            }
          }
          return (count == M.size());
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
            if (M2.M[key.x][key.y] != M.get(key)) {
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
