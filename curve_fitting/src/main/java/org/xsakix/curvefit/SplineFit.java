package org.xsakix.curvefit;

public class SplineFit {

    private int N;
    private double x[];
    private double y[];
    private double d[];
    private double u[];
    private double w[];
    private double p[];

    public SplineFit(double[] x, double[] y) {
        this.x = x;
        this.y = y;
        this.N = x.length;

        this.d = new double[N - 1];
        this.u = new double[N - 1];
        this.w = new double[N - 1];
        this.p = new double[N];

    }

    public void makespline() {
        for (int i = 1; i < N - 1; i++) {
            d[i] = 2 * (x[i + 1] - x[i - 1]);
        }
        for (int i = 0; i < N - 1; i++) {
            u[i] = x[i + 1] - x[i];
        }
        for (int i = 1; i < N - 1; i++) {
            w[i] = (y[i + 1] - y[i]) / u[i] - (y[i] - y[i - 1]) / u[i - 1];
        }
        p[0] = 0.0;
        p[N - 1] = 0.0;
        for (int i = 1; i < N - 2; i++) {
            w[i + 1] = w[i + 1] - w[i] * u[i] / d[i];
            d[i + 1] = d[i + 1] - u[i] * u[i] / d[i];
        }
        for (int i = N - 2; i > 0; i--) {
            p[i] = (w[i] - u[i] * p[i + 1]) / d[i];
            System.out.println(String.format("p[%d]=%.5f", i, p[i]));
        }
    }

    private double f(double x) {
        return Math.pow(x, 3) - x;
    }

    public double eval(double v) {
        int i = -1;

        do {
            if (v < x[i + 1])
                break;
            i++;
        } while (i + 2 < N);

        double t = (v - x[i]) / u[i];

        return t * y[i + 1] + (1 - t) * y[i] + u[i] * u[i] * (f(t) * p[i + 1] - f(1 - t) * p[i]);
    }

}
