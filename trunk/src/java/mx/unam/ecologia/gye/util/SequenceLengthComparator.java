//@license@
package mx.unam.ecologia.gye.util;

import mx.unam.ecologia.gye.model.Sequence;

import java.util.Comparator;

/**
 * Implements a <tt>Comparator</tt> that will compare sequences
 * by length only.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class SequenceLengthComparator
    implements Comparator<Sequence> {

  public static SequenceLengthComparator instance = new SequenceLengthComparator();

  public int compare(Sequence s1, Sequence s2) {
    if (s1 == null || s2 == null) {
      throw new IllegalArgumentException("Null Argument not supported.");
    }

    return ComparatorUtil.compare(s1.getSize(), s2.getSize());
  }//compare


}//class SequenceLengthComparator
