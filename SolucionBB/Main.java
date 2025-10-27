package SolucionBB;

public class Main {
    public static void main(String[] args) {
        int N = 7;
        int startX = 2, startY = 3;

        java.time.Instant inicio = java.time.Instant.now();
        int[][] resultado = BB2.solve(N, startX, startY);
        java.time.Instant fin = java.time.Instant.now();
        System.out.println("Posición inicial del caballo: (" + startX + ", " + startY + ")");


        if (resultado != null) {

            long millis = java.time.Duration.between(inicio, fin).toMillis();
            System.out.println("Tiempo de ejecución: " + millis + " ms");
            System.out.println("Total de saltos intentados: " + BB2.getSaltosTotales());
            imprimirTablero(resultado);
        } else {
            System.out.println("No se encontró solución.");
        }
    }

    public static void imprimirTablero(int[][] board) {
        for (int[] fila : board) {
            for (int val : fila)
                System.out.printf("%3d ", val);
            System.out.println();
        }
    }
}
