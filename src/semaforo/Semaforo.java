/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semaforo;


import controlador.Almacen;
import modelo.Gestion;
import vista.Texto;

/**
 *
 * @author John
 */
public class Semaforo {

    /**
     * @param args the command line arguments
     */
     
    public static void main(String[] args) {
        Texto vista = new Texto();
        Gestion ges = new Gestion();
        vista.setVisible(true);
        Almacen almacen = new Almacen(vista);
    }
}
