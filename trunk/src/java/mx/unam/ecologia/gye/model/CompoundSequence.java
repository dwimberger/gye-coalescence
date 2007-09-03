//@license@
package mx.unam.ecologia.gye.model;

import java.util.ListIterator;

/**
 * This class is a compound of {@link Sequence} instances.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public interface CompoundSequence {

  /**
   * Adds a {@link Sequence} to this <tt>CompoundSequence</tt>.
   *
   * @param s a {@link Sequence}.
   */
  public void add(Sequence s);

  /**
   * Removes a {@link Sequence} from this <tt>CompoundSequence</tt>.
   *
   * @param s a {@link Sequence}.
   */
  public void remove(Sequence s);

  /**
   * Returns the {@link Sequence} at the given index.
   *
   * @param i the index of the sequence.
   * @return a {@link Sequence}.
   */
  public Sequence get(int i);

  /**
   * Iterator over the {@link Sequence} instances in this
   * <tt>CompoundSequence</tt>.
   *
   * @return a <tt>ListIterator</tt>.
   */
  public ListIterator iterator();

  /**
   * Sets the flag that determines if this <tt>Sequence</tt>
   * is tracing changes.
   *
   * @param b true if changes are to be traced, false otherwise.
   */
  public void setTracingChanges(boolean b);

  /**
   * Tests if this <tt>Sequence</tt> is tracing changes.
   *
   * @return true if tracing changes, false otherwise.
   */
  public boolean isTracingChanges();

  /**
   * Returns a deep copy of this <tt>CompoundSequence</tt>.
   *
   * @return a deep copy.
   */
  public CompoundSequence getCopy();

  /**
   * Returns the number of {@link Sequence}s in this <tt>CompoundSequence</tt>.
   *
   * @return the number of {@link Sequence}s.
   */
  public int getSequenceCount();

  /**
   * Returns the total size of this <tt>CompundSequence</tt> as a count
   * of {@link SequenceUnit}s.
   *
   * @return the total size in sequence units.
   */
  public int size();

  /**
   * Returns the total size of this <tt>CompoundSequence</tt> as a count
   * of bases.
   * This is the sequence unit count multiplied with the
   * sequence unit size in bases.
   *
   * @return the total size in bases.
   */
  public int getSize();

  /**
   * Tests if this <tt>CompoundSequence<tt> is to be treated as a single
   * locus.
   *
   * @return true if single locus, false otherwise.
   */
  public boolean isLocus();


  /**
   * Sets if this <tt>CompoundSequence<tt> is to be treated as a single
   * locus.
   *
   * @param b true single locus, false otherwise
   */
  public void setLocus(boolean b);

  /**
   * Returns a short String representing this <tt>CompoundSequence</tt>.
   *
   * @return a String.
   */
  public String toShortString();

  /**
   * Returns a full string representing this <tt>CompoundSequence</tt> and
   * all its information.
   *
   * @return a String.
   */
  public String toFullString();

  /**
   * Returns a full string that represents a readable
   * serialization of this <tt>CompoundSequence</tt>.
   *
   * @return a String.
   */
  public String toStoreString();

  /**
   * Returns a string that represents only the lengths of
   * this <tt>CompoundSequence</tt>.
   *
   * @return a String.
   */
  public String toLengthString();

}//class CompoundSequence
