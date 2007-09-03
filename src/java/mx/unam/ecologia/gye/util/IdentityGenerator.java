//@license@
package mx.unam.ecologia.gye.util;

/**
 * An utility class that will provide long identifiers.
 * <p/>
 * The identifiers will be unique starting from 1 to
 * Long.MAX_VALUE, within the context of a classloader.
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class IdentityGenerator {

  private static long c_IDCounter = 0;

  /**
   * Returns the next identity.
   *
   * @return the next identity as long.
   */
  public synchronized static final long nextIdentity() {
    return ++c_IDCounter;
  }//nextIdentity

  /**
   * Resets the generator.
   */
  public synchronized static final void reset() {
    c_IDCounter = 0;
  }//reset

}//class IdentityGenerator
