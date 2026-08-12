package mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PuntoFijoSistemaRespuesta {
    private int iteracion; //[cite: 3]
    private double x; //[cite: 3]
    private double y; //[cite: 3]
    private double xNuevo; //[cite: 3]
    private double yNuevo; //[cite: 3]
    private double errorX; //[cite: 3]
    private double errorY; //[cite: 3]
    private double errorMaximo; //[cite: 3]
}