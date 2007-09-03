//@license@
package mx.unam.ecologia.gye.coalescence.app;

import cern.colt.list.DoubleArrayList;
import cern.colt.list.IntArrayList;
import cern.colt.map.OpenIntIntHashMap;
import mx.unam.ecologia.gye.coalescence.model.CoalescentGenealogy;
import mx.unam.ecologia.gye.coalescence.model.UniParentalGene;
import mx.unam.ecologia.gye.coalescence.util.SimulationParameters;
import mx.unam.ecologia.gye.coalescence.visitors.MicrosatelliteMutationVisitor;
import mx.unam.ecologia.gye.model.CompoundSequence;
import mx.unam.ecologia.gye.util.CSAncestryComparator;
import mx.unam.ecologia.gye.util.CompoundSequenceIdentityComparator;
import mx.unam.ecologia.gye.util.HaplotypeFreqSet;
import mx.unam.ecologia.gye.util.IdentityGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Simulates micro coalescence with mutations and collects a
 * statistic of frequency vs. ancestry.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class AncestryFrequencySimulation {

  private static final Log log = LogFactory.getLog(AncestryFrequencySimulation.class);

  private Analysis m_Analysis;
  private CoalescentGenealogy m_Genealogy;
  private int m_NumMCSamples;
  private CompoundSequence m_MRCACompoundSequence;
  private MicrosatelliteMutationVisitor m_Visitor;
  private SimulationParameters m_SimParam;

  public AncestryFrequencySimulation(SimulationParameters params, MicrosatelliteMutationVisitor vis) {
    m_Analysis = new Analysis();
    m_Genealogy = new CoalescentGenealogy(params);
    m_NumMCSamples = params.samples();
    m_MRCACompoundSequence = params.CompoundSequence();
    m_Visitor = vis;
    m_SimParam = params;
    log.info(params.toString());
  }//MicrosatelliteSimulation

  public void init() {
    log.info("Initializing.");
    m_MRCACompoundSequence.setTracingChanges(true); //required
    m_MRCACompoundSequence.setLocus(m_SimParam.isLocus());
  }//init

  private final void addSample(List leaves) {
    m_Analysis.analyze(leaves);
  }//addSample

  private final void run() {

    //1. Simulations
    log.info("Running Simulations");
    DoubleArrayList heightstats = new DoubleArrayList(m_NumMCSamples);
    long start = System.currentTimeMillis();
    for (int i = 0; i < m_NumMCSamples; i++) {
      IdentityGenerator.reset();
      m_Visitor.reset();
      if (i > 0 && i % 100 == 0) {
        System.out.print('#');
      }
      m_Genealogy.generate();
      UniParentalGene mrca = m_Genealogy.getMRCA();
      mrca.setCompoundSequence(m_MRCACompoundSequence.getCopy());
      UniParentalGene.traverse(mrca, m_Visitor);
      addSample(m_Genealogy.getLeaves());
      heightstats.add(m_Genealogy.getHeight());
    }
    long end = System.currentTimeMillis();
    System.out.println("#"); //prints the last 100 counter gutter
    log.info("Finished Simulations in " + ((end - start) / 1000) + " seconds.");
    m_Analysis.calculateStats();
    System.out.println(m_Analysis.toString());
  }//run

  public static void main(String[] args) throws Exception {
    org.apache.log4j.BasicConfigurator.configure();
    SimulationParameters params = new SimulationParameters(args);
    MicrosatelliteMutationVisitor vis = new MicrosatelliteMutationVisitor(params);
    AncestryFrequencySimulation msim = new AncestryFrequencySimulation(params, vis);
    msim.init();
    msim.run();
  }//main

  private class Analysis {

    private OpenIntIntHashMap m_Bins;
    private DoubleArrayList m_Stats;

    public Analysis() {
      m_Bins = new OpenIntIntHashMap();
      m_Stats = new DoubleArrayList();
    }//Analysis

    public void analyze(List leaves) {
      HaplotypeFreqSet ident = new HaplotypeFreqSet(new CompoundSequenceIdentityComparator());

      for (int i = 0; i < leaves.size(); i++) {
        UniParentalGene upgene = (UniParentalGene) leaves.get(i);
        CompoundSequence h = upgene.getCompoundSequence();
        ident.add(h);
      }
      ident.sort(new CSAncestryComparator());
      storeSample(ident.findMostFrequent() + 1);
    }//analyze

    public void storeSample(int pos) {

      if (m_Bins.containsKey(pos)) {
        m_Bins.put(pos, m_Bins.get(pos) + 1);
      } else {
        m_Bins.put(pos, 1);
      }
    }//addSample

    public void calculateStats() {
      IntArrayList pos = m_Bins.keys();
      pos.sort();
      pos.trimToSize();
      double sum = 0;
      for (int i = 0; i < pos.size(); i++) {
        sum += m_Bins.get(pos.get(i));
      }//pos

      for (int i = 0; i < pos.size(); i++) {
        m_Stats.add((double) m_Bins.get(pos.get(i)) / sum);
        System.out.println(pos.get(i) + " -> " + m_Bins.get(pos.get(i)) + " = " + m_Stats.get(i));
      }//pos

    }//calculateStats

    public String toString() {
      return m_Bins.toString() + "\n" + m_Stats.toString();
    }//toString

  }//inner class

}//class AncestryFrequencySimulation
