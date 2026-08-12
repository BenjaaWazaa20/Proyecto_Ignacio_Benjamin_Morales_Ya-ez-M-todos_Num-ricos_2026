package mx.edu.itses.ibmy.metodosnumericos.dto.request.raices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Secante {
    private double x0;
    private double x1;
    private String fx;
    private double er;
    private int maximoIteraciones;
}
