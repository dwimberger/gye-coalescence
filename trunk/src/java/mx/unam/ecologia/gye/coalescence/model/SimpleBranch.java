//@license@
package mx.unam.ecologia.gye.coalescence.model;

import cern.jet.random.Poisson;
import cern.jet.random.Uniform;

import java.util.Arrays;

/**
 * Implements a {@link MutateableBranch}.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class SimpleBranch implements Branch {

  protected double m_Time;
  protected double m_Theta;

  public SimpleBranch() {
    //factory
  }//constructor

  private SimpleBranch(double t) {
    m_Time = t;
  }//constructor

  public Branch createBranch(double t, double start) {
    return new SimpleBranch(t);
  }//factory

  public double getLength() {
    return m_Time;
  }//getLength

  public int drawNumberOfMutations(double theta) {
    return Poisson.staticNextInt(((m_Time * theta) / 2));
  }//drawNumberOfMutations

  public double[] getTimesOfEvents(int numev) {
    double[] et = new double[numev];
    if (numev == 0) {
      return et;
    }
    for (int i = 0; i < numev; i++) {
      et[i] = Uniform.staticNextDoubleFromTo(0, m_Time);
    }
    Arrays.sort(et);
    return et;
  }//getTimesOfEvents

}//SimpleBranch
