package mx.edu.itses.ibmy.metodosnumericos.service.impl;

import lombok.extern.slf4j.Slf4j;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.Biseccion;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.BiseccionRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.Newton;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.NewtonRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.PuntoFijo;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.PuntoFijoRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.ReglaFalsa;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.ReglaFalsaRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.Secante;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.SecanteModificada;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.SecanteModificadaRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.SecanteRespuesta;
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

    @Override
    public ReglaFalsaRespuesta[] reglaFalsa(ReglaFalsa peticion) { //[cite: 2]
        log.info("Inicializando cálculos para método de Regla Falsa con función: {}", peticion.getFx());
        List<ReglaFalsaRespuesta> iteraciones = new ArrayList<>();
        ExprEvaluator evaluator = new ExprEvaluator(); // Motor Symja

        double xl = peticion.getXl();
        double xu = peticion.getXu();
        String fx = peticion.getFx();
        double erEsperado = peticion.getEr();
        int maxIter = peticion.getMaximoIteraciones();

        double xrActual = 0.0;
        double xrAnterior = 0.0;
        double erActual = 100.0;

        // Paso 0: Ciclo repetitivo para cada punto
        for (int i = 1; i <= maxIter; i++) { //[cite: 2]

            // Paso 1: Evalúa función FX en XL y XU via Symja
            evaluator.defineVariable("x", xl);
            double fxl = evaluator.eval(fx).evalDouble(); //[cite: 2]

            evaluator.defineVariable("x", xu);
            double fxu = evaluator.eval(fx).evalDouble(); //[cite: 2]

            // Paso 2: Criterio inicial de cambio de signo en el intervalo
            if (fxl * fxu > 0 && i == 1) { //[cite: 2]
                log.warn("La función no presenta cambio de signo en el intervalo inicial [{}, {}]", xl, xu);
                break; // Pasa al paso 9[cite: 2]
            }

            // Evitar división por cero si fxl == fxu
            if (fxl - fxu == 0) {
                log.error("División por cero detectada en la interpolación.");
                break;
            }

            // Paso 3: Calcular raíz XRactual (Interpolación lineal / Falsa Posición)
            xrActual = xu - (fxu * (xl - xu)) / (fxl - fxu); //[cite: 2]

            // Paso 4: Evalúa función en XRactual
            evaluator.defineVariable("x", xrActual);
            double fxr = evaluator.eval(fx).evalDouble(); //[cite: 2]

            // Paso 6: Calcular valor de Error Relativo ER (Omitir para i=1 que ya es 100)
            if (i > 1) { //[cite: 2]
                erActual = Math.abs((xrActual - xrAnterior) / xrActual) * 100.0; //[cite: 2]
            }

            // Paso 7: Almacenar los valores para la iteración actual
            ReglaFalsaRespuesta respuesta = ReglaFalsaRespuesta.builder()
                    .iteracion(i) //[cite: 2]
                    .xl(xl).xu(xu).xr(xrActual) //[cite: 2]
                    .fx(fx) //[cite: 2]
                    .fxl(fxl).fxu(fxu).fxr(fxr) //[cite: 2]
                    .er(erActual) //[cite: 2]
                    .build();
            iteraciones.add(respuesta); //[cite: 2]

            // Paso 8: Evalúa criterio de convergencia
            if (erActual < erEsperado && i > 1) { //[cite: 2]
                log.info("Criterio de convergencia alcanzado exitosamente en la iteración {}", i);
                break;
            }

            // Paso 5: Determinar condiciones del subintervalo
            if (fxl * fxr < 0) { //[cite: 2]
                xu = xrActual; // Subintervalo inferior/izquierdo[cite: 2]
            } else if (fxl * fxr > 0) {
                xl = xrActual; // Subintervalo superior/derecho[cite: 2]
            } else {
                // Si F(XL)*F(XR) == 0, XRactual es una raíz exacta
                break; //[cite: 2]
            }

            // Actualización obligatoria para el cálculo correcto del ER de la próxima ronda
            xrAnterior = xrActual;
        }

        // Paso 9: Retorna el arreglo de resultados
        return iteraciones.toArray(new ReglaFalsaRespuesta[0]); //[cite: 2]
    }

    @Override
public PuntoFijoRespuesta[] puntoFijo(PuntoFijo peticion) {
        log.info("Iniciando algoritmo de Punto Fijo para GX: {} | FX: {}", peticion.getGx(), peticion.getFx());

        List<PuntoFijoRespuesta> iteraciones = new ArrayList<>();
        ExprEvaluator evaluator = new ExprEvaluator();

        // Paso 0: Inicializa Xi = X0
        double xi = peticion.getX0();
        String gxStr = peticion.getGx();
        double erObjetivo = peticion.getEr();
        int maxIter = peticion.getMaximoIteraciones();

        for (int i = 0; i <= maxIter; i++) {
            double xr = 0.0;

            try {
                // Paso 1: Evalúa GX en Xi mediante Symja para obtener Xr = G(Xi)
                evaluator.defineVariable("x", xi);
                xr = evaluator.eval(gxStr).evalDouble();

                // Manejo defensivo: Aborta si la función diverge (Infinito o NaN)
                if (Double.isInfinite(xr) || Double.isNaN(xr)) {
                    log.warn("Divergencia detectada en la iteración {}. Interrumpiendo bucle.", i);
                    break;
                }
            } catch (Exception e) {
                log.error("Error al evaluar G(X) en iteración {}: {}", i, e.getMessage());
                break;
            }

            // Paso 2: Calcula ER = |(Xr - Xi) / Xr| * 100 desde la PRIMERA iteración usando X0 como Xi anterior
            double erActual = (xr != 0.0) ? Math.abs((xr - xi) / xr) * 100.0 : 0.0;

            // Paso 3: Almacena el historial en PuntoFijoRespuesta
            iteraciones.add(PuntoFijoRespuesta.builder()
                    .iteracion(i)
                    .xi(xi)
                    .xr(xr)
                    .gx(xr)
                    .er(erActual)
                    .build());

            // Paso 4: Evalúa el criterio de convergencia contra el ER objetivo
            if (erActual < erObjetivo) {
                log.info("Convergencia exitosa alcanzada en la iteración {}", i);
                break;
            }

            // Paso 5: Actualiza Xi = Xr y continúa el ciclo si no supera MaximoIteraciones
            xi = xr;
        }

        // Paso 6: Retorna el arreglo resultante
        return iteraciones.toArray(new PuntoFijoRespuesta[0]);
    }
    @Override
public NewtonRespuesta[] newtonRaphson(Newton peticion) {
        log.info("Iniciando algoritmo de Newton-Raphson para FX: {}", peticion.getFx());

        List<NewtonRespuesta> iteraciones = new ArrayList<>();
        ExprEvaluator evaluator = new ExprEvaluator();

        // Paso 0: Inicialización de Xi = X0
        double xi = peticion.getX0();
        String fxStr = peticion.getFx();
        double erObjetivo = peticion.getEr();
        int maxIter = peticion.getMaximoIteraciones();

        String fpXStr = "";
        try {
            // Paso 1: Derivación simbólica única antes del ciclo repetitivo
            fpXStr = evaluator.eval("D(" + fxStr + ", x)").toString();
            log.info("Derivada simbólica F'(X) calculada: {}", fpXStr);
        } catch (Exception e) {
            log.error("Error al obtener la derivada simbólica para {}: {}", fxStr, e.getMessage());
            return new NewtonRespuesta[0];
        }

        // Paso 0: Inicializa el contador en 0 de acuerdo con la especificación
        for (int i = 1; i <= maxIter; i++) {
            double fXi = 0.0;
            double fpXi = 0.0;
            double xr = 0.0;

            try {
                evaluator.defineVariable("x", xi);

                // Paso 2 y 3: Evalúa F(Xi) y la derivada F'(Xi) en Symja
                fXi = evaluator.eval(fxStr).evalDouble();
                fpXi = evaluator.eval(fpXStr).evalDouble();

                // Paso 4: Manejo defensivo contra división por cero (tangente horizontal)
                if (Math.abs(fpXi) < 1e-12 || Double.isNaN(fpXi) || Double.isInfinite(fpXi)) {
                    log.warn("Derivada F'(Xi) cercana a 0 o divergente en iteración {}. Deteniendo bucle.", i);
                    break;
                }

                // Paso 5: Cálculo del nuevo valor Xr (Xi+1)
                xr = xi - (fXi / fpXi);

                if (Double.isInfinite(xr) || Double.isNaN(xr)) {
                    log.warn("Divergencia en el cálculo de Xr en la iteración {}", i);
                    break;
                }
            } catch (Exception e) {
                log.error("Error durante la evaluación numérica en iteración {}: {}", i, e.getMessage());
                break;
            }

            // Paso 6: Cálculo del Error Relativo desde la iteración 0 utilizando la fórmula estándar
            double erActual = (xr != 0.0) ? Math.abs((xr - xi) / xr) * 100.0 : 0.0;

            // Paso 7: Almacena las evaluaciones de cada iteración del ciclo
            iteraciones.add(NewtonRespuesta.builder()
                    .iteracion(i)
                    .xi(xi)
                    .fx(fXi)
                    .fpX(fpXi)
                    .xr(xr)
                    .er(erActual)
                    .build());

            // Paso 8: Evaluación del criterio de convergencia
            if (erActual < erObjetivo && i > 0) {
                log.info("Convergencia exitosa alcanzada en la iteración {}", i);
                break;
            }

            // Paso 9: Actualiza Xi = Xr para continuar iterando
            xi = xr;
        }

        // Paso 10: Retorno del historial en forma de arreglo
        return iteraciones.toArray(new NewtonRespuesta[0]);
    }
    @Override
public SecanteRespuesta[] secante(Secante peticion) {
        log.info("Iniciando algoritmo de Secante para F(X): {}", peticion.getFx());

        List<SecanteRespuesta> iteraciones = new ArrayList<>();
        ExprEvaluator evaluator = new ExprEvaluator();

        double xAnterior = peticion.getX0();
        double xActual = peticion.getX1();
        String fxStr = peticion.getFx();
        double erObjetivo = peticion.getEr();
        int maxIter = peticion.getMaximoIteraciones();

        for (int i = 1; i <= maxIter; i++) {
            double fXAnterior = 0.0;
            double fXActual = 0.0;
            double xr = 0.0;

            try {
                // Paso 1 y 2: Evaluaciones de F(Xanterior) y F(Xactual) usando Symja
                evaluator.defineVariable("x", xAnterior);
                fXAnterior = evaluator.eval(fxStr).evalDouble();

                evaluator.defineVariable("x", xActual);
                fXActual = evaluator.eval(fxStr).evalDouble();

                // Paso 3: Manejo defensivo contra división entre cero (secante horizontal)
                double denominador = fXAnterior - fXActual;
                if (Math.abs(denominador) < 1e-12 || Double.isNaN(denominador) || Double.isInfinite(denominador)) {
                    log.warn("La secante es horizontal o cercana a 0 en la iteración {}. Sugerencia: cambie los valores X0/X1.", i);
                    break;
                }

                // Paso 4: Fórmula de la secante
                xr = xActual - ((fXActual * (xAnterior - xActual)) / denominador);

                // Paso 9: Control de divergencia aritmética
                if (Double.isInfinite(xr) || Double.isNaN(xr)) {
                    log.error("Divergencia aritmética en iteración {} desde valores X0/X1 proporcionados.", i);
                    break;
                }
            } catch (Exception e) {
                log.error("Error al evaluar numéricamente la expresión en la iteración {}: {}", i, e.getMessage());
                break;
            }

            // Paso 5: Cálculo del error relativo (disponible desde iteración 0)
            double erActual = (xr != 0.0) ? Math.abs((xr - xActual) / xr) * 100.0 : 0.0;

            // Paso 6: Almacenamiento en el modelo DTO utilizando Lombok builder
            iteraciones.add(SecanteRespuesta.builder()
                    .iteracion(i)
                    .xi(xAnterior)
                    .xi1(xActual)
                    .fXi(fXAnterior)
                    .fXi1(fXActual)
                    .xr(xr)
                    .er(erActual)
                    .build());

            // Paso 7: Criterio de convergencia
            if (erActual < erObjetivo && i > 0) {
                log.info("Convergencia exitosa del método de Secante alcanzada en iteración: {}", i);
                break;
            }

            // Paso 8: Desplazamiento de ventana de puntos
            xAnterior = xActual;
            xActual = xr;
        }

        // Paso 10: Retorno de la lista convertida en arreglo
        return iteraciones.toArray(new SecanteRespuesta[0]);
    }
@Override
public SecanteModificadaRespuesta[] secanteModificada(SecanteModificada peticion) {
        log.info("Iniciando algoritmo de Secante Modificada para F(X): {}", peticion.getFx());

        List<SecanteModificadaRespuesta> iteraciones = new ArrayList<>();
        ExprEvaluator evaluator = new ExprEvaluator();

        // Paso 0: Inicialización del contador y de Xi = X0
        double xi = peticion.getX0();
        String fxStr = peticion.getFx();
        double sigma = peticion.getSigma();
        double erObjetivo = peticion.getEr();
        int maxIter = peticion.getMaximoIteraciones();

        for (int i = 1; i <= maxIter; i++) {
            double fXi = 0.0;
            double xii = 0.0;
            double fXii = 0.0;
            double xr = 0.0;

            try {
                // Paso 1: Evaluar F(Xi) en Symja
                evaluator.defineVariable("x", xi);
                fXi = evaluator.eval(fxStr).evalDouble();

                // Paso 2: Cálculo del punto perturbado (Xi + Delta*Xi)
                xii = xi + sigma; 

                // Paso 3: Evaluar F(XiPerturbado) en Symja
                evaluator.defineVariable("x", xii);
                fXii = evaluator.eval(fxStr).evalDouble();

                // Paso 4: Manejo defensivo para evitar división por cero si la pendiente es nula
                double denominador = fXii - fXi;
                if (Math.abs(denominador) < 1e-12 || Double.isNaN(denominador) || Double.isInfinite(denominador)) {
                    log.warn("La pendiente aproximada es nula en la iteración {}. Sugerencia: cambie Delta o X0.", i);
                    break;
                }

                // Paso 5: Fórmula de la Secante Modificada
                xr = xi - ((sigma * fXi) / (fXii - fXi));

                // Paso 10: Control de divergencia o desbordamiento numérico
                if (Double.isInfinite(xr) || Double.isNaN(xr)) {
                    log.error("Divergencia en el cálculo de Xr en iteración {} desde el X0 y Delta proporcionados.", i);
                    break;
                }
            } catch (Exception e) {
                log.error("Error en evaluación simbólica/numérica durante iteración {}: {}", i, e.getMessage());
                break;
            }

            // Paso 6: Cálculo del Error Relativo desde la primera iteración
            double erActual = (xr != 0.0) ? Math.abs((xr - xi) / xr) * 100.0 : 0.0;

            // Paso 7: Almacenamiento en el historial mediante el builder de Lombok
            iteraciones.add(SecanteModificadaRespuesta.builder()
                    .iteracion(i)
                    .xi(xi)
                    .fXi(fXi)
                    .xii(xii)
                    .fXii(fXii)
                    .xr(xr)
                    .er(erActual)
                    .build());

            // Paso 8: Evaluación del criterio de convergencia
            if (erActual < erObjetivo && i > 0) {
                log.info("Convergencia exitosa del método Secante Modificada en la iteración {}", i);
                break;
            }

            // Paso 9: Actualiza Xi = Xr para la siguiente iteración
            xi = xr;
        }

        // Paso 11: Retorno del historial como arreglo
        return iteraciones.toArray(new SecanteModificadaRespuesta[0]);
    }
}
