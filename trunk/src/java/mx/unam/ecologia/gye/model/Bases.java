//@license@
package mx.unam.ecologia.gye.model;

/**
 * Provides all valid {@link Base} instances.
 * <p/>
 * These are Flyweights that represent the bases.
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class Bases {

  /**
   * Defines the {@link Base} Adenine.
   */
  public static final Base A = new Base('a');

  /**
   * Defines the {@link Base} Cytosine.
   */
  public static final Base C = new Base('c');

  /**
   * Defines the {@link Base} Guanine.
   */
  public static final Base G = new Base('g');

  /**
   * Defines the {@link Base} Thymine.
   */
  public static final Base T = new Base('t');

  /**
   * Converts the charsym to the flyweight {@link Base} references.
   *
   * @param c a character.
   * @return the corresponding {@link Base} flyweight.
   */
  public static final Base fromCharSym(char c) {
    switch (c) {
      case'a':
        return Bases.A;
      case'g':
        return Bases.G;
      case'c':
        return Bases.C;
      case't':
        return Bases.T;
      default:
        throw new IllegalArgumentException();
    }
  }//fromCharSym

}//class Bases
