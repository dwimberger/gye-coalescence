//@license@
package mx.unam.ecologia.gye.util;

/**
 * Provides utility method for comparisons.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class ComparatorUtil {

  public static final int compare(int a, int b) {
    return a < b ? -1 : a > b ? 1 : 0;
  }//compare

  public static final long max(long a, long b) {
    return a < b ? b : a > b ? a : a;
  }//max

  public static final int compare(long a, long b) {
    return a < b ? -1 : a > b ? 1 : 0;
  }//compare

  public static final double max(double a, double b) {
    return a < b ? b : a > b ? a : a;
  }//max

  public static final int compare(double a, double b) {
    return a < b ? -1 : a > b ? 1 : 0;
  }//compare

}//class ComparatorUtil
