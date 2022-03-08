package com.company;
import static java.lang.Math.sqrt;

public class Pkt3 {

    public double[] waga = new double[9];
    public double[][] punkt1 = new double[3][2];;
    public double[][] punkt2 = new double[3][2];;
    public double[][] punkt3 = new double[3][2];;
    public double[][] punkt4 = new double[3][2];;


    Pkt3() {

        waga[0] = 5. / 9. * 5. / 9.;   //wagi do macierzy H i C 3pkt(dla kazdego pkt.calk)
        waga[1] = 8. / 9. * 5. / 9.;
        waga[2] = 5. / 9. * 5. / 9.;
        waga[3] = 8. / 9. * 5. / 9.;
        waga[4] = 8. / 9. * 8. / 9.;
        waga[5] = 8. / 9. * 5. / 9.;
        waga[6] = 5. / 9. * 5. / 9.;
        waga[7] = 8. / 9. * 5. / 9.;
        waga[8] = 5. / 9. * 5. / 9.;


        this.punkt1[0][0] = -sqrt(3.0 / 5.0); //pc1 to 3pkt na jednej scianie do HBC
        this.punkt1[0][1] = -1.0;
        this.punkt1[1][0] = 0.0;
        this.punkt1[1][1] = -1.0;
        this.punkt1[2][0] = sqrt(3.0 / 5.0);
        this.punkt1[2][1] = -1.0;

        this.punkt2[0][0] = 1.0;
        this.punkt2[0][1] = -sqrt(3.0 / 5.0);
        this.punkt2[1][0] = 1.0;
        this.punkt2[1][1] = 0.0;
        this.punkt2[2][0] = 1.0;
        this.punkt2[2][1] = sqrt(3.0 / 5.0);

        this.punkt3[0][0] = sqrt(3.0 / 5.0);
        this.punkt3[0][1] = 1.0;
        this.punkt3[1][0] = 0.0;
        this.punkt3[1][1] = 1.0;
        this.punkt3[2][0] = -sqrt(3.0 / 5.0);
        this.punkt3[2][1] = 1.0;

        this.punkt4[0][0] = -1.0;
        this.punkt4[0][1] = sqrt(3.0 / 5.0);
        this.punkt4[1][0] = -1.0;
        this.punkt4[1][1] = 0.0;
        this.punkt4[2][0] = -1.0;
        this.punkt4[2][1] = -sqrt(3.0 / 5.0);

    }

    public double getPc1(int i, int j)
    {
        return punkt1[i][j];
    }
    public double getPc2(int i, int j) { return punkt2[i][j]; }
    public double getPc3(int i, int j)
    {
        return punkt3[i][j];
    }
    public double getPc4(int i, int j)
    {
        return punkt4[i][j];
    }
    public double getWaga(int i) {return waga[i];}
}
