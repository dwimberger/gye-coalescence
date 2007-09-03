//@license@
package mx.unam.ecologia.gye.coalescence.app;

import forester.atv.ATVjframe;
import forester.tree.Tree;
import mx.unam.ecologia.gye.coalescence.model.CoalescentGenealogy;
import mx.unam.ecologia.gye.coalescence.model.ExponentialGrowthBranch;
import mx.unam.ecologia.gye.coalescence.model.UniParentalGene;
import mx.unam.ecologia.gye.coalescence.util.ExponentialGrowth;
import mx.unam.ecologia.gye.coalescence.util.SimulationParameters;

/**
 * Small class for displaying a coalescent
 * genealogy tree with a given number of genes using
 * ATV.
 * <p/>
 * This is a utility class with a main routine for showing
 * a sample tree driectly from commandline.
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class ShowTree {


  public static final void showTree(UniParentalGene MRCA)
      throws Exception {
    Tree tree = new Tree(MRCA.toNHXString());
    forester.atv.ATVjframe frame = new ATVjframe(tree);
    frame.showWhole();
  }//showTree

  public static void main(String[] args) {
    SimulationParameters params = new SimulationParameters(args);
    if (params.beta() > 0) {
      UniParentalGene.setBranchFactory(
          new ExponentialGrowthBranch(new ExponentialGrowth(params.beta())));
    }
    UniParentalGene mrca =
        CoalescentGenealogy.createUniParentalCoalescentGenealogy(params);
    try {
      showTree(mrca);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }//main


}//class ShowTree
