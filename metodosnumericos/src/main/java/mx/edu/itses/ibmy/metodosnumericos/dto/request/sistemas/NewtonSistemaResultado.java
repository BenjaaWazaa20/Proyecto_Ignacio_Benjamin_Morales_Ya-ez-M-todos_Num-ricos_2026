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
public class NewtonSistemaResultado {
    private List<NewtonSistemaRespuesta> historial;
    private boolean convergio;
    private double xFinal;
    private double yFinal;
    private String mensajeError;
}