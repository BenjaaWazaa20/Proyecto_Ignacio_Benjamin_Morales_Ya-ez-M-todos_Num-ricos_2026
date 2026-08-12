package mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewtonSistemaRespuesta {
    private int iteracion;
    private double xi;
    private double yi;
    private double xiNuevo;
    private double yiNuevo;
    private double ui;
    private double vi;
    private double duDx;
    private double duDy;
    private double dvDx;
    private double dvDy;
    private double jacobiano;
    private double errorX;
    private double errorY;
}
