//@license@
package mx.unam.ecologia.gye.coalescence.util;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.list.DoubleArrayList;
import cern.colt.list.IntArrayList;
import mx.unam.ecologia.gye.model.CompoundSequence;
import mx.unam.ecologia.gye.model.SequenceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Class providing a container for simulation parameters.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class SimulationParameters {

  private static final Log log = LogFactory.getLog(SimulationParameters.class);

  private int m_K = -1;
  private int m_N = -1;
  private int m_Samples = 2000;
  private double m_U = -1;
  private boolean m_Haploid = true;
  private double m_Theta = -1;
  private double m_Beta = -1;
  private double m_P = 0.5;
  private int m_DeleteFloor = 2;
  private CompoundSequence m_CompoundSequence;
  private boolean m_Locus = false;
  private boolean m_Store = false;
  private IntArrayList m_KSeries;
  private IntArrayList m_NSeries;
  private DoubleArrayList m_USeries;
  private DoubleArrayList m_BetaSeries;
  private String m_Output;
  private DoubleDoubleFunction m_GrowthFunction;

  public SimulationParameters(String[] args) {
    parseFrom(args);
  }//constructor

  public int k() {
    return m_K;
  }//k

  public int getKCount() {
    if (m_KSeries == null) {
      return 1;
    } else {
      return m_KSeries.size();
    }
  }//getKCount

  public void selectK(int i) {
    if (m_KSeries != null) {
      m_K = m_KSeries.getQuick(i);
    }
  }//selectK

  public double u() {
    return m_U;
  }//u

  public int getUCount() {
    if (m_USeries == null) {
      return 1;
    } else {
      return m_USeries.size();
    }
  }//getUCount

  public void selectU(int i) {
    if (m_USeries != null) {
      m_U = m_USeries.getQuick(i);
      //update Theta
      calculateTheta();
    }
  }//selectU

  public int N() {
    return m_N;
  }//N

  public int getNCount() {
    if (m_NSeries == null) {
      return 1;
    } else {
      return m_NSeries.size();
    }
  }//getNCount

  public void selectN(int i) {
    if (m_NSeries != null) {
      m_N = m_NSeries.getQuick(i);
      //update Theta
      calculateTheta();
    }
  }//selectN

  public double p() {
    return m_P;
  }//p


  public int samples() {
    return m_Samples;
  }//samples

  public double theta() {
    return m_Theta;
  }//theta

  public double beta() {
    return m_Beta;
  }//beta

  public boolean isDynamicN() {
    return m_Beta != -1;
  }//isDynamicN


  public static Log getLog() {
    return log;
  }

  public DoubleDoubleFunction getGrowthFunction() {
    return m_GrowthFunction;
  }//getGrowthFunction

  public void selectBeta(int i) {
    if (m_BetaSeries != null) {
      m_Beta = m_BetaSeries.getQuick(i);
      m_GrowthFunction = new ExponentialGrowth(m_Beta);
    }
  }//selectBeta

  public int getBetaCount() {
    if (m_BetaSeries == null) {
      return 1;
    } else {
      return m_BetaSeries.size();
    }
  }//getBetaCount

  public int deleteFloor() {
    return m_DeleteFloor;
  }//deleteFloor

  public boolean isLocus() {
    return m_Locus;
  }//isLocus

  public boolean isStore() {
    return m_Store;
  }//isStore

  public boolean isHaploid() {
    return m_Haploid;
  }//isHaploid

  public String getOutput() {
    return m_Output;
  }//getOutput

  public CompoundSequence CompoundSequence() {
    return m_CompoundSequence;
  }//CompoundSequence

  private void parseFrom(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].startsWith("-k=")) {
        try {
          String[] kas = args[i].substring(3).split(",");
          if (kas.length == 1) {
            m_K = Integer.parseInt(args[i].substring(3));
            //log.info("k=" + m_K);
          } else {
            m_KSeries = new IntArrayList(kas.length);
            for (String k : kas) {
              m_KSeries.add(Integer.parseInt(k));
            }
            //Sort it
            m_KSeries.trimToSize();
            m_KSeries.sort();
            m_K = m_KSeries.getQuick(0);
          }
        } catch (Exception ex) {
          log.error("k values need to be integers.");
        }
      } else if (args[i].startsWith("-N=")) {
        try {
          String[] enes = args[i].substring(3).split(",");
          if (enes.length == 1) {
            m_N = Integer.parseInt(args[i].substring(3));
          } else {
            m_NSeries = new IntArrayList(enes.length);
            for (String n : enes) {
              m_NSeries.add(Integer.parseInt(n));
            }
            //Sort it
            m_NSeries.trimToSize();
            m_NSeries.sort();
            m_N = m_NSeries.getQuick(0);
          }
          //log.info("N=" + m_N);
        } catch (Exception ex) {
          log.error("N values need to be an integers.");
        }
      } else if (args[i].startsWith("-u=")) {
        try {
          String[] us = args[i].substring(3).split(",");
          if (us.length == 1) {
            m_U = Double.parseDouble(args[i].substring(3));
          } else {
            m_USeries = new DoubleArrayList(us.length);
            for (String u : us) {
              m_USeries.add(Double.parseDouble(u));
            }
            m_USeries.trimToSize();
            m_USeries.sort();
            m_U = m_USeries.getQuick(0);
          }
          //log.info("u=" + m_U);
        } catch (Exception ex) {
          log.error("u values need to be double precision floating points.");
        }
      } else if (args[i].startsWith("-s=")) {
        try {
          m_Samples = Integer.parseInt(args[i].substring(3));
          //log.info("s=" + m_Samples);
        } catch (Exception ex) {
          log.error("s needs to be an integer value.");
        }
      } else if (args[i].startsWith("-delfloor=")) {
        try {
          m_DeleteFloor = Integer.parseInt(args[i].substring(10));
          if (m_DeleteFloor < 2) {
            m_DeleteFloor = 2;
          }
          //log.info("s=" + m_Samples);
        } catch (Exception ex) {
          log.error("delfloor needs to be an integer value.");
        }
      } else if (args[i].startsWith("-p=")) {
        try {
          m_P = Double.parseDouble(args[i].substring(3));
          if (m_P < 0.1 | m_P > 0.9) {
            m_P = 0.5;
          }
        } catch (Exception ex) {
          log.error("p needs to be a double value.");
        }
      } else if (args[i].startsWith("-beta=")) {
        try {
          String[] betas = args[i].substring(6).split(",");
          if (betas.length == 1) {
            m_Beta = Double.parseDouble(args[i].substring(6));
            m_GrowthFunction = new ExponentialGrowth(m_Beta);
          } else {
            m_BetaSeries = new DoubleArrayList(betas.length);
            for (String b : betas) {
              m_BetaSeries.add(Double.parseDouble(b));
            }
            m_BetaSeries.trimToSize();
            m_BetaSeries.sort();
            m_Beta = m_BetaSeries.getQuick(0);
            m_GrowthFunction = new ExponentialGrowth(m_Beta);
          }
        } catch (Exception ex) {
          log.error("beta values need to be double precision floating points.");
        }
      } else if (args[i].startsWith("-output=")) {
        m_Output = args[i].substring(8);
      } else if (args[i].startsWith("-mrca=")) {
        m_CompoundSequence = SequenceFactory.createCompoundSequence(args[i].substring(6));
      } else if (args[i].equals("-diploid")) {
        m_Haploid = false;
      } else if (args[i].equals("-locus")) {
        m_Locus = true;
      } else if (args[i].equals("-store")) {
        m_Store = true;
      }
    }

    if (m_U != -1 && m_N != -1) {
      calculateTheta();
    }
  }//args

  public String toString() {
    final StringBuilder sbuf = new StringBuilder("SimulationParameters {");
    if (m_K > 0) {
      sbuf.append("k=").append(m_K).append(',');
    }
    if (m_N > 0) {
      sbuf.append("N=").append(m_N).append(',');
    }
    if (m_U > 0) {
      sbuf.append("u=").append(m_U).append(',');
    }
    if (m_Theta > 0) {
      sbuf.append("theta=").append(m_Theta).append(',');
    }
    sbuf.append("p=").append(m_P).append(',');
    if (m_Samples > 0) {
      sbuf.append("#MC Samples=").append(m_Samples).append(',');
    }
    if (m_DeleteFloor > 0) {
      sbuf.append("delfloor=").append(m_DeleteFloor).append(',');
    }
    if (m_CompoundSequence != null) {
      sbuf.append("mrca=").append(m_CompoundSequence.toString()).append(',');
    }
    if (m_Haploid) {
      sbuf.append("haploid");
    } else {
      sbuf.append("diploid");
    }
    sbuf.append(',');
    if (m_Locus) {
      sbuf.append("locus");
    } else {
      sbuf.append("multilocus");
    }
    sbuf.append(',');
    if (m_Store) {
      sbuf.append("storing}");
    } else {
      sbuf.append("in-memory}");
    }
    return sbuf.toString();
  }//toString

  private void calculateTheta() {
    if (m_Haploid) {
      m_Theta = 2 * m_U * m_N;
    } else {
      m_Theta = 4 * m_U * m_N;
    }
  }//calculateTheta

}//class SimulationParameters
