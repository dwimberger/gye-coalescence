//@license@
package mx.unam.ecologia.gye.coalescence.app;

import mx.unam.ecologia.gye.coalescence.model.CoalescentGenealogy;
import mx.unam.ecologia.gye.coalescence.model.UniParentalGene;
import mx.unam.ecologia.gye.coalescence.util.SimulationParameters;
import mx.unam.ecologia.gye.coalescence.visitors.MicrosatelliteMutationVisitor;
import mx.unam.ecologia.gye.model.CompoundSequence;
import mx.unam.ecologia.gye.model.Sequence;
import mx.unam.ecologia.gye.util.CompoundSequenceIdentityComparator;
import mx.unam.ecologia.gye.util.CompoundSequenceLengthComparator;
import mx.unam.ecologia.gye.util.HaplotypeFreqSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Provides a small application for generating Micsat input
 * files.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class CreateSampleConfiguration {

  private static final Log log = LogFactory.getLog(CreateSampleConfiguration.class);

  protected static final void writeSampleConfig(List leaves, String fname) {
    try {

      FileOutputStream fout = new FileOutputStream(fname);
      PrintWriter pw = new PrintWriter(fout);
      HaplotypeFreqSet len1 = new HaplotypeFreqSet(new CompoundSequenceLengthComparator());
      HaplotypeFreqSet len2 = new HaplotypeFreqSet(new CompoundSequenceLengthComparator());
      HaplotypeFreqSet ident = new HaplotypeFreqSet(new CompoundSequenceIdentityComparator());

      for (int i = 0; i < leaves.size(); i++) {
        UniParentalGene upgene = (UniParentalGene) leaves.get(i);
        CompoundSequence h = upgene.getCompoundSequence();
        len1.add(h);
        ident.add(h);
        CompoundSequence cs = h.getCopy();
        cs.setLocus(true);
        len2.add(cs);

        int numloc = h.getSequenceCount();
        for (int n = 0; n < numloc; n++) {
          Sequence s = h.get(n);
          pw.print(s.toFullString());
          pw.print(" ");
        }
        pw.println();
      }
      //print length as locus
      pw.println();
      pw.println("Haplotypes by Length (locus)");
      pw.println(len2.toString());
      pw.println("Haplotypes by Length (multilocus)");
      pw.println(len1.toString());
      pw.println("Haplotypes by Identity");
      pw.println(ident.toFullString());
      pw.println();

      //System.out.println("TEST");
      //ident.sort(new CSAncestryComparator());
      //System.out.println(ident.toFullString());
      //System.out.println(ident.findMostFrequent() + 1);


      pw.flush();
      pw.close();
      fout.close();
    } catch (IOException ex) {
      CreateSampleConfiguration.log.error("writeSampleConfig()", ex);
    }

  }//writeMicsat

  public static void main(String[] args) throws Exception {
    String fname = "sample_config.txt";
    BasicConfigurator.configure();
    for (int i = 0; i < args.length; i++) {
      if (args[i].startsWith("-output=")) {
        fname = args[i].substring(8);
      }
    }
    SimulationParameters params = new SimulationParameters(args);

    CreateSampleConfiguration.log.info(params.toString());
    CoalescentGenealogy genea = new CoalescentGenealogy(params);
    MicrosatelliteMutationVisitor vis = new MicrosatelliteMutationVisitor(params);
    CreateSampleConfiguration.log.info("Generating Genealogy");
    genea.generate();
    CreateSampleConfiguration.log.info("Applying mutations");
    genea.getMRCA().setCompoundSequence(params.CompoundSequence());
    UniParentalGene.traverse(genea.getMRCA(), vis);
    CreateSampleConfiguration.log.info("Writing Sample Configuration file");
    List leaves = genea.getLeaves();
    CreateSampleConfiguration.writeSampleConfig(leaves, fname);
    CreateSampleConfiguration.log.info("Done.");
  }//main

}//class MicrosatelliteSimulation
