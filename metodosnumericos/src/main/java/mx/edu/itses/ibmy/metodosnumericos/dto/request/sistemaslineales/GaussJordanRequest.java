package mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemaslineales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para capturar la dimensión y coeficientes del sistema [A|B] para Gauss-Jordan.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GaussJordanRequest {
    private int dimension;
    private double[][] matrizA;
    private double[] vectorB;
}