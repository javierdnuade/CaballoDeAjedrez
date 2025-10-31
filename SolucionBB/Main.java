package SolucionBB;

public class Main {
    public static void main(String[] args) {
        int N = 7;
        int startX = 6, startY = 4;
        System.out.println("Posición inicial del caballo: (" + startX + ", " + startY + ") En el tablero de tamaño " + N + "x" + N);

        java.time.Instant inicio = java.time.Instant.now();
        //int[][] resultado = BBCorregido.solve(N, startY, startX);
        boolean resultado = BBCorregido.solve(N, startY, startX);

        java.time.Instant fin = java.time.Instant.now();

        System.out.println("Posicion inicial: (" + startX + ", " + startY + ")");
        if (resultado == true) {
        //if (resultado != null) {
            System.out.println("¡Solución encontrada!. Se utilizo el " + BBCorregido.FASE_BORDE*100 + "% para la heuristica de tomar el centro del tablero.");
            //System.out.println("Total de saltos intentados: " + BBConPoda.getSaltosTotales());
            //imprimirTablero(resultado);
        } else {
            System.out.println("No se encontró solución.");
        }
        long millis = java.time.Duration.between(inicio, fin).toMillis();
        System.out.println("Tiempo de ejecución: " + millis + " ms");
    }

    public static void imprimirTablero(int[][] board) {
        for (int[] fila : board) {
            for (int val : fila)
                System.out.printf("%3d ", val);
            System.out.println();
        }
    }
}
