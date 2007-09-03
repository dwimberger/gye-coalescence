//@license@
package mx.unam.ecologia.gye.util;

import cern.colt.list.LongArrayList;
import cern.jet.math.IntFunctions;
import mx.unam.ecologia.gye.model.Sequence;

import java.util.Comparator;

/**
 * Implements a <tt>Comparator</tt> that will compare sequences
 * by length and identity, latter of which is defined by uniquely
 * identifiable mutations of the sequence (aka the sequences history).
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class SequenceIdentityComparator
    implements Comparator<Sequence> {

  public static SequenceIdentityComparator instance = new SequenceIdentityComparator();

  public int compare(Sequence s1, Sequence s2) {
    if (s1 == null || s2 == null) {
      throw new IllegalArgumentException("Null Argument not supported.");
    }

    int szc = IntFunctions.compare.apply(s1.getSize(), s2.getSize());
    if (szc == 0) {
      //compare content
      LongArrayList l1 = s1.getChangeTrace();
      LongArrayList l2 = s2.getChangeTrace();

      //compare trace sizes
      szc = IntFunctions.compare.apply(l1.size(), l2.size());
      if (szc != 0) {
        return szc;
      }
      for (int i = 0; i < l1.size(); i++) {
        szc = ComparatorUtil.compare(l1.getQuick(i), l2.getQuick(i));
        if (szc != 0) {
          return szc;
        }
      }
      return 0;
    } else {
      return szc;
    }
  }//compare

}//class SequenceAncestryComparator
