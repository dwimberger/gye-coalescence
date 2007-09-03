//@license@
package mx.unam.ecologia.gye.coalescence.model;

/**
 * This interface defines a {@link Branch} that provide a
 * number of mutation events and the times of their occurrence.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public interface MutateableBranch
    extends Branch {

  /**
   * Returns the number of mutations on this
   * <tt>MutateableBranch</tt>.
   *
   * @return the number of mutations as <tt>int</tt>.
   */
  public int getMutationEventNumber();

  /**
   * Returns a the times of the mutation event,
   * distributed over the total time that it represented by
   * {@link Branch#getLength()}.
   *
   * @return the times of the mutation events as <tt>double[]</tt> with
   *         a <tt>length</tt> of {@link Branch#getLength()}.
   */
  public double[] getMutationEventTimes();

}//interface MutateableBranch
