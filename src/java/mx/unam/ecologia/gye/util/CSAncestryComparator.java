//@license@
package mx.unam.ecologia.gye.util;

import mx.unam.ecologia.gye.model.CompoundSequence;

import java.util.Comparator;

//import cern.colt.list.LongArrayList;

/**
 * Provides a
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class CSAncestryComparator
    implements Comparator<CompoundSequence> {

  public static final CSAncestryComparator instance =
      new CSAncestryComparator();

  public int compare(CompoundSequence cs1, CompoundSequence cs2) {
    if (cs1 == null || cs2 == null) {
      throw new IllegalArgumentException("Null Argument not supported.");
    }
    int sc = cs1.getSequenceCount();
    DoubleMinHeap h1 = new DoubleMinHeap(sc);
    DoubleMinHeap h2 = new DoubleMinHeap(sc);

    for (int i = 0; i < sc; i++) {
      h1.add(cs1.get(i).getLastMutation());
      h2.add(cs2.get(i).getLastMutation());
    }
    h1.build();
    h2.build();

    int szc = 0;
    for (int i = 0; i < sc; i++) {
      szc = ComparatorUtil.compare(h2.extractMin(), h1.extractMin());
      if (szc != 0) {
        return szc;
      }
    }
    return 0;
  }//compare

}//class CSAncestryComparator
