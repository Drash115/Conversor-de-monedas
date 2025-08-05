import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ConversorDeMonedas {

    public static void main(String[] args) throws IOException, InterruptedException {

        // Petición a la API
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                //Colocar tu API Key
                .uri(URI.create("COLOCA TU API KEY AQUI"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        Respuesta data = gson.fromJson(response.body(), Respuesta.class);

        // Bucle
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("""
                   ********* Conversor de Monedas ***********
                   Escriba 'SALIR' para cerrar el programa.
                   ******************************************""");

            System.out.print("Ingrese la moneda a convertir (por ejemplo, USD): ");
            String monedaInicial = scanner.nextLine().toUpperCase();
            if (monedaInicial.equals("SALIR")) break;

            System.out.print("Ingrese la moneda de destino (por ejemplo, COP): ");
            String monedaFinal = scanner.nextLine().toUpperCase();
            if (monedaFinal.equals("SALIR")) break;

            System.out.print("Ingrese el monto: ");
            String montoTexto = scanner.nextLine();
            if (montoTexto.equalsIgnoreCase("SALIR")) break;

            double monto;

            try {
                monto = Double.parseDouble(montoTexto);
            } catch (NumberFormatException e) {
                System.out.println("Monto inválido.");
                continue;
            }
            Double tasaOrigen = data.conversion_rates.get(monedaInicial);
            Double tasaFinal = data.conversion_rates.get(monedaFinal);

            if (tasaOrigen == null || tasaFinal == null) {
                System.out.println("""
                       ******************************************
                        Una o ambas monedas no son válidas. Intente de nuevo.
                       ******************************************
                       """);
                continue;
            }

            // Realizar conversión
            double resultado = monto * (tasaFinal / tasaOrigen);

            System.out.printf("""
                    --------------------------------
                    \n%.2f %s equivalen a %.2f %s%n
                    --------------------------------
                    """, monto, monedaInicial, resultado, monedaFinal);
        }

        System.out.println("""
                ****************************************************************
                ****************************************************************
                    Programa finalizado. ¡Gracias por usar el conversor!
                ****************************************************************
                ****************************************************************
                """);
        scanner.close();
    }
}