package mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SistemaIterativo {
    private int tamano;
    private double[][] matriz;
    private double[] vector;
    private double[] valoresIniciales;
    private double er;
    private int maximoIteraciones;
}