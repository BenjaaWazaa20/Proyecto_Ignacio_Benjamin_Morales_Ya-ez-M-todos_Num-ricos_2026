package mx.edu.itses.ibmy.metodosnumericos.dto.request.integracion;

import lombok.Data;

@Data
public class Simpson13Request {
    private String fx;
    private double a;
    private double b;
    private int n;
}