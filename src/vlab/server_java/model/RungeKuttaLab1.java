package vlab.server_java.model;

import vlab.server_java.model.util.HtmlParamEscaper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static vlab.server_java.model.util.HtmlParamEscaper.shrink;

/**
 * Created by efimchick on 27.04.16.
 */
public class RungeKuttaLab1 {

    public CalculateCodeResult calculate(CalculateTask task, GenerateCodeResult variantCode, GenerateInstructionsResult variantInstr) {

        final double g = 9.81;
        final double timeStep = 0.01;
        final double timeLimit = task.getTime().doubleValue();

        final double m = variantCode.getMass().doubleValue();//1;
        final double i = variantInstr.getI().doubleValue();//0.3;
        final double r = task.getRadius().doubleValue();//1;
        final double mu = variantInstr.getV().doubleValue();//0.1;

        final double a = m * (i*i + r*r);
        final double b = 1 / a;

        final double G = g * m;

        final double k0 = sqrt(b*G*r);
        final double n = b*mu/2;

        final Function omgFun = (x, y, z) -> -(2*n*z + k0 * k0 * sin(y));
        final Function phiFun = (x, y, z) -> z;

        double time, phi, omega;
        double k1, k2, k4, k3;
        double q1, q2, q4, q3;

        time = 0;
        phi = 0.8;
        omega = 0;

        //h = 0.1; // шаг

        System.out.println("\tT\t\t\tphi\t\t\tomega");
        List<BigDecimal[]> rows = new ArrayList<>();
        rows.add(new BigDecimal[]{new BigDecimal(time), new BigDecimal(phi), new BigDecimal(omega)});
        for(; time < timeLimit; time += timeStep){

            k1 = timeStep * omgFun.count(time, phi, omega);
            q1 = timeStep * phiFun.count(time, phi, omega);

            k2 = timeStep * omgFun.count(time + timeStep / 2.0, phi + q1 / 2.0, omega + k1 / 2.0);
            q2 = timeStep * phiFun.count(time + timeStep / 2.0, phi + q1 / 2.0, omega + k1 / 2.0);

            k3 = timeStep * omgFun.count(time + timeStep / 2.0, phi + q2 / 2.0, omega + k2 / 2.0);
            q3 = timeStep * phiFun.count(time + timeStep / 2.0, phi + q2 / 2.0, omega + k2 / 2.0);

            k4 = timeStep * omgFun.count(time + timeStep, phi + q3, omega + k3);
            q4 = timeStep * phiFun.count(time + timeStep, phi + q3, omega + k3);

            omega = omega + (k1 + 2.0*k2 + 2.0*k3 + k4)/6.0;
            phi = phi + (q1 + 2.0*q2 + 2.0*q3 + q4)/6.0;
            BigDecimal[] row = {new BigDecimal(time + timeStep), new BigDecimal(phi), new BigDecimal(omega)};
            rows.add(row);

            System.out.println("\t" + shrink(row[0]) +"\t" + shrink(row[1]) + "\t" + shrink(row[2]));
        }
        return new CalculateCodeResult(rows);
    }

    public interface Function{
        double count(double x, double y, double z);
    }
}
