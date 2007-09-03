//@license@
package mx.unam.ecologia.gye.util;

import mx.unam.ecologia.gye.model.Sequence;

import java.util.Comparator;

/**
 * Implements a <tt>Comparator</tt> that will compare sequences
 * by ancestry, latter of which is defined by the timeline of
 * the mutations (the highest number in the trace is the most recent).
 * <p/>
 * Given the fact that the trace will always in order, it is enough to
 * compare the last mutation in each trace.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class SequenceAncestryComparator
    implements Comparator<Sequence> {

  public static final SequenceAncestryComparator instance = new SequenceAncestryComparator();

  public int compare(Sequence s1, Sequence s2) {
    if (s1 == null || s2 == null) {
      throw new IllegalArgumentException("Null Argument not supported.");
    }
    return ComparatorUtil.compare(s1.getLastMutation(), s2.getLastMutation());

  }//compare

}//class SequenceAncestryComparator
