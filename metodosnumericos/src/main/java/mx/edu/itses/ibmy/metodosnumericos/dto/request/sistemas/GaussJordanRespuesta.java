package mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GaussJordanRespuesta {
    private double[][] matrizOriginal;
    private double[][] matrizIdentidad;
    private double[] variables;
    private List<double[][]> pasosReduccion;
    private boolean esSingular;
}