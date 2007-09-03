//@license@
package mx.unam.ecologia.gye.coalescence.visitors;

import mx.unam.ecologia.gye.coalescence.model.UniParentalGene;

/**
 * Defines a basic visitor for {@link UniParentalGene} instances.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public interface UniParentalGeneVisitor {

  /**
   * Resets the visitor to initial state.
   */
  public void reset();

  /**
   * Visits an {@link UniParentalGene}.
   *
   * @param upgene the gene to be visited.
   */
  public void visit(UniParentalGene upgene);

}//interface UniParentalGeneVisitor
