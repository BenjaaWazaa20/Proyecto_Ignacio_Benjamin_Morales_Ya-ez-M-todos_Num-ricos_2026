package mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Representa el modelo de respuesta del método de Determinantes (Regla de Cramer).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeterminantesRespuesta {
    private double[][] matrizOriginal;
    private double[] vectorOriginal;
    private double determinanteA;
    private List<double[][]> matricesAuxiliares;
    private double[] determinantesAuxiliares;
    private double[] variables;
    private boolean esSingular;
}