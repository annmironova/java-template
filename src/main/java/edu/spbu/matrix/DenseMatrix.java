package edu.spbu.matrix;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix
{
  /**
   * загружает матрицу из файла
   * @param fileName
   */
  public DenseMatrix(String fileName) throws IOException {
    BufferedReader buf = new BufferedReader(new FileReader(fileName));
    ArrayList<String> rows = new ArrayList<>();


    while (buf.ready()) {
      rows.add(buf.readLine());
    }
    int height = rows.size();
    int weight = rows.get(0).split(" ").length;
    int[][] M = new int[height][weight];
    for (int i = 0; i < height; i++)
      for (int j = 0; j < weight; j++) {
        String[] row = rows.get(i).split(" ");
        M[i][j] = Integer.parseInt(row[j]);
      }
  }

  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   */
  @Override public Matrix mul(Matrix o)
  {
    return null;
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
