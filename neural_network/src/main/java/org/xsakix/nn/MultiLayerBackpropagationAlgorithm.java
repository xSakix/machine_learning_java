package org.xsakix.nn;

import cern.colt.list.DoubleArrayList;
import cern.jet.random.Uniform;

import java.util.ArrayList;
import java.util.List;

public class MultiLayerBackpropagationAlgorithm {

    private int max_iterations = 1000;
    private boolean isOneWayBackprop = false;

    private MultiLayerBackpropagationAlgorithm(int max_iterations) {
        this.max_iterations = max_iterations;
    }

    private MultiLayerBackpropagationAlgorithm(int max_iterations, boolean isOneWayBackprop) {
        this.max_iterations = max_iterations;
        this.isOneWayBackprop = isOneWayBackprop;
    }

    public static MultiLayerBackpropagationAlgorithm create(int max_iterations) {
        return new MultiLayerBackpropagationAlgorithm(max_iterations);
    }

    public static MultiLayerBackpropagationAlgorithm createWithOnlyOneWayBackpropagation(int max_iterations) {
        return new MultiLayerBackpropagationAlgorithm(max_iterations, true);
    }

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    private boolean is_input_layer(int layer) {
        return layer == 0;
    }

    private void compute_forward_input_nodes(double x[], List<List<Node>> nodes, int layer, int output_node) {
        nodes.get(layer).get(output_node).out = x[output_node];
    }

    private void compute_output_layer(List<List<Node>> nodes, int layer, int output_node, List<List<DoubleArrayList>> w) {
        double sum = 0.0;
        int prev_layer = nodes.get(layer - 1).size();
        for (int input_node = 0; input_node < prev_layer; input_node++) {
            double weight = w.get(layer).get(output_node).elements()[input_node];
            double input = nodes.get(layer - 1).get(input_node).out;
            sum += weight * input;
            //print_summing_line_info(layer, output_node, input_node)
        }
        nodes.get(layer).get(output_node).out = sigmoid(sum);
        //print_ouput_line_info(layer, output_node, nodes)
    }

    private void compute_layer(List<List<Node>> nodes, int layer, double x[], List<List<DoubleArrayList>> w) {
        //System.out.println("-------Layer:" + layer + "---------------")
        int current_layer = nodes.get(layer).size();
        for (int output_node = 0; output_node < current_layer; output_node++) {
            if (is_input_layer(layer)) {
                compute_forward_input_nodes(x, nodes, layer, output_node);
            } else {
                compute_output_layer(nodes, layer, output_node, w);
            }
        }
    }

    private void forward(double x[], List<List<DoubleArrayList>> w, List<List<Node>> nodes) {
        int layers = nodes.size();
        for (int layer = 0; layer < layers; layer++) {
            compute_layer(nodes, layer, x, w);
        }
    }

    void print_summing_line_info(int layer, int output_node_index, int input_node_index) {
        System.out.println(String.join("",
                "Summing:w[" + layer + "]",
                "[" + output_node_index + "]",
                "[" + input_node_index + "]",
                "*N[" + (layer - 1) + "][" + input_node_index + "].out"));
    }

    void print_output_line_info(int layer, int output_node_index, List<List<Node>> nodes) {

        System.out.println(String.join("",
                "N[" + layer + "][" + output_node_index + "].out=",
                String.valueOf(nodes.get(layer).get(output_node_index).out)));
    }

    void print_backward_base_error_info(int layer, int output_node_index) {
        System.out.println(String.join("",
                "p1=N[" + layer + "][" + output_node_index + "].out",
                "*(1-N[" + layer + "][" + output_node_index + "].out)"));
    }

    void print_backward_error_output_layer(int layer, int output_node_index) {
        System.out.println(String.join("",
                "N[" + layer + "]",
                "[" + output_node_index + "].error",
                "= p1*(t-", "N[" + layer + "]",
                "[" + output_node_index + "].out)"));
    }

    void println_backward_error_sum(int layer, int input_node, int output_node) {
        System.out.println(String.join("",
                "sum += w[" + layer + 1 + "]",
                "[" + input_node + "]",
                "[" + output_node + "]",
                "*N[" + layer + 1 + "]",
                "[" + input_node + "].error"));
    }

    void println_backward_error_hidden_layer(int layer, int output_node) {

        System.out.println("N[" + layer + "][" + output_node + "].error = p1*sum");
    }

    private void compute_error_output_layer(List<List<Node>> nodes, int layer, int output_node, double p1, double t, double y) {
        nodes.get(layer).get(output_node).error = p1 * (t - y);
        //println_backward_error_output_layer(layer, output_node)
    }

    private void compute_error_hidden_layer(List<List<Node>> nodes, int layer, int node, List<List<DoubleArrayList>> w, double p1) {
        double sum = 0.0;
        int output_node = 0;
        for (int input_node = 0; input_node < nodes.get(layer + 1).size(); input_node++) {
            for (output_node = 0; output_node < nodes.get(layer).size(); output_node++) {
                double wkh = w.get(layer + 1).get(input_node).elements()[output_node];
                double deltah = nodes.get(layer + 1).get(input_node).error;
                sum += wkh * deltah;
                //println_backward_error_sum(layer, input_node, output_node);
            }
        }
        if (isOneWayBackprop) {
            nodes.get(layer).get(output_node - 1).error = p1 * sum;
        } else {
            //this is the proper way, but gives worser results !
            //gives better results ??why ?
            nodes.get(layer).get(node).error = p1 * sum;
        }
        //println_backward_error_hidden_layer(layer, node)
    }

    private void backward(List<List<Node>> nodes, double t, List<List<DoubleArrayList>> w) {
        int number_of_layers = nodes.size();
        for (int layer = number_of_layers - 1; layer > 0; layer--) {
            //System.out.println("-------Layer backtracking:"+layer)+"---------------")
            int number_of_nodes_current_layer = nodes.get(layer).size();
            for (int node = 0; node < number_of_nodes_current_layer; node++) {
                double y = nodes.get(layer).get(node).out;
                double p1 = y * (1 - y);
                //println_backward_base_error_info(layer, node);
                if (layer == nodes.size() - 1) {
                    compute_error_output_layer(nodes, layer, node, p1, t, y);
                } else {
                    compute_error_hidden_layer(nodes, layer, node, w, p1);
                }
            }
        }
    }

    void println_delta_weight_info(int layer, int output_node, int input_node) {

        System.out.println(String.join("",
                "delta_w = ",
                "alfa*N[" + layer + "][" + output_node + "].error",
                "*N[" + (layer - 1) + "][" + input_node + "].out"));
    }

    void println_weight_recompute_info(int layer, int output_node, int input_node) {

        System.out.println(String.join("",
                "w[" + layer + "]",
                "[" + output_node + "]",
                "[" + input_node + "]",
                "+= delta_w"));
    }

    private void compute_weight(List<List<Node>> nodes, int layer, int output_node, int input_node, List<List<DoubleArrayList>> w, double alfa) {
        double delta_h = nodes.get(layer).get(output_node).error;
        double y = nodes.get(layer - 1).get(input_node).out;
        double delta_w = alfa * delta_h * y;
        w.get(layer).get(output_node).elements()[input_node] += delta_w;
    }

    private void recompute_weights(List<List<Node>> nodes, List<List<DoubleArrayList>> w, double alfa) {
        for (int layer = 0; layer < nodes.size(); layer++) {
            //System.out.println("-------Layer recomputing weights:"+layer+"------------");
            if (layer == 0)
                continue;
            for (int output_node = 0; output_node < nodes.get(layer).size(); output_node++) {
                for (int input_node = 0; input_node < nodes.get(layer - 1).size(); input_node++) {
                    compute_weight(nodes, layer, output_node, input_node, w, alfa);
                    //println_delta_weight_info(layer, output_node, input_node)
                    //println_weight_recompute_info(layer, output_node, input_node)
                }
            }
        }
    }

    private void init_input_weights(List<List<DoubleArrayList>> w) {
        w.add(0, new ArrayList<>(1));
        w.get(0).add(new DoubleArrayList());
    }

    private void init_hidden_weights(List<List<DoubleArrayList>> w, int num_in, int list_num_hid[], double w_min, double w_max) {
        //w[layer][node][len(input)]

        int num = num_in;
        for (int k_hid = 0; k_hid < list_num_hid.length; k_hid++) {
            List<DoubleArrayList> w_hidd = new ArrayList<>();
            for (int k = 0; k < list_num_hid[k_hid]; k++) {
                DoubleArrayList w_hid = new DoubleArrayList(num);
                for (int j = 0; j < num; j++) {
                    //w[0] = input layer
                    w_hid.add(Uniform.staticNextDoubleFromTo(w_min, w_max));
                }
                w_hidd.add(w_hid);
            }
            num = list_num_hid[k_hid];
            w.add(w_hidd);
        }
    }

    private void init_output_weights(List<List<DoubleArrayList>> w, int num_out, int num_in, double w_min, double w_max) {
        List<DoubleArrayList> w_out = new ArrayList<>();
        for (int k = 0; k < num_out; k++) {
            DoubleArrayList w_out_part = new DoubleArrayList();
            for (int j = 0; j < num_in; j++) {
                w_out_part.add(Uniform.staticNextDoubleFromTo(w_min, w_max));
            }
            w_out.add(w_out_part);
        }
        w.add(w_out);
    }

    public List<List<DoubleArrayList>> init_weights(int num_in, int list_num_hid[], int num_out) {
        double w_min = 0.0;
        double w_max = 1.0;
        List<List<DoubleArrayList>> w = new ArrayList<>(list_num_hid.length + 2);

        init_input_weights(w);

        init_hidden_weights(w, num_in, list_num_hid, w_min, w_max);

        init_output_weights(w, num_out, list_num_hid[list_num_hid.length - 1], w_min, w_max);

        return w;
    }

    private void init_input_nodes(List<List<Node>> nodes, int num_in) {
        List<Node> nodes_in = new ArrayList<>(num_in);
        for (int i = 0; i < num_in; i++) {
            nodes_in.add(new Node());
        }
        nodes.add(nodes_in);
    }

    private void init_hidden_nodes(List<List<Node>> nodes, int list_num_hid[]) {
        for (int aList_num_hid : list_num_hid) {
            List<Node> nodes_hidden = new ArrayList<>(aList_num_hid);
            for (int i = 0; i < aList_num_hid; i++) {
                nodes_hidden.add(new Node());
            }

            nodes.add(nodes_hidden);
        }
    }

    private void init_output_nodes(List<List<Node>> nodes, int num_out) {
        List<Node> nodes_out = new ArrayList<>(num_out);
        for (int i = 0; i < num_out; i++) {
            nodes_out.add(new Node());
        }
        nodes.add(nodes_out);
    }

    public List<List<Node>> init_nodes(int num_in, int list_num_hid[], int num_out) {
        List<List<Node>> nodes = new ArrayList<>(list_num_hid.length);

        init_input_nodes(nodes, num_in);

        init_hidden_nodes(nodes, list_num_hid);

        init_output_nodes(nodes, num_out);

        return nodes;
    }

    private boolean global_error_rises(DoubleArrayList global_error) {
        int len = global_error.size();
        return global_error.size() > 2 && global_error.elements()[len - 2] < global_error.elements()[len - 1];
    }

    private boolean stop_condition(DoubleArrayList global_error, int it) {
        int len = global_error.size();
        return global_error_rises(global_error) || global_error.elements()[len - 1] < 0.001 || it > max_iterations;
    }

    public DoubleArrayList train_network(List<double[]> x, List<List<DoubleArrayList>> w, double t[], List<List<Node>> nodes, double alfa) {
        int it = 0;
        DoubleArrayList global_error = new DoubleArrayList();
        while (true) {
            double e = 0.0;
            //takze indexi su[layer][output_node_index][input_node_index]
            for (int index_input = 0; index_input < x.size(); index_input++) {

                forward(x.get(index_input), w, nodes);

                backward(nodes, t[index_input], w);

                recompute_weights(nodes, w, alfa);

                e += Math.pow(t[index_input] - nodes.get(nodes.size() - 1).get(0).out, 2);
            }

            e = 0.5 * e;
            global_error.add(e);
            it++;
            if (stop_condition(global_error, it)) {
                break;
            }
        }

        System.out.println("global_error = " + global_error.elements()[global_error.size() - 1]);

        System.out.println("Iterations=" + it);

        return global_error;
    }

    public DoubleArrayList compute_network(List<double[]> x, List<List<DoubleArrayList>> w, List<List<Node>> nodes) {
        DoubleArrayList result = new DoubleArrayList();
        for (double[] aX : x) {

            forward(aX, w, nodes);
            result.add(nodes.get(nodes.size() - 1).get(0).out);
        }

        return result;
    }

    public void println_results(double t[], DoubleArrayList result) {

        System.out.println("RESULTS:");
        for (int i = 0; i < t.length; i++) {
            System.out.println(t[i] + "->" + result.elements()[i]);
        }
    }

    public class Node {
        double out = 0.0;
        double error = 0.0;
        double x = 0.0;
    }


}
