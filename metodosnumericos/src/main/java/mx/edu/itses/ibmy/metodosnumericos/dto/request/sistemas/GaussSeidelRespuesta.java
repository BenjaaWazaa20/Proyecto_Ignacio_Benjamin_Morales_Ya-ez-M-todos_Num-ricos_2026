package mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GaussSeidelRespuesta {
    private int iteracion; //
    private double[] valoresAnteriores; //
    private double[] valoresNuevos; //[cite: 3]
    private double[] errorPorVariable; //[cite: 3]
    private double errorMaximo; //[cite: 3]
}