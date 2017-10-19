package org.xsakix.curvefit;

public class LSMFit {

    private int M = 2;
    private int N = 0;

    private double x[];
    private double y[];
    private double f[][];
    private double c[];
    private double a[][];
    private double b[];

    public LSMFit(double[] x, double[] y, int M) {
        assert x != null;
        assert y != null;
        assert x.length == y.length;
        assert M > 0;
        this.x = x;
        this.y = y;
        this.M = M;
        this.N = this.x.length;

        this.f = new double[M][N];
        this.c = new double[M];
        this.a = new double[M][M];
        this.b = new double[M];
    }

    private double func(double x, double m) {
        return Math.pow(x, m);
    }

    public void computeFunctionValues() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                //0-eth poly in form of x^0 makes no sense for now, so we do +1
                f[i][j] = func(x[j], i + 1);
            }
        }
    }

    public void computeMatrixA() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                double t = 0.0;
                for (int k = 0; k < N; k++) {
                    t += f[i][k] * f[j][k];
                }
                a[i][j] = t;
            }
        }
    }

    public void computeMatrixB() {
        //b[j]=f[j].y - robi sa dot produkt vektorov, bacha
    }

}
