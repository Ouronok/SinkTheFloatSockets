package servidor.flota.sockets;

import java.net.ServerSocket;

import comun.flota.sockets.MyStreamSocket;

/**
 * Este modulo contiene la logica de aplicacion del servidor del juego Hundir la flota
 * Utiliza sockets en modo stream para llevar a cabo la comunicacion entre procesos.
 * Puede servir a varios clientes de modo concurrente lanzando una hebra para atender a cada uno de ellos.
 * Se le puede indicar el puerto del servidor en linea de ordenes.
 */

//Acepta conexiones vía socket de distintos clientes.
// Por cada conexión establecida lanza una hebra de la clase HiloServidorFlota.


// Revisad el apartado 5.5 del libro de Liu

public class ServidorFlotaSockets {

    public static void main(String[] args) {

        int puertoServidor =1090; // puerto por defecto
                                    //String message;

        if (args.length == 1) {
            puertoServidor = Integer.parseInt(args[0]);
        }
        try {
            //instancia un socket stream para aceptar las conexiones
            ServerSocket miSocketconexion = new ServerSocket(puertoServidor);
            System.out.println("Servidor Echo listo");

            while (true) {//bucle infinito de conexiones
                //espera una conexion
                System.out.println("Esperando conexion");

                MyStreamSocket miSocketDatos = new MyStreamSocket(miSocketconexion.accept());

                System.out.println("Conexion Aceptada");

                //ARRANCAMOS UN HILO PARA CADA CLIENTE

                Thread hilo = new Thread(new HiloServidorFlota(miSocketDatos));
                hilo.start();

                // continua el otro cliente

            }//fin del while infinito

        }//fin de try
        catch (Exception ex) {
            ex.printStackTrace();
        }//fin de catch
    } //fin main
} // fin class
