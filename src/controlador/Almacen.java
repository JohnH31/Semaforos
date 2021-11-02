/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import modelo.Gestion;
import vista.Texto;

/**
 *
 * @author John
 */
public class Almacen {

    Texto vista = new Texto();
    int MAX = 10;
    int producto = 0;
    int cajas = 0;
    String caja;
    private Semaphore Empaquetador = new Semaphore(MAX);
    private Semaphore Colocador = new Semaphore(0);
    private Semaphore mutex = new Semaphore(1);
    String nombreProductor = "Empaquetador";
    String nombreConsumidor = "Colocador";

    public Almacen(Texto vista) {
        this.vista = vista;
        caja = JOptionPane.showInputDialog("Ingrese el numero de Cajas");
            
        while (true) {
            this.producir();
            this.consumir();
        }
    }

    public void producir() {
        vista.jtaEditar.append(nombreProductor + " intentando preparar una botella \n");
        for (int i = 1; i < 2; i++) {
            try {
                Empaquetador.acquire();
                mutex.acquire();
vista.jtaEditar.append(nombreProductor + " Entregando una botella. \n");
                mutex.release();

                Thread.sleep(500);

            } catch (InterruptedException ex) {
                Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                Colocador.release();
            }

        }

    }

    public void consumir() {
        if (producto == 10) {
            vista.jtaEditar.append("'*Caja llena, retirando caja y almacenando' \n");
            producto = 0;
            cajas++;
            vista.jtaEditar.append(cajas + (cajas > 1 ? " Cajas Almacenadas \n":" Cajas Almacenada \n"));
        } else if (cajas == Integer.parseInt(caja)) {
            vista.jtaEditar.append(cajas + " Cajas Almacenadas \n");
            vista.jtaEditar.append("Se finaliza proceso ya que no hay cajas para empaquetar \n");
            int men = JOptionPane.showConfirmDialog(null, "Desea guardar el proceso?", "pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (men == JOptionPane.YES_OPTION) {
                this.Guardar();
            } else if (men == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
        } else {
            vista.jtaEditar.append(nombreConsumidor + " intentando colocar una botella \n");
            for (int i = 1; i < 2; i++) {
                try {
                    Colocador.acquire();
                    mutex.acquire();

                    producto++;
                    vista.jtaEditar.append(nombreConsumidor + " botella colocada en la caja. "
                            + "Caja con " + producto + (producto > 1 ? " litros. \n" : " litro. \n"));
                    mutex.release();

                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    Empaquetador.release();

                }
            }

        }

    }
    File archivo;
    Gestion ges = new Gestion();

    public void Guardar() {
        if (vista.jfcMenu.showDialog(null, "Guardar archivo") == JFileChooser.APPROVE_OPTION) {
            archivo = vista.jfcMenu.getSelectedFile();
            if (archivo.getName().endsWith("txt")) {
                String contenido = vista.jtaEditar.getText();
                String respuesta = ges.GuardarTexto(archivo, contenido);
                if (respuesta != null) {
                    JOptionPane.showMessageDialog(null, respuesta);
                } else {
                    JOptionPane.showMessageDialog(null, "error al guardar txt");
                }
            } else {
                JOptionPane.showMessageDialog(null, "el texto se debe guardar en un formato de texto");
            }
        }
    }

}
