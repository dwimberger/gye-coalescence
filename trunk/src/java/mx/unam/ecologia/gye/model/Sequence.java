//@license@
package mx.unam.ecologia.gye.model;

import cern.colt.list.LongArrayList;

/**
 * Defines the interface of a <tt>Sequence</tt>.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public interface Sequence {

  /**
   * Returns the {@link SequenceUnit} at the given position.
   *
   * @param pos the position.
   * @return a {@link SequenceUnit}.
   */
  public SequenceUnit get(int pos);

  /**
   * Returns the size of this <tt>Sequence</tt> as a count
   * of {@link SequenceUnit}s.
   *
   * @return the size in sequence units.
   */
  public int size();

  /**
   * Returns the size of this <tt>Sequence</tt> as a count
   * of bases.
   * This is the sequence unit count multiplied with the
   * sequence unit size in bases.
   *
   * @return the size in bases.
   */
  public int getSize();

  /**
   * Replaces a {@link SequenceUnit} at a given position.
   *
   * @param pos the position.
   * @param su  a {@link SequenceUnit}.
   */
  public void replace(int pos, SequenceUnit su);

  /**
   * Add a {@link SequenceUnit} at the end of this
   * <tt>Sequence</tt>.
   *
   * @param su the {@link SequenceUnit} to be added.
   */
  public void add(SequenceUnit su);

  /**
   * Adds a {@link SequenceUnit} at a given position.
   * This is basically an insert.
   *
   * @param pos the position.
   * @param su  a {@link SequenceUnit}.
   */
  public void add(int pos, SequenceUnit su);

  /**
   * Removes the {@link SequenceUnit} from the given
   * position.
   *
   * @param pos the position.
   */
  public void remove(int pos);

  /**
   * Tests if this <tt>Sequence</tt> is tracing changes.
   *
   * @return true if tracing changes, false otherwise.
   */
  public boolean isTracingChanges();

  /**
   * Sets the flag that determines if this <tt>Sequence</tt>
   * is tracing changes.
   *
   * @param trackChanges true if changes are to be traced, false otherwise.
   */
  public void setTracingChanges(boolean trackChanges);

  /**
   * Returns the change trace of this <tt>Sequence</tt>.
   *
   * @return the trace of changes to this <tt>Sequence</tt>.
   */
  public LongArrayList getChangeTrace();

  /**
   * Returns the absolute time of the last mutation.
   * Absolute is meant om terms of the coalescent process.
   *
   * @return the time as double.
   */
  public double getLastMutation();

  /**
   * Sets the absolute time of the last mutation.
   * Absolute is meant in terms of the coalescent process.
   *
   * @param d the time as double.
   */
  public void setLastMutation(double d);

  /**
   * Returns a deep copy of the <tt>Sequence</tt>.
   *
   * @return a deep copy of this <tt>Sequence</tt>.
   */
  public Sequence getCopy();

  /**
   * Returns a String representing this sequence.
   *
   * @return a String representation of the <tt>Sequence</tt>.
   */
  public String toString();

  /**
   * Returns a short String representing this sequence.
   *
   * @return a String representation of the <tt>Sequence</tt>.
   */
  public String toShortString();

  /**
   * Returns a full string representing this sequence and
   * all its information.
   *
   * @return a String representation of the <tt>Sequence</tt>.
   */
  public String toFullString();

  /**
   * Returns a full string that represents a readable
   * serialization of the sequence.
   *
   * @return a String representation of the <tt>Sequence</tt>.
   */
  public String toStoreString();

  /**
   * Returns a String representing the trace of changes of
   * this <tt>Sequence</tt>.
   *
   * @return a String representation of the change trace.
   */
  public String toTraceString();

}//interface Sequence
