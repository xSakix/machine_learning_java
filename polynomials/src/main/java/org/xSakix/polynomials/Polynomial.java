package org.xSakix.polynomials;

/*
 p(x,n) = p[0]+p[1]*x+p[2]*x^2+p[3]*x^3+....+p[n]*x^n

 */
public class Polynomial {

    private double p[];
    private int n;

    public Polynomial(double[] p) {
        assert p != null;
        assert p.length > 0;

        this.p = p;
        this.n = p.length;
    }

    public double evaluate(double x){

        double y = p[n-1];
        for (int i = n-2;i >= 0;i--){
            y = y*x+p[i];
        }

        return y;
    }
}
