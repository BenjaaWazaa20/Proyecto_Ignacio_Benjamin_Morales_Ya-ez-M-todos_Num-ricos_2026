package mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewtonSistema {
    private double x0;
    private double y0;
    private String u; // Ecuación 1: U(x,y) = 0
    private String v; // Ecuación 2: V(x,y) = 0
    private double er;
    private int maximoIteraciones;
}