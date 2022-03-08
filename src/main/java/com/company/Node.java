package com.company;

public class Node {
    double x;
    double y;
    int BC;
    double temp;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
        this.temp = 0;
        this.BC = 0;
    }

    public void setTemp(double t){temp=t;}
    public void setBC(int BC){
        this.BC=BC;
    }
}
