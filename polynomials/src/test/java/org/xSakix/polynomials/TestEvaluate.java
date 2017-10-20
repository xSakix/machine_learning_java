package org.xSakix.polynomials;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TestEvaluate {

    @Test
    public void test(){
        Polynomial poly = new Polynomial(new double[]{10.0});
        assertEquals(poly.evaluate(0.0), 10.0);
        assertEquals(poly.evaluate(1.0), 10.0);

        poly = new Polynomial(new double[]{10.0,10.0});
        assertEquals(poly.evaluate(1.0), 20.0);

        poly = new Polynomial(new double[]{10.0,10.0,10.0});
        assertEquals(poly.evaluate(1.0), 30.0);
        assertEquals(poly.evaluate(2.0), 70.0);
        assertEquals(poly.evaluate(3.0), 130.0);

        double x = 2;
        double expected = 0.0;
        for(int i = 9; i >=0;i--){
            expected+= Math.pow(x,i);
        }

        poly = new Polynomial(new double[]{1,1,1,1,1,1,1,1,1,1});
        assertEquals(poly.evaluate(2.0), expected);

    }

}
