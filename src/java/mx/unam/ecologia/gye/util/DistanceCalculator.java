//@license@
package mx.unam.ecologia.gye.util;

import cern.colt.list.LongArrayList;
import mx.unam.ecologia.gye.model.CompoundSequence;
import mx.unam.ecologia.gye.model.Sequence;


/**
 * Utility class that calculates a distance between
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class DistanceCalculator {

  public static int calculateLengthDistance(CompoundSequence cs1, CompoundSequence cs2) {
    if (cs1 == null || cs2 == null) {
      return 0;
    }
    if (cs1.isLocus() && cs2.isLocus()) {
      return Math.abs(cs1.size() - cs2.size());
    } else {
      int dist = 0;
      int len = cs1.getSequenceCount();
      for (int i = 0; i < len; i++) {
        dist += calculateLengthDistance(cs1.get(i), cs2.get(i));
      }
      return dist;
    }
  }//calculateLengthDistance

  public static int calculateLocusLengthDistance(CompoundSequence cs1, CompoundSequence cs2) {
    if (cs1 == null || cs2 == null) {
      return 0;
    }
    return Math.abs(cs1.size() - cs2.size());
  }//calculateLocusLengthDistance

  public static int calculateMultilocusLengthDistance(CompoundSequence cs1, CompoundSequence cs2) {
    if (cs1 == null || cs2 == null) {
      return 0;
    }
    int dist = 0;
    int len = cs1.getSequenceCount();
    for (int i = 0; i < len; i++) {
      dist += calculateLengthDistance(cs1.get(i), cs2.get(i));
    }
    return dist;
  }//calculateMultilocusLengthDistance


  public static double calculateMultilocusGoldsteinDistance(CompoundSequence cs1, CompoundSequence cs2) {

    int numr = cs1.getSequenceCount();
    //compare haplotype i with haplotype j
    double sum = 0;
    //Sum of absolutes
    for (int k = 0; k < numr; k++) {
      sum += Math.abs(cs1.get(k).getSize() - cs2.get(k).getSize());
    }
    //square
    sum = sum * sum;
    return sum / numr;
  }//calculateMultilocusGoldsteinDistance

  public static int calculateLengthDistance(Sequence s1, Sequence s2) {
    if (s1 == null || s2 == null) {
      return 0;
    }
    return Math.abs(s1.size() - s2.size());
  }//calculateLengthDistance

  public static int calculateIdentityDistance(CompoundSequence cs1, CompoundSequence cs2) {
    if (cs1 == null || cs2 == null) {
      return 0;
    }
    int dist = 0;
    int len = cs1.getSequenceCount();
    for (int i = 0; i < len; i++) {
      dist += calculateIdentityDistance(cs1.get(i), cs2.get(i));
    }
    return dist;
  }//calculateIdentityDistance

  public static int calculateIdentityDistance(Sequence s1, Sequence s2) {
    if (s1 == null || s2 == null) {
      return 0;
    }
    int dist = 0;
    LongArrayList ls1 = s1.getChangeTrace();
    LongArrayList ls2 = s2.getChangeTrace();
    final int ls1size = (ls1 == null) ? 0 : ls1.size();
    final int ls2size = (ls2 == null) ? 0 : ls2.size();
    for (int i = 0; i < ls1size; i++) {
      if (!ls2.contains(ls1.get(i))) {
        dist++;
      }
    }
    for (int i = 0; i < ls2size; i++) {
      if (!ls1.contains(ls2.get(i))) {
        dist++;
      }
    }
    return dist;
  }//calculateIdentityDistance


}//class DistanceCalculator
