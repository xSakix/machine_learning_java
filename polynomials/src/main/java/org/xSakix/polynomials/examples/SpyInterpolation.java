package org.xSakix.polynomials.examples;

import cern.colt.list.DoubleArrayList;
import cern.jet.random.Uniform;
import org.xSakix.polynomials.LagrangeInterpolation;
import org.xsakix.tools.Errors;
import org.xsakix.tools.csv.CsvToDoubleArray;

import java.util.Arrays;

public class SpyInterpolation {


    public static void main(String argv[]) {
        final int test_size = 100;

        double spy[] = CsvToDoubleArray.loadSpyTestDataset();
        System.out.println(Arrays.toString(spy));
        if( spy == null){
            return;
        }

        //we can't use the whole spy data set, as it would result in NaN
        //we need to choose significant points, that means for starter local min and local max
        //how do we find such points in the whole data set?
        //split the whole set into same spaced intervals
        //find the lowest point(index) and the highest point(index)
        //x = spy[index-1], y = spy[index]
        DoubleArrayList xx = new DoubleArrayList();
        DoubleArrayList yy = new DoubleArrayList();
        //logicaly, shorter interval should result in more precise results, but it's not true
        //for space = 50:
        // LSE = 16020242912907303000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000,000000
        //RMS = 17899856375349665000000000000000000000000000000000000000000000,000000
        //for space = 500
        //LSE = 640271,739546
        //RMS = 113,161101
        //so there is a possible overflow with more numbers due to much computation?

        int space = 500;

        //can be done also in modulo space
        for(int i  = 0; i < spy.length;i+=space){
            double sub[] = Arrays.copyOfRange(spy,i,i+space);

            double x_max=0.0;
            double x_min=0.0;

            double max = 0.0;
            double min = 9999.0;
            for(int j = 0; j < sub.length;j++){
                if(sub[j] > max){
                    max = sub[j];
                    if(i == 0 && j == 0){
                        x_max = spy[i];
                    }else if(j == 0){
                        x_max = spy[i - 1];
                    }else {
                        x_max = spy[i + j - 1];
                    }
                }
                if(sub[j] < min){
                    min = sub[j];
                    if(i == 0 && j == 0){
                        x_min = spy[i];
                    }else if(j == 0){
                        x_min = spy[i - 1];
                    }else {
                        x_min = spy[i + j - 1];
                    }
                }
            }
            xx.add(x_min);
            xx.add(x_max);
            yy.add(min);
            yy.add(max);
        }

        int k = 0;
        for(; k < xx.size();k++){
            if(((int)Math.round(xx.get(k))) == 0)
                break;
        }

        double x[] = Arrays.copyOfRange(xx.elements(),0, k);
        double y[] = Arrays.copyOfRange(yy.elements(),0,k);

        for (int i = 0; i < x.length; i++) {
            System.out.print(String.format("(%.3f;%.3f) , ", x[i], y[i]));
        }
        System.out.println();

        LagrangeInterpolation splineFit = new LagrangeInterpolation(x, y);

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
            System.out.println(String.format("interpolation(%.2f) : exact(%.2f) = %.2f : %.2f", test[i], test[i], evaluated[i], exact[i]));
        }
        double error = Errors.leastSquareError(exact,evaluated);
        System.out.println(String.format("LSE = %.6f", error));
        double rmse = Errors.rootMeanSquareError(error,test.length);
        System.out.println(String.format("RMS = %.6f",rmse));
    }
}
