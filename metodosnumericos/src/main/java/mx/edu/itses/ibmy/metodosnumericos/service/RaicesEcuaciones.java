package mx.edu.itses.ibmy.metodosnumericos.service;

import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.Biseccion;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.BiseccionRespuesta;

public interface RaicesEcuaciones {
    // Método que recibe el DTO Biseccion y retorna un arreglo de BiseccionRespuesta
    BiseccionRespuesta[] biseccion(Biseccion peticion); // 
}