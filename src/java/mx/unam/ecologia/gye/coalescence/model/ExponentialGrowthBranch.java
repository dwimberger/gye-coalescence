//@license@
package mx.unam.ecologia.gye.coalescence.model;

import cern.jet.random.Poisson;
import cern.jet.random.Uniform;
import mx.unam.ecologia.gye.coalescence.util.ExponentialGrowth;

/**
 * Implements a {@link MutateableBranch}.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class ExponentialGrowthBranch implements Branch {

  protected double m_Time;
  protected double m_Theta;
  protected ExponentialGrowth m_ExponentialGrowth;

  public ExponentialGrowthBranch(ExponentialGrowth g) {
    m_ExponentialGrowth = g;
  }//constructor

  private ExponentialGrowthBranch(double t) {
    m_Time = t;
  }//constructor

  public Branch createBranch(double t, double start) {
    return new ExponentialGrowthBranch(m_ExponentialGrowth.apply(start, t));
  }//factory

  public double getLength() {
    return m_Time;
  }//getLength

  public int drawNumberOfMutations(double theta) {
    return Poisson.staticNextInt(((m_Time * theta) / 2));
  }//drawNumberOfMutations

  public double[] getTimesOfEvents(int numev) {
    double[] et = new double[numev];
    for (int i = 0; i < numev; i++) {
      et[i] = Uniform.staticNextDoubleFromTo(0, m_Time);
    }
    return et;
  }//getTimesOfEvents

}//SimpleBranch
