//@license@
package mx.unam.ecologia.gye.util;

import java.util.Arrays;

public class DoubleMaxHeap {

  private double[] m_Heap;
  private int m_Size;

  public DoubleMaxHeap(double[] in) {
    m_Size = in.length;
    m_Heap = in;
    build();
  }//DoubleMaxHeap

  public DoubleMaxHeap(int size) {
    m_Size = 0;
    m_Heap = new double[size];
  }//DoubleMaxHeap

  public void add(double i) {
    m_Heap[m_Size++] = i;
  }//add

  public void build() {
    for (int i = (int) Math.floor((double) (m_Heap.length / 2)); i >= 0; i--) {
      heapify(i);
    }
  }//build

  private void heapify(int i) {
    //System.out.println("Heapifying node ["+i +"]=" + m_Heap[i]);
    int l = left(i);
    int r = right(i);
    int largest = 0;
    if (l < m_Size && m_Heap[l] > m_Heap[i]) {
      largest = l;
    } else {
      largest = i;
    }
    if (r < m_Size && m_Heap[r] > m_Heap[largest]) {
      largest = r;
    }
    if (largest != i) {
      double swap = m_Heap[i];
      m_Heap[i] = m_Heap[largest];
      m_Heap[largest] = swap;
      //System.out.println("swapped "+ swap + "<->"+ m_Heap[i]);
      heapify(largest);
    }
  }//heapify

  public double getMaximum() {
    return m_Heap[0];
  }//getMaximum

  public int size() {
    return m_Size;
  }//size

  public double extractMax() {
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
    DoubleMaxHeap heap = new DoubleMaxHeap(in);
    //System.out.println(heap.toString());
    for (int i = in.length - 1; i >= 0; i--) {
      in[i] = heap.extractMax();
      //System.out.println(heap.toString());
    }
  }//sort

  public static void main(String[] args) {
    //DoubleMaxHeap mh = new DoubleMaxHeap(new int[] {9,2,3,5,1});
    //System.out.println(mh.toString());
    //while(mh.size() > 0) {
    //  System.out.println(mh.extractMin());
    //}
    DoubleMaxHeap mh = new DoubleMaxHeap(5);
    mh.add(9);
    mh.add(2);
    mh.add(3);
    mh.add(5);
    mh.add(1);
    mh.build();
    System.out.println(mh.toString());
    while (mh.size() > 0) {
      System.out.println(mh.extractMax());
    }

  }


}//inner class DoubleMaxHeap