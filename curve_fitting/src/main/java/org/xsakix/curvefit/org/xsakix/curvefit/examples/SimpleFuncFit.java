package org.xsakix.curvefit.org.xsakix.curvefit.examples;

import cern.jet.random.Uniform;
import org.xsakix.curvefit.SplineFit;

import java.util.Arrays;

public class SimpleFuncFit {

    private static double func(double x) {
        return 1.0 + 1.0 / x;
    }

    public static void main(String argv[]) {
        double x[] = new double[]{1.0, 2.0, 4.0, 5.0, 8.0, 10.0};
        double y[] = Arrays.stream(x).map(SimpleFuncFit::func).toArray();

        for (int i = 0; i < x.length; i++) {
            System.out.print(String.format("(%.3f;%.3f) , ", x[i], y[i]));
        }
        System.out.println();

        SplineFit splineFit = new SplineFit(x, y);
        splineFit.makespline();

        double test[] = new double[10];
        double evaluated[] = new double[10];
        double exact[] = new double[10];
        double error = 0.0;
        for (int i = 0; i < 10; i++) {
            test[i] = Uniform.staticNextDoubleFromTo(1.0, 10.0);
            evaluated[i] = splineFit.eval(test[i]);
            exact[i] = func(test[i]);
            System.out.println(String.format("spline(%.2f) : exact(%.2f) = %.2f : %.2f", test[i], test[i], evaluated[i], exact[i]));
            error += Math.pow(exact[i] - evaluated[i], 2.0);
        }
        error = 0.5 * error;
        System.out.println(String.format("LSE = %.6f", error));


    }
}
