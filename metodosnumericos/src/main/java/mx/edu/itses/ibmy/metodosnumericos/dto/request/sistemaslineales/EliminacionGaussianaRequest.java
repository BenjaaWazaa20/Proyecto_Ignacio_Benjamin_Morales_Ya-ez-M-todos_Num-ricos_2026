package mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemaslineales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la captura del sistema de ecuaciones [A|B].
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EliminacionGaussianaRequest {
    private int dimension;
    private double[][] matrizA;
    private double[] vectorB;
}