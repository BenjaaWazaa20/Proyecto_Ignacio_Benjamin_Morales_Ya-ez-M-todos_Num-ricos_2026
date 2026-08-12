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
public class JacobiResultado {
    private List<JacobiRespuesta> historial;
    private boolean convergio;
    private double[] variablesFinales;
    private String mensajeError;
}