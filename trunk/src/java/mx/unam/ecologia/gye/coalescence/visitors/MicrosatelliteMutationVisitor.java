//@license@
package mx.unam.ecologia.gye.coalescence.visitors;

import cern.jet.random.Binomial;
import cern.jet.random.Distributions;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;
import mx.unam.ecologia.gye.coalescence.model.Branch;
import mx.unam.ecologia.gye.coalescence.model.UniParentalGene;
import mx.unam.ecologia.gye.coalescence.util.SimulationParameters;
import mx.unam.ecologia.gye.model.CompoundSequence;
import mx.unam.ecologia.gye.model.Sequence;
import mx.unam.ecologia.gye.model.SequenceUnit;

import java.util.Date;
import java.util.Iterator;

/**
 * Implements a {@link UniParentalGeneVisitor} that applies mutations
 * to the branches.
 * <p/>
 * The number of mutations is drawn as random sample from the
 * branch, and applied according to a known microsatellite mutation
 * schema.
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class MicrosatelliteMutationVisitor
    implements UniParentalGeneVisitor {

  private final RandomEngine m_RndGen = new MersenneTwister(new Date());
  private double m_GeometricMean = 1;
  private double m_DeleteProbability = 0.5;
  private long m_NodesVisited = 0;
  private int m_MinimumSizeForMutation = 2;
  private double m_Theta;
  private long m_NumMutations;

  public MicrosatelliteMutationVisitor(SimulationParameters params) {
    m_Theta = params.theta();
    if (m_Theta < 0) {
      throw new RuntimeException("Theta required (defined by N and u).");
    }
    m_MinimumSizeForMutation = params.deleteFloor();
    m_DeleteProbability = params.p();
  }//constructor

  public long getNodesVisited() {
    return m_NodesVisited;
  }//getNodesVisited

  public void reset() {
    m_NodesVisited = 0;
    m_NumMutations = 0;
  }//reset

  public double getGeometricMean() {
    return m_GeometricMean;
  }//getGeometricMean

  public void setGeometricMean(double geometricMean) {
    m_GeometricMean = geometricMean;
  }//setGeometricMean

  public double getDeleteProbability() {
    return m_DeleteProbability;
  }//getDeleteProbability

  public void setDeleteProbability(double deleteProbability) {
    m_DeleteProbability = deleteProbability;
  }//setDeleteProbability

  public int getMinimumSizeForMutation() {
    return m_MinimumSizeForMutation;
  }//getMinimumSizeForMutation

  public void setMinimumSizeForMutation(int minimumSizeForMutation) {
    m_MinimumSizeForMutation = minimumSizeForMutation;
  }//setMinimumSizeForMutation

  public void visit(UniParentalGene upgene) {
    double t = upgene.getAbsTime();
    CompoundSequence h = upgene.getCompoundSequence();
    if (upgene != null && upgene.isAncestor() && h != null) {
      //handle left branch
      applyMutations(h, upgene.getLeftDescendant(), upgene.getLeftBranch(), t);
      //handle right branch
      applyMutations(h, upgene.getRightDescendant(), upgene.getRightBranch(), t);
      m_NodesVisited++;
    }
  }//visit

  protected void applyMutations(CompoundSequence csi, UniParentalGene g, Branch br, double t) {
    CompoundSequence cs = csi.getCopy();
    for (Iterator iterator = cs.iterator(); iterator.hasNext();) {
      Sequence l = (Sequence) iterator.next();
      int to = l.size();

      SequenceUnit u = l.get(0); //all are the same base
      int nummut = br.drawNumberOfMutations(m_Theta);
      double[] mutt = br.getTimesOfEvents(nummut);

      m_NumMutations += nummut;
      for (int i = 0; i < nummut; i++) {
        //add or delete
        switch (Binomial.staticNextInt(1, m_DeleteProbability)) { //Bernoulli Trial
          case ADD:
            l.add(sampleReverseGeometricFromTo(0, to, m_GeometricMean, m_RndGen), u);
            l.setLastMutation(mutt[i] + t);
            to++; //track size
            continue;
          case DELETE:
            if (to < m_MinimumSizeForMutation) {
              m_NumMutations--; //track applied number
              continue; //don't apply mutations when small
            }
            l.remove(sampleGeometricFromTo(0, to, m_GeometricMean, m_RndGen));
            l.setLastMutation(mutt[i] + t);
            to--; //track size
            continue;
        }
      }
    }
    g.setCompoundSequence(cs);
  }//applyMutations

  public long getNumberOfMutations() {
    return m_NumMutations;
  }//getNumberOfMutations

  private static final int sampleGeometricFromTo(int from, int to, double p, RandomEngine rndgen) {
    int sample = 0;
    do {
      sample = Distributions.nextGeometric(p, rndgen);
    } while (sample < from || sample >= to);
    return sample;
  }//sampleGeometricFromTo

  private static final int sampleReverseGeometricFromTo(int from, int to, double p, RandomEngine rndgen) {
    int sample = 0;
    do {
      sample = Distributions.nextGeometric(p, rndgen);
    } while (sample < from || sample >= to);
    return to - 1 - sample;
  }//sampleReverseGeometricFromTo

  private static final int ADD = 0;
  private static final int DELETE = 1;

}//class MicrosatelliteMutationVisitor
