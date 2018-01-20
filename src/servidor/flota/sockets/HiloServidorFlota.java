package servidor.flota.sockets;


import java.io.IOException;
import java.net.SocketException;

import partida.flota.sockets.*;
import comun.flota.sockets.MyStreamSocket;

/**
 * Clase ejecutada por cada hebra encargada de servir a un cliente del juego Hundir la flota.
 * El metodo run contiene la logica para gestionar una sesion con un cliente.
 */

// Revisar el apartado 5.5. del libro de Liu

class HiloServidorFlota implements Runnable {
    MyStreamSocket myDataSocket;
    private Partida partida = null;

    /**
     * Construye el objeto a ejecutar por la hebra para servir a un cliente
     *
     * @param    myDataSocket    socket stream para comunicarse con el cliente
     */
    HiloServidorFlota(MyStreamSocket myDataSocket) {

        this.myDataSocket = myDataSocket;

    }

    /**
     * Gestiona una sesion con un cliente
     */
    public void run() {
        boolean done = false;
        int operacion;

        try {

            while (!done) {
                String message = myDataSocket.receiveMessage();
                String[] parts = message.split("#");
                operacion = Integer.parseInt(parts[0]);// Recibe una peticion del cliente
                // Extrae la operación y los argumentos

                switch (operacion) {
                    case 0:  // fin de conexión con el cliente
                        myDataSocket.close();
                        done = true;
                        break;

                    case 1: { // Crea nueva partida
                        int nf = Integer.parseInt(parts[1]);
                        int nc = Integer.parseInt(parts[2]);
                        int nb = Integer.parseInt(parts[3]);
                        partida = new Partida(nf, nc, nb);
                        myDataSocket.sendMessage("Nueva partida creada");
                        break;
                    }
                    case 2: { // Prueba una casilla y devuelve el resultado al cliente

                        int f = Integer.parseInt(parts[1]);
                        int c = Integer.parseInt(parts[2]);
                        myDataSocket.sendMessage(String.valueOf(partida.pruebaCasilla(f, c)));

                        break;
                    }
                    case 3: { // Obtiene los datos de un barco y se los devuelve al cliente
                        // ...
                        int id = Integer.parseInt(parts[1]);
                        myDataSocket.sendMessage(partida.getBarco(id));
                        break;
                    }
                    case 4: { // Devuelve al cliente la solucion en forma de vector de cadenas
                        // Primero envia el numero de barcos
                        // Despues envia una cadena por cada barco

                        String[] sol = partida.getSolucion();
                        myDataSocket.sendMessage(String.valueOf(sol.length));
                        for(String barco : sol) {
                            myDataSocket.sendMessage(barco);
                        }
                        break;
                    }
                } // fin switch
            } // fin while
        } // fin try
        catch (Exception ex) {
            System.out.println("Exception caught in thread: " + ex);
        } // fin catch
    } //fin run

} //fin class 
