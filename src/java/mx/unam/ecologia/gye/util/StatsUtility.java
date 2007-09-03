//@license@
package mx.unam.ecologia.gye.util;

import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive;

/**
 * Provides utility methods for obtaining or printing
 * simple statistics of a list of doubles.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class StatsUtility {

  private StatsUtility() {
  } //constructor

  public static final void printStats(String desc, DoubleArrayList dal) {
    double mean = Descriptive.mean(dal);
    double var = Descriptive.sampleVariance(dal, mean);
    System.out.println(desc + " Mean = " + mean);
    System.out.println(desc + " Var  = " + var);
  }//printStats

  /**
   * Returns the basic summary stats for a given list of double values.
   * Basic summary stats are the mean and the sample variance.
   *
   * @param dal the <tt>DoubleArrayList</tt>.
   * @return the mean and the sample variance in a double[2].
   */
  public static final double[] getStats(DoubleArrayList dal) {
    double[] stats = new double[2];
    stats[0] = Descriptive.mean(dal);
    stats[1] = Descriptive.sampleVariance(dal, stats[0]);
    return stats;
  }//getStats

}//class StatsUtility