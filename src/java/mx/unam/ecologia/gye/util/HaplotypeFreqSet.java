//@license@
package mx.unam.ecologia.gye.util;

import mx.unam.ecologia.gye.model.CompoundSequence;

import java.util.*;

/**
 * Implements an ordered haplotype set.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class HaplotypeFreqSet {

  private Comparator<CompoundSequence> m_Comparator;
  private ArrayList<CompoundSequence> m_List;
  private ArrayList<Integer> m_FrequencyList;

  public HaplotypeFreqSet(Comparator<CompoundSequence> comp) {
    m_Comparator = comp;
    m_List = new ArrayList<CompoundSequence>();
    m_FrequencyList = new ArrayList<Integer>();
  }//HaplotypeSet

  public CompoundSequence get(int i) {
    return m_List.get(i);
  }//get

  public int add(CompoundSequence cs) {
    int i = contains(cs) - 1;
    if (i >= 0) {
      //aument frequency
      m_FrequencyList.set(i, m_FrequencyList.get(i) + 1);
      return i;
    } else {
      m_List.add(cs);
      m_FrequencyList.add(1);
      return m_List.size();
    }
  }//add

  public void sort(Comparator<CompoundSequence> c) {
    //System.out.println(toFullString());
    ArrayList<CompoundSequence> nl = new ArrayList<CompoundSequence>(m_List);
    ArrayList<Integer> nfl = new ArrayList<Integer>(m_FrequencyList);
    //sort
    Collections.sort(nl, c);
    //sort frequencies
    for (CompoundSequence cs : nl) {
      int oldidx = m_List.indexOf(cs);
      nfl.set(nl.indexOf(cs), m_FrequencyList.get(oldidx));
    }
    m_List = nl;
    m_FrequencyList = nfl;
    //System.out.println(toFullString());
  }//sort

  /**
   * Sorts by frequency.
   */
  public HaplotypeFreqSet sort() {
    FibonacciHeap<CompoundSequence, Integer> fh = new FibonacciHeap<CompoundSequence, Integer>();
    ArrayList<CompoundSequence> nl = new ArrayList<CompoundSequence>(m_List);
    ArrayList<Integer> nfl = new ArrayList<Integer>(m_List.size());
    for (int i = 0; i < nl.size(); i++) {
      fh.add(nl.get(i), m_FrequencyList.get(i));
    }
    nl.clear();
    while (fh.size() > 0) {
      CompoundSequence cs = fh.peekMin();
      int freq = fh.getPriority(cs);
      nl.add(cs);
      nfl.add(freq);
      fh.popMin(); //pop off
    }
    Collections.reverse(nl);
    Collections.reverse(nfl);
    m_List = nl;
    m_FrequencyList = nfl;
    return this;
  }//sort

  public int findMostFrequent() {
    int idx = 0;
    int max = -1;
    int maxidx = 0;
    for (Iterator<Integer> iterator = m_FrequencyList.listIterator(); iterator.hasNext(); idx++) {
      int freq = iterator.next();
      if (max < freq) {
        max = freq;
        maxidx = idx;
      }
    }
    return maxidx;
  }//findMostFrequent

  public Iterator<CompoundSequence> iterator() {
    return m_List.listIterator();
  }//iterator

  public List<CompoundSequence> getHaplotypes() {
    return m_List;
  }//getHaplotypes

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

  public int getFrequency(CompoundSequence cs) {
    return m_FrequencyList.get(m_List.indexOf(cs));
  }//getFrequency

  public int getHaplotypeCount() {
    return m_List.size();
  }//getHaplotypeCount

  public int getAlleleCount() {
    int count = 0;
    for (int i : m_FrequencyList) {
      count += i;
    }
    return count;
  }//getAlleleCount

  public void clear() {
    m_List.clear();
  }//clear

  public double calculatePILocusLength() {
    int i = m_FrequencyList.size();
    double pid = 0;
    for (int n = 1; n < i - 1; n++) {
      for (int m = n + 1; m < i; m++) {
        int dist = DistanceCalculator.calculateLocusLengthDistance(
            (CompoundSequence) m_List.get(n), (CompoundSequence) m_List.get(m)
        );
        pid += m_FrequencyList.get(n) * m_FrequencyList.get(m) * (double) dist;
      }
    }
    return pid;
  }//calculatePILocusLength

  public double calculatePIMultilocusLength() {
    int i = m_FrequencyList.size();
    double pid = 0;
    for (int n = 1; n < i - 1; n++) {
      for (int m = n + 1; m < i; m++) {
        int dist = DistanceCalculator.calculateMultilocusLengthDistance(
            (CompoundSequence) m_List.get(n), (CompoundSequence) m_List.get(m)
        );
        pid += m_FrequencyList.get(n) * m_FrequencyList.get(m) * (double) dist;
      }
    }
    return pid;
  }//calculatePILocusLength

  public double calculatePIIdentity() {
    int i = m_FrequencyList.size();
    double pid = 0;
    for (int n = 0; n < i - 1; n++) {
      for (int m = n + 1; m < i; m++) {
        int dist = DistanceCalculator.calculateIdentityDistance(
            (CompoundSequence) m_List.get(n), (CompoundSequence) m_List.get(m)
        );
        pid += m_FrequencyList.get(n) * m_FrequencyList.get(m) * (double) dist;

      }
    }
    return pid;
  }//calculatePIIdentity

  public String toString() {
    StringBuilder sbuf = new StringBuilder();
    Iterator<CompoundSequence> csiter = m_List.listIterator();
    Iterator<Integer> freqiter = m_FrequencyList.listIterator();
    while (csiter.hasNext() && freqiter.hasNext()) {
      CompoundSequence cs = csiter.next();
      int freq = freqiter.next();
      sbuf.append(freq);
      sbuf.append(":");
      sbuf.append(cs.toString());
      sbuf.append("\n");
    }
    return sbuf.toString();
  }//toString

  public String toFullString() {
    StringBuilder sbuf = new StringBuilder();
    Iterator<CompoundSequence> csiter = m_List.listIterator();
    Iterator<Integer> freqiter = m_FrequencyList.listIterator();
    while (csiter.hasNext() && freqiter.hasNext()) {
      CompoundSequence cs = csiter.next();
      int freq = freqiter.next();
      sbuf.append(freq);
      sbuf.append(":");
      sbuf.append(cs.toStoreString());
      sbuf.append("\n");
    }
    return sbuf.toString();
  }//toFullString

  public String toResumeString() {
    StringBuilder sbuf = new StringBuilder();
    Iterator<CompoundSequence> csiter = m_List.listIterator();
    Iterator<Integer> freqiter = m_FrequencyList.listIterator();
    while (csiter.hasNext() && freqiter.hasNext()) {
      CompoundSequence cs = csiter.next();
      int freq = freqiter.next();
      sbuf.append(freq);
      sbuf.append(":");
      if (cs.isLocus()) {
        sbuf.append(cs.getSize());
      } else {
        sbuf.append(cs.toShortString());
      }
      sbuf.append("\n");
    }
    return sbuf.toString();
  }//toString

  public String toLengthResumeString() {
    StringBuilder sbuf = new StringBuilder();
    Iterator<CompoundSequence> csiter = m_List.listIterator();
    Iterator<Integer> freqiter = m_FrequencyList.listIterator();
    while (csiter.hasNext() && freqiter.hasNext()) {
      CompoundSequence cs = csiter.next();
      int freq = freqiter.next();
      sbuf.append(freq);
      sbuf.append(":");
      if (cs.isLocus() || m_Comparator instanceof CompoundSequenceTotalLengthComparator) {
        sbuf.append(cs.getSize());
      } else {
        sbuf.append(cs.toLengthString());
      }
      sbuf.append("\n");
    }
    return sbuf.toString();
  }//toString

}//class HaplotypeFreqSet
