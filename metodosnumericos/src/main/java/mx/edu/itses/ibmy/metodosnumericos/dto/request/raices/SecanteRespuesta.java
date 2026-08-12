package mx.edu.itses.ibmy.metodosnumericos.dto.request.raices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecanteRespuesta {
    private int iteracion;
    private double xi;
    private double xi1;
    private double fXi;
    private double fXi1;
    private double xr;
    private double er;
}