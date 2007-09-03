//@license@
package mx.unam.ecologia.gye.util;

/**
 * Copyright 2005 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.HashMap;

/**
 * A Fibonacci Heap, as described in <i>Introduction to Algorithms</i> by
 * Charles E. Leiserson, Thomas H. Cormen, Ronald L. Rivest.
 * <p/>
 * <p/>
 * <p/>
 * A Fibonacci heap is a very efficient data structure for priority
 * queuing.
 */
public class FibonacciHeap<T1, T2 extends Comparable<T2>> {

  private FibonacciHeapNode<T1, T2> m_MinNode;
  private HashMap<T1, FibonacciHeapNode<T1, T2>> m_ItemsToNodes;


  /**
   * Creates a new <code>FibonacciHeap</code>.
   */
  public FibonacciHeap() {
    this.m_MinNode = null;
    this.m_ItemsToNodes = new HashMap<T1, FibonacciHeapNode<T1, T2>>();
  }//constructor

  /**
   * Adds the Object <code>item</code>, with the supplied
   * <code>priority</code>.
   *
   * @param item     the item to be added.
   * @param priority the priority of the item.
   */
  public void add(T1 item, T2 priority) {
    if (m_ItemsToNodes.containsKey(item))
      throw new IllegalStateException("heap already contains item! (item= "
          + item + ")");
    FibonacciHeapNode<T1, T2> newNode = new FibonacciHeapNode<T1, T2>(item, priority);
    m_ItemsToNodes.put(item, newNode);

    if (m_MinNode == null) {
      m_MinNode = newNode;
    } else {
      concatenateSiblings(newNode, m_MinNode);
      if (newNode.compareTo(m_MinNode) < 0) {
        m_MinNode = newNode;
      }
    }
  }

  /**
   * Returns the priority associated with an item.
   *
   * @param item the item.
   * @return the priority associated with the item.
   */
  public T2 getPriority(T1 item) {
    return m_ItemsToNodes.get(item).m_Priority;
  }//getPriority

  /**
   * Returns <code>true</code> if <code>item</code> exists in this
   * <code>FibonacciHeap</code>, false otherwise.
   *
   * @param item the item to be checked.
   * @return true it item is contained, false otherwise.
   */
  public boolean contains(T1 item) {
    return m_ItemsToNodes.containsKey(item);
  }//contains


  /**
   * Returns the same Object that {@link #popMin()} would, without
   * removing it.
   *
   * @return the min item without removing it.
   */
  public T1 peekMin() {
    if (m_MinNode == null)
      return null;
    return m_MinNode.m_UserItem;
  }//peekMin

  /**
   * Returns the number of objects in the heap.
   *
   * @return the number of objects in the heap.
   */
  public int size() {
    return m_ItemsToNodes.size();
  }//size

  /**
   * Returns the object which has the <em>lowest</em> priority in the
   * heap.  If the heap is empty, <code>null</code> is returned.
   *
   * @return returns the min item removing it from the heap.
   */
  public T1 popMin() {
    if (m_MinNode == null)
      return null;
    if (m_MinNode.m_Child != null) {
      FibonacciHeapNode<T1, T2> tmp = m_MinNode.m_Child;
      // rempve m_Parent pointers to m_MinNode
      while (tmp.m_Parent != null) {
        tmp.m_Parent = null;
        tmp = tmp.m_NextSibling;
      }
      // add children of m_MinNode to root list
      concatenateSiblings(tmp, m_MinNode);
    }
    // remove m_MinNode from root list
    FibonacciHeapNode<T1, T2> oldMin = m_MinNode;
    if (m_MinNode.m_NextSibling == m_MinNode) {
      m_MinNode = null;
    } else {
      m_MinNode = m_MinNode.m_NextSibling;
      removeFromSiblings(oldMin);
      consolidate();
    }
    m_ItemsToNodes.remove(oldMin.m_UserItem);
    return oldMin.m_UserItem;
  }//popMin

  /**
   * Decreases the <code>priority</code> value associated with
   * <code>item</code>.
   * <p/>
   * <p/>
   * <p/>
   * <code>item<code> must exist in the heap, and it's current
   * priority must be greater than <code>priority</code>.
   *
   * @param item     the item.
   * @param priority the new priority
   * @throws IllegalStateException if <code>item</code> does not exist
   *                               in the heap, or if <code>item</code> already has an equal or
   *                               lower priority than the supplied<code>priority</code>.
   */
  public void decreaseKey(T1 item, T2 priority) {
    FibonacciHeapNode<T1, T2> node = m_ItemsToNodes.get(item);
    if (node == null)
      throw new IllegalStateException("No such element: " + item);
    if (node.compareTo(priority) < 0)
      throw new IllegalStateException("decreaseKey(" + item + ", "
          + priority + ") called, but priority="
          + node.m_Priority);
    node.m_Priority = priority;
    FibonacciHeapNode<T1, T2> parent = node.m_Parent;
    if ((parent != null) && (node.compareTo(parent) < 0)) {
      cut(node, parent);
      cascadingCut(parent);
    }
    if (node.compareTo(m_MinNode) < 0) {
      m_MinNode = node;
    }
  }//decreaseKey


  // makes x's nextSibling and m_PrevSibling point to itself
  private void removeFromSiblings(FibonacciHeapNode x) {
    if (x.m_NextSibling == x)
      return;
    x.m_NextSibling.m_PrevSibling = x.m_PrevSibling;
    x.m_PrevSibling.m_NextSibling = x.m_NextSibling;
    x.m_NextSibling = x;
    x.m_PrevSibling = x;
  }//removeFromSiblings

  // joins siblings lists of a and b
  private void concatenateSiblings(FibonacciHeapNode<T1, T2> a, FibonacciHeapNode<T1, T2> b) {
    a.m_NextSibling.m_PrevSibling = b;
    b.m_NextSibling.m_PrevSibling = a;
    FibonacciHeapNode<T1, T2> origAnext = a.m_NextSibling;
    a.m_NextSibling = b.m_NextSibling;
    b.m_NextSibling = origAnext;
  }//concatenateSiblings


  // consolidates heaps of same degree
  private void consolidate() {
    int size = size();
    FibonacciHeapNode<T1, T2>[] newRoots = new FibonacciHeapNode[size];

    FibonacciHeapNode<T1, T2> node = m_MinNode;
    FibonacciHeapNode<T1, T2> start = m_MinNode;
    do {
      FibonacciHeapNode<T1, T2> x = node;
      int currDegree = node.degree;
      while (newRoots[currDegree] != null) {
        FibonacciHeapNode<T1, T2> y = newRoots[currDegree];
        if (x.compareTo(y) > 0) {
          //swap
          FibonacciHeapNode<T1, T2> tmp = x;
          x = y;
          y = tmp;
        }
        if (y == start) {
          start = start.m_NextSibling;
        }
        if (y == node) {
          node = node.m_PrevSibling;
        }
        link(y, x);
        newRoots[currDegree++] = null;
      }
      newRoots[currDegree] = x;
      node = node.m_NextSibling;
    } while (node != start);

    m_MinNode = null;
    for (int i = 0; i < newRoots.length; i++)
      if (newRoots[i] != null) {
        if ((m_MinNode == null)
            || newRoots[i].compareTo(m_MinNode) < 0) {
          m_MinNode = newRoots[i];
        }
      }
  }//consolidate

  // links y under x
  private void link(FibonacciHeapNode<T1, T2> y, FibonacciHeapNode<T1, T2> x) {
    removeFromSiblings(y);
    y.m_Parent = x;
    if (x.m_Child == null)
      x.m_Child = y;
    else
      concatenateSiblings(x.m_Child, y);
    x.degree++;
    y.mark = false;
  }//link


  // cut node x from below y
  private void cut(FibonacciHeapNode<T1, T2> x, FibonacciHeapNode<T1, T2> y) {
    // remove x from y's children
    if (y.m_Child == x)
      y.m_Child = x.m_NextSibling;
    if (y.m_Child == x)
      y.m_Child = null;

    y.degree--;
    removeFromSiblings(x);
    concatenateSiblings(x, m_MinNode);
    x.m_Parent = null;
    x.mark = false;

  }//cut

  private void cascadingCut(FibonacciHeapNode<T1, T2> y) {
    FibonacciHeapNode<T1, T2> z = y.m_Parent;
    if (z != null) {
      if (!y.mark) {
        y.mark = true;
      } else {
        cut(y, z);
        cascadingCut(z);
      }
    }
  }//cascadingCut


  // private node class
  private static class FibonacciHeapNode<T1, T2 extends Comparable<T2>>
      implements Comparable<FibonacciHeapNode<T1, T2>> {

    private T1 m_UserItem;
    private T2 m_Priority;

    private FibonacciHeapNode<T1, T2> m_Parent;
    private FibonacciHeapNode<T1, T2> m_PrevSibling;
    private FibonacciHeapNode<T1, T2> m_NextSibling;
    private FibonacciHeapNode<T1, T2> m_Child;
    private int degree;
    private boolean mark;

    FibonacciHeapNode(T1 userObject, T2 priority) {
      this.m_UserItem = userObject;
      this.m_Priority = priority;

      this.m_Parent = null;
      this.m_PrevSibling = this;
      this.m_NextSibling = this;
      this.m_Child = null;
      this.degree = 0;
      this.mark = false;
    }//constructor

    public String toString() {
      return "[" + m_UserItem + ", " + degree + "]";
    }

    public int compareTo(FibonacciHeapNode<T1, T2> other) {
      return this.m_Priority.compareTo(other.m_Priority);
    }//compareTo

    public int compareTo(T2 other) {
      return this.m_Priority.compareTo(other);
    }//compareTo

  }//inner class FibonacciHeapNode

}//class FibonacciHeap