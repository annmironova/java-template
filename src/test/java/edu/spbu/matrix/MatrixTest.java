package edu.spbu.matrix;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatrixTest
{

  @Test
  public void mulSD() {
    Matrix m1 = new SparseMatrix("m1.txt");
    Matrix m2 = new DenseMatrix("m2.txt");
    Matrix expected = new DenseMatrix("result.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulDS() {
    Matrix m1 = new DenseMatrix("m1.txt");
    Matrix m2 = new SparseMatrix("m2.txt");
    Matrix expected = new SparseMatrix("result.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulSS() {
    Matrix m1 = new SparseMatrix("m1.txt");
    Matrix m2 = new SparseMatrix("m2.txt");
    Matrix expected = new SparseMatrix("result.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulDD() {
    Matrix m1 = new DenseMatrix("m1.txt");
    Matrix m2 = new DenseMatrix("m2.txt");
    Matrix expected = new DenseMatrix("result.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void dmulDD() throws Exception {
    Matrix m1 = new DenseMatrix("m1.txt");
    Matrix m2 = new DenseMatrix("m2.txt");
    Matrix expected = new DenseMatrix("result.txt");
    assertEquals(expected, m1.dmul(m2));
  }

  @Test
  public void dmulSS() throws Exception {
    Matrix m1 = new SparseMatrix("m1.txt");
    Matrix m2 = new SparseMatrix("m2.txt");
    Matrix expected = new SparseMatrix("result.txt");
    assertEquals(expected, m1.dmul(m2));
  }
}
