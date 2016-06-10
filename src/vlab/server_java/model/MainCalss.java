package vlab.server_java.model;

import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class MainCalss {

    static double g = 9.81;
    static double mu = 0.1;

    static double phi0 = 0.3;
    static double omega0 = 0;

    static double m = 1;
    static double i = 0.3;
    static double r = 1;

    static double a = m * (i*i + r*r);
    static double b = 1 / a;

    static double G = g * m;

    static double k0 = sqrt(b*G*r);
    static double n = b*mu/2;

    public static void main(String[] args) {
        int k = 2;
        double Xo, Yo, Y1, Zo, Z1;
        double k1, k2, k4, k3;
        double q1, q2, q4, q3;
                /*
                 *Начальные условия
                 */







        double h = 0.01;
        double timeLimit = 4;

        double phiI = phi0;
        double omegaI = omega0;


        Xo = 0;
        Yo = 0.8;
        Zo = 0;

        //h = 0.1; // шаг

        System.out.println("\tX\t\tY\t\tZ");
        for(; r(Xo,2)<4.0; Xo += h){

            k1 = h * f(Xo, Yo, Zo);
            q1 = h * g(Xo, Yo, Zo);

            k2 = h * f(Xo + h/2.0, Yo + q1/2.0, Zo + k1/2.0);
            q2 = h * g(Xo + h/2.0, Yo + q1/2.0, Zo + k1/2.0);

            k3 = h * f(Xo + h/2.0, Yo + q2/2.0, Zo + k2/2.0);
            q3 = h * g(Xo + h/2.0, Yo + q2/2.0, Zo + k2/2.0);

            k4 = h * f(Xo + h, Yo + q3, Zo + k3);
            q4 = h * g(Xo + h, Yo + q3, Zo + k3);

            Z1 = Zo + (k1 + 2.0*k2 + 2.0*k3 + k4)/6.0;
            Y1 = Yo + (q1 + 2.0*q2 + 2.0*q3 + q4)/6.0;
            System.out.printf("%.2f , phi = %.2f , omega = %.2f", Xo, Yo, Zo);
            System.out.println();
            Yo = Y1;
            Zo = Z1;
        }

    }
    /**
     * функция для округления и отбрасывания "хвоста"
     */
    public static double r(double value, int k){
        return (double)Math.round((Math.pow(10, k)*value))/Math.pow(10, k);
    }
    /**
     * функции, которые получаются из системы
     */
    public static double f(double x, double y, double z){
        return -(2*n*z + k0 * k0 * sin(y));
    }
    public static double g(double x, double y, double z){
        return (z);
    }

}