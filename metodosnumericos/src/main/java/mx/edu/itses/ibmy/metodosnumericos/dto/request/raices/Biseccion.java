package mx.edu.itses.ibmy.metodosnumericos.dto.request.raices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Biseccion {

    private double xl; 
    private double xu; 
    private String fx; 
    private double er; 

    private int maximoIteraciones;
}
