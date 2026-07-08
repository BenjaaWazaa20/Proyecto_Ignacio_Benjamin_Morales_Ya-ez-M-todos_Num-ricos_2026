package mx.edu.itses.ibmy.metodosnumericos.service.impl;

import lombok.extern.slf4j.Slf4j;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.Biseccion;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.BiseccionRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.service.RaicesEcuaciones;

import java.util.ArrayList;
import java.util.List;

import org.matheclipse.core.eval.ExprEvaluator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RaicesEcuacionesImplementacion implements RaicesEcuaciones {

@Override
    public BiseccionRespuesta[] biseccion(Biseccion peticion) {
        log.info("Ejecutando algoritmo de Bisección...");
        
        List<BiseccionRespuesta> iteraciones = new ArrayList<>();
        ExprEvaluator evaluator = new ExprEvaluator(); // Motor Symja
        
        double xl = peticion.getXl();
        double xu = peticion.getXu();
        String fx = peticion.getFx();
        double erEsperado = peticion.getEr();
        int maxIter = peticion.getMaximoIteraciones();
        
        double xrActual = 0.0;
        double xrAnterior = 0.0;
        double erActual = 100.0;

        // 0: Ciclo repetitivo
        for (int i = 1; i <= maxIter; i++) {
            
            // 1: Evaluar FX en XL y XU (Corrección API Symja)
            evaluator.defineVariable("x", xl);
            double fxl = evaluator.eval(fx).evalDouble();
            
            evaluator.defineVariable("x", xu);
            double fxu = evaluator.eval(fx).evalDouble();

            // 2: Evaluar criterio F(XL)*F(XU) < 0
            if (fxl * fxu > 0 && i == 1) {
                log.warn("La función no cambia de signo en el intervalo dado.");
                break; // Pasa al paso 9
            }

            // 3: Calcular raíz XRactual
            xrActual = (xl + xu) / 2.0;

            // 4: Evaluar FX en el punto XRactual (Corrección API Symja)
            evaluator.defineVariable("x", xrActual);
            double fxr = evaluator.eval(fx).evalDouble();

            // 6: Calcular valor ER (omitiendo i=1 que ya es 100)
            if (i > 1) {
                erActual = Math.abs((xrActual - xrAnterior) / xrActual) * 100.0;
            }

            // 7: Almacenar los valores para la iteración actual
            BiseccionRespuesta respuesta = BiseccionRespuesta.builder()
                    .iteracion(i)
                    .xl(xl).xu(xu).xr(xrActual)
                    .fx(fx)
                    .fxl(fxl).fxu(fxu).fxr(fxr)
                    .er(erActual)
                    .build();
            iteraciones.add(respuesta);

            // 8: Evalúa el criterio de convergencia
            if (erActual < erEsperado && i > 1) {
                log.info("Criterio de convergencia alcanzado en la iteración {}", i);
                break;
            }

            // 5: Determinar subintervalos
            if (fxl * fxr < 0) {
                xu = xrActual;
            } else if (fxl * fxr > 0) {
                xl = xrActual;
            } else {
                // Si es exactamente 0, xrActual es la raíz exacta
                break; 
            }

            xrAnterior = xrActual;
        }

        // 9: Retorna el arreglo
        return iteraciones.toArray(new BiseccionRespuesta[0]);
    }
}
