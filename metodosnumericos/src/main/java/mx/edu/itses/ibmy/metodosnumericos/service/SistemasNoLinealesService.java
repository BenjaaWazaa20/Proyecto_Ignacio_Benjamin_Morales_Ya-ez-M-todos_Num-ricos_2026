package mx.edu.itses.ibmy.metodosnumericos.service;

import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemasnolineales.NewtonRaphsonNoLinealRequest;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemasnolineales.PuntoFijoNoLinealRequest;
import mx.edu.itses.ibmy.metodosnumericos.dto.response.sistemasnolineales.NewtonRaphsonNoLinealRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.response.sistemasnolineales.PuntoFijoNoLinealRespuesta;

/**
 * Contrato de servicio para métodos de solución de Sistemas de Ecuaciones No Lineales.
 */
public interface SistemasNoLinealesService {
    PuntoFijoNoLinealRespuesta resolverPuntoFijo(PuntoFijoNoLinealRequest request);
    NewtonRaphsonNoLinealRespuesta resolverNewtonRaphson(NewtonRaphsonNoLinealRequest request);
}