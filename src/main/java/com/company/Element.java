package com.company;

public class Element {

    int[] ID = new int[4];
    double H_sum[][] = new double[4][4];
    double H_sum_3pkt[][] = new double[4][4];
    double C[][] = new double[4][4];
    double pomoc_c[][] = new double[4][4];
    double C_3pkt[][] = new double[4][4];
    double Hbc[][] = new double[4][4];
    double Hbc_3pkt[][] = new double[4][4];
    double P[] = new double[4];
    double P_3pkt[] = new double[4];
    Pkt3 waga = new Pkt3();

    public Element(int i, int nH) {    ///ZMIENIC
        ID[0] = i;
        ID[1] = nH + ID[0];
        ID[2] = 1 + ID[1];
        ID[3] = 1 + ID[0];
    }


    public void H(Jakobian jak, int k) {

        double macierzHX[][] = new double[4][4];
        double macierzHY[][] = new double[4][4];
        double H_help[][] = new double[4][4];
        double H[][] = new double[4][4];


        for (int pc = 0; pc < 4; pc++) {
            for (int m = 0; m < 4; m++) {
                for (int l = 0; l < 4; l++) {
                    macierzHY[m][l] = jak.pochNprzezpochY[pc][m] * jak.pochNprzezpochY[pc][l];
                }
            }

            for (int m = 0; m < 4; m++) {
                for (int l = 0; l < 4; l++) {
                    macierzHX[m][l] = jak.pochNprzezpochX[pc][m] * jak.pochNprzezpochX[pc][l];
                }
            }

            for (int m = 0; m < 4; m++) {
                for (int l = 0; l < 4; l++) {
                    H_help[m][l] = macierzHX[m][l] + macierzHY[m][l];
                }
            }

            for (int m = 0; m < 4; m++) {
                for (int l = 0; l < 4; l++) {
                    H[m][l] = k * (H_help[m][l]) * jak.det[pc]; // waga=1 nie wstawialam
                }

            }

            for (int m = 0; m < 4; m++) {
                for (int l = 0; l < 4; l++) {
                    H_sum[m][l] += H[m][l]; // suma H dla wszystkich pkt calkowania w elemencie
                }
            }
        }
    }


    public void H_3pkt(Jakobian_3pkt jak, int k) {

        double macierzHX[][] = new double[4][4];
        double macierzHY[][] = new double[4][4];
        double H_help[][] = new double[4][4];
        double H[][] = new double[4][4];

        for (int pc = 0; pc < 9; pc++) {
            for (int m = 0; m < 4; m++) {
                for (int l = 0; l < 4; l++) {
                    macierzHY[m][l] = jak.pochNprzezpochY[pc][m] * jak.pochNprzezpochY[pc][l];
                }
            }

            for (int m = 0; m < 4; m++) {
                for (int l = 0; l < 4; l++) {
                    macierzHX[m][l] = jak.pochNprzezpochX[pc][m] * jak.pochNprzezpochX[pc][l];
                }
            }

            for (int m = 0; m < 4; m++) {
                for (int l = 0; l < 4; l++) {
                    H_help[m][l] = macierzHY[m][l] + macierzHX[m][l];
                }
            }

            for (int m = 0; m < 4; m++) {
                for (int l = 0; l < 4; l++) {
                    H[m][l] = k * waga.getWaga(pc) * (H_help[m][l]) * jak.det[pc];
                }
            }

            for (int m = 0; m < 4; m++) {
                for (int l = 0; l < 4; l++) {
                    H_sum_3pkt[m][l] += H[m][l];
                }
            }
        }
    }

    public void C(Jakobian jak, Element2D_4 ele, int c, int ro) {

        for (int i = 0; i < 4; i++) { //pkt calk
            for (int m = 0; m < 4; m++) {
                for (int l = 0; l < 4; l++) {
                    pomoc_c[m][l] += ro * c * ele.f_ksztaltu[i][m] * ele.f_ksztaltu[i][l] * jak.det[i]; //waga=1 w 2pkt nie wstawialam
                    C[m][l] = pomoc_c[m][l];
                }
            }
        }
    }

    public void C_3pkt( Jakobian_3pkt jak,Element2D_9 ele, int c, int ro) {

        for (int i = 0; i < 9; i++) { //pkt calk
            for (int m = 0; m < 4; m++) {
                for (int l = 0; l < 4; l++) {
                    pomoc_c[m][l] += ro * c * waga.getWaga(i) * ele.f_ksztaltu[i][m] * ele.f_ksztaltu[i][l] * jak.det[i];
                    C_3pkt[m][l] = pomoc_c[m][l];
                }
            }
        }
    }
}


