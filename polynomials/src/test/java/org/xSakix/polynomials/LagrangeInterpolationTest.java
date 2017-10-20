package org.xSakix.polynomials;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LagrangeInterpolationTest {

    @Test
    public void test(){
        double[] x = new double[]{1,2,3};
        double[] y = new double[]{3,7,13};
        LagrangeInterpolation interpolation = new LagrangeInterpolation(x,y);
        assertEquals(13, interpolation.eval(3));
        assertEquals(7, interpolation.eval(2));
        assertEquals(3, interpolation.eval(1));
        System.out.println(interpolation.eval(1.5));
    }
}
