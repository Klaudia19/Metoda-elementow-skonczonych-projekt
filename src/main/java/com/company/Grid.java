package com.company;


public class Grid {
    double H;
    double B;
    int nH;
    int nB;
    public int nN;
    public int nE;
    double dX, dY;
    public Element[] elements;
    public Node[] nodes;
    double Hglobalne[][];
    double Pglobalne[];
    double Cglobalne[][];
    double HBCglobalne[][];
    double HBCsumHglobalne[][];
    double H_with_time_step[][];
    double P_with_time_step[];
    double Hglobalne_3pkt[][];
    double Pglobalne_3pkt[];
    double Cglobalne_3pkt[][];
    double HBCglobalne_3pkt[][];
    double HBCsumHglobalne_3pkt[][];
    double H_with_time_step_3pkt[][];
    double P_with_time_step_3pkt[];


    public Grid(double B,double H, int nB, int nH) {
        this.H = H;
        this.B = B;
        this.nH = nH;
        this.nB = nB;
        nN = nH * nB;               //calkowita l.wezlow
        nE = (nH - 1) * (nB - 1);   //calkowita l.elementow
        elements = new Element[nE];
        nodes = new Node[nN];
        dX = B / (nB - 1);         //krok po szerokosci
        dY = H / (nH - 1);         //krok po wysokosci

        Hglobalne = new double[nN][nN];
        Pglobalne = new double[nN];
        Cglobalne = new double[nN][nN];
        HBCglobalne =new double[nN][nN];
        HBCsumHglobalne= new double[nN][nN];
        Hglobalne_3pkt = new double[nN][nN];
        Pglobalne_3pkt = new double[nN];
        Cglobalne_3pkt = new double[nN][nN];
        HBCglobalne_3pkt=new double[nN][nN];
        HBCsumHglobalne_3pkt= new double[nN][nN];
        H_with_time_step=new double[nN][nN];
        P_with_time_step=new double[nN];
        H_with_time_step_3pkt=new double[nN][nN];
        P_with_time_step_3pkt=new double[nN];
    }

    public void dodajWez() {                          //ZMIENIC
        double x = 0;
        double y = 0;

        for (int i = 0; i <= (nN - 1); i++) {
            if (y % nH == 0 && y != 0) {
                x++;
                y = 0;
            }
            nodes[i] = new Node(x * dX, y * dY);

            if (nodes[i].x == 0 || nodes[i].x == B || nodes[i].y == 0 || nodes[i].y == H) {
                nodes[i].setBC(1);
            }
            int a = i + 1;
            y++;
        }
    }

    public void dodajElem() {                  //ZMIENIC
        int j = 0;
        for (int i = 0; i <= nE - 1; i++) {
            if (j % nH == 0) j++;
            elements[i] = new Element(j, nH);
            int a = i + 1;
            j++;
        }
    }

    public double funkcjaKsztaltu1(double ksi1, double eta1) {

        return (0.25 * (1.0 - ksi1) * (1.0 - eta1));
    }

    public double funkcjaKsztaltu2(double ksi1, double eta1) {

        return (0.25 * (1.0 + ksi1) * (1.0 - eta1));
    }

    public double funkcjaKsztaltu3(double ksi1, double eta1) {

        return (0.25 * (1.0 + ksi1) * (1.0 + eta1));
    }

    public double funkcjaKsztaltu4(double ksi1, double eta1) {

        return (0.25 * (1.0 - ksi1) * (1.0 + eta1));
    }


    public void Hbc(Pkt pkt_calk, int alfa, int temp) {

        double waga = 1.0;
        double[] f_pomoc1 = new double[4];
        double[] f_pomoc2 = new double[4];


        for (int i = 0; i < nE; i++) {
            if (nodes[(elements[i].ID[0]) - 1].BC == 1 && nodes[(elements[i].ID[1]) - 1].BC == 1) {
                double detJ = dX / 2; //długość ściany podzielona przez 2

                f_pomoc1[0] = funkcjaKsztaltu1(pkt_calk.getPc1(0, 0), pkt_calk.getPc1(0, 1));
                f_pomoc1[1] = funkcjaKsztaltu2(pkt_calk.getPc1(0, 0), pkt_calk.getPc1(0, 1));
                f_pomoc1[2] = funkcjaKsztaltu3(pkt_calk.getPc1(0, 0), pkt_calk.getPc1(0, 1));
                f_pomoc1[3] = funkcjaKsztaltu4(pkt_calk.getPc1(0, 0), pkt_calk.getPc1(0, 1));

                f_pomoc2[0] = funkcjaKsztaltu1(pkt_calk.getPc1(1, 0), pkt_calk.getPc1(1, 1));
                f_pomoc2[1] = funkcjaKsztaltu2(pkt_calk.getPc1(1, 0), pkt_calk.getPc1(1, 1));
                f_pomoc2[2] = funkcjaKsztaltu3(pkt_calk.getPc1(1, 0), pkt_calk.getPc1(1, 1));
                f_pomoc2[3] = funkcjaKsztaltu4(pkt_calk.getPc1(1, 0), pkt_calk.getPc1(1, 1));

                // System.out.println("\n \n" + Arrays.toString(f_help1));

                for (int z = 0; z < 4; z++) { //𝛼 𝑁 𝑁 𝑇
                    for (int v = 0; v < 4; v++) {
                        elements[i].Hbc[z][v] += ((alfa * f_pomoc1[z] * f_pomoc1[v] * waga) + (alfa * f_pomoc2[z] * f_pomoc2[v] * waga)) * detJ;
                    }
                }

                for (int z = 0; z < 4; z++) { //
                    f_pomoc1[z] = f_pomoc1[z] * temp * waga; //4 wartosci f. ksztaltu dla 1pc
                    f_pomoc2[z] = f_pomoc2[z] * temp * waga; //4 wartosci f. ksztaltu dla 2pc
                    elements[i].P[z] += (f_pomoc1[z] + f_pomoc2[z]) * detJ * alfa;
                }
            }

            if (nodes[(elements[i].ID[1]) - 1].BC == 1 && nodes[(elements[i].ID[2]) - 1].BC == 1) {
                double detJ = dY / 2;

                f_pomoc1[0] = funkcjaKsztaltu1(pkt_calk.getPc2(0, 0), pkt_calk.getPc2(0, 1));
                f_pomoc1[1] = funkcjaKsztaltu2(pkt_calk.getPc2(0, 0), pkt_calk.getPc2(0, 1));
                f_pomoc1[2] = funkcjaKsztaltu3(pkt_calk.getPc2(0, 0), pkt_calk.getPc2(0, 1));
                f_pomoc1[3] = funkcjaKsztaltu4(pkt_calk.getPc2(0, 0), pkt_calk.getPc2(0, 1));

                f_pomoc2[0] = funkcjaKsztaltu1(pkt_calk.getPc2(1, 0), pkt_calk.getPc2(1, 1));
                f_pomoc2[1] =funkcjaKsztaltu2(pkt_calk.getPc2(1, 0), pkt_calk.getPc2(1, 1));
                f_pomoc2[2] = funkcjaKsztaltu3(pkt_calk.getPc2(1, 0), pkt_calk.getPc2(1, 1));
                f_pomoc2[3] = funkcjaKsztaltu4(pkt_calk.getPc2(1, 0), pkt_calk.getPc2(1, 1));


                for (int z = 0; z < 4; z++) {
                    for (int v = 0; v < 4; v++) {
                        elements[i].Hbc[z][v] += ((alfa * f_pomoc1[z] * f_pomoc1[v] * waga) + (alfa * f_pomoc2[z] * f_pomoc2[v] * waga)) * detJ;

                    }
                }

                for (int z = 0; z < 4; z++) {
                    f_pomoc1[z] = f_pomoc1[z] * temp * waga;
                    f_pomoc2[z] = f_pomoc2[z] * temp * waga;
                    elements[i].P[z] += (f_pomoc1[z] + f_pomoc2[z]) * detJ * alfa;
                }
            }

            if (nodes[(elements[i].ID[2]) - 1].BC == 1 && nodes[(elements[i].ID[3]) - 1].BC == 1) {
                double detJ = dX / 2;


                f_pomoc1[0] = funkcjaKsztaltu1(pkt_calk.getPc3(0, 0), pkt_calk.getPc3(0, 1));
                f_pomoc1[1] = funkcjaKsztaltu2(pkt_calk.getPc3(0, 0), pkt_calk.getPc3(0, 1));
                f_pomoc1[2] = funkcjaKsztaltu3(pkt_calk.getPc3(0, 0), pkt_calk.getPc3(0, 1));
                f_pomoc1[3] = funkcjaKsztaltu4(pkt_calk.getPc3(0, 0), pkt_calk.getPc3(0, 1));

                f_pomoc2[0] = funkcjaKsztaltu1(pkt_calk.getPc3(1, 0), pkt_calk.getPc3(1, 1));
                f_pomoc2[1] = funkcjaKsztaltu2(pkt_calk.getPc3(1, 0), pkt_calk.getPc3(1, 1));
                f_pomoc2[2] = funkcjaKsztaltu3(pkt_calk.getPc3(1, 0), pkt_calk.getPc3(1, 1));
                f_pomoc2[3] = funkcjaKsztaltu4(pkt_calk.getPc3(1, 0), pkt_calk.getPc3(1, 1));


                for (int z = 0; z < 4; z++) {
                    for (int v = 0; v < 4; v++) {
                        elements[i].Hbc[z][v] += ((alfa * f_pomoc1[z] * f_pomoc1[v] * waga) + (alfa * f_pomoc2[z] * f_pomoc2[v] * waga)) * detJ;
                    }
                }

                for (int z = 0; z < 4; z++) {
                    f_pomoc1[z] = f_pomoc1[z] * temp * waga;
                    f_pomoc2[z] = f_pomoc2[z] * temp * waga;
                    elements[i].P[z] += (f_pomoc1[z] + f_pomoc2[z]) * detJ * alfa;
                }
            }

            if (nodes[(elements[i].ID[3]) - 1].BC == 1 && nodes[(elements[i].ID[0]) - 1].BC == 1) {
                double detJ = dY / 2;


                f_pomoc1[0] = funkcjaKsztaltu1(pkt_calk.getPc4(0, 0), pkt_calk.getPc4(0, 1));
                f_pomoc1[1] = funkcjaKsztaltu2(pkt_calk.getPc4(0, 0), pkt_calk.getPc4(0, 1));
                f_pomoc1[2] = funkcjaKsztaltu3(pkt_calk.getPc4(0, 0), pkt_calk.getPc4(0, 1));
                f_pomoc1[3] = funkcjaKsztaltu4(pkt_calk.getPc4(0, 0), pkt_calk.getPc4(0, 1));

                f_pomoc2[0] = funkcjaKsztaltu1(pkt_calk.getPc4(1, 0), pkt_calk.getPc4(1, 1));
                f_pomoc2[1] = funkcjaKsztaltu2(pkt_calk.getPc4(1, 0), pkt_calk.getPc4(1, 1));
                f_pomoc2[2] = funkcjaKsztaltu3(pkt_calk.getPc4(1, 0), pkt_calk.getPc4(1, 1));
                f_pomoc2[3] = funkcjaKsztaltu4(pkt_calk.getPc4(1, 0), pkt_calk.getPc4(1, 1));


                for (int z = 0; z < 4; z++) {
                    for (int v = 0; v < 4; v++) {
                        elements[i].Hbc[z][v] += ((alfa * f_pomoc1[z] * f_pomoc1[v] * waga) + (alfa * f_pomoc2[z] * f_pomoc2[v] * waga)) * detJ;
                    }
                }
                    for (int z = 0; z < 4; z++) {
                        f_pomoc1[z] = f_pomoc1[z] * temp * waga;
                        f_pomoc2[z] = f_pomoc2[z] * temp * waga;
                        elements[i].P[z] += (f_pomoc1[z] + f_pomoc2[z]) * detJ * alfa;
                    }
                }
            }
        }


    public void Hbc_3pkt(Pkt3 pc, int alfa, int temp) {

        double[] f_pomoc1 = new double[4];
        double[] f_pomoc2 = new double[4];
        double[] f_pomoc3 = new double[4];
        double waga1= 5.0 / 9.0;
        double waga2= 8.0 / 9.0;
        double waga3= 5.0 / 9.0;

        for (int i = 0; i < nE; i++) {
            if (nodes[(elements[i].ID[0]) - 1].BC == 1 && nodes[(elements[i].ID[1]) - 1].BC == 1) {
                double detJ = dX / 2;

                f_pomoc1[0] = funkcjaKsztaltu1(pc.getPc1(0, 0), pc.getPc1(0, 1));
                f_pomoc1[1] = funkcjaKsztaltu2(pc.getPc1(0, 0), pc.getPc1(0, 1));
                f_pomoc1[2] = funkcjaKsztaltu3(pc.getPc1(0, 0), pc.getPc1(0, 1));
                f_pomoc1[3] = funkcjaKsztaltu4(pc.getPc1(0, 0), pc.getPc1(0, 1));

                f_pomoc2[0] = funkcjaKsztaltu1(pc.getPc1(1, 0), pc.getPc1(1, 1));
                f_pomoc2[1] = funkcjaKsztaltu2(pc.getPc1(1, 0), pc.getPc1(1, 1));
                f_pomoc2[2] = funkcjaKsztaltu3(pc.getPc1(1, 0), pc.getPc1(1, 1));
                f_pomoc2[3] = funkcjaKsztaltu4(pc.getPc1(1, 0), pc.getPc1(1, 1));

                f_pomoc3[0] = funkcjaKsztaltu1(pc.getPc1(2, 0), pc.getPc1(2, 1));
                f_pomoc3[1] = funkcjaKsztaltu2(pc.getPc1(2, 0), pc.getPc1(2, 1));
                f_pomoc3[2] = funkcjaKsztaltu3(pc.getPc1(2, 0), pc.getPc1(2, 1));
                f_pomoc3[3] = funkcjaKsztaltu4(pc.getPc1(2, 0), pc.getPc1(2, 1));

                // System.out.println("\n \n" + Arrays.toString(f_help1));

                for (int z = 0; z < 4; z++) {
                    for (int v = 0; v < 4; v++) {
                        elements[i].Hbc_3pkt[z][v] += ((alfa * f_pomoc1[z] * f_pomoc1[v] * waga1) + (alfa * f_pomoc2[z] * f_pomoc2[v] * waga2) + (alfa * f_pomoc3[z] * f_pomoc3[v] * waga3)) * detJ;
                    }
                }

                for (int z = 0; z < 4; z++) {
                    f_pomoc1[z] = f_pomoc1[z] * temp * waga1;
                    f_pomoc2[z] = f_pomoc2[z] * temp * waga2;
                    f_pomoc3[z] = f_pomoc3[z] * temp * waga3;
                    elements[i].P_3pkt[z] += (f_pomoc1[z] + f_pomoc2[z] + f_pomoc3[z]) * detJ * alfa;
                }
            }

            if (nodes[(elements[i].ID[1]) - 1].BC == 1 && nodes[(elements[i].ID[2]) - 1].BC == 1) {
                double detJ = dY / 2;

                f_pomoc1[0] = funkcjaKsztaltu1(pc.getPc2(0, 0), pc.getPc2(0, 1));
                f_pomoc1[1] = funkcjaKsztaltu2(pc.getPc2(0, 0), pc.getPc2(0, 1));
                f_pomoc1[2] = funkcjaKsztaltu3(pc.getPc2(0, 0), pc.getPc2(0, 1));
                f_pomoc1[3] = funkcjaKsztaltu4(pc.getPc2(0, 0), pc.getPc2(0, 1));

                f_pomoc2[0] = funkcjaKsztaltu1(pc.getPc2(1, 0), pc.getPc2(1, 1));
                f_pomoc2[1] = funkcjaKsztaltu2(pc.getPc2(1, 0), pc.getPc2(1, 1));
                f_pomoc2[2] = funkcjaKsztaltu3(pc.getPc2(1, 0), pc.getPc2(1, 1));
                f_pomoc2[3] = funkcjaKsztaltu4(pc.getPc2(1, 0), pc.getPc2(1, 1));

                f_pomoc3[0] = funkcjaKsztaltu1(pc.getPc2(2, 0), pc.getPc2(2, 1));
                f_pomoc3[1] = funkcjaKsztaltu2(pc.getPc2(2, 0), pc.getPc2(2, 1));
                f_pomoc3[2] = funkcjaKsztaltu3(pc.getPc2(2, 0), pc.getPc2(2, 1));
                f_pomoc3[3] = funkcjaKsztaltu4(pc.getPc2(2, 0), pc.getPc2(2, 1));


                for (int z = 0; z < 4; z++) {
                    for (int v = 0; v < 4; v++) {
                        elements[i].Hbc_3pkt[z][v] += ((alfa * f_pomoc1[z] * f_pomoc1[v] * waga1) + (alfa * f_pomoc2[z] * f_pomoc2[v] * waga2) + (alfa * f_pomoc3[z] * f_pomoc3[v] * waga3)) * detJ;

                    }
                }

                for (int z = 0; z < 4; z++) {
                    f_pomoc1[z] = f_pomoc1[z] * temp * waga1;
                    f_pomoc2[z] = f_pomoc2[z] * temp * waga2;
                    f_pomoc3[z] = f_pomoc3[z] * temp * waga3;
                    elements[i].P_3pkt[z] += (f_pomoc1[z] + f_pomoc2[z] + f_pomoc3[z]) * detJ * alfa;
                }
            }

            if (nodes[(elements[i].ID[2]) - 1].BC == 1 && nodes[(elements[i].ID[3]) - 1].BC == 1) {
                double detJ = dX / 2;


                f_pomoc1[0] = funkcjaKsztaltu1(pc.getPc3(0, 0), pc.getPc3(0, 1));
                f_pomoc1[1] = funkcjaKsztaltu2(pc.getPc3(0, 0), pc.getPc3(0, 1));
                f_pomoc1[2] = funkcjaKsztaltu3(pc.getPc3(0, 0), pc.getPc3(0, 1));
                f_pomoc1[3] = funkcjaKsztaltu4(pc.getPc3(0, 0), pc.getPc3(0, 1));

                f_pomoc2[0] = funkcjaKsztaltu1(pc.getPc3(1, 0), pc.getPc3(1, 1));
                f_pomoc2[1] = funkcjaKsztaltu2(pc.getPc3(1, 0), pc.getPc3(1, 1));
                f_pomoc2[2] = funkcjaKsztaltu3(pc.getPc3(1, 0), pc.getPc3(1, 1));
                f_pomoc2[3] = funkcjaKsztaltu4(pc.getPc3(1, 0), pc.getPc3(1, 1));

                f_pomoc3[0] = funkcjaKsztaltu1(pc.getPc3(2, 0), pc.getPc3(2, 1));
                f_pomoc3[1] = funkcjaKsztaltu2(pc.getPc3(2, 0), pc.getPc3(2, 1));
                f_pomoc3[2] = funkcjaKsztaltu3(pc.getPc3(2, 0), pc.getPc3(2, 1));
                f_pomoc3[3] = funkcjaKsztaltu4(pc.getPc3(2, 0), pc.getPc3(2, 1));

                for (int z = 0; z < 4; z++) {
                    for (int v = 0; v < 4; v++) {
                        elements[i].Hbc_3pkt[z][v] += ((alfa * f_pomoc1[z] * f_pomoc1[v] * waga1) + (alfa * f_pomoc2[z] * f_pomoc2[v] * waga2)+ (alfa * f_pomoc3[z] * f_pomoc3[v] * waga3)) * detJ; // a tutaj wrzucam te wartosci do calego wzoru
                    }
                }

                for (int z = 0; z < 4; z++) {
                    f_pomoc1[z] = f_pomoc1[z] * temp * waga1;
                    f_pomoc2[z] = f_pomoc2[z] * temp * waga2;
                    f_pomoc3[z] = f_pomoc3[z] * temp * waga3;
                    elements[i].P_3pkt[z] += (f_pomoc1[z] + f_pomoc2[z] + f_pomoc3[z]) * detJ * alfa;
                }
            }

            if (nodes[(elements[i].ID[3]) - 1].BC == 1 && nodes[(elements[i].ID[0]) - 1].BC == 1) {
                double detJ = dY / 2;


                f_pomoc1[0] = funkcjaKsztaltu1(pc.getPc4(0, 0), pc.getPc4(0, 1));
                f_pomoc1[1] = funkcjaKsztaltu2(pc.getPc4(0, 0), pc.getPc4(0, 1));
                f_pomoc1[2] = funkcjaKsztaltu3(pc.getPc4(0, 0), pc.getPc4(0, 1));
                f_pomoc1[3] = funkcjaKsztaltu4(pc.getPc4(0, 0), pc.getPc4(0, 1));

                f_pomoc2[0] = funkcjaKsztaltu1(pc.getPc4(1, 0), pc.getPc4(1, 1));
                f_pomoc2[1] = funkcjaKsztaltu2(pc.getPc4(1, 0), pc.getPc4(1, 1));
                f_pomoc2[2] = funkcjaKsztaltu3(pc.getPc4(1, 0), pc.getPc4(1, 1));
                f_pomoc2[3] = funkcjaKsztaltu4(pc.getPc4(1, 0), pc.getPc4(1, 1));

                f_pomoc3[0] = funkcjaKsztaltu1(pc.getPc4(2, 0), pc.getPc4(2, 1));
                f_pomoc3[1] = funkcjaKsztaltu2(pc.getPc4(2, 0), pc.getPc4(2, 1));
                f_pomoc3[2] = funkcjaKsztaltu3(pc.getPc4(2, 0), pc.getPc4(2, 1));
                f_pomoc3[3] = funkcjaKsztaltu4(pc.getPc4(2, 0), pc.getPc4(2, 1));

                for (int z = 0; z < 4; z++) {
                    for (int v = 0; v < 4; v++) {
                        elements[i].Hbc_3pkt[z][v] += ((alfa * f_pomoc1[z] * f_pomoc1[v] * waga1) + (alfa * f_pomoc2[z] * f_pomoc2[v] * waga2)+ (alfa * f_pomoc3[z] * f_pomoc3[v] * waga3)) * detJ; // a tutaj wrzucam te wartosci do calego wzoru
                    }
                }

                for (int z = 0; z < 4; z++) {
                    f_pomoc1[z] = f_pomoc1[z] * temp * waga1;
                    f_pomoc2[z] = f_pomoc2[z] * temp * waga2;
                    f_pomoc3[z] = f_pomoc3[z] * temp * waga3;
                    elements[i].P_3pkt[z] += (f_pomoc1[z] + f_pomoc2[z] + f_pomoc3[z]) * detJ * alfa;
                }
            }
        }
    }

    public void Hzagregowane(){
        for (int i = 0; i < nE; i++) {
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {//wybieramy konkretne wartosci ID a nastepnie dopisywane sa wartosci z elem.H lokalnego
                    Hglobalne[(elements[i].ID[x])-1][(elements[i].ID[y])-1] += elements[i].H_sum[x][y];
                }
            }
        }
    }

    public void Hzagregowane_3pkt(){
        for (int i = 0; i < nE; i++) {
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                    Hglobalne_3pkt[(elements[i].ID[x])-1][(elements[i].ID[y])-1] += elements[i].H_sum_3pkt[x][y];
                }
            }
        }
    }

    public void HBC_plus_Hzagregowane(){
        for (int i = 0; i < nE; i++) {
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                    HBCsumHglobalne[(elements[i].ID[x]-1)][(elements[i].ID[y]-1)] += elements[i].H_sum[x][y] + elements[i].Hbc[x][y];
                }
            }
        }
    }

    public void HBC_plus_Hzagregowane_3pkt(){
        for (int i = 0; i < nE; i++) {
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                    HBCsumHglobalne_3pkt[(elements[i].ID[x]-1)][(elements[i].ID[y]-1)] += elements[i].H_sum_3pkt[x][y] + elements[i].Hbc_3pkt[x][y];
                }
            }
        }
    }

    public void Pzagregowane(){
        for(int i = 0; i < nE; i++){
            for(int x = 0; x < 4; x++){//wybieramy konkretne wartosci ID a nastepnie dopisywane sa wartosci z elem.P lokalnego
                Pglobalne[(elements[i].ID[x])-1] += elements[i].P[x];
            }
        }
    }
    public void Pzagregowane_3pkt(){
        for(int i = 0; i < nE; i++){
            for(int x = 0; x < 4; x++){
                Pglobalne_3pkt[(elements[i].ID[x])-1] += elements[i].P_3pkt[x];
            }
        }
    }

    public void Czagregowane(){
        for (int i = 0; i < nE; i++) {
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {//wybieramy konkretne wartosci ID a nastepnie dopisywane sa wartosci z elem.C lokalnego
                    Cglobalne[(elements[i].ID[x]-1)][(elements[i].ID[y]-1)] += elements[i].C[x][y] ;
                }
            }
        }
    }
    public void Czagregowane_3pkt(){
        for (int i = 0; i < nE; i++) {
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                    Cglobalne_3pkt[(elements[i].ID[x]-1)][(elements[i].ID[y]-1)] += elements[i].C_3pkt[x][y] ;
                }
            }
        }
    }


    public void matrixH(int steptime){ //matrix H=[H]+[C]/dtał
        for (int i=0; i<nN;i++){
            for(int j=0; j<nN;j++){
                H_with_time_step[i][j]=HBCsumHglobalne[i][j]+Cglobalne[i][j]/steptime;
            }
        }
    }

    public void matrixH_3pkt(int steptime){ //matrix H=[H]+[C]/dtał
        for (int i=0; i<nN;i++){
            for(int j=0; j<nN;j++){
                H_with_time_step_3pkt[i][j]=HBCsumHglobalne_3pkt[i][j]+Cglobalne_3pkt[i][j]/steptime;
            }
        }
    }

    public void vectorP(int steptime){ //P={P}+{[C]/stemtime} * {T0}
        double []CprzezSteptimeRazyT0=new double[nN];

        for (int i=0; i<nN; i++){
            for (int j=0; j<nN;j++) {
                CprzezSteptimeRazyT0[i] += (Cglobalne[i][j] / steptime) * nodes[j].temp;
            }
        }
        for (int i=0; i<nN;i++){
            P_with_time_step[i]=Pglobalne[i] + CprzezSteptimeRazyT0[i];
        }
    }

    public void vectorP_3pkt(int steptime){ // P={P}+{[C]/stemtime} * {T0}
        double []CprzezSteptimeRazyT0=new double[nN];

        for (int i=0; i<nN; i++){
            for (int j=0; j<nN;j++) {
                CprzezSteptimeRazyT0[i] += (Cglobalne_3pkt[i][j] / steptime) * nodes[j].temp;
            }
        }
        for (int i=0; i<nN;i++){
            P_with_time_step_3pkt[i]=Pglobalne_3pkt[i] + CprzezSteptimeRazyT0[i];
        }
    }

    public void simulation(int simulationTime, int stepTime) {
        for (int i = 0; i < (simulationTime / stepTime); i++) {
            System.out.format("\nIteracja %d, Time[s] %d\n", i, ((i * stepTime) + stepTime));

            vectorP(stepTime);
            matrixH(stepTime);

            double[] temp = new double[nN];
            GaussElimination temperatures = new GaussElimination();

            double[][] A = new double[nN][nN];
            A =  H_with_time_step;
            double[] B = new double[nN];
            B = P_with_time_step;

            temp = temperatures.solve(A, B);
            for (int j = 0; j < nN; j++) {
                System.out.format("%.3f\t", temp[j]);
            }

            for (int j = 0; j < nN; j++) {
                nodes[j].temp = temp[j];
            }

            double minValue = temp[0];
            double maxValue = temp[0];
            for (int j = 0; j < nN; j++) {
                if (minValue > temp[j]) {
                    minValue = temp[j];
                }
                if (maxValue < temp[j]) {
                    maxValue = temp[j];
                }
            }
            System.out.format("\nMinTemp[s]: %.3f \tMaxTemp[s]: %.3f\n", minValue, maxValue);
        }
     }


    public void simulation_3pkt(int simulationTime, int stepTime) {
        for (int i = 0; i < (simulationTime / stepTime); i++) {
            System.out.format("Iteracja %d, Time[s] %d\n", i, ((i * stepTime) + stepTime));

            vectorP_3pkt(stepTime);
            matrixH_3pkt(stepTime);

            double[] temp = new double[nN];
            GaussElimination temperatures = new GaussElimination();

            double[][] A = new double[nN][nN];
            A =  H_with_time_step_3pkt;
            double[] B = new double[nN];
            B = P_with_time_step_3pkt;

            temp = temperatures.solve(A, B);
            for (int j = 0; j < nN; j++) {
                System.out.format("%.2f\t", temp[j]);
            }

            for (int j = 0; j < nN; j++) {
                nodes[j].temp = temp[j]; //przypisujemy jako temperaruty poczatkowe temperatury obliczone z ukladu równań
            }

            double minValue = temp[0];
            double maxValue = temp[0];
            for (int j = 0; j < nN; j++) {
                if (minValue > temp[j]) {
                    minValue = temp[j];
                }
                if (maxValue < temp[j]) {
                    maxValue = temp[j];
                }
            }
            System.out.format("\nMinTemp[s]: %.3f \tMaxTemp[s]: %.3f\n", minValue, maxValue);
        }
    }
}


