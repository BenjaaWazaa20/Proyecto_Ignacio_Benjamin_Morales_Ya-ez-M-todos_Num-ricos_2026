package mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EliminacionGaussianaRespuesta {
    private double[][] matrizOriginal;
    private double[][] matrizTriangular;
    private double[] variables;
    private List<double[][]> pasosEliminacion;
    private boolean esSingular;
}