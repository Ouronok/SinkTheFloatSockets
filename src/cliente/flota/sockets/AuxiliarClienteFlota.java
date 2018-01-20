
package cliente.flota.sockets;
import java.net.*;
import java.io.*;

import comun.flota.sockets.*;

/**
 * Esta clase implementa el intercambio de mensajes
 * asociado a cada una de las operaciones basicas que comunican cliente y servidor
 */

public class AuxiliarClienteFlota {
	/*Sera el encargado de comunicarse mediante en el envio de mensajes ( cadenas String) , con el servidor*/
	
   private MyStreamSocket mySocket;
   private InetAddress serverHost;
   private int serverPort;

	/**
	 * Constructor del objeto auxiliar del cliente
	 * Crea un socket de tipo 'MyStreamSocket' y establece una conexión con el servidor
	 * 'hostName' en el puerto 'portNum'
	 * @param	hostName	nombre de la máquina que ejecuta el servidor
	 * @param	portNum		numero de puerto asociado al servicio en el servidor
	 */
   //obtenemos los parametros necesarios y establecemos la conexion
   AuxiliarClienteFlota(String hostName,
                     String portNum) throws SocketException,
                     UnknownHostException, IOException {
	   this.serverHost = InetAddress.getByName(hostName);
	   this.serverPort = Integer.parseInt(portNum);
	   this.mySocket = new MyStreamSocket(serverHost, serverPort);
	   System.out.println("Conexion establecida");
	   
   } // end constructor
   
   /**
	 * Usa el socket para enviar al servidor una petición de fin de conexión
	 * con el formato: "0"
	 * @throws	IOException
	 */
   
   public void fin( ) throws IOException {
	  try {
	  mySocket.sendMessage("0");}
	  catch (Exception e) {
		   System.out.println("No hay conexion con el servidor, producido en fin");
		   /*lanzamos dicho mensaje por si tras haber apagado el servido , el cliente sigue 
		   intentando enviar mensajes a este, repetimos este procedimiento en los dem�s m�todos*/
		   System.exit(-1);
		// TODO: handle exception
	}
	   
   } // end fin 
  
   /**
    * Usa el socket para enviar al servidor una petición de creación de nueva partida 
    * con el formato: "1#nf#nc#nb"
    * @param nf	número de filas de la partida
    * @param nc	número de columnas de la partida
    * @param nb	número de barcos de la partida
    * @throws IOException
    */
   public void nuevaPartida(int nf, int nc, int nb)  throws IOException {
	   try{mySocket.sendMessage("1#"+nf+"#"+nc+"#"+nb);
	   System.out.println(mySocket.receiveMessage());}
	   catch (Exception e) {
		   System.out.println("No hay conexion con el servidor, producido en nueva partida");
		   System.exit(-1);
		// TODO: handle exception
	}
	   
   } // end nuevaPartida

   /**
    * Usa el socket para enviar al servidor una petición de disparo sobre una casilla 
    * con el formato: "2#f#c"
    * @param f	fila de la casilla
    * @param c	columna de la casilla
    * @return	resultado del disparo devuelto por la operación correspondiente del objeto Partida
    * 			en el servidor.
    * @throws IOException
    */
   public int pruebaCasilla(int f, int c) throws IOException {
	   try { mySocket.sendMessage("2#"+f+"#"+c);
	   return Integer.parseInt(mySocket.receiveMessage());}catch (Exception e) {
		   System.out.println("no hay conexion con el servidor, producido en pruebacasilla");
		   System.exit(-1);
		// TODO: handle exception
	}
	   
	  return -1;
	   
	   
	   
    } // end getCasilla
   
   /**
    * Usa el socket para enviar al servidor una petición de los datos de un barco
    * con el formato: "3#idBarco"
    * @param idBarco	identidad del Barco
    * @return			resultado devuelto por la operación correspondiente del objeto Partida
    * 					en el servidor.
    * @throws IOException
    */
   public String getBarco(int idBarco) throws IOException {
	   
	   mySocket.sendMessage("3#"+idBarco);
	   return mySocket.receiveMessage();
	   
    } // end getCasilla
   
   /**
    * Usa el socket para enviar al servidor una petición de los datos de todos los barcos
    * con el formato: "4"
    * @return	resultado devuelto por la operación correspondiente del objeto Partida
    * 			en el servidor
    * @throws IOException
    */
   public String[] getSolucion() throws IOException {
	   
	   
	   try {
		 
	   mySocket.sendMessage("4");
	   int numBarcos = Integer.parseInt(mySocket.receiveMessage());
	   String[] solucion = new String[numBarcos];
	   
	   for(int i = 0;i<numBarcos;i++) {
		   solucion[i]=mySocket.receiveMessage();
	   }
	   
	   return solucion;
	   }catch (Exception e) {
		   System.out.println("no hay conexion con el servidor, producido en getSolucion");
		   System.exit(-1);
		// TODO: handle exception
	}
	  return null;
    } // end getSolucion
   


} //end class
