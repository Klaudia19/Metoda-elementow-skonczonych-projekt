package com.company;
import java.util.Scanner;

public class Main{

    static public void main(String [] args) {

        double H = 0.1;         //wysokosc [m] inicjalizacja
        double B = 0.1;         //szerokosc [m]
        int nH = 4;             //l. wezlow w pione
        int nB = 4;             //liczba węzłów w poziomie
        int k = 25;            //wsp. przewodzenia ciepla [W/(m0C)]
        int alfa = 300;         //wsp. konwekcji [W/m2K]
        int temp = 1200;        //temperatura otoczenia [C]
        int ro = 7800;          //gęstość [kg/m3]
        int c = 700;            //ciepło właściwe [J/(kgC)]
        int simulation_step_time = 50;       //czas kroku symulacji [s]
        int simulation_time = 500; //simulation time [s]
        double t0 = 100;          //temp. poczatkowa


        Scanner scan = new Scanner(System.in);
        Grid grid = new Grid(B, H, nB, nH);
        Element2D_4 e = new Element2D_4();
        Element2D_9 elem = new Element2D_9();
        Jakobian jak = new Jakobian();
        Jakobian_3pkt jako = new Jakobian_3pkt();
        Pkt pc = new Pkt();
        Pkt3 pc3 = new Pkt3();

        System.out.println("1. Schemat 2D 2-punktowy.");
        System.out.println("2. Schemat 2D 3-punktowy.");
        int choice = scan.nextInt();

        if (choice == 1) {

            grid.dodajWez();
            System.out.println();
            grid.dodajElem();

            for (int i = 0; i < grid.nE; i++) {
                    jak.Jakobian(i,grid, e);
                }
                jak.pochNprzezpochXandY(e);


            for(int i=0; i<grid.nE;i++){
                grid.elements[i].H(jak,k);
            }

            grid.Hbc(pc, alfa, temp);

            for(int i=0; i<grid.nE;i++){
                grid.elements[i].C(jak, e, c, ro);
            }

                grid.Hzagregowane();
                grid.HBC_plus_Hzagregowane();
                grid.Pzagregowane();
                grid.Czagregowane();


            for (int i =0; i<grid.nN; i++){
                grid.nodes[i].setTemp(t0);
            }

            grid.matrixH(simulation_step_time);
            grid.vectorP(simulation_step_time);
            grid.simulation(simulation_time,simulation_step_time);
        }

        else if (choice == 2) {

            grid.dodajWez();
            System.out.println();
            grid.dodajElem();

            for (int i = 0; i < grid.nE; i++) {
                    jako.Jakobian(i,grid,elem);
            }
            jako.pochNprzezpocXandY(elem);

            for(int i=0; i<grid.nE;i++){
                grid.elements[i].H_3pkt(jako,k);
            }

            for(int i=0; i<grid.nE;i++){
                grid.elements[i].C_3pkt(jako,elem,ro, c);
          }

            grid.Hbc_3pkt(pc3, alfa, temp);
            grid.Hzagregowane_3pkt();
            grid.HBC_plus_Hzagregowane_3pkt();
            grid.Pzagregowane_3pkt();
            grid.Czagregowane_3pkt();


            for (int i =0; i<grid.nN; i++){
                grid.nodes[i].setTemp(t0);
            }

            grid.matrixH_3pkt(simulation_step_time);
            grid.vectorP_3pkt(simulation_step_time);
            grid.simulation_3pkt(simulation_time,simulation_step_time);
        }
      }
    }


