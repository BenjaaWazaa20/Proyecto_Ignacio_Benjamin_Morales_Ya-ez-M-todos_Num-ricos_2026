package mx.edu.itses.ibmy.metodosnumericos.dto.request.raices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReglaFalsaRespuesta {
    private int iteracion; //
    private double xl; //
    private double xu; //
    private double xr; //
    private String fx; //
    private double fxl; //[cite: 2]
    private double fxu; //[cite: 2]
    private double fxr; //[cite: 2]
    private double er; //[cite: 2]
}