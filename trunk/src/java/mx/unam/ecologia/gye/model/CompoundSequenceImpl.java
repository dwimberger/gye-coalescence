//@license@
package mx.unam.ecologia.gye.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * An implementation of {@link CompoundSequence}.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class CompoundSequenceImpl
    implements CompoundSequence {

  protected List<Sequence> m_Sequences;
  protected boolean m_TracingChanges = true;
  protected boolean m_Locus = false;

  public CompoundSequenceImpl(boolean b) {
    m_Sequences = new ArrayList<Sequence>();
    m_TracingChanges = b;
  }//constructor

  public CompoundSequenceImpl() {
    m_Sequences = new ArrayList<Sequence>();
  }//constructor

  public boolean isTracingChanges() {
    return m_TracingChanges;
  }//isTracingChanges

  public void setTracingChanges(boolean trackChanges) {
    m_TracingChanges = trackChanges;
    for (Iterator iterator = m_Sequences.iterator(); iterator.hasNext();) {
      ((Sequence) iterator.next()).setTracingChanges(trackChanges);
    }
  }//setTracingChanges

  public boolean isLocus() {
    return m_Locus;
  }//isLocus

  public void setLocus(boolean locus) {
    m_Locus = locus;
  }//setLocus

  public void add(Sequence s) {
    s.setTracingChanges(m_TracingChanges);
    m_Sequences.add(s);
  }//add

  public void remove(Sequence s) {
    m_Sequences.remove(s);
  }//remove

  public Sequence get(int i) {
    return (Sequence) m_Sequences.get(i);
  }//get

  public ListIterator iterator() {
    return m_Sequences.listIterator();
  }//getRepeats

  public CompoundSequence getCopy() {
    CompoundSequenceImpl cs = new CompoundSequenceImpl();
    for (Iterator iterator = m_Sequences.iterator(); iterator.hasNext();) {
      cs.add((Sequence) ((Sequence) iterator.next()).getCopy());
    }
    cs.m_TracingChanges = this.m_TracingChanges;
    cs.m_Locus = this.m_Locus;
    return cs;
  }//getCopy

  public int getSequenceCount() {
    return m_Sequences.size();
  }//getSequenceCount

  //REAL SIZE
  public int getSize() {
    int size = 0;
    for (Iterator iterator = m_Sequences.iterator(); iterator.hasNext();) {
      Sequence s = (Sequence) iterator.next();
      size += s.getSize();
    }
    return size;
  }//getSize

  //size in entities
  public int size() {
    int size = 0;
    for (Iterator iterator = m_Sequences.iterator(); iterator.hasNext();) {
      Sequence s = (Sequence) iterator.next();
      size += s.size();
    }
    return size;
  }//size

  public String toString() {
    final StringBuilder sbuf = new StringBuilder();
    for (Iterator iterator = m_Sequences.iterator(); iterator.hasNext();) {
      Sequence s = (Sequence) iterator.next();
      sbuf.append(s.toString());
      if (iterator.hasNext()) {
        sbuf.append("-");
      }
    }
    return sbuf.toString();
  }//toString

  public String toFullString() {
    StringBuilder sbuf = new StringBuilder();
    for (Iterator iterator = m_Sequences.iterator(); iterator.hasNext();) {
      Sequence s = (Sequence) iterator.next();
      sbuf.append(s.toString());
      sbuf.append(s.toTraceString());
      if (iterator.hasNext()) {
        sbuf.append("-");
      }
    }
    return sbuf.toString();
  }//toFullString

  public String toShortString() {
    StringBuilder sbuf = new StringBuilder();
    for (Iterator iterator = m_Sequences.iterator(); iterator.hasNext();) {
      Sequence s = (Sequence) iterator.next();
      sbuf.append(s.toShortString());
      if (iterator.hasNext()) {
        sbuf.append("-");
      }
    }
    return sbuf.toString();
  }//toShortString

  public String toLengthString() {
    StringBuilder sbuf = new StringBuilder();
    int tsize = 0;
    for (Iterator iterator = m_Sequences.iterator(); iterator.hasNext();) {
      Sequence s = (Sequence) iterator.next();
      tsize += s.getSize();
      sbuf.append(s.getSize());
      if (iterator.hasNext()) {
        sbuf.append("-");
      } else {
        sbuf.append(" [" + tsize + "]");
      }
    }
    return sbuf.toString();
  }//toLengthString

  public String toStoreString() {
    StringBuilder sbuf = new StringBuilder();
    sbuf.append('$');
    for (Iterator iterator = m_Sequences.iterator(); iterator.hasNext();) {
      Sequence s = (Sequence) iterator.next();
      sbuf.append(s.toStoreString());
      if (iterator.hasNext()) {
        sbuf.append("-");
      }
    }
    return sbuf.toString();
  }//toStoreString


}//class CompoundSequenceImpl
