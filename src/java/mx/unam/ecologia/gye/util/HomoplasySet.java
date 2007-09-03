//@license@
package mx.unam.ecologia.gye.util;

import cern.colt.list.IntArrayList;
import cern.colt.map.OpenIntObjectHashMap;
import mx.unam.ecologia.gye.model.CompoundSequence;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Implements a set that keeps track of different {@link HaplotypeFreqSet}
 * instances to calculate homoplasies.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class HomoplasySet {

  //Static means no use if not serial processing
  private static int c_MNumDecisions = 0;
  private static int c_MNumMostFrequent = 0;
  private static int c_LNumDecisions = 0;
  private static int c_LNumMostFrequent = 0;

  //Sets
  private HaplotypeFreqSet m_LocusSMMHaplotypes =
      new HaplotypeFreqSet(CSLocusLengthComparator.instance);

  private HaplotypeFreqSet m_MultilocusSMMHaplotypes =
      new HaplotypeFreqSet(CSMultilocusLengthComparator.instance);

  private HaplotypeFreqSet m_IAMHaplotypes =
      new HaplotypeFreqSet(CompoundSequenceIdentityComparator.instance);

  private double m_LocusHomoplasy = -1d;
  private double m_MultilocusHomoplasy = -1d;
  private double m_LLHomoplasy = -1d;

  public void add(CompoundSequence cs) {
    //add to all
    m_LocusSMMHaplotypes.add(cs);
    m_MultilocusSMMHaplotypes.add(cs);
    m_IAMHaplotypes.add(cs);
  }//add

  public double[] calculateCountedHomoplasy() {
    OpenIntObjectHashMap locus_map = new OpenIntObjectHashMap();
    OpenIntObjectHashMap multilocus_map = new OpenIntObjectHashMap();
    OpenIntObjectHashMap ll_map = new OpenIntObjectHashMap();
    double[] params = new double[2];
    //prepare data structure Map per comparator type, and with
    //ancestry sorted sets as entries.
    for (Iterator<CompoundSequence> iter = m_IAMHaplotypes.iterator(); iter.hasNext();) {
      CompoundSequence cs = iter.next();
      //Locus
      int locus_id = m_LocusSMMHaplotypes.contains(cs);
      Object o = locus_map.get(locus_id);
      if (o == null) {
        SortedSet<CompoundSequence> s = new TreeSet<CompoundSequence>(CSAncestryComparator.instance);
        s.add(cs);
        locus_map.put(locus_id, s);
      } else {
        ((SortedSet<CompoundSequence>) o).add(cs);
      }
      //multilocus
      int multilocus_id = m_MultilocusSMMHaplotypes.contains(cs);
      o = multilocus_map.get(multilocus_id);
      if (o == null) {
        SortedSet<CompoundSequence> s = new TreeSet<CompoundSequence>(CSAncestryComparator.instance);
        s.add(cs);
        multilocus_map.put(multilocus_id, s);
      } else {
        ((SortedSet<CompoundSequence>) o).add(cs);
      }
    }

    for (Iterator<CompoundSequence> iter = m_MultilocusSMMHaplotypes.iterator(); iter.hasNext();) {
      CompoundSequence cs = iter.next();
      //Locus
      int locus_id = m_LocusSMMHaplotypes.contains(cs);
      //System.out.println("Lid=" + locus_id + " :: " + cs.toString());
      Object o = ll_map.get(locus_id);
      if (o == null) {
        SortedSet<CompoundSequence> s = new TreeSet<CompoundSequence>(CSAncestryComparator.instance);
        s.add(cs);
        ll_map.put(locus_id, s);
      } else {
        ((SortedSet<CompoundSequence>) o).add(cs);
      }
    }

    int acount = m_IAMHaplotypes.getAlleleCount();

    //Locus analysis
    int sumhp = 0;
    //int selmfreq = 0;
    int maxfreq = 0;
    for (int id = 1; id <= locus_map.size(); id++) {
      //set with id
      SortedSet<CompoundSequence> s = (SortedSet<CompoundSequence>) locus_map.get(id);
      Iterator<CompoundSequence> iterator = s.iterator();
      maxfreq = 0;
      if (iterator.hasNext()) {
        CompoundSequence cs = iterator.next(); //skip first
        maxfreq = m_IAMHaplotypes.getFrequency(cs);
        if (iterator.hasNext()) {
          c_LNumDecisions++;
        } else {
          //no decision
          maxfreq = 0;
        }
        while (iterator.hasNext()) {
          cs = iterator.next();
          int freq = m_IAMHaplotypes.getFrequency(cs);
          sumhp += freq;
          if (freq > maxfreq) {
            maxfreq = 0;
          }
        }
      }
      if (maxfreq > 0) {
        c_LNumMostFrequent++;
      }
    }
    m_LocusHomoplasy = (double) sumhp / (double) acount;

    //MultiLocus analysis
    sumhp = 0;
    //selmfreq = 0;
    for (int id = 1; id <= multilocus_map.size(); id++) {
      //set with id
      SortedSet<CompoundSequence> s = (SortedSet<CompoundSequence>) multilocus_map.get(id);
      Iterator<CompoundSequence> iterator = s.iterator();
      if (iterator.hasNext()) {
        CompoundSequence cs = iterator.next(); //skip first
        maxfreq = m_IAMHaplotypes.getFrequency(cs);
        if (iterator.hasNext()) {
          c_MNumDecisions++;
        } else {
          //no decision
          maxfreq = 0;
        }
        while (iterator.hasNext()) {
          cs = iterator.next();
          int freq = m_IAMHaplotypes.getFrequency(cs);
          sumhp += freq;
          if (freq > maxfreq) {
            maxfreq = 0;
          }
        }
      }
      if (maxfreq > 0) {
        c_MNumMostFrequent++;
      }
    }
    m_MultilocusHomoplasy = (double) sumhp / (double) acount;

    //LL analysis
    acount = m_MultilocusSMMHaplotypes.getAlleleCount();
    sumhp = 0;
    for (int id = 1; id <= ll_map.size(); id++) {
      //set with id
      SortedSet<CompoundSequence> s = (SortedSet<CompoundSequence>) ll_map.get(id);
      Iterator<CompoundSequence> iterator = s.iterator();
      if (iterator.hasNext()) {
        iterator.next(); //skip first
        while (iterator.hasNext()) {
          CompoundSequence cs = iterator.next();
          sumhp += m_MultilocusSMMHaplotypes.getFrequency(cs);
        }
      }
    }
    m_LLHomoplasy = (double) sumhp / (double) acount;
    return params;
  }//calculateCountedHomoplasy

  public double[] calculateHomocygosisHomoplasy() {
    double[] params = new double[9];
    //Homoplasy Stats
    int acount = m_IAMHaplotypes.getAlleleCount();
    double ncorr = (double) acount / (double) (acount - 1);

    //IAM
    double heiamsum = 0;
    for (Iterator<CompoundSequence> iterator = m_IAMHaplotypes.iterator(); iterator.hasNext();) {
      CompoundSequence cs = iterator.next();
      heiamsum += Math.pow((double) m_IAMHaplotypes.getFrequency(cs) / (double) acount, 2);
    }
    double corriamsum = ncorr * (1 - heiamsum);
    params[0] = m_IAMHaplotypes.getHaplotypeCount(); //N_H,IAM
    params[1] = (double) 1 / heiamsum; //Ne,IAM
    params[2] = corriamsum; //He,IAM

    //SMM Locus
    double smmsum1 = 0;
    for (Iterator<CompoundSequence> iterator = m_LocusSMMHaplotypes.iterator(); iterator.hasNext();) {
      CompoundSequence cs = iterator.next();
      smmsum1 += Math.pow((double) m_LocusSMMHaplotypes.getFrequency(cs) / (double) acount, 2);
    }
    double corrsmmsum1 = ncorr * (1 - smmsum1);
    params[3] = m_LocusSMMHaplotypes.getHaplotypeCount();
    params[4] = (double) 1 / smmsum1;
    params[5] = corrsmmsum1; //He,SMM (Locus)
    if (corrsmmsum1 == 1) {
      m_LocusHomoplasy = 0;
    } else {
      m_LocusHomoplasy = (double) 1 - (1 - corriamsum) / (1 - corrsmmsum1);
    }

    //SMM Multilocus
    double smmsum2 = 0;
    for (Iterator<CompoundSequence> iterator = m_MultilocusSMMHaplotypes.iterator(); iterator.hasNext();) {
      CompoundSequence cs = iterator.next();
      smmsum2 += Math.pow((double) m_MultilocusSMMHaplotypes.getFrequency(cs) / (double) acount, 2);
    }
    double corrsmmsum2 = ncorr * (1 - smmsum2);
    params[6] = m_MultilocusSMMHaplotypes.getHaplotypeCount();
    params[7] = (double) 1 / smmsum2;
    params[8] = corrsmmsum2; //He,SMM (Multilocus)
    if (corrsmmsum2 == 1) {
      m_MultilocusHomoplasy = 0;
    } else {
      m_MultilocusHomoplasy = (double) 1 - (1 - corriamsum) / (1 - corrsmmsum2);
    }
    if (corrsmmsum1 == 1) {
      m_LLHomoplasy = 0;
    } else {
      m_LLHomoplasy = (double) 1 - (1 - corrsmmsum2) / (1 - corrsmmsum1);
    }

    return params;
  }//calculateHomocygosisHomoplasy


  public double[] calculatePIHomoplasy() {
    double[] params = new double[3];
    //Homoplasy Stats
    //iam
    double iampi = m_IAMHaplotypes.calculatePIIdentity();
    //locus
    double smm1pi = m_LocusSMMHaplotypes.calculatePILocusLength();
    //multilocus
    double smm2pi = m_MultilocusSMMHaplotypes.calculatePIMultilocusLength();

    if (iampi == 0) {
      m_LocusHomoplasy = 0;
      m_MultilocusHomoplasy = 0;
    } else {
      m_LocusHomoplasy = (double) 1 - ((double) smm1pi / (double) iampi);
      m_MultilocusHomoplasy = (double) 1 - ((double) smm2pi / (double) iampi);
    }

    params[0] = iampi;//Pi iam
    params[1] = smm1pi;//Pi locus
    params[2] = smm2pi;//Pi multilocus
    return params;
  }//calculatePIHomoplasy

  public double getLocusHomoplasy() {
    return m_LocusHomoplasy;
  }//getLocusHomoplasy

  public double getMultilocusHomoplasy() {
    return m_MultilocusHomoplasy;
  }//getMultiLocusHomoplasy

  public double getLLHomoplasy() {
    return m_LLHomoplasy;
  }//getLLHomoplasy

  public String toString() {
    StringBuilder sbuf = new StringBuilder();
    sbuf.append(m_LocusSMMHaplotypes.toString());
    sbuf.append(m_MultilocusSMMHaplotypes.toString());
    sbuf.append(m_IAMHaplotypes.toString());
    return sbuf.toString();
  }//toString

  public String toString(OpenIntObjectHashMap map) {
    StringBuilder sbuf = new StringBuilder();
    IntArrayList keys = map.keys();
    keys.sort();
    for (int i = 0; i < keys.size(); i++) {
      sbuf.append(keys.getQuick(i));
      sbuf.append(" -> ");
      sbuf.append(toString((SortedSet<CompoundSequence>) map.get(keys.getQuick(i))));
      sbuf.append("\n");
    }

    return sbuf.toString();
  }//toString

  public String toString(SortedSet<CompoundSequence> seqs) {
    StringBuilder sbuf = new StringBuilder();
    sbuf.append("[");
    boolean first = true;
    for (CompoundSequence cs : seqs) {
      if (first) {
        first = false;
      } else {
        sbuf.append(",");
      }
      sbuf.append(m_IAMHaplotypes.getFrequency(cs));
      sbuf.append(":");
      sbuf.append(cs.toStoreString());
    }

    sbuf.append("]");
    return sbuf.toString();
  }//toString

  public static double getLFM() {
    return (double) c_LNumMostFrequent / (double) c_LNumDecisions;
  }//getLFM

  public static double getMFM() {
    return (double) c_MNumMostFrequent / (double) c_MNumDecisions;
  }//getMFM

  public static void resetMF() {
    c_LNumMostFrequent = 0;
    c_LNumDecisions = 0;
    c_MNumMostFrequent = 0;
    c_MNumDecisions = 0;
  }//resetMF

}//class HomoplasySet
