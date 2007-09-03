//@license@
package mx.unam.ecologia.gye.model;

/**
 * A single base implementing {@link SequenceUnit}.
 * <p/>
 * This is a flyweight.
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class Base implements SequenceUnit {

  private final char m_SymChar;

  protected Base(char c) {
    m_SymChar = c;
  }//constructor

  /**
   * Returns the character representing this <tt>Base</tt>.
   *
   * @return the character representing this base.
   */
  public char toChar() {
    return m_SymChar;
  }//toChar

  public int getUnitSize() {
    return 1;
  }//size

  public String toString() {
    return m_SymChar + "";
  }//toString

  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Base)) return false;

    final Base symbol = (Base) o;

    if (m_SymChar != symbol.m_SymChar) return false;

    return true;
  }//equals

  public int hashCode() {
    return (int) m_SymChar;
  }//hashCode

}//class Base
