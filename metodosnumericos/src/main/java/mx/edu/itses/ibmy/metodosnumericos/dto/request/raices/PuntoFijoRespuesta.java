package mx.edu.itses.ibmy.metodosnumericos.dto.request.raices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PuntoFijoRespuesta {
    private int iteracion;
    private double xi;
    private double xr;
    private double gx;
    private double er;
}