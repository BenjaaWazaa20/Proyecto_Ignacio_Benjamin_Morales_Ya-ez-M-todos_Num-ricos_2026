package mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JacobiRespuesta {
    private int iteracion;
    private double[] valoresX;
    private double[] errorPorVariable;
    private double errorMaximo;
}