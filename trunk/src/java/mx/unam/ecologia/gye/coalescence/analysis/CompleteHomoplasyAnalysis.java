//@license@
package mx.unam.ecologia.gye.coalescence.analysis;

import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive;
import mx.unam.ecologia.gye.coalescence.model.Analysis;
import mx.unam.ecologia.gye.coalescence.model.UniParentalGene;
import mx.unam.ecologia.gye.model.CompoundSequence;
import mx.unam.ecologia.gye.util.CompoundSequenceLengthComparator;
import mx.unam.ecologia.gye.util.CompoundSequenceTotalLengthComparator;
import mx.unam.ecologia.gye.util.HaplotypeFreqSet;
import mx.unam.ecologia.gye.util.HomoplasySet;

import java.util.List;
import java.util.Locale;

/**
 * Provides a complete analysis that will yield homoplasy
 * related statistics.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class CompleteHomoplasyAnalysis implements Analysis {

  private DoubleArrayList m_N_H_Locus;
  private DoubleArrayList m_N_H_Multilocus;
  private DoubleArrayList m_N_H_IAM;

  private DoubleArrayList m_N_e_Locus;
  private DoubleArrayList m_N_e_Multilocus;
  private DoubleArrayList m_N_e_IAM;

  private DoubleArrayList m_He_Locus;
  private DoubleArrayList m_He_Multilocus;
  private DoubleArrayList m_He_IAM;

  private DoubleArrayList m_P_H_Locus;
  private DoubleArrayList m_P_H_Multilocus;
  private DoubleArrayList m_P_H_LL;

  private DoubleArrayList m_P_R_Locus;
  private DoubleArrayList m_P_R_Multilocus;
  private DoubleArrayList m_P_R_LL;

  //Estimates based on haplotype number
  private DoubleArrayList m_SH_NH;
  private DoubleArrayList m_MASH_NH;
  private DoubleArrayList m_SASH_NH;

  private HaplotypeFreqSet m_RunHaplotypesML =
      new HaplotypeFreqSet(new CompoundSequenceLengthComparator());
  private HaplotypeFreqSet m_RunHaplotypesL =
      new HaplotypeFreqSet(new CompoundSequenceTotalLengthComparator());


  public CompleteHomoplasyAnalysis(int numsamples) {
    //Number of haplotypes
    m_N_H_Locus = new DoubleArrayList(numsamples);
    m_N_H_Multilocus = new DoubleArrayList(numsamples);
    m_N_H_IAM = new DoubleArrayList(numsamples);

    //Effective number of haplotypes
    m_N_e_Locus = new DoubleArrayList(numsamples);
    m_N_e_Multilocus = new DoubleArrayList(numsamples);
    m_N_e_IAM = new DoubleArrayList(numsamples);

    //Heterocygosis
    m_He_Locus = new DoubleArrayList(numsamples);
    m_He_Multilocus = new DoubleArrayList(numsamples);
    m_He_IAM = new DoubleArrayList(numsamples);

    //Homoplasy by Heterocygosis 2 SMM against IAM
    m_P_H_Locus = new DoubleArrayList(numsamples);
    m_P_H_Multilocus = new DoubleArrayList(numsamples);
    m_P_H_LL = new DoubleArrayList(numsamples);

    //Homoplasy by counting 2 SMM against IAM
    m_P_R_Locus = new DoubleArrayList(numsamples);
    m_P_R_Multilocus = new DoubleArrayList(numsamples);
    m_P_R_LL = new DoubleArrayList(numsamples);

    //Homoplasies by numbers of haplotypes
    m_SH_NH = new DoubleArrayList(numsamples);
    m_MASH_NH = new DoubleArrayList(numsamples);
    m_SASH_NH = new DoubleArrayList(numsamples);

  }//Analysis

  /**
   * Returns 18 (*2 = 36) statistics of values
   * computed during the analysis.
   *
   * @return basic sample statistics of values computed during analsis.
   */
  public double[] getStats() {
    int i = 0;
    double[] stats = new double[38];
    double[] tempstats;

    //1. SMM (as locus)

    //Number of Haplotypes NH
    tempstats = getStats(m_N_H_Locus);
    stats[i++] = tempstats[0];  //0
    stats[i++] = tempstats[1];  //1

    //Effective Number of Haplotypes
    tempstats = getStats(m_N_e_Locus);
    stats[i++] = tempstats[0];  //2
    stats[i++] = tempstats[1];  //3

    //Heterocygosis
    tempstats = getStats(m_He_Locus);
    stats[i++] = tempstats[0];  //4
    stats[i++] = tempstats[1];  //5

    //2. SMM (as multilocus)

    //Number of Haplotypes NH
    tempstats = getStats(m_N_H_Multilocus);
    stats[i++] = tempstats[0];  //6
    stats[i++] = tempstats[1];  //7

    //Effective Number of Haplotypes
    tempstats = getStats(m_N_e_Multilocus);
    stats[i++] = tempstats[0];  //8
    stats[i++] = tempstats[1];  //9

    //Heterocygosis
    tempstats = getStats(m_He_Multilocus);
    stats[i++] = tempstats[0];  //10
    stats[i++] = tempstats[1];  //11

    //3. IAM (Identities)

    //Number of Haplotypes NH
    tempstats = getStats(m_N_H_IAM);
    stats[i++] = tempstats[0];  //12
    stats[i++] = tempstats[1];  //13

    //Effective Number of Haplotypes
    tempstats = getStats(m_N_e_IAM);
    stats[i++] = tempstats[0];  //14
    stats[i++] = tempstats[1];  //15

    //Heterocygosis
    tempstats = getStats(m_He_IAM);
    stats[i++] = tempstats[0];  //16
    stats[i++] = tempstats[1];  //17

    //4. Homoplasy

    //Locus
    tempstats = getStats(m_P_H_Locus);
    stats[i++] = tempstats[0];  //18
    stats[i++] = tempstats[1];  //19

    tempstats = getStats(m_P_R_Locus);
    stats[i++] = tempstats[0];  //20
    stats[i++] = tempstats[1];  //21

    //Multilocus
    tempstats = getStats(m_P_H_Multilocus);
    stats[i++] = tempstats[0];  //22
    stats[i++] = tempstats[1];  //23

    tempstats = getStats(m_P_R_Multilocus);
    stats[i++] = tempstats[0];  //24
    stats[i++] = tempstats[1];  //25

    //LL
    tempstats = getStats(m_P_H_LL);
    stats[i++] = tempstats[0];  //26
    stats[i++] = tempstats[1];  //27

    tempstats = getStats(m_P_R_LL);
    stats[i++] = tempstats[0];  //28
    stats[i++] = tempstats[1];  //29


    stats[i++] = HomoplasySet.getLFM();
    stats[i++] = HomoplasySet.getMFM();
    HomoplasySet.resetMF();

    //SH based on number of haplotypes
    tempstats = getStats(m_SH_NH);
    stats[i++] = tempstats[0];  //30
    stats[i++] = tempstats[1];  //31

    //MASH based on number of haplotypes
    tempstats = getStats(m_MASH_NH);
    stats[i++] = tempstats[0];  //32
    stats[i++] = tempstats[1];  //33

    //SASH based on number of haplotypes
    tempstats = getStats(m_SASH_NH);
    stats[i++] = tempstats[0];  //34
    stats[i++] = tempstats[1];  //35

    return stats;
  }//getStats

  public final void addSample(List leaves) {
    HomoplasySet hs = new HomoplasySet();

    for (int i = 0; i < leaves.size(); i++) {
      UniParentalGene upgene = (UniParentalGene) leaves.get(i);
      CompoundSequence h = upgene.getCompoundSequence();
      hs.add(h);
      //store run haplotypes
      m_RunHaplotypesL.add(h);
      m_RunHaplotypesML.add(h);
    }

    //calculate homocygosis homoplasy
    double[] params = hs.calculateHomocygosisHomoplasy();
    m_P_H_Locus.add(hs.getLocusHomoplasy());
    m_P_H_Multilocus.add(hs.getMultilocusHomoplasy());
    m_P_H_LL.add(hs.getLLHomoplasy());

    m_N_H_IAM.add(params[0]);
    m_N_e_IAM.add(params[1]);
    m_He_IAM.add(params[2]);


    m_N_H_Locus.add(params[3]);
    m_N_e_Locus.add(params[4]);
    m_He_Locus.add(params[5]);

    m_N_H_Multilocus.add(params[6]);
    m_N_e_Multilocus.add(params[7]);
    m_He_Multilocus.add(params[8]);

    //calculate haplotype number based homoplasy
    double n_size = params[3];
    double n_multilocus = params[6];
    double n_identity = params[0];

    m_SH_NH.add((n_identity - n_size) / n_identity);
    m_MASH_NH.add((n_multilocus - n_size) / n_multilocus);
    m_SASH_NH.add((n_identity - n_multilocus) / n_identity);

    //calculate counted homoplasy
    params = hs.calculateCountedHomoplasy();
    m_P_R_Locus.add(hs.getLocusHomoplasy());
    m_P_R_Multilocus.add(hs.getMultilocusHomoplasy());
    m_P_R_LL.add(hs.getLLHomoplasy());


  }//addSample

  public String statsToCSV() {
    double[] stats = getStats();
    StringBuilder sbuf = new StringBuilder();
    for (int i = 0; i < 38; i++) {
      //Kick Me :) If its another locale, it won't be .!
      sbuf.append(String.format(Locale.ENGLISH, "%.6f", stats[i]));
      if (i < 37) {
        sbuf.append(',');
      }
    }
    return sbuf.toString();
  }//statsToCSV

  public String getCSVHeader() {
    StringBuilder sbuf = new StringBuilder();
    for (int i = 0; i < 38; i++) {
      sbuf.append(FIELDS[i]);
      if (i < 37) {
        sbuf.append(',');
      }
    }
    return sbuf.toString();
  }//getCVSHeader

  private final double[] getStats(DoubleArrayList dal) {
    double[] stats = new double[2];
    stats[0] = Descriptive.mean(dal);
    stats[1] = Descriptive.sampleVariance(dal, stats[0]);
    return stats;
  }//getStats

  public HaplotypeFreqSet getRunHaplotypes(boolean ml) {
    if (ml) {
      return m_RunHaplotypesML;
    }
    return m_RunHaplotypesL;
  }//getRunHaplotypes

  private static final String[] FIELD_DESC = {
      "N_h Locus",
      "N_e Locus",
      "H_e Locus",
      "N_h Multilocus",
      "N_e Multilocus",
      "H_e Multilocus",
      "N_h Identity",
      "N_e Identity",
      "H_e Identity",
      "P_H Locus",
      "P_Counted Locus",
      "P_H Multilocus",
      "P_Counted Multilocus",
      "P_H LL",
      "P_Counted LL",
      "MFS Locus",
      "MFS Multilocus"
  };

  private static final String HDR =
      "NHL,NHLV,NEL,NELV,HEL,HELV,NHM,NHMV,NEM,NEMV,HEM," +
          "HEMV,NHI,NHIV,NEI,NEIV,HEI,HEIV,PHL,PHLV,PCL,PCLV," +
          "PHM,PHMV,PCM,PCMV,PHS,PHSV,PCS,PCSV,MFAL,MFAM," +
          "SHNH,SHNHV,MASHNH,MASHNHV,SASHNH,SASHNHV";//,MFL,MFLV,MFM,MFMV";

  private static final String[] FIELDS = HDR.split(",");

}//class CompleteHomoplasyAnalysis
