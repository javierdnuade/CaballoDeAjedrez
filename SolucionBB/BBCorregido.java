package SolucionBB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import impl.ColaPrioridadDA;

public class BBCorregido {

    static int paso_MAX;
    static final int MAX_SALTOS = 35000;
    static boolean corteGlobal = false;
    static int mejorRotacionGlobal = -1;
    //static List<int[][]> mejoresSoluciones = new ArrayList<>();
    static List<Integer> mejoresSolucionesRotaciones = new ArrayList<>();
    static int cantidadMejoresSoluciones = 0;
    static int contadorSaltos;
    static int mejorSolucionSaltos = Integer.MAX_VALUE;
    static int[][] mejorSolucion;

    // 🔹 PESOS Y FASES (HEURÍSTICAS COMPUESTAS)
    static final int PESO_WARNSDORFF = 1_000;
    static final int PESO_CENTRO = 100;
    static final int PESO_ORDEN = 1;
    static final double FASE_BORDE = 0.8; // % del recorrido. Se puede modificar

    public static boolean solve(int N, int startX, int startY) {
        mejorSolucion = null;
        mejorSolucionSaltos = Integer.MAX_VALUE;

        int[] dx = {-2, 2, -1, -2, 2, -1, 1, 1};
        int[] dy = {-1, 1, 2, 1, -1, -2, -2, 2};

        for (int rot = 0; rot < 8; rot++) {
            System.out.println("\n Ejecutando rotación #" + (rot + 1));
            System.out.println(" Vector dy: " + Arrays.toString(dx));
            System.out.println(" Vector dx: " + Arrays.toString(dy));

            int[][] board = new int[N][N];
            for (int[] fila : board) Arrays.fill(fila, -1);

            if (!enRango(startX, startY, N)) return false;

            board[startX][startY] = 0;
            contadorSaltos = 0;
            paso_MAX = -1;
            corteGlobal = false;

            System.out.println("Contador de saltos reiniciado: " + contadorSaltos);
            boolean rotacionResultado= resolver(board, N, startX, startY, 1, rot, dx, dy);
            System.out.println("Contador de saltos final: " + contadorSaltos);

            if (corteGlobal) {
                System.out.println("Se alcanzó el límite máximo de " + MAX_SALTOS + " saltos en la rotacion " + (rot+1) +  " .Paso máximo: "  + paso_MAX);
            }

            if (rotacionResultado) {
                System.out.println("Solución completa encontrada en la rotación #" + (rot + 1));
            } else {
                System.out.println("No se encontró solución completa en la rotación #" + (rot + 1) + " Saltos totales hechos: " + contadorSaltos);
            }

            // Rotamos los movimientos una posición a la izquierda    
            rotarArray(dx);
            rotarArray(dy);

        }

        if (mejorSolucion != null) {
            if (cantidadMejoresSoluciones > 1) {
                System.out.println("\nSe encontraron " + cantidadMejoresSoluciones + " soluciones con la misma cantidad mínima de saltos: " + mejorSolucionSaltos);
            }

            else {
                System.out.println("\n Mejor solución final encontrada con " + mejorSolucionSaltos + " saltos en la rotacion " + mejorRotacionGlobal);
                imprimirTablero(mejorSolucion);
            }
            return true;
        }
        return false;
    }


    // 🔸 Modificado: se agrega parámetro saltosLocales
    private static boolean resolver(int[][] board, int N, int x, int y, int paso, int rotacionActual,  int[] dx, int[] dy) {

        if (contadorSaltos + ((N * N) - paso) > mejorSolucionSaltos) {
            //System.out.println("Podamos rama por superar mejor solución actual: " + (contadorSaltos + saltosRestantes) + " >= " + mejorSolucionSaltos);
            System.out.println("Poda de rama en paso: " + paso + ", saltos restantes: " + ((N * N) - paso) + " En posicion (" + y + ", " + x +")");
            return false;
        }
        
        if (paso == N * N) {
            System.out.println("Solucion encontrada en rotacion: " + (rotacionActual + 1) +" con " + (contadorSaltos) + " saltos totales.");
            imprimirTablero(board);
            if (contadorSaltos == mejorSolucionSaltos || rotacionActual == 0) {
                if (rotacionActual == 0) {
                    System.out.println(" Primera solución encontrada en la rotación 1.");
                    cantidadMejoresSoluciones = 1;
                    //mejoresSoluciones.add(copiarTablero(board));
                    mejoresSolucionesRotaciones.add(rotacionActual + 1);
                    System.out.println(" Se agregó a la lista de mejores soluciones. Total ahora: " + cantidadMejoresSoluciones);

                } else {
                    System.out.println(" La solución encontrada tiene la misma cantidad de saltos que la mejor solución actual: " + mejorSolucionSaltos);
                    cantidadMejoresSoluciones++;
                    //mejoresSoluciones.add(copiarTablero(board));
                    //System.out.println("Tablero agregado en indice: " + (cantidadMejoresSoluciones - 1));
                    mejoresSolucionesRotaciones.add(rotacionActual + 1);
                    System.out.println(" Se agregó a la lista de mejores soluciones. Total ahora: " + cantidadMejoresSoluciones);
                }   
            }
            if (contadorSaltos < mejorSolucionSaltos) {
                System.out.println("Nueva solucion: " + contadorSaltos + " saltos, mejor que la anterior: " + mejorSolucionSaltos);
                mejorSolucionSaltos = contadorSaltos;
                mejorSolucion = copiarTablero(board);
                System.out.println("\n ### NUEVA MEJOR SOLUCION ENCONTRADA  ###");
                mejorRotacionGlobal = rotacionActual + 1;

                // Barremos contador y lista de tableros de mejores soluciones
                cantidadMejoresSoluciones = 1;
                //mejoresSoluciones.clear();
            }
            return true;
        }

        if (contadorSaltos >= MAX_SALTOS) {
            corteGlobal = true;
            return false;
        }

        ColaPrioridadDA cola = new ColaPrioridadDA();
        cola.inicializarCola();

        // 🔹 PREFERENCIA DINÁMICA (Juez 2)
        boolean prefiereCentro = paso < (int) Math.ceil(FASE_BORDE * N * N);
        int cx = N / 2, cy = N / 2; // Coordenadas del centro

        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (enRango(nx, ny, N) && board[nx][ny] == -1) { // posición válida

                // 1. Primer Heuristica: Warnsdorff
                int opciones = contarOpciones(board, N, nx, ny, dx, dy); // cuántas opciones tiene

                //2. Segunmda Heuristica: Centro/Borde
                int distC = Math.abs(nx - cx) + Math.abs(ny - cy); 
                int desempate = prefiereCentro ? distC : (2 * N) - distC;

                //3. Tercer Heuristica: Orden de movimientos
                int prioridad = opciones * PESO_WARNSDORFF + desempate * PESO_CENTRO + i * PESO_ORDEN;
                cola.acolarPrioridad(i, -prioridad); // prioridad inversa


            }
        }

        while (!cola.colaVacia()) {
            int mov = cola.primero();
            cola.desacolar();

            int nx = x + dx[mov];
            int ny = y + dy[mov];

            int saltosRestantes = (N * N) - paso;
            System.out.println(" Intento de salto #" + contadorSaltos + " a (" + ny + ", " + nx + ") en rotación #" + (rotacionActual + 1) +
                    ". Paso numero: " + paso + ", saltos restantes: " + saltosRestantes +
                   ", mejor solución actual: " + mejorSolucionSaltos);
            /*if (contadorSaltos + saltosRestantes > mejorSolucionSaltos) {
                //System.out.println("Podamos rama por superar mejor solución actual: " + (contadorSaltos + saltosRestantes) + " >= " + mejorSolucionSaltos);
                continue; // 🔹 poda
            }*/

            contadorSaltos++;
            board[nx][ny] = paso;
            if (paso_MAX < paso) paso_MAX = paso;
            //System.out.println("\n Salto a ($" + ny + ", " + nx + ") marcado con paso " + paso + ".");
            if (resolver(board, N, nx, ny, paso + 1, rotacionActual, dx, dy)) {
                // 🚀 Si encontramos una solución completa, cortamos TODA la recursión actual
                board[nx][ny] = -1;
                return true;
            }
            board[nx][ny] = -1;
            System.out.println("Retrocediendo de (" + ny + ", " + nx + "). No se pudo seguir desde aca.");
        }
        return false;
    }

    private static int contarOpciones(int[][] board, int N, int x, int y, int[] dx ,int [] dy) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (enRango(nx, ny, N) && board[nx][ny] == -1)
                count++;
        }
        return count;
    }

    private static boolean enRango(int x, int y, int N) {
        return x >= 0 && y >= 0 && x < N && y < N;
    }

    // 🔹 Copia profunda del tablero
    private static int[][] copiarTablero(int[][] original) {
        int[][] copia = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++)
            copia[i] = Arrays.copyOf(original[i], original[i].length);
        return copia;
    }

    public static void imprimirTablero(int[][] board) {
        for (int[] fila : board) {
            for (int val : fila)
                System.out.printf("%3d ", val);
            System.out.println();
        }
    }
    private static void rotarArray(int[] arr) {
        // 🔹 Rota una posición a la izquierda: [a, b, c, d] → [b, c, d, a]
        if (arr.length > 0) {
            int first = arr[0];
            for (int i = 0; i < arr.length - 1; i++) {
                arr[i] = arr[i + 1];
            }
            arr[arr.length - 1] = first;
        }
    }
}
