//@license@
package mx.unam.ecologia.gye.util;

import mx.unam.ecologia.gye.model.CompoundSequence;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Implements an ordered haplotype set.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class HaplotypeSet {

  private Comparator<CompoundSequence> m_Comparator;
  private ArrayList<CompoundSequence> m_List;

  public HaplotypeSet(Comparator<CompoundSequence> comp) {
    m_Comparator = comp;
    m_List = new ArrayList<CompoundSequence>();
  }//HaplotypeSet

  public int add(CompoundSequence cs) {
    int i = contains(cs);
    if (i != -1) {
      return i;
    } else {
      m_List.add(cs);
      return m_List.size();
    }
  }//add

  public Iterator iterator() {
    return m_List.listIterator();
  }//iterator

  public int contains(CompoundSequence cs) {
    int i = 1;
    for (Iterator<CompoundSequence> iterator = m_List.listIterator(); iterator.hasNext(); i++) {
      CompoundSequence compoundSequence = iterator.next();
      if (m_Comparator.compare(compoundSequence, cs) == 0) {
        return i;
      }
    }
    return -1;
  }//contains

  public String toString() {
    StringBuilder sbuf = new StringBuilder();
    Iterator<CompoundSequence> csiter = m_List.listIterator();
    while (csiter.hasNext()) {
      CompoundSequence cs = csiter.next();
      sbuf.append(cs.toString());
      sbuf.append("\n");
    }
    return sbuf.toString();
  }//toString

  public void clear() {
    m_List.clear();
  }//clear

}//class HaplotypeSet
