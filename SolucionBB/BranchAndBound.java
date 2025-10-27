package SolucionBB;

import java.util.Arrays;


import impl.ColaPrioridadDA;

public class BranchAndBound {

    // Movimientos posibles del caballo
    static int[] dx = {2, 1, -1, -2, -2, -1, 1, 2};
    static int[] dy = {1, 2, 2, 1, -1, -2, -2, -1};

    // Método principal
    public static int[][] solve(int N, int startX, int startY) {
        int[][] board = new int[N][N];
        for (int[] fila : board) Arrays.fill(fila, -1); // Inicializamos con -1 (no visitado)

        if (!enRango(startX, startY, N)) return null;

        board[startX][startY] = 0; // Posición inicial
        if (resolver(board, N, startX, startY, 1))
            return board;

        return null; // No hay solución
    }

    // Método recursivo con Branch & Bound
    private static boolean resolver(int[][] board, int N, int x, int y, int paso) {
        if (paso == N * N) return true; // Si se recorrieron todas las casillas

        // Obtener los posibles movimientos ordenados por la cantidad de movimientos siguientes (heurística)
        ColaPrioridadDA cola = new ColaPrioridadDA();
        cola.inicializarCola();
        
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (enRango(nx, ny, N) && board[nx][ny] == -1) {
                int opciones = contarOpciones(board, N, nx, ny);
                // NOTA: usamos prioridad inversa (prioridad más alta = menor cantidad de opciones)
                cola.acolarPrioridad(i, -opciones);
            }
        }

        // Sacar movimientos en orden de prioridad
        while (!cola.colaVacia()) {
            int mov = cola.primero();
            cola.desacolar();

            int nx = x + dx[mov];
            int ny = y + dy[mov];

            board[nx][ny] = paso; // Marcar movimiento
            if (resolver(board, N, nx, ny, paso + 1)) return true; // Solución encontrada
            board[nx][ny] = -1; // backtracking
        }

        return false; // No se encontró solución desde esta posición
    }

    // Cuenta cuántos movimientos posibles tiene el caballo desde (x, y)
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

    // Verifica si una posición está dentro del tablero
    private static boolean enRango(int x, int y, int N) {
        return x >= 0 && y >= 0 && x < N && y < N;
    }
}
