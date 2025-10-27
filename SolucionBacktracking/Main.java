package SolucionBacktracking;

public class Main {
    public static void main(String[] args) {
        int N = 6;

        java.util.Random random = new java.util.Random();
        // int startX = random.nextInt(N);
        // int startY = random.nextInt(N);
        int startX = 2;
        int startY = 3;
        System.out.println("Posición inicial del caballo: (" + startX + ", " + startY + ")");

        java.time.Instant inicio = java.time.Instant.now();
        // int[][] solution = Backtracking.solve(N,startX, startY);
        int[][] solution = Backtracking2.solve(N,startX, startY);

        java.time.Instant fin = java.time.Instant.now();

        long millis = java.time.Duration.between(inicio, fin).toMillis();
        System.out.println("Tiempo de ejecución: " + millis + " ms");
        
        if (solution != null) {
            for (int[] fila : solution) {
                for (int val : fila) System.out.printf("%2d ", val);
                System.out.println();
            }
        } else {
            System.out.println("No hay solución 😢");
        }
    }
}