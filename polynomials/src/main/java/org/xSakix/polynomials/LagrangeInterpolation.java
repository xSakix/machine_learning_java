package org.xSakix.polynomials;

public class LagrangeInterpolation {

    private double x[];
    private double y[];

    public LagrangeInterpolation(double[] x, double[] y) {
        assert x != null;
        assert y != null;
        assert  x.length == y.length;

        this.x = x;
        this.y = y;
    }

    public double eval(double xx){
        double yy = 0.0;

        for(int j = 0; j < y.length;j++){
            double yj = y[j];
            for(int i = 0;i < x.length;i++){
                if(i == j) {
                    continue;
                }

                yj = yj*(xx - x[i])/(x[j]-x[i]);
            }
            yy+=yj;
        }

        return yy;
    }
}
