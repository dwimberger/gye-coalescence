//@license@
package mx.unam.ecologia.gye.util;

import mx.unam.ecologia.gye.model.CompoundSequence;

import java.util.Comparator;

/**
 * Compares {@link CompoundSequence} instances by comparing the
 * complete size of the sequence {@link SequenceLengthComparator}.
 * <p/>
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class CSLocusLengthComparator
    implements Comparator<CompoundSequence> {

  public static final CSLocusLengthComparator instance
      = new CSLocusLengthComparator();

  public int compare(CompoundSequence cs1, CompoundSequence cs2) {
    if (cs1 == null || cs2 == null) {
      throw new IllegalArgumentException("Null Argument not supported.");
    }
    return ComparatorUtil.compare(cs1.size(), cs2.size());
  }//compare

}//class CompoundSequenceLengthComparator
