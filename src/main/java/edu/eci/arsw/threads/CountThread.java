/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThread extends  Thread{
    int min;
    int max;
    CountThread(int min, int max){
        this.min = min;
        this.max = max;
    }
    public  void run(){
        for (int i = min;i<max;i++)System.out.println(i);
    }

}