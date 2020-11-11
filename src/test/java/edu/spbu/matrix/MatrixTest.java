package edu.spbu.matrix;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatrixTest
{

  @Test
  public void mulSD() {
    Matrix m1 = new SparseMatrix("sm1.txt");
    Matrix m2 = new DenseMatrix("sm2.txt");
    Matrix expected = new DenseMatrix("resultS.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulDS() {
    Matrix m1 = new DenseMatrix("sm1.txt");
    Matrix m2 = new SparseMatrix("sm2.txt");
    Matrix expected = new SparseMatrix("resultS.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulSS() {
    Matrix m1 = new SparseMatrix("sm1.txt");
    Matrix m2 = new SparseMatrix("sm2.txt");
    Matrix expected = new SparseMatrix("resultS.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulDD() {
    Matrix m1 = new DenseMatrix("m1.txt");
    Matrix m2 = new DenseMatrix("m2.txt");
    Matrix expected = new DenseMatrix("result.txt");
    assertEquals(expected, m1.mul(m2));
  }
}
