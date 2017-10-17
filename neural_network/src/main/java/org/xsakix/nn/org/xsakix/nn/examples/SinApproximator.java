package org.xsakix.nn.org.xsakix.nn.examples;

import cern.colt.list.DoubleArrayList;
import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import org.xsakix.nn.MultiLayerBackpropagationAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class SinApproximator {

    public static void main(String argv[]) {
        for (int i = 0; i < 10; i++)
            run_experiment();
    }

    private static void run_experiment() {
        MultiLayerBackpropagationAlgorithm mlp =
                MultiLayerBackpropagationAlgorithm.create(100000);
        List<double[]> x = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            x.add(new double[]{Uniform.staticNextDoubleFromTo(0.0, 1.0)});
        }
        DoubleArrayList tt = new DoubleArrayList();
        for (int i = 0; i < 10; i++) {
            tt.add(Math.sin(2 * Math.PI * x.get(i)[0]) + Normal.staticNextDouble(-0.3, 0.3));
        }


        double t[] = tt.elements();

        int[] skryte_vrstvy = {4};
        int num_in = 1;
        int num_out = 1;

        List<List<DoubleArrayList>> w = mlp.init_weights(num_in, skryte_vrstvy, num_out);
        List<List<MultiLayerBackpropagationAlgorithm.Node>> nodes = mlp.init_nodes(num_in, skryte_vrstvy, num_out);
        double alfa = 0.1;

        DoubleArrayList global_error = mlp.train_network(x, w, t, nodes, alfa);
        DoubleArrayList result = mlp.compute_network(x, w, nodes);
        //mlp.println_results(t,result);
    }

}
