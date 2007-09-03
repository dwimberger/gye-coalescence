//@license@
package mx.unam.ecologia.gye.coalescence.model;

import mx.unam.ecologia.gye.coalescence.visitors.UniParentalGeneVisitor;
import mx.unam.ecologia.gye.model.CompoundSequence;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;


/**
 * This class implements a <tt>UniParentalGene</tt>.
 * <p/>
 * Note that instances representing ancestors have
 * a tree structure that can be traversed.
 * </p>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class UniParentalGene {

  private static final Log log = LogFactory.getLog(UniParentalGene.class);

  protected static Branch c_BranchFactory = new SimpleBranch();
  protected UniParentalGene m_Ancestor;
  protected UniParentalGene m_LDescendant;
  protected UniParentalGene m_RDescendant;
  protected Branch m_LeftBranch;
  protected Branch m_RightBranch;
  protected CompoundSequence m_CompoundSequence;
  protected double m_CoTime;
  protected double m_NoCoTime = 0;
  protected StringBuffer m_Tree;
  protected double m_Since = 0;

  /**
   * Creates a non related <tt>UniParentalGene</tt> with a given tag.
   *
   * @param tag the tag of the <tt>UniParentalGene</tt>.
   */
  public UniParentalGene(String tag) {
    m_Tree = new StringBuffer(tag);
  }//constructor

  /**
   * Creates a <tt>UniParentalGene</tt> ancestor, with the given
   * descendants, the coalescent time and a scaled mutation rate
   * for generating mutation events on the branches.
   *
   * @param des1 a descendant <tt>UniParentalGene</tt>.
   * @param des2 a descendant <tt>UniParentalGene</tt>.
   * @param tkc  the coalescent time as <tt>double</tt>.
   */
  public UniParentalGene(UniParentalGene des1, UniParentalGene des2, double tkc, double tkca) {
    m_Since = tkca;
    m_CoTime = tkc;
    m_LeftBranch = c_BranchFactory.createBranch(tkc + des1.m_NoCoTime, des1.m_Since);
    m_RightBranch = c_BranchFactory.createBranch(tkc + des2.m_NoCoTime, des2.m_Since);
    m_Tree = new StringBuffer("(")
        .append(des1.m_Tree)
        .append(':')
        .append(m_LeftBranch.getLength())
        .append(',')
        .append(des2.m_Tree)
        .append(':')
        .append(m_RightBranch.getLength())
        .append(')');
    des1.setAncestor(this);
    des2.setAncestor(this);
    m_LDescendant = des1;
    m_RDescendant = des2;
  }//constructor

  public static void setBranchFactory(Branch b) {
    c_BranchFactory = b;
  }//setBranchFactory

  /**
   * Returns the CompoundSequence of this <tt>UniParentalGene</tt>.
   *
   * @return a {@link CompoundSequence}.
   */
  public CompoundSequence getCompoundSequence() {
    return m_CompoundSequence;
  }//getCompoundSequence

  /**
   * Sets the CompoundSequence of this <tt>UniParentalGene</tt>.
   *
   * @param h a {@link CompoundSequence}.
   */
  public void setCompoundSequence(CompoundSequence h) {
    m_CompoundSequence = h;
  }//setCompoundSequence

  /**
   * Returns the cummulative coalescent
   * time to this <tt>UniParentalGene</tt>.
   *
   * @return the cummulative coalescent time as <tt>double</tt>.
   */
  public double getCoalescentTime() {
    return m_CoTime;
  }//getCoalescentTime

  public double getAbsTime() {
    return m_Since;
  }//getAbsTime

  /**
   * Adds the waiting time of a coalescent event
   * this <tt>UniParentalGene</tt> was not involved in.
   * <p/>
   * This will keep track of time that has passed without
   * events for this instance to adjust the branch lengths
   * accordingly.
   *
   * @param coTime the waiting time of a coalescent event
   *               this <tt>UniParentalGene</tt> was not involved in.
   */
  public void addNoCoalescenceTime(double coTime) {
    m_NoCoTime += coTime;
  }//addNoCoalescenceTime

  protected void setAncestor(UniParentalGene anc) {
    m_Ancestor = anc;
  }//setAncestor

  protected UniParentalGene getAncestor() {
    return m_Ancestor;
  }//getAncestor

  /**
   * Returns a descendant of this <tt>UniParentalGene</tt>.
   *
   * @return a descendant <tt>UniParentalGene</tt>.
   */
  public UniParentalGene getLeftDescendant() {
    return m_LDescendant;
  }//getLeftDescendant

  /**
   * Returns a descendant of this <tt>UniParentalGene</tt>.
   *
   * @return a descendant <tt>UniParentalGene</tt>.
   */
  public UniParentalGene getRightDescendant() {
    return m_RDescendant;
  }//getRightDescendant


  /**
   * Returns the {@link Branch} to a descendant of this <tt>UniParentalGene</tt>.
   *
   * @return a {@link Branch}.
   */
  public Branch getLeftBranch() {
    return m_LeftBranch;
  }//getLeft

  /**
   * Returns the {@link Branch} to a descendant of this <tt>UniParentalGene</tt>.
   *
   * @return a {@link Branch}.
   */
  public Branch getRightBranch() {
    return m_RightBranch;
  }//getRight

  /**
   * Tests if this <tt>UniParentalGene</tt> is a leaf.
   *
   * @return true if leaf, false otherwise.
   */
  public boolean isLeaf() {
    return m_RDescendant == null && m_LDescendant == null;
  }//isLeaf

  /**
   * Tests if this <tt>UniParentalGene</tt> is an ancestor
   * (== !{@link #isLeaf()}).
   *
   * @return true if ancestor, false otherwise.
   */
  public boolean isAncestor() {
    return !isLeaf();
  }//isAncestor

  /**
   * Tests if this <tt>UniParentalGene</tt> is a
   * MRCA (i.e. has no Ancestor).
   *
   * @return true if MRCA, false otherwise.
   */
  public boolean isMRCA() {
    return m_Ancestor == null;
  }//isMRCA


  /**
   * Returns a NH/NHX formatted <tt>String</tt> of the
   * subtree of this <tt>UniParentalGene</tt>.
   *
   * @return a String in NH/NHX format.
   */
  public String toNHXString() {
    return m_Tree.toString();
  }//toNHXString

  //iterative level-order (BF)
  public static final void traverse(UniParentalGene upgene, UniParentalGeneVisitor visitor) {
    //log.debug("traverse()");
    LinkedList<UniParentalGene> queue = new LinkedList<UniParentalGene>();
    if (upgene.isAncestor()) {
      queue.add(upgene);
    } else {
      return; //nothing to be done!
    }
    while (!(queue.isEmpty())) {
      UniParentalGene upg = (UniParentalGene) queue.removeFirst();
      visitor.visit(upg);
      //log.debug("traverse()::Visited " + upg.toNHXString());

      if (upg.m_LDescendant != null) {
        queue.add(upg.m_LDescendant);
      }
      if (upg.m_RDescendant != null) {
        queue.add(upg.m_RDescendant);
      }
    }
  }//traverse

}//class UniParentalGene