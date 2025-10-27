package Tests;

import java.util.Arrays;

import impl.ColaPrioridadDA;

public class TestBB {

    // Movimientos posibles del caballo
    static final int[] dx = {2, 1, -1, -2, -2, -1, 1, 2};
    static final int[] dy = {1, 2,  2,  1, -1, -2,-2,-1};

    // Método principal
    public static int[][] solve(int N, int startX, int startY) {
        if (!enRango(startX, startY, N)) return null;

        int[][] board = new int[N][N];
        for (int[] fila : board) Arrays.fill(fila, -1); // Inicializamos con -1 (no visitado)

        board[startX][startY] = 0; // Posición inicial
        if (resolver(board, N, startX, startY, 1)) return board;
        return null; // No hay solución
    }

    // Método recursivo con Branch & Bound (poda de islotes) + PQ con desempate (look-ahead)
    private static boolean resolver(int[][] board, int N, int x, int y, int paso) {
        if (paso == N * N) return true; // Si se recorrieron todas las casillas

        // Cola de prioridad (tu implementación)
        ColaPrioridadDA cola = new ColaPrioridadDA();
        cola.inicializarCola();

        // Generar candidatos con prioridad
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i], ny = y + dy[i];
            if (enRango(nx, ny, N) && board[nx][ny] == -1) {

                // Simular el paso para evaluar poda y prioridad
                board[nx][ny] = paso;

                // BOUND: si crea islotes, no encolamos (podamos esta rama)
                if (!creaIslotes(board, N)) {
                    // Heurística 1: grado libre en el destino (Warnsdorff)
                    int grado = gradoLibre(board, N, nx, ny);

                    // Heurística 2 (look-ahead): mejor grado de los movimientos desde el destino
                    int mejorSiguienteGrado = 9; // max posible 8, 9 = "peor"
                    for (int k = 0; k < 8; k++) {
                        int sx = nx + dx[k], sy = ny + dy[k];
                        if (enRango(sx, sy, N) && board[sx][sy] == -1) {
                            int g2 = gradoLibre(board, N, sx, sy);
                            if (g2 < mejorSiguienteGrado) mejorSiguienteGrado = g2;
                        }
                    }
                    if (mejorSiguienteGrado == 9) mejorSiguienteGrado = 0; // si no hay siguiente, no penalices

                    // PRIORIDAD: menor grado y menor mejorSiguienteGrado primero
                    // Usamos prioridad negativa para que tu PQ (que saca mayor primero) priorice los menores
                    int prioridad = -(grado * 10 + mejorSiguienteGrado);

                    // Encolamos el índice del movimiento con esa prioridad
                    cola.acolarPrioridad(i, prioridad);
                }

                // Deshacer simulación
                board[nx][ny] = -1;
            }
        }

        // Expandir según prioridad
        while (!cola.colaVacia()) {
            int mov = cola.primero();
            cola.desacolar();

            int nx = x + dx[mov], ny = y + dy[mov];

            board[nx][ny] = paso; // Marcar movimiento

            // Poda en línea por seguridad (barata)
            if (!creaIslotes(board, N)) {
                if (resolver(board, N, nx, ny, paso + 1)) return true; // Solución encontrada
            }
            board[nx][ny] = -1; // backtracking
        }
        return false; // No se encontró solución desde esta posición
    }

    // --- Utilidades ---

    // Devuelve true si existe alguna casilla NO visitada que quedó sin salidas (islote)
    private static boolean creaIslotes(int[][] board, int N) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == -1 && gradoLibre(board, N, i, j) == 0) return true;
            }
        }
        return false;
    }

    // Cantidad de casillas libres alcanzables con un salto de caballo desde (x,y)
    private static int gradoLibre(int[][] board, int N, int x, int y) {
        int c = 0;
        for (int k = 0; k < 8; k++) {
            int nx = x + dx[k], ny = y + dy[k];
            if (enRango(nx, ny, N) && board[nx][ny] == -1) c++;
        }
        return c;
    }
    // Verifica si una posición está dentro del tablero
    private static boolean enRango(int x, int y, int N) {
        return x >= 0 && y >= 0 && x < N && y < N;
    }
}