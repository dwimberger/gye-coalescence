//@license@
package mx.unam.ecologia.gye.util;

import mx.unam.ecologia.gye.model.CompoundSequence;

/**
 * Interface for a distance calculating function.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public interface DistanceFunction {

  /**
   * Calculates the distance between the two given {@link CompoundSequence} instances.
   *
   * @param cs1 a {@link CompoundSequence}.
   * @param cs2 a {@link CompoundSequence}.
   * @return
   */
  public double apply(CompoundSequence cs1, CompoundSequence cs2);

}//class DistanceFunction
