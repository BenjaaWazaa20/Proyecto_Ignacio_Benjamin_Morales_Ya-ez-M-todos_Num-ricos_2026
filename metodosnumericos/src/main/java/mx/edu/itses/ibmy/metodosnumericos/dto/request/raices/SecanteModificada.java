package mx.edu.itses.ibmy.metodosnumericos.dto.request.raices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecanteModificada {
    private double x0;
    private String fx;
    private double sigma;
    private double er;
    private int maximoIteraciones;
}