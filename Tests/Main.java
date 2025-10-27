package Tests;

// Main.java
public class Main {
    public static void main(String[] args) {
        int N = 6;
        int startX = 2, startY = 3;

        var inicio = java.time.Instant.now();
        int[][] resultado = TestBB.solve(N, startX, startY);
        var fin = java.time.Instant.now();

        System.out.println("Posición inicial del caballo: (" + startX + ", " + startY + ")");
        long millis = java.time.Duration.between(inicio, fin).toMillis();
        System.out.println("Tiempo de ejecución: " + millis + " ms");

        if (resultado != null) {
            imprimirTablero(resultado);
        } else {
            System.out.println("No se encontró solución.");
        }
    }

    public static void imprimirTablero(int[][] board) {
        for (int[] fila : board) {
            for (int val : fila) System.out.printf("%3d ", val);
            System.out.println();
        }
    }
}
