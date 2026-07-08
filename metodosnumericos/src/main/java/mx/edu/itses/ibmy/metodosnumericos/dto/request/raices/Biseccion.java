package mx.edu.itses.ibmy.metodosnumericos.dto.request.raices;

import lombok.Data;

@Data
public class Biseccion {
    private double xl; // [cite: 40, 45]
    private double xu; // [cite: 41, 45]
    private String fx; // [cite: 42, 46]
    private double er; // [cite: 43, 45]
    private int maximoIteraciones; // [cite: 44, 45]
}