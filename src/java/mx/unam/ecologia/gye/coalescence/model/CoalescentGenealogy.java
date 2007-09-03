//@license@
package mx.unam.ecologia.gye.coalescence.model;

import cern.colt.function.DoubleDoubleFunction;
import cern.jet.math.Arithmetic;
import cern.jet.random.Exponential;
import cern.jet.random.Poisson;
import cern.jet.random.Uniform;
import cern.jet.random.sampling.RandomSamplingAssistant;
import mx.unam.ecologia.gye.coalescence.util.SimulationParameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class implements a <tt>CoalescentGenealogy</tt>.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class CoalescentGenealogy {

  protected ArrayList<UniParentalGene> m_Leaves;
  protected ArrayList<UniParentalGene> m_Genes;
  protected int m_NumGenes = 25;
  protected double m_Height = 0;
  protected double m_BranchSum = 0;
  protected boolean m_IsDynamicN;
  protected DoubleDoubleFunction m_GrowthFunc;

  static {
    Exponential.makeDefaultGenerator();
    Poisson.makeDefaultGenerator();
  }//static

  public CoalescentGenealogy(SimulationParameters params) {
    m_NumGenes = params.k();
    m_Genes = new ArrayList<UniParentalGene>(m_NumGenes);
    m_IsDynamicN = params.isDynamicN();
    if (m_IsDynamicN) {
      m_GrowthFunc = params.getGrowthFunction();
    }
  }//constructor

  public void generate() {
    //1. Start with k=n genes
    m_Genes.clear();
    int k = m_NumGenes;
    int[] tags = new int[k];
    for (int i = 0; i < tags.length; i++) {
      tags[i] = i + 1;
    }
    tags = RandomSamplingAssistant.sampleArray(k, tags);
    m_Leaves = new ArrayList<UniParentalGene>();

    for (int i = 0; i < k; i++) {
      UniParentalGene m_Hind = new UniParentalGene("" + tags[i]);
      m_Genes.add(m_Hind);
      m_Leaves.add(m_Hind);
    }
    double tkc = 0;
    m_Height = 0;
    m_BranchSum = 0;
    do {
      //2. simulate waiting time
      tkc = Exponential.staticNextDouble(Arithmetic.binomial(k, 2));

      //2.1 If dynamic N, stretch or compress
      if (m_IsDynamicN) {
        tkc = m_GrowthFunc.apply(m_Height, tkc);
      }

      //3. choose uniform random pair (taking care about list index movements)
      k--;
      int i = Uniform.staticNextIntFromTo(0, k);
      int j = Uniform.staticNextIntFromTo(0, k);
      while (j == i) {
        j = Uniform.staticNextIntFromTo(0, k);
      }
      UniParentalGene ij = null;
      UniParentalGene ii = null;
      if (i < j) {
        ij = (UniParentalGene) m_Genes.remove(j);
        ii = (UniParentalGene) m_Genes.remove(i);
      } else {
        ij = (UniParentalGene) m_Genes.remove(i);
        ii = (UniParentalGene) m_Genes.remove(j);
      }

      //Add no coalescense time to others
      for (Iterator iterator = m_Genes.iterator(); iterator.hasNext();) {
        ((UniParentalGene) iterator.next()).addNoCoalescenceTime(tkc);
      }

      //4. Merge i and j
      UniParentalGene ancestor = new UniParentalGene(ii, ij, tkc, m_Height);
      m_Genes.add(ancestor);
      // update stats
      m_BranchSum += ancestor.getLeftBranch().getLength()
          + ancestor.getRightBranch().getLength();
      m_Height += tkc;
    } while (k > 1); //Step 5.
  }//generate

  public List getLeaves() {
    return m_Leaves;
  }//getLeafs

  public UniParentalGene getMRCA() {
    return (UniParentalGene) m_Genes.get(0);
  }//getMRCA

  public double getExpectedHeight() {
    return 2 * (1 - (1 / m_NumGenes));
  }//getExpectedHeight

  public double getHeight() {
    return m_Height;
  }//getHeight

  public double getTotalBranchLength() {
    return m_BranchSum;
  }//getTotalBranchLength

  //Nordborg, 
  public double getExpectedTotalBranchLength() {
    return 2 * (EULER_MASCHERONI + Math.log(m_NumGenes));
  }//getExpectedTotalBranchLength

  public static final UniParentalGene createUniParentalCoalescentGenealogy(SimulationParameters params) {
    CoalescentGenealogy cg = new CoalescentGenealogy(params);
    cg.generate();
    return cg.getMRCA();
  }//createUniParentalCoalescentGenealogy


  public static final CoalescentGenealogy createCoalescentGenealogy(SimulationParameters params) {
    CoalescentGenealogy cg = new mx.unam.ecologia.gye.coalescence.model.CoalescentGenealogy(params);
    cg.generate();
    return cg;
  }//createCoalescentGenealogy


  private static final double EULER_MASCHERONI = 0.577215664901532d;

}//class CoalescentGenealogy
