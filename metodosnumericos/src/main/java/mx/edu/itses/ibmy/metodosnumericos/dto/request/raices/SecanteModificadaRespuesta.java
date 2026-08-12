package mx.edu.itses.ibmy.metodosnumericos.dto.request.raices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecanteModificadaRespuesta {
    private int iteracion;
    private double xi;
    private double fXi;
    private double xii;
    private double fXii;
    private double xr;
    private double er;
}