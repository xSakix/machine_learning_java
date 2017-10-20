package org.xsakix.curvefit.org.xsakix.curvefit.examples;

import cern.colt.list.DoubleArrayList;
import cern.jet.random.Uniform;
import org.xsakix.curvefit.SplineFit;
import org.xsakix.tools.Errors;
import org.xsakix.tools.csv.CsvToDoubleArray;

import java.util.Arrays;

/*

Example of simple function fit(linear) when used on a nonlinear function
which is represented by the ETF NAV data of SPY.

Example error computed vals:

LSE = 2951,777153
RMS = 7,683459

LSE = 972,439035
RMS = 4,410077

LSE = 3485,982530
RMS = 8,349829

It's individual and depends on chosen points.(anyway it's a big error...)
 */
public class SpyFit {


    public static void main(String argv[]) {
        final int test_size = 100;

        double spy[] = CsvToDoubleArray.loadSpyTestDataset();
        System.out.println(Arrays.toString(spy));
        if( spy == null){
            return;
        }

        double x[] = Arrays.copyOfRange(spy,0, spy.length-1);
        double y[] = Arrays.copyOfRange(spy,1,spy.length);

        for (int i = 0; i < x.length; i++) {
            System.out.print(String.format("(%.3f;%.3f) , ", x[i], y[i]));
        }
        System.out.println();

        SplineFit splineFit = new SplineFit(x, y);
        splineFit.makespline();

        double test[] = new double[test_size];

        double exact[] = new double[test_size];

        for(int i =0; i < test_size;i++){
            int index = Uniform.staticNextIntFromTo(0,spy.length-1);
            test[i] = spy[index];
            exact[i] = spy[index+1];
        }

        double evaluated[] = new double[test_size];
        for (int i = 0; i < test_size; i++) {
            evaluated[i] = splineFit.eval(test[i]);
            System.out.println(String.format("spline(%.2f) : exact(%.2f) = %.2f : %.2f", test[i], test[i], evaluated[i], exact[i]));
        }
        double error = Errors.leastSquareError(exact,evaluated);
        System.out.println(String.format("LSE = %.6f", error));
        double rmse = Errors.rootMeanSquareError(error,test.length);
        System.out.println(String.format("RMS = %.6f",rmse));
    }
}
