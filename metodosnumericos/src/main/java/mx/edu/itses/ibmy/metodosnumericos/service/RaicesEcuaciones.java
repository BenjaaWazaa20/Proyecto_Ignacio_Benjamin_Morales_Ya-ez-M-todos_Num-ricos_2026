package mx.edu.itses.ibmy.metodosnumericos.service;

import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.Biseccion;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.BiseccionRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.Newton;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.NewtonRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.PuntoFijo;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.PuntoFijoRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.ReglaFalsa;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.ReglaFalsaRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.Secante;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.SecanteRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.SecanteModificada;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.SecanteModificadaRespuesta;



public interface RaicesEcuaciones {
    // Método que recibe el DTO Biseccion y retorna un arreglo de BiseccionRespuesta
    BiseccionRespuesta[] biseccion(Biseccion peticion); // 
    ReglaFalsaRespuesta[] reglaFalsa(ReglaFalsa peticion);
    PuntoFijoRespuesta[] puntoFijo(PuntoFijo peticion);
    NewtonRespuesta[] newtonRaphson(Newton peticion);
    SecanteRespuesta[] secante(Secante peticion);
    SecanteModificadaRespuesta[] secanteModificada(SecanteModificada peticion);
}