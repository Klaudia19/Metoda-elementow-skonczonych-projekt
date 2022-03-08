package com.company;
import static java.lang.Math.sqrt;

public class Element2D_9 {

    public double[][]pochN_pochEta=new double[9][4];
    public double[][]pochN_pochKsi=new double[9][4];
    double f_ksztaltu[][]=new double[9][4];
    double []KSI={(-sqrt(3.0/5.0)), 0.0, (sqrt(3.0/5.0)), (-sqrt(3.0/5.0)), 0.0, (sqrt(3.0/5.0)),(-sqrt(3.0/5.0)), 0.0, (sqrt(3.0/5.0))};
    double []ETA= {(-sqrt(3.0/5.0)),(-sqrt(3.0/5.0)),(-sqrt(3.0/5.0)),0.0, 0.0, 0.0, (sqrt(3.0/5.0)), (sqrt(3.0/5.0)), (sqrt(3.0/5.0))};


    public Element2D_9(){

        for (int i = 0; i < 9; i++) {
            f_ksztaltu[i][0] = 0.25 * (1.0 - KSI[i]) * (1.0 - ETA[i]);
            f_ksztaltu[i][1] = 0.25 * (1.0 + KSI[i]) * (1.0 - ETA[i]);
            f_ksztaltu[i][2] = 0.25 * (1.0 + KSI[i]) * (1.0 + ETA[i]);
            f_ksztaltu[i][3] = 0.25 * (1.0 - KSI[i]) * (1.0 + ETA[i]);
        }

        for (int i = 0; i < 9; i++) //dla kazdego pkt.calk
        {
            for (int k = 0; k < 4; k++)
            {
                if (k == 0) this.pochN_pochKsi[i][k] = (-1.0 / 4.0) * (1.0 - ETA[i]);
                if (k == 1) this.pochN_pochKsi[i][k] = (1.0 / 4.0) * (1.0 - ETA[i]);
                if (k == 2) this.pochN_pochKsi[i][k] = (1.0 / 4.0) * (1.0 + ETA[i]);
                if (k == 3)  this.pochN_pochKsi[i][k] = (-1.0 / 4.0) * (1.0 + ETA[i]);
            }

            for (int k = 0; k < 4; k++)
            {
                if (k == 0) this.pochN_pochEta[i][k] = (-1.0 / 4.0) * (1.0 - KSI[i]);
                if (k == 1) this.pochN_pochEta[i][k] = (-1.0 / 4.0) * (1.0 + KSI[i]);
                if (k == 2) this.pochN_pochEta[i][k] = (1.0 / 4.0) * (1.0 +  KSI[i]);
                if (k == 3) this.pochN_pochEta[i][k] = (1.0 / 4.0) * (1.0 -  KSI[i]);
            }
        }
    }
}
