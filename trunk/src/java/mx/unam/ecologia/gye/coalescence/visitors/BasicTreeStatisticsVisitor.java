//@license@
package mx.unam.ecologia.gye.coalescence.visitors;

import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive;
import mx.unam.ecologia.gye.coalescence.model.Branch;
import mx.unam.ecologia.gye.coalescence.model.UniParentalGene;
import mx.unam.ecologia.gye.coalescence.util.SimulationParameters;

/**
 * This class implements a {@link UniParentalGeneVisitor}
 * collecting basic statistics of a geneaology tree (or subtree).
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class BasicTreeStatisticsVisitor
    implements UniParentalGeneVisitor {

  protected DoubleArrayList m_BranchLengths;
  protected DoubleArrayList m_MutationCounts;
  protected double m_TotalBranchLength = -1;
  protected long m_TotalMutationCount = -1;
  protected double m_Theta = -1;

  public BasicTreeStatisticsVisitor(SimulationParameters params) {
    m_Theta = params.theta();
    m_BranchLengths = new DoubleArrayList();
    m_MutationCounts = new DoubleArrayList();
  }//constructor

  public void visit(UniParentalGene upgene) {
    if (upgene.isAncestor()) {
      Branch br = upgene.getLeftBranch();
      m_MutationCounts.add(br.drawNumberOfMutations(m_Theta));
      m_BranchLengths.add(br.getLength());
      br = upgene.getRightBranch();
      m_MutationCounts.add(br.drawNumberOfMutations(m_Theta));
      m_BranchLengths.add(br.getLength());
    }
  }//visit

  public void reset() {

  }//reset

  public double getTotalBranchLength() {
    return m_TotalBranchLength;
  }//getTotalBranchLength


  public long getTotalMutationCount() {
    return m_TotalMutationCount;
  }//getTotalMutationCount

  public void calculateStatistics() {
    m_TotalBranchLength = Descriptive.sum(m_BranchLengths);
    m_TotalMutationCount = (long) Descriptive.sum(m_MutationCounts);
  }//calculateStatistics

  public String toString() {
    calculateStatistics();
    final StringBuilder sbuf = new StringBuilder();
    sbuf.append("{L_b = ")
        .append(m_TotalBranchLength)
        .append("; Total #Mutations=")
        .append(m_TotalMutationCount).append("}");
    return sbuf.toString();
  }//toString

  public void recycle() {
    m_BranchLengths.clear();
    m_MutationCounts.clear();
    m_TotalBranchLength = -1;
    m_TotalMutationCount = -1;
  }//recycle

}//class BasicTreeStatisticsVisitor
