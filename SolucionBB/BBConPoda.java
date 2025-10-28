package CaballoAjedrez;
import java.util.Arrays;

import impl.ColaPrioridadDA;

public class BBConPoda {

	static int[] dx = {2, 1, -1, -2, -2, -1, 1, 2};
    static int[] dy = {1, 2, 2, 1, -1, -2, -2, -1};

    static int contadorSaltos;
    static int mejorSolucionSaltos = Integer.MAX_VALUE; // 🔹 NUEVO
    static int[][] mejorSolucion; // 🔹 NUEVO para guardar el mejor tablero

    public static int[][] solve(int N, int startX, int startY) {
        int[][] board = new int[N][N];
        for (int[] fila : board) Arrays.fill(fila, -1);

        if (!enRango(startX, startY, N)) return null;

        board[startX][startY] = 0;
        contadorSaltos = 0;
        mejorSolucionSaltos = Integer.MAX_VALUE; // 🔹 reiniciamos antes de comenzar
        mejorSolucion = null; // 🔹 limpia el mejor tablero previo

        resolver(board, N, startX, startY, 1, 0); // 🔹 paso + saltosLocales

        if (mejorSolucion != null) {
            System.out.println("🔹 Mejor solución encontrada con " + mejorSolucionSaltos + " saltos.");
            return mejorSolucion;
        }
        return null;
    }

    // 🔸 Modificado: se agrega parámetro saltosLocales
    private static boolean resolver(int[][] board, int N, int x, int y, int paso, int saltosLocales) {
        if (paso == N * N) {
            // 🔹 Se guarda la primera o mejor solución
            if (saltosLocales < mejorSolucionSaltos) {
                mejorSolucionSaltos = saltosLocales;
                mejorSolucion = copiarTablero(board);
            }
            if (mejorSolucionSaltos == N * N) {
            	return true;
            }
            return true;
        }

        ColaPrioridadDA cola = new ColaPrioridadDA();
        cola.inicializarCola();

        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (enRango(nx, ny, N) && board[nx][ny] == -1) {
                int opciones = contarOpciones(board, N, nx, ny);
                cola.acolarPrioridad(i, -opciones);
            }
        }

        while (!cola.colaVacia()) {
            int mov = cola.primero();
            cola.desacolar();

            int nx = x + dx[mov];
            int ny = y + dy[mov];

            contadorSaltos++; // 🔹 Contamos cada intento

            int nuevosSaltos = saltosLocales + 1;
            int saltosRestantes = (N * N) - paso;

            if (nuevosSaltos + saltosRestantes >= mejorSolucionSaltos) {
                // 🔹 Podamos esta rama
                continue;
            }
            board[nx][ny] = paso; // Marca
            resolver(board, N, nx, ny, paso + 1, saltosLocales + 1);
            board[nx][ny] = -1; // Desmarca
        }

        return false;
    }

    private static int contarOpciones(int[][] board, int N, int x, int y) {
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

    public static int getSaltosTotales() {
        return contadorSaltos;
    }
	
}
