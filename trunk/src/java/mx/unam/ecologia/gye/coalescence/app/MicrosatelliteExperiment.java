//@license@
package mx.unam.ecologia.gye.coalescence.app;

import cern.colt.list.DoubleArrayList;
import mx.unam.ecologia.gye.coalescence.analysis.CompleteHomoplasyAnalysis;
import mx.unam.ecologia.gye.coalescence.model.Analysis;
import mx.unam.ecologia.gye.coalescence.model.CoalescentGenealogy;
import mx.unam.ecologia.gye.coalescence.model.UniParentalGene;
import mx.unam.ecologia.gye.coalescence.util.SimulationParameters;
import mx.unam.ecologia.gye.coalescence.visitors.MicrosatelliteMutationVisitor;
import mx.unam.ecologia.gye.model.CompoundSequence;
import mx.unam.ecologia.gye.util.IdentityGenerator;
import mx.unam.ecologia.gye.util.StatsUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class MicrosatelliteExperiment
    implements Runnable {

  private static final Log log = LogFactory.getLog(MicrosatelliteExperiment.class);
  private Analysis m_Analysis;
  private CoalescentGenealogy m_Genealogy;
  private int m_NumMCSamples;
  private CompoundSequence m_MRCACompoundSequence;
  private MicrosatelliteMutationVisitor m_Visitor;
  private SimulationParameters m_SimParam;
  private DoubleArrayList m_Heights;
  private DoubleArrayList m_TBLs;
  private DoubleArrayList m_Mutations;
  private long m_SimTime;

  public MicrosatelliteExperiment(SimulationParameters params) {
    m_Genealogy = new CoalescentGenealogy(params);
    m_NumMCSamples = params.samples();
    m_MRCACompoundSequence = params.CompoundSequence();
    m_Visitor = new MicrosatelliteMutationVisitor(params);
    m_SimParam = params;
    m_Analysis = new CompleteHomoplasyAnalysis(m_NumMCSamples);
  }//MicrosatelliteSimulation

  public void init() {
    log.info("Initializing.");
    log.info(m_SimParam.toString());
    if (m_SimParam.isHaploid()) {
      log.info("Using theta=2*N*u");
    } else {
      log.info("Using theta=4*N*u");
    }
    if (m_SimParam.beta() > 0) {
      // UniParentalGene.setBranchFactory(
      //  new ExponentialGrowthBranch(new ExponentialGrowth(m_SimParam.beta())));
      log.info("Using exponential growth with beta=" + m_SimParam.beta());
    }
    //Prepare ancestor and analysis
    m_MRCACompoundSequence.setTracingChanges(true); //required
    m_MRCACompoundSequence.setLocus(m_SimParam.isLocus());
  }//init

  public String getCSVHeader() {
    StringBuilder sbuf = new StringBuilder();
    sbuf.append("beta,N,k,u,p,");
    sbuf.append(m_Analysis.getCSVHeader());
    return sbuf.toString();
  }//getCSVHeader

  public String resultsToCSV() {
    StringBuilder sbuf = new StringBuilder();
    sbuf.append(m_SimParam.beta());
    sbuf.append(',');
    sbuf.append(m_SimParam.N());
    sbuf.append(',');
    sbuf.append(m_SimParam.k());
    sbuf.append(',');
    sbuf.append(m_SimParam.u());
    sbuf.append(',');
    sbuf.append(m_SimParam.p());
    sbuf.append(',');
    sbuf.append(m_Analysis.statsToCSV());
    return sbuf.toString();
  }//getCSVString

  public final void run() {
    log.info("Running Simulations.");
    //Tree height samples
    m_Heights = new DoubleArrayList(m_NumMCSamples);
    //Total branch length samples
    m_TBLs = new DoubleArrayList(m_NumMCSamples);
    //Number of Mutations
    m_Mutations = new DoubleArrayList(m_NumMCSamples);

    long start = System.currentTimeMillis();
    for (int i = 0; i < m_NumMCSamples; i++) {
      IdentityGenerator.reset();
      m_Visitor.reset();

      //Generate coalescent genealogy
      m_Genealogy.generate();

      //Apply mutations traversing the tree
      UniParentalGene mrca = m_Genealogy.getMRCA();
      mrca.setCompoundSequence(m_MRCACompoundSequence.getCopy());
      UniParentalGene.traverse(mrca, m_Visitor);

      //analyze and store samples
      m_Analysis.addSample(m_Genealogy.getLeaves());
      m_Heights.add(m_Genealogy.getHeight());
      m_TBLs.add(m_Genealogy.getTotalBranchLength());
      m_Mutations.add(m_Visitor.getNumberOfMutations());

      //dispatch a mem clean up
      System.gc();
      //track progress
      if (i > 0 && i % 100 == 0) {
        System.out.print('#');
      }
    }
    long end = System.currentTimeMillis();
    m_SimTime = end - start;

    log.info("Finished Simulations in " + (m_SimTime / 1000) + " seconds.");
    double[] hs = StatsUtility.getStats(m_Heights);
    log.info("Genealogy Expected Height     = " + m_Genealogy.getExpectedHeight());
    log.info("Genealogy Expected Height Var = 1.159");
    log.info("Genealogy Height Mean     = " + hs[0]);
    log.info("Genealogy Height Variance = " + hs[1]);
    hs = StatsUtility.getStats(m_TBLs);
    log.info("Genealogy Expected TBL     = " + m_Genealogy.getExpectedTotalBranchLength());
    log.info("Genealogy Expected TBL Var = 6.579");
    log.info("Genealogy TBL Mean = " + hs[0]);
    log.info("Genealogy TBL Variance = " + hs[1]);
    hs = StatsUtility.getStats(m_Mutations);
    log.info("Number of Mutations Mean = " + hs[0]);
    log.info("Number of Mutations Var  = " + hs[1]);
    //log.info("Run Haplotypes");
    //PrintWriter pw = new PrintWriter(System.out);
    // HaplotypeFreqSet multilocus = m_Analysis.getRunHaplotypes(true);
    // HaplotypeFreqSet locus = m_Analysis.getRunHaplotypes(false);
/*
    pw.println();
    pw.println();
    pw.println("MULTILOCUS");
    pw.println(multilocus.sort().toLengthResumeString());

    pw.println();
    pw.println();
    pw.println("LOCUS");
    pw.println(locus.sort().toLengthResumeString());
    */
    //pw.flush();
  }//run


  public static void main(String[] args) throws Exception {
    org.apache.log4j.BasicConfigurator.configure();
    SimulationParameters params = new SimulationParameters(args);
    MicrosatelliteExperiment msim = new MicrosatelliteExperiment(params);
    msim.init();
    msim.run();
    System.out.println(msim.getCSVHeader());
    System.out.println(msim.resultsToCSV());
  }//main


}//class MicrosatelliteExperiment
