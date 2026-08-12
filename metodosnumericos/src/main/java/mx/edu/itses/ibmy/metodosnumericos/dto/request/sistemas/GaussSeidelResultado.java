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
public class GaussSeidelResultado {
    private List<GaussSeidelRespuesta> historial; //[cite: 3]
    private boolean convergio; //[cite: 3]
    private double[] variablesFinales; //[cite: 3]
    private String mensajeError; //[cite: 3]
}