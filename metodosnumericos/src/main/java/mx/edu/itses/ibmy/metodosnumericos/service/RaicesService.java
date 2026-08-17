package mx.edu.itses.ibmy.metodosnumericos.service;

import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.Biseccion;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.ReglaFalsa;
import mx.edu.itses.ibmy.metodosnumericos.dto.response.raices.BiseccionRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.response.raices.ReglaFalsaRespuesta;

import java.util.List;

public interface RaicesService {
    // Método público que regresa un arreglo (o Lista) de objetos BiseccionRespuesta y recibe la clase Biseccion [cite: 174, 175]
    List<BiseccionRespuesta> biseccion(Biseccion request);
    List<ReglaFalsaRespuesta> reglaFalsa(ReglaFalsa request);
}
