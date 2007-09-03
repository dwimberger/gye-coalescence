//@license@
package mx.unam.ecologia.gye.coalescence.app;

import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.decorators.EdgeStringer;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.graph.impl.SparseGraph;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.visualization.ISOMLayout;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import mx.unam.ecologia.gye.coalescence.model.CoalescentGenealogy;
import mx.unam.ecologia.gye.coalescence.model.UniParentalGene;
import mx.unam.ecologia.gye.coalescence.util.SimulationParameters;
import mx.unam.ecologia.gye.coalescence.visitors.MicrosatelliteMutationVisitor;
import mx.unam.ecologia.gye.model.CompoundSequence;
import mx.unam.ecologia.gye.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;


/**
 * Evaluates ancestry in the haplotype sets.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class EvaluateAncestry {

  private static final Log log = LogFactory.getLog(EvaluateAncestry.class);

  protected static final void process(CompoundSequence mrca, List leaves) {
    try {

      PrintWriter pw = new PrintWriter(System.out);
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
      }

      //Identity
      pw.println("IDENTITY");
      ident.sort(new CSAncestryComparator());
      CompoundSequence cs = ident.get(0);
      pw.println("MA: " + ident.contains(cs));
      pw.println("MF: " + (ident.findMostFrequent() + 1));
      pw.println(ident.toFullString());


      pw.println();
      pw.println();
      pw.println("MULTILOCUS");
      pw.println("MA: " + len1.contains(cs));
      pw.println("MF: " + (len1.findMostFrequent() + 1));
      pw.println(len1.toResumeString());

      pw.println();
      pw.println();
      pw.println("LOCUS");
      cs.setLocus(true);
      pw.println("MA: " + len2.contains(cs));
      pw.println("MF: " + (len2.findMostFrequent() + 1));
      pw.println(len2.toResumeString());
      pw.flush();


      SparseGraph sg = new SparseGraph();

      List<CompoundSequence> ht = len1.getHaplotypes();
      //add vertices
      List<SparseVertex> vertices = new ArrayList<SparseVertex>(ht.size());

      for (Iterator<CompoundSequence> iter = ht.iterator(); iter.hasNext();) {
        CompoundSequence cseq = iter.next();
        SparseVertex sv = new SparseVertex();
        sv.addUserDatum("seq", cseq, UserData.SHARED);
        sv.addUserDatum("freq", len1.getFrequency(cseq), UserData.SHARED);
        if (cseq.equals(cs)) {
          sv.addUserDatum("mf", true, UserData.SHARED);
        }
        sg.addVertex(sv);
        vertices.add(sv);
      }
      //add edges
      for (int i = 0; i < ht.size(); i++) {
        for (int j = 0; j <= i; j++) {
          double d = DistanceCalculator.calculateMultilocusLengthDistance(ht.get(i), ht.get(j));
          UndirectedSparseEdge ue = new UndirectedSparseEdge(vertices.get(i), vertices.get(j));
          ue.setUserDatum(EDGE_WEIGHT_KEY, new Double(d), UserData.SHARED);
          if (d != 0) {
            sg.addEdge(ue);
          }
        }
      }

      //System.out.println(sg.toString());
      //System.out.println(GraphMatrixOperations.graphToSparseMatrix(sg,EDGE_WEIGHT_KEY));
      visualizeGraph(sg);

      //Prim
      SparseGraph nsg = new SparseGraph();
      FibonacciHeap<Vertex, Double> q = new FibonacciHeap<Vertex, Double>();
      Map<Vertex, Vertex> pi = new HashMap<Vertex, Vertex>((int) (ht.size() * 1.3));
      //System.out.println("Have structures");
      Vertex r = null;
      for (SparseVertex u : vertices) {
        q.add(u, Double.MAX_VALUE);
        u.copy(nsg);
        if (r == null) {
          r = u;
        } else {
          //start from max freq
          r = (((Integer) r.getUserDatum("freq")).compareTo((Integer) u.getUserDatum("freq")) > 0) ? r : u;
        }
      }
      q.decreaseKey(r, 0d);
      //System.out.println("initialized starting loop");
      do {
        Vertex u = (Vertex) q.popMin();
        Set<Vertex> s = u.getNeighbors();
        for (Vertex v : s) {
          Edge e = u.findEdge(v);
          double w = (Double) e.getUserDatum(EDGE_WEIGHT_KEY);
          if (q.contains(v) && w < q.getPriority(v)) {
            pi.put(v, u);
            q.decreaseKey(v, w);
          }
        }
      } while (q.size() > 0);

      //put edges

      for (Map.Entry<Vertex, Vertex> entry : pi.entrySet()) {
        Vertex v = entry.getKey();
        Vertex u = entry.getValue();
        u.findEdge(v).copy(nsg);
      }

      /*
      for(SparseVertex sv:vertices) {
        //edges
        Set s =  sv.getIncidentEdges();
        Edge[] edges = (Edge[])s.toArray(new Edge[s.size()]);
        //sort
        Arrays.sort(edges,new Comparator<Edge>() {
          public int compare(Edge o1, Edge o2) {
            return ((Double)o2.getUserDatum(EDGE_WEIGHT_KEY)).compareTo((Double)o1.getUserDatum(EDGE_WEIGHT_KEY));
          }
        });
        System.out.println(Arrays.toString(edges));
        //just leave the edge with the lowest weight
        for(Edge e:edges) {
          if(sv.degree() > 1 && e.getOpposite(sv).degree() > 1) {
            sg.removeEdge(e);
            System.out.println("Removing " + e.toString() + "::w="+e.getUserDatum(EDGE_WEIGHT_KEY));
          }
        }

        System.out.println(sv.toString());
      }
       */
      visualizeGraph(nsg);
    } catch (Exception ex) {
      log.error("process()", ex);
    }

  }//process


  public double calculateDistance(CompoundSequence cs1, CompoundSequence cs2) {
    return DistanceCalculator.calculateMultilocusLengthDistance(cs1, cs2);
  }//calculateDistance


  public static void main(String[] args) throws Exception {
    String fname = "sample_config.txt";
    BasicConfigurator.configure();
    for (int i = 0; i < args.length; i++) {
      if (args[i].startsWith("-output=")) {
        fname = args[i].substring(8);
      }
    }
    SimulationParameters params = new SimulationParameters(args);

    log.info(params.toString());
    CoalescentGenealogy genea = new CoalescentGenealogy(params);
    MicrosatelliteMutationVisitor vis = new MicrosatelliteMutationVisitor(params);
    log.info("Generating Genealogy");
    genea.generate();
    log.info("Applying mutations");
    genea.getMRCA().setCompoundSequence(params.CompoundSequence());
    UniParentalGene.traverse(genea.getMRCA(), vis);
    log.info("Evaluating Ancestry");
    List leaves = genea.getLeaves();
    process(genea.getMRCA().getCompoundSequence(), leaves);
    log.info("Done.");
  }//main

  private static void visualizeGraph(Graph g) {
    JFrame jf = new JFrame();
    PluggableRenderer r = new PluggableRenderer();
    ISOMLayout sl = new ISOMLayout(g);
    //SpringLayout sl = new SpringLayout(g);
    //sl.setRepulsionRange(1000);
    //sl.setStretch(0.50);
    VertexStringer vs = new VertexStringer() {
      public String getLabel(ArchetypeVertex v) {
        StringBuilder sb = new StringBuilder();
        sb.append(v.getUserDatum("freq").toString());
        sb.append(":");
        sb.append(((CompoundSequence) v.getUserDatum("seq")).toShortString());
        return sb.toString();
      }
    };
    /*
    r.setVertexIconFunction(new VertexPaintFunction(){
      public Paint getFill(Vertex vertex) {
        if(vertex.containsUserDatumKey("mf")) {
          return new Stroke() {}
        }

      }
    });
    */
    VertexPaintFunction vpf = r.getVertexPaintFunction();
    r.setVertexPaintFunction(new PaintFunctionWrapper(vpf));
    r.setVertexStringer(vs);
    EdgeStringer es = new EdgeStringer() {
      public String getLabel(ArchetypeEdge e) {
        return e.getUserDatum(EDGE_WEIGHT_KEY).toString();
      }
    };

    r.setEdgeStringer(es);

    VisualizationViewer vv = new VisualizationViewer(
        sl,
        r
    );
    jf.getContentPane().add(vv);
    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jf.pack();
    jf.setVisible(true);
  }//visualizeGraph


  private static class PaintFunctionWrapper
      implements VertexPaintFunction {

    VertexPaintFunction m_VPF;
    Paint m_Ancestral;

    public PaintFunctionWrapper(VertexPaintFunction vpf) {
      m_VPF = vpf;
      m_Ancestral = Color.BLUE;
    }//PaintFunctionWrapper

    public Paint getFillPaint(Vertex vertex) {
      if (vertex.containsUserDatumKey("mf")) {
        return m_Ancestral;
      }
      return m_VPF.getFillPaint(vertex);
    }//getFillPaint

    public Paint getDrawPaint(Vertex vertex) {
      return m_VPF.getDrawPaint(vertex);
    }//getDrawPaint

  }//PaintFunctionWrapper

  private static final String EDGE_WEIGHT_KEY = "weight";
}//class EvaluateAncestry
