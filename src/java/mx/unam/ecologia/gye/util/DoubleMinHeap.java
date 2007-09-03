//@license@
package mx.unam.ecologia.gye.util;

import java.util.Arrays;

public class DoubleMinHeap {

  private double[] m_Heap;
  private int m_Size;

  public DoubleMinHeap(double[] in) {
    m_Size = in.length;
    m_Heap = in;
    build();
  }//DoubleMinHeap

  public DoubleMinHeap(int size) {
    m_Size = 0;
    m_Heap = new double[size];
  }//DoubleMinHeap

  public void add(double i) {
    m_Heap[m_Size++] = i;
  }//add

  public void build() {
    for (int i = (int) Math.floor((double) (m_Heap.length / 2)); i >= 0; i--) {
      heapify(i);
    }
  }//build

  private void heapify(int i) {
    int l = left(i);
    int r = right(i);
    int smallest = 0;
    if (l < m_Size && m_Heap[l] < m_Heap[i]) {
      smallest = l;
    } else {
      smallest = i;
    }
    if (r < m_Size && m_Heap[r] < m_Heap[smallest]) {
      smallest = r;
    }
    if (smallest != i) {
      double swap = m_Heap[i];
      m_Heap[i] = m_Heap[smallest];
      m_Heap[smallest] = swap;
      heapify(smallest);
    }
  }//heapify

  public double getMaximum() {
    return m_Heap[0];
  }//getMaximum

  public int size() {
    return m_Size;
  }//size

  public double extractMin() {
    if (m_Heap.length < 1) {
      throw new RuntimeException("Heap underflow");
    }
    double max = m_Heap[0];
    m_Heap[0] = m_Heap[m_Size - 1];
    m_Size--;
    heapify(0);
    return max;
  }//extractMin

  private int parent(int i) {
    return (int) Math.floor((double) (i / 2));
  }//parent

  private int left(int i) {
    return 2 * i;
  }//left

  private int right(int i) {
    return 2 * i + 1;
  }//right

  public String toString() {
    return Arrays.toString(m_Heap);
  }//toString

  public static final void sort(double[] in) {
    DoubleMinHeap heap = new DoubleMinHeap(in);
    for (int i = in.length - 1; i >= 0; i--) {
      in[i] = heap.extractMin();
    }
  }//sort


}//class DoubleMaxHeap
