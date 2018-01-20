package cliente.flota.sockets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class ClienteFlotaSockets {
	
	//se trata de la clase juego de la practica anterior y se ejecutar� en local

    /**
     * Implementa el juego 'Hundir la flota' mediante una interfaz gráfica (GUI)
     */

    /**
     * Parametros por defecto de una partida
     */
    public static final int NUMFILAS = 8, NUMCOLUMNAS = 8, NUMBARCOS = 6;

    private GuiTablero guiTablero = null;
    private AuxiliarClienteFlota auxCon = null; // Objeto con los datos de la partida en
    // juego

    /**
     * Atributos de la partida guardados en el juego para simplificar su
     * implementación
     */
    private int quedan = NUMBARCOS, disparos = 0;

    /**
     * Programa principal. Crea y lanza un nuevo juego
     *
     * @param args
     */
    public static void main(String[] args) {
        ClienteFlotaSockets juego = new ClienteFlotaSockets();
        juego.conecta();

    } // end main

    /*hemos modificado el m�todo ejecuta como lo denominamos en la practica anterior y 
    ahora se denominar� conecta, un m�todo que creara un auxiliar cliente , que ser� la 
    clase que se comunicar� con el servidor para crear una nueva partida, nuestro m�todo
     conecta tambien crear� el tablero en local*/
    private void conecta() {
        try {
            auxCon = new AuxiliarClienteFlota("localhost", "1090");
            auxCon.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
            SwingUtilities.invokeLater(() -> {
                guiTablero = new GuiTablero(NUMFILAS, NUMCOLUMNAS);
                guiTablero.dibujaTablero();
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Lanza una nueva hebra que crea la primera partida y dibuja la interfaz
     * grafica: tablero

     private void ejecuta() {
     // Instancia la primera partida
     partida = new Partida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);

     });
     } // end ejecuta
     */


    /******************************************************************************************/
    /********************* * CLASE INTERNA GuiTablero * ***************************************/
    /******************************************************************************************/
    private class GuiTablero {

        private int numFilas, numColumnas;

        private JFrame frame = null; // Tablero de juego
        private JLabel estado = null; // Texto en el panel de estado
        private JButton buttons[][] = null; // Botones asociados a las casillas
        // de la partida

        /**
         * Constructor de una tablero dadas sus dimensiones
         */
        GuiTablero(int numFilas, int numColumnas) {
            this.numFilas = numFilas;
            this.numColumnas = numColumnas;
            frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }

        /**
         * Dibuja el tablero de juego y crea la partida inicial
         */
        public void dibujaTablero() {
            anyadeMenu();
            anyadeGrid(numFilas, numColumnas);
            anyadePanelEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
            frame.setSize(300, 300);
            frame.setVisible(true);
        } // end dibujaTablero

        /**
         * Anyade el menu de opciones del juego y le asocia un escuchador
         */
        private void anyadeMenu() { //COMENTAR
            MenuListener mlist = new MenuListener();
            JMenuBar menuBar;
            JMenu menu;
            JMenuItem mossol, npar, salir;
            menuBar = new JMenuBar();
            menu = new JMenu("Opciones");
            menuBar.add(menu);
            mossol = new JMenuItem("Mostrar Solucion");
            npar = new JMenuItem("Nueva Partida");
            salir = new JMenuItem("Salir");
            menu.add(mossol);
            menu.add(npar);
            menu.add(salir);
            mossol.addActionListener(mlist);
            npar.addActionListener(mlist);
            salir.addActionListener(mlist);
            frame.getContentPane().add(menuBar, BorderLayout.NORTH);
        } // end anyadeMenu

        /**
         * Anyade el panel con las casillas del mar y sus etiquetas. Cada
         * casilla sera un boton con su correspondiente escuchador
         *
         * @param nf numero de filas
         * @param nc numero de columnas
         */
        private void anyadeGrid(int nf, int nc) { //COMENTAR
            buttons = new JButton[nf][nc];
            ButtonListener blist = new ButtonListener();
            GridLayout centroLay = new GridLayout(nf + 1, nc + 2);
            JPanel centro = new JPanel();
            centro.setLayout(centroLay);
            centro.add(new JLabel(" "));
            for (int i = 1; i <= nc; i++) {
                centro.add(new JLabel(Integer.toString(i), SwingConstants.CENTER));
            }
            centro.add(new JLabel(" "));
            char letra = 'A';
            for (int i = 0; i < nf; i++) {
                centro.add(new JLabel(String.valueOf((letra)), SwingConstants.CENTER));

                for (int n = 0; n < nc; n++) {
                    buttons[i][n] = new JButton();
                    buttons[i][n].addActionListener(blist);
                    buttons[i][n].putClientProperty("fila", i);
                    buttons[i][n].putClientProperty("columna", n);
                    centro.add(buttons[i][n]);
                }
                centro.add(new JLabel(String.valueOf((letra)), SwingConstants.CENTER));
                letra++;
            }

            frame.getContentPane().add(centro, BorderLayout.CENTER);

        }

        /**
         * Anyade el panel de estado al tablero
         *
         * @param cadena cadena inicial del panel de estado
         */
        private void anyadePanelEstado(String cadena) { // DONE
            JPanel panelEstado = new JPanel();
            estado = new JLabel(cadena);
            panelEstado.add(estado);
            // El panel de estado queda en la posición SOUTH del frame
            frame.getContentPane().add(panelEstado, BorderLayout.SOUTH);
        } // end anyadePanel Estado

        /**
         * Cambia la cadena mostrada en el panel de estado
         *
         * @param cadenaEstado nuevo estado
         */
        public void cambiaEstado(String cadenaEstado) { // DONE
            estado.setText(cadenaEstado);
        } // end cambiaEstado

        /**
         * Muestra la solucion de la partida y marca la partida como finalizada
         */
        public void muestraSolucion() {
            Color colorAzul = new Color(0, 100, 255);// clase Color de Java, utilizada para pintar botones
            // POR IMPLEMENTAR
            String[] cadenaBarcos;
            try {
                cadenaBarcos = auxCon.getSolucion();

                int nf = this.numFilas;
                int nc = this.numColumnas;
                // pinto todos los botones de azul

                for (int i = 0; i < nf; i++) {

                    for (int j = 0; j < nc; j++) {
                        pintaBoton(buttons[i][j], colorAzul);

                    }
                }
                for (String barco : cadenaBarcos) {

                    pintaBarcoHundido(barco);// llamada a pintarBarcoHundido
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } // end muestraSolucion

        /**
         * Pinta un barco como hundido en el tablero
         *
         * @param cadenaBarco cadena con los datos del barco codifificados como
         *                    "filaInicial#columnaInicial#orientacion#tamanyo"
         */
        public void pintaBarcoHundido(String cadenaBarco) {
            // Una vez hundido se encarga de pintarlo segun orientacion
            String[] partes = cadenaBarco.split("#");

            int fInicial = Integer.parseInt(partes[0]);
            int cInicial = Integer.parseInt(partes[1]);
            String orientacion = partes[2];
            int tamayo = Integer.parseInt(partes[3]);

            if (orientacion.equals("V")) {
                for (int i = 0; i < tamayo; i++) {
                    pintaBoton(buttons[fInicial + i][cInicial], Color.RED);
                }
            } else {
                for (int i = 0; i < tamayo; i++) {
                    pintaBoton(buttons[fInicial][cInicial + i], Color.RED);
                }
            }
        }

        // end pintaBarcoHundido

        /**
         * Pinta un botón de un color dado
         *
         * @param b     boton a pintar
         * @param color color a usar
         */
        public void pintaBoton(JButton b, Color color) {
            b.setBackground(color);
            // El siguiente código solo es necesario en Mac OS X
            b.setOpaque(true);
            b.setBorderPainted(false);
        } // end pintaBoton

        /**
         * Limpia las casillas del tablero pintándolas del gris por defecto
         */
        public void limpiaTablero() {
            for (int i = 0; i < numFilas; i++) {
                for (int j = 0; j < numColumnas; j++) {
                    buttons[i][j].setBackground(null);
                    buttons[i][j].setOpaque(true);
                    buttons[i][j].setBorderPainted(true);
                }
            }
        } // end limpiaTablero

        /**
         * Destruye y libera la memoria de todos los componentes del frame
         */
        public void liberaRecursos() {
            frame.dispose();
        } // end liberaRecursos

    } // end class GuiTablero

    /******************************************************************************************/
    /********************* * CLASE INTERNA MenuListener * ***************************************/
    /******************************************************************************************/

    /**
     * Clase interna que escucha el menu de Opciones del tablero
     */
    private class MenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) { // Segun elemento menu
            // realiza accion
            JMenuItem elem = (JMenuItem) e.getSource();
            String texto = elem.getText();
            if (texto.equals("Mostrar Solucion")) {
                guiTablero.muestraSolucion();
            } else if (texto.equals("Nueva Partida")) {
                try {
                    auxCon.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
                    guiTablero.limpiaTablero();
                    quedan = NUMBARCOS;
                    disparos = 0;
                    guiTablero.cambiaEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else if (texto.equals("Salir")) {
                guiTablero.liberaRecursos();
                try {
                    auxCon.fin();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                System.exit(0);
            }

        } // end actionPerformed

    } // end class MenuListener

    /******************************************************************************************/
    /********************* * CLASE INTERNA ButtonListener * *************************************/
    /******************************************************************************************/
    /**
     * Clase interna que escucha cada uno de los botones del tablero Para poder
     * identificar el boton que ha generado el evento se pueden usar las
     * propiedades de los componentes, apoyandose en los metodos
     * putClientProperty y getClientProperty
     */
    private class ButtonListener implements ActionListener { // Segun casilla la
        // prueba si no
        // ha sido ya
        // tocada

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton boton = new JButton();
            Color colorDefault = boton.getBackground();
            boton = (JButton) e.getSource();

            if (quedan > 0 && boton.getBackground().equals(colorDefault)) {
                int fila = (int) boton.getClientProperty("fila");
                int columna = (int) boton.getClientProperty("columna");
                int casilla;
                try {
                    casilla = auxCon.pruebaCasilla(fila, columna);
                    if (casilla == -1)
                        guiTablero.pintaBoton(boton, Color.CYAN);
                    else if (casilla == -2)
                        guiTablero.pintaBoton(boton, Color.ORANGE);
                    else {
                        String barco = auxCon.getBarco(casilla);
                        guiTablero.pintaBarcoHundido(barco);
                        quedan--;
                    }
                    disparos++;

                    if (quedan == 0) {
                        guiTablero.cambiaEstado("Game Over con disparos: " + disparos);

                    } else {
                        guiTablero.cambiaEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
                    }
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }

            }
            // end actionPerformed

        } // end class ButtonListener
    }
}// end class Juego
