package mx.edu.itses.ibmy.metodosnumericos.dto.request.raices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiseccionRespuesta {
    private int iteracion; // [cite: 48, 57]
    private double xl; // [cite: 49, 57]
    private double xu; // [cite: 50, 57]
    private double xr; // [cite: 51, 57]
    private String fx; // [cite: 52, 57]
    private double fxl; // [cite: 53, 57]
    private double fxu; // [cite: 54, 57]
    private double fxr; // [cite: 55, 57]
    private double er; // [cite: 56, 57]
}