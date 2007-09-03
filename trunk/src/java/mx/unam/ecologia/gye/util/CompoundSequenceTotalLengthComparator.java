//@license@
package mx.unam.ecologia.gye.util;

import mx.unam.ecologia.gye.model.CompoundSequence;

import java.util.Comparator;

/**
 * Provides a total length comparator for {@link CompoundSequence}
 * instances.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class CompoundSequenceTotalLengthComparator
    implements Comparator<CompoundSequence> {


  public int compare(CompoundSequence cs1, CompoundSequence cs2) {
    if (cs1 == null || cs2 == null) {
      throw new IllegalArgumentException("Null Argument not supported.");
    }
    return ComparatorUtil.compare(cs1.getSize(), cs2.getSize());
  }//compare

}//class CompoundSequenceTotalLengthComparator
