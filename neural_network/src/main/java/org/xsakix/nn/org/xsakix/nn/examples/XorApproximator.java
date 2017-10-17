package org.xsakix.nn.org.xsakix.nn.examples;

import cern.colt.list.DoubleArrayList;
import org.xsakix.nn.MultiLayerBackpropagationAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class XorApproximator {

    public static void main(String argv[]) {
        for (int num = 0; num < 10; num++) {
            MultiLayerBackpropagationAlgorithm mlp =
                    MultiLayerBackpropagationAlgorithm.createWithOnlyOneWayBackpropagation(100000);
            List<double[]> x = new ArrayList<>();
            x.add(new double[]{0.0, 0.0});
            x.add(new double[]{0.0, 1.0});
            x.add(new double[]{1.0, 0.0});
            x.add(new double[]{1.0, 1.0});

            double t[] = new double[]{1.0, 0.0, 0.0, 1.0};
            int[] skryte_vrstvy = {14};
            List<List<DoubleArrayList>> w = mlp.init_weights(2, skryte_vrstvy, 1);
            List<List<MultiLayerBackpropagationAlgorithm.Node>> nodes = mlp.init_nodes(2, skryte_vrstvy, 1);
            double alfa = 0.01;

            DoubleArrayList global_error = mlp.train_network(x, w, t, nodes, alfa);
            DoubleArrayList result = mlp.compute_network(x, w, nodes);
            mlp.println_results(t, result);
        }

//        Plot2DPanel plot = new Plot2DPanel();
//
//
//
//        // add a line plot to the PlotPanel
//        plot.addLinePlot("my plot", global_error.elements());
//
//        // put the PlotPanel in a JFrame, as a JPanel
//        JFrame frame = new JFrame("a plot panel");
//        frame.setContentPane(plot);
//        frame.setVisible(true);
    }

}
