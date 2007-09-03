//@license@
package mx.unam.ecologia.gye.coalescence.util;

import cern.colt.function.DoubleDoubleFunction;

/**
 * Provides a DoubleDoubleFunction to compress or stretch the
 * coalescent time due to an exponential growth.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class ExponentialGrowth
    implements DoubleDoubleFunction {

  private double m_Beta;

  public ExponentialGrowth(double beta) {
    m_Beta = beta;
  }//ExponentialGrowth

  public double apply(double total, double time) {
    return (1 / m_Beta) * Math.log(1 + m_Beta * time * Math.exp(-1 * m_Beta * total));
  }//apply

}//class ExponentialGrowth
