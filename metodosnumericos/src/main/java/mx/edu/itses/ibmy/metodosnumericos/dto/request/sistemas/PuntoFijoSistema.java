package mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PuntoFijoSistema {
    private double x0; //
    private double y0; //
    private String f1; //
    private String f2; //
    private String g1; //
    private String g2; //[cite: 3]
    private double er; //[cite: 3]
    private int maximoIteraciones; //[cite: 3]
}