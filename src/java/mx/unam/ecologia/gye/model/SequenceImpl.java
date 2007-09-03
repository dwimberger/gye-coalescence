//@license@
package mx.unam.ecologia.gye.model;

import cern.colt.Arrays;
import cern.colt.list.LongArrayList;
import mx.unam.ecologia.gye.util.IdentityGenerator;

import java.util.ArrayList;

/**
 * Implementation of {@link Sequence}.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class SequenceImpl
    implements Sequence {

  private int m_UnitSize;
  private ArrayList<SequenceUnit> m_Sequence;
  private boolean m_TracingChanges = false;
  private LongArrayList m_ChangeTrace;
  private double m_LastMutation = Double.MAX_VALUE;

  private SequenceImpl(ArrayList<SequenceUnit> alist) {
    m_Sequence = alist;
  }//constructor

  public SequenceImpl(int unitsize) {
    m_UnitSize = unitsize;
    m_Sequence = new ArrayList<SequenceUnit>();
  }//constructor

  public int getUnitSize() {
    return m_UnitSize;
  }//getUnitSize

  public void setUnitSize(int unitSize) {
    m_UnitSize = unitSize;
  }//setUnitSize

  public boolean isTracingChanges() {
    return m_TracingChanges;
  }//isTracingChanges

  public void setTracingChanges(boolean trackChanges) {
    if (m_ChangeTrace == null) {
      m_ChangeTrace = new LongArrayList();
    }
    m_TracingChanges = trackChanges;
  }//setTracingChanges

  public LongArrayList getChangeTrace() {
    return m_ChangeTrace;
  }//getChangeTrace

  public void setChangeTrace(LongArrayList lal) {
    m_ChangeTrace = lal;
  }//setChangeTrace

  public SequenceUnit get(int pos) {
    return m_Sequence.get(pos);
  }//get

  public int size() {
    return m_Sequence.size();
  }//size

  public int getSize() {
    return m_Sequence.size() * m_UnitSize;
  }//getSize

  public void replace(int pos, SequenceUnit t) {
    m_Sequence.set(pos, t);
    traceChange();
  }//replace

  public void add(SequenceUnit t) {
    m_Sequence.add(t);
    traceChange();
  }//add

  public void add(int pos, SequenceUnit t) {
    m_Sequence.add(pos, t);
    traceChange();
  }//add

  public void remove(int pos) {
    m_Sequence.remove(pos);
    traceChange();
  }//remove

  public String toString() {
    final StringBuilder sbuf = new StringBuilder();
    int size = m_Sequence.size();
    for (int i = 0; i < size; i++) {
      sbuf.append(m_Sequence.get(i).toString());
    }
    return sbuf.toString();
  }//toString

  public final String toTraceString() {
    if (m_ChangeTrace == null) {
      return "";
    }
    m_ChangeTrace.trimToSize();
    return Arrays.toString(m_ChangeTrace.elements());
  }//toString

  public final String toFullString() {
    final StringBuilder sbuf = new StringBuilder();
    sbuf.append(toString());
    sbuf.append(toTraceString());
    //sbuf.append('|');
    //sbuf.append(m_LastMutation);
    return sbuf.toString();
  }//toFullString

  public String toStoreString() {
    final StringBuilder sbuf = new StringBuilder();

    int size = m_Sequence.size();
    sbuf.append(m_Sequence.get(0).toString());
    sbuf.append('(');
    sbuf.append(size);
    sbuf.append(')');
    m_ChangeTrace.trimToSize();
    long[] trace = m_ChangeTrace.elements();
    sbuf.append(';');
    if (trace.length > 0) {
      for (int i = 0; i < trace.length; i++) {
        sbuf.append(trace[i]);
        if (i < (trace.length - 1)) {
          sbuf.append(',');
        }
      }
    }
    if (m_LastMutation < Double.MAX_VALUE) {
      sbuf.append('|');
      sbuf.append(m_LastMutation);
    }
    return sbuf.toString();
  }//toStoreString

  public String toShortString() {
    final StringBuilder sbuf = new StringBuilder();

    int size = m_Sequence.size();
    sbuf.append(m_Sequence.get(0).toString());
    sbuf.append('(');
    sbuf.append(size);
    sbuf.append(')');
    return sbuf.toString();
  }//toStoreString

  public Sequence getCopy() {
    SequenceImpl s = new SequenceImpl((ArrayList<SequenceUnit>) m_Sequence.clone());
    s.m_UnitSize = this.m_UnitSize;
    s.m_ChangeTrace = this.m_ChangeTrace.copy();
    s.m_TracingChanges = this.m_TracingChanges;
    s.m_LastMutation = this.m_LastMutation;
    return s;
  }//getCopy

  public double getLastMutation() {
    return m_LastMutation;
  }//getLastMutation

  public void setLastMutation(double d) {
    m_LastMutation = d;
  }//setLastMutation

  private void traceChange() {
    if (m_TracingChanges) {
      m_ChangeTrace.add(IdentityGenerator.nextIdentity());
    }//traceChange
  }//traceChange

}//class SequenceImpl