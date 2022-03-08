package com.company;

public class Jakobian {

    double J[][] = new double[2][2];
    double j_inv[][] = new double[4][4];
    double det[] = new double[4];
    double odw_det[] = new double[4];
    double pochNprzezpochX[][]=new double[4][4];
    double pochNprzezpochY[][]=new double[4][4];
    Node punkt[] = new Node[4];



    public void Jakobian(int i, Grid g, Element2D_4 elem) {

        punkt[0] = g.nodes[((g.elements[i]).ID[0]) - 1]; //INACZEJ ZAPISAC
        punkt[1] = g.nodes[((g.elements[i]).ID[1]) - 1];
        punkt[2] = g.nodes[((g.elements[i]).ID[2]) - 1];
        punkt[3] = g.nodes[((g.elements[i]).ID[3]) - 1];

        for (int j = 0; j < 4; j++){
        J[0][0] = elem.pochN_pochKsi[j][0] * punkt[0].x + elem.pochN_pochKsi[j][1] * punkt[1].x + elem.pochN_pochKsi[j][2] * punkt[2].x + elem.pochN_pochKsi[j][3] * punkt[3].x;
        J[0][1] = elem.pochN_pochKsi[j][0] * punkt[0].y + elem.pochN_pochKsi[j][1] * punkt[1].y + elem.pochN_pochKsi[j][2] * punkt[2].y + elem.pochN_pochKsi[j][3] * punkt[3].y;
        J[1][0] = elem.pochN_pochEta[j][0] * punkt[0].x + elem.pochN_pochEta[j][1] * punkt[1].x + elem.pochN_pochEta[j][2] * punkt[2].x + elem.pochN_pochEta[j][3] * punkt[3].x;
        J[1][1] = elem.pochN_pochEta[j][0] * punkt[0].y + elem.pochN_pochEta[j][1] * punkt[1].y + elem.pochN_pochEta[j][2] * punkt[2].y + elem.pochN_pochEta[j][3] * punkt[3].y;

    }
        for (int l = 0; l < 4; l++) {
            det[l] = J[0][0] * J[1][1] - J[0][1] * J[0][1];
            odw_det[l] = 1 / det[l];
        }

        for (int l = 0; l < 4; l++) {
            j_inv[l][0] = J[1][1] * odw_det[l];
            j_inv[l][1] = J[0][1] * odw_det[l]*(-1);
            j_inv[l][2] = J[1][0] * odw_det[l]*(-1);
            j_inv[l][3] = J[0][0] * odw_det[l];
        }
    }

    public void pochNprzezpochXandY(Element2D_4 elem){

        for (int m=0; m < 4; m++) {
            for (int l = 0; l < 4; l++) {
                pochNprzezpochY[m][l] = j_inv[m][2] * elem.pochN_pochKsi[m][l] + j_inv[m][3] * elem.pochN_pochEta[m][l];
                pochNprzezpochX[m][l] = j_inv[m][0] * elem.pochN_pochKsi[m][l] + j_inv[m][1] * elem.pochN_pochEta[m][l];

            }
        }
    }
}