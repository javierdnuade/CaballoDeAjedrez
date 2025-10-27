package SolucionBacktracking;

public class Backtracking2 {
    // Definimos los 8 posibles movimientos del caballo
    static int[] dx = {2, 1, -1, -2, -2, -1, 1, 2};
    static int[] dy = {1, 2, 2, 1, -1, -2, -2, -1};

    static int contadorSaltos = 0; // ✅ Contador global de movimientos evaluados

    public static int[][] solve(int N, int startX, int startY) {
        int[][] board = new int[N][N]; // Creamos la matriz de n numeros
        
        if (!enRango(startX, startY, N)) return null; // Verificamos que la posición inicial esté dentro del rango
        board[startX][startY] = 1; // Colocamos el primer paso del caballo
        
        if (resolver(board, N, startX, startY, 2)) {
            System.out.println("Movimientos evaluados: " + contadorSaltos); // ✅ Imprimimos el contador al encontrar la solución
            return board;
        }
        
        return null;

    }

    private static boolean resolver(int[][] board, int N, int x, int y, int paso) {
        if (paso > N * N) return true; // Si ya recorrimos todas las posiciones, retornamos true
        for (int i = 0; i < 8; i++) {  // Probamos los 8 movimientos posibles en el peor caso
            int nx = x + dx[i];
            int ny = y + dy[i];

            contadorSaltos++; // Incrementamos el contador por cada movimiento evaluado

            if (enRango(nx, ny, N) && board[nx][ny] == 0) { // Verificamos que la nueva posición esté dentro del rango y no haya sido visitada
                board[nx][ny] = paso;
                if (resolver(board, N, nx, ny, paso + 1)) return true; // Llamada recursiva, retorna true cuando se encuentra la solución
                board[nx][ny] = 0; // Backtracking: desmarcamos la posición si no lleva a una solución
            }
        }
        return false;
    }

    private static boolean enRango(int x, int y, int N) { // Verifica si una posición está dentro del tablero
        return x >= 0 && y >= 0 && x < N && y < N; 
    }
}