//@license@
package mx.unam.ecologia.gye.util;

import mx.unam.ecologia.gye.model.CompoundSequence;
import mx.unam.ecologia.gye.model.Sequence;

import java.util.Comparator;

/**
 * Compares {@link CompoundSequence} instances by comparing the
 * sizes of the sequences it is composed of using {@link SequenceLengthComparator}.
 * <p/>
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class CSMultilocusLengthComparator
    implements Comparator<CompoundSequence> {

  public static final CSMultilocusLengthComparator instance =
      new CSMultilocusLengthComparator();

  private static final SequenceLengthComparator SEQLEN_COMPARATOR =
      SequenceLengthComparator.instance;

  public int compare(CompoundSequence cs1, CompoundSequence cs2) {
    if (cs1 == null || cs2 == null) {
      throw new IllegalArgumentException("Null Argument not supported.");
    }
    for (int i = 0; i < cs1.getSequenceCount(); i++) {
      Sequence s1 = cs1.get(i);
      Sequence s2 = cs2.get(i);
      int szc = SEQLEN_COMPARATOR.compare(s1, s2);
      if (szc != 0) {
        return szc;
      }
    }
    return 0;
  }//compare


}//class CompoundSequenceLengthComparator
