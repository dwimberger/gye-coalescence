//@license@
package mx.unam.ecologia.gye.coalescence.model;

/**
 * This interface defines a <tt>Branch</tt>.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public interface Branch {

  /**
   * Returns the length of this <tt>Branch</tt>.
   *
   * @return the length of this branch as <tt>double</tt>.
   */
  public double getLength();

  /**
   * Draws a number of mutations from a poisson
   * distribution based on the branch length
   * (time between coalescence events) and theta.
   *
   * @param theta the scaled mutation rate as <tt>double</tt>.
   * @return a number of mutations drawn from a poisson distribution.
   */
  public int drawNumberOfMutations(double theta);

  /**
   * Returns the times of the given number of events,
   * distributed uniformly over the time represented by
   * the length of this branch.
   *
   * @param numev a number of events.
   * @return the times of each event uniformly distributed over the
   *         time represented by the length of this branch.
   */
  public double[] getTimesOfEvents(int numev);

  /**
   * Factory method to create branch instances.
   *
   * @param t     the time the branch represents.
   * @param start the time from which the branch starts.
   * @return a new <tt>Branch</tt> instance.
   */
  public Branch createBranch(double t, double start);

}//interface Branch
