package com.company;
import static java.lang.Math.sqrt;

public class Pkt {

    public double[][] punkt1 = new double[2][2];
    public double[][] punkt2 = new double[2][2];
    public double[][] punkt3 = new double[2][2];
    public double[][] punkt4 = new double[2][2];

    public Pkt() {

            this.punkt1[0][0] = -1.0 / sqrt(3.0);    //pc1 to 2pkt na jednej scianie do HBC
            this.punkt1[0][1] = -1.0;
            this.punkt1[1][0] = 1.0 / sqrt(3.0);
            this.punkt1[1][1] = -1.0;

            this.punkt2[0][0] = 1.0;
            this.punkt2[0][1] = -1.0 / sqrt(3.0);
            this.punkt2[1][0] = 1.0;
            this.punkt2[1][1] = 1.0 / sqrt(3.0);

            this.punkt3[0][0] = 1.0 / sqrt(3.0);
            this.punkt3[0][1] = 1.0;
            this.punkt3[1][0] = -1.0 / sqrt(3.0);
            this.punkt3[1][1] = 1.0;

            this.punkt4[0][0] = -1.0;
            this.punkt4[0][1] = 1.0 / sqrt(3.0);
            this.punkt4[1][0] = -1.0;
            this.punkt4[1][1] = -1.0 / sqrt(3.0);
    }

    public double getPc1(int i, int j) {return punkt1[i][j];}  //pobrania elementu znajdującego się na liście pod określonym indeksem
    public double getPc2(int i, int j) {return punkt2[i][j];}
    public double getPc3(int i, int j) {return punkt3[i][j];}
    public double getPc4(int i, int j) {return punkt4[i][j];}

}
