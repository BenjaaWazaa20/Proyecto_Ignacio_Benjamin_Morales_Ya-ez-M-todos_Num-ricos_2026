package mx.edu.itses.ibmy.metodosnumericos.service.impl;

import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.DeterminantesRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.EliminacionGaussianaRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.GaussJordanRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.GaussSeidelRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.GaussSeidelResultado;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.JacobiRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.JacobiResultado;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.NewtonSistema;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.NewtonSistemaResultado;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.NewtonSistemaRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.PuntoFijoSistema;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.PuntoFijoSistemaRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.PuntoFijoSistemaResultado;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.SistemaIterativo;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.SistemaLineal;
import mx.edu.itses.ibmy.metodosnumericos.service.SistemasEcuaciones;
import lombok.extern.slf4j.Slf4j;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SistemasEcuacionesImplementacion implements SistemasEcuaciones {

@Override
    public EliminacionGaussianaRespuesta eliminacionGaussiana(SistemaLineal request) {
        int n = request.getTamano();
        double[][] A = request.getMatriz();
        double[] b = request.getVector();

        // 0. Copiar la matriz original y construir la matriz aumentada M [n x (n+1)]
        double[][] matrizOriginal = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, matrizOriginal[i], 0, n);
        }

        double[][] M = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, M[i], 0, n);
            M[i][n] = b[i];
        }

        List<double[][]> pasos = new ArrayList<>();

        // 1. Fase de Eliminación con Pivoteo Parcial
        for (int k = 0; k < n - 1; k++) {
            // a) Pivoteo parcial: encontrar la fila con el mayor valor absoluto en la columna k
            int maxRow = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(M[i][k]) > Math.abs(M[maxRow][k])) {
                    maxRow = i;
                }
            }

            // Intercambiar filas si el mayor no está en la diagonal
            if (maxRow != k) {
                double[] temp = M[k];
                M[k] = M[maxRow];
                M[maxRow] = temp;
            }

            // b) Manejo defensivo: verificar pivote cero
            if (Math.abs(M[k][k]) < 1e-10) {
                return EliminacionGaussianaRespuesta.builder()
                        .matrizOriginal(matrizOriginal)
                        .pasosEliminacion(pasos)
                        .esSingular(true)
                        .build();
            }

            // c) Eliminar elementos debajo del pivote
            for (int i = k + 1; i < n; i++) {
                double factor = M[i][k] / M[k][k];
                for (int j = k; j <= n; j++) {
                    M[i][j] -= factor * M[k][j];
                }
            }

            // d) Guardar copia del estado actual de la matriz aumentada M
            pasos.add(copiarMatriz(M));
        }

        // 2. Verificar el último elemento pivote en M[n-1][n-1]
        if (Math.abs(M[n - 1][n - 1]) < 1e-10) {
            return EliminacionGaussianaRespuesta.builder()
                    .matrizOriginal(matrizOriginal)
                    .pasosEliminacion(pasos)
                    .esSingular(true)
                    .build();
        }

        // 3. Sustitución hacia atrás
        double[] variables = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double suma = 0.0;
            for (int j = i + 1; j < n; j++) {
                suma += M[i][j] * variables[j];
            }
            variables[i] = (M[i][n] - suma) / M[i][i];
        }

        // 4. Retornar el objeto de respuesta estructurado
        return EliminacionGaussianaRespuesta.builder()
                .matrizOriginal(matrizOriginal)
                .matrizTriangular(copiarMatriz(M))
                .pasosEliminacion(pasos)
                .variables(variables)
                .esSingular(false)
                .build();
    }

    /**
     * Helper para clonar una matriz bidimensional de tipo double.
     */
    

    @Override
public DeterminantesRespuesta determinante(SistemaLineal request) {
    int n = request.getTamano();
    double[][] a = request.getMatriz();
    double[] b = request.getVector();

    // Paso 1: Determinante de A completa
    double detA = calcularDeterminante(a, n);

    // Paso 2: Manejo defensivo - sistema singular
    if (Math.abs(detA) < 1e-10) {
        log.warn("Determinante de A es 0. Sistema singular.");
        return DeterminantesRespuesta.builder()
                .matrizOriginal(a)
                .vectorOriginal(b)
                .determinanteA(detA)
                .esSingular(true)
                .build();
    }

    // Paso 3: Regla de Cramer - construir cada matriz auxiliar Ai y su determinante
    List<double[][]> matricesAuxiliares = new ArrayList<>();
    double[] determinantesAuxiliares = new double[n];
    double[] variables = new double[n];

    for (int i = 0; i < n; i++) {
        double[][] ai = construirMatrizConColumnaReemplazada(a, b, i, n);
        double detAi = calcularDeterminante(ai, n);

        matricesAuxiliares.add(ai);
        determinantesAuxiliares[i] = detAi;
        variables[i] = detAi / detA;
    }

    // Paso 4: Construir respuesta completa
    return DeterminantesRespuesta.builder()
            .matrizOriginal(a)
            .vectorOriginal(b)
            .determinanteA(detA)
            .matricesAuxiliares(matricesAuxiliares)
            .determinantesAuxiliares(determinantesAuxiliares)
            .variables(variables)
            .esSingular(false)
            .build();
}


    @Override
    public GaussJordanRespuesta gaussJordan(SistemaLineal request) {
        int n = request.getTamano();
        double[][] a = request.getMatriz();
        double[] b = request.getVector();

        // Preservar la matriz original capturada para la vista
        double[][] matrizOriginal = copiarMatriz(a);

        // Paso 0: Construcción de la matriz aumentada [A|b] de tamaño n x (n+1)
        double[][] m = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, m[i], 0, n);
            m[i][n] = b[i];
        }

        List<double[][]> pasosReduccion = new ArrayList<>();

        // Paso 1: Algoritmo de Gauss-Jordan
        for (int k = 0; k < n; k++) {
            // a) Pivoteo parcial: buscar el valor absoluto máximo en la columna k
            int maxFila = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(m[i][k]) > Math.abs(m[maxFila][k])) {
                    maxFila = i;
                }
            }

            // Intercambio de filas si se encuentra un pivote mayor
            if (maxFila != k) {
                double[] temp = m[k];
                m[k] = m[maxFila];
                m[maxFila] = temp;
            }

            // b) Manejo defensivo: verificar si el pivote es cero
            double pivote = m[k][k];
            if (Math.abs(pivote) < 1e-10) {
                log.warn("Sistema singular detectado en pivote de columna {}", k);
                return GaussJordanRespuesta.builder()
                        .matrizOriginal(matrizOriginal)
                        .esSingular(true)
                        .pasosReduccion(pasosReduccion)
                        .build();
            }

            // c) Normalizar la fila pivote k para dejar M[k][k] == 1
            for (int j = 0; j <= n; j++) {
                m[k][j] /= pivote;
            }

            // d) Eliminar la columna k en TODAS las demás filas (i != k)
            for (int i = 0; i < n; i++) {
                if (i != k) {
                    double factor = m[i][k];
                    for (int j = 0; j <= n; j++) {
                        m[i][j] -= factor * m[k][j];
                    }
                }
            }

            // e) Guardar la copia del estado actual de la matriz aumentada
            pasosReduccion.add(copiarMatriz(m));
        }

        // Paso 3: Extraer el vector solución directamente de la columna n (sin sustitución hacia atrás)
        double[] variables = new double[n];
        for (int i = 0; i < n; i++) {
            variables[i] = m[i][n];
        }

        // Paso 4: Construir respuesta
        return GaussJordanRespuesta.builder()
                .matrizOriginal(matrizOriginal)
                .matrizIdentidad(m)
                .variables(variables)
                .pasosReduccion(pasosReduccion)
                .esSingular(false)
                .build();
    }

    /**
     * Utilidad para clonar matrices bidimensionales de forma profunda.
     */
    private double[][] copiarMatriz(double[][] origen) {
        double[][] destino = new double[origen.length][];
        for (int i = 0; i < origen.length; i++) {
            destino[i] = origen[i].clone();
        }
        return destino;
    }

    /**
     * Calcula el determinante de una matriz cuadrada mediante expansión por cofactores.
     * Válido para matrices de hasta 4x4 (límite del proyecto).
     */
    private double calcularDeterminante(double[][] m, int n) {
        if (n == 1) {
            return m[0][0];
        }
        if (n == 2) {
            return m[0][0] * m[1][1] - m[0][1] * m[1][0];
        }

        double det = 0.0;
        for (int j = 0; j < n; j++) {
            double[][] menor = obtenerMenor(m, 0, j, n);
            double cofactor = Math.pow(-1, j) * m[0][j] * calcularDeterminante(menor, n - 1);
            det += cofactor;
        }
        return det;
    }

    /**
     * Extrae la submatriz eliminando la fila y columna indicadas.
     */
    private double[][] obtenerMenor(double[][] m, int filaExcluir, int colExcluir, int n) {
        double[][] menor = new double[n - 1][n - 1];
        int fi = 0;
        for (int i = 0; i < n; i++) {
            if (i == filaExcluir) continue;
            int fj = 0;
            for (int j = 0; j < n; j++) {
                if (j == colExcluir) continue;
                menor[fi][fj] = m[i][j];
                fj++;
            }
            fi++;
        }
        return menor;
    }

    /**
     * Construye la matriz Ai reemplazando la columna indicada por el vector b.
     */
    private double[][] construirMatrizConColumnaReemplazada(double[][] a, double[] b, int columna, int n) {
        double[][] ai = new double[n][n];
        for (int fila = 0; fila < n; fila++) {
            System.arraycopy(a[fila], 0, ai[fila], 0, n);
            ai[fila][columna] = b[fila];
        }
        return ai;
    }
@Override
    public JacobiResultado jacobi(SistemaIterativo request) {
        int n = request.getTamano();
        double[][] a = request.getMatriz();
        double[] b = request.getVector();
        double[] xActual = request.getValoresIniciales().clone();
        double erObjetivo = request.getEr();
        int maxIter = request.getMaximoIteraciones();

        List<JacobiRespuesta> historial = new ArrayList<>();

        // Paso 1: Manejo defensivo - Validación de ceros en la diagonal principal
        for (int i = 0; i < n; i++) {
            if (Math.abs(a[i][i]) < 1e-10) {
                log.warn("Cero o elemento casi nulo detectado en la diagonal principal A[{}][{}]", i, i);
                return JacobiResultado.builder()
                        .historial(historial)
                        .convergio(false)
                        .variablesFinales(xActual)
                        .mensajeError("El método de Jacobi no puede aplicarse: el elemento en la diagonal A[" + (i + 1) + "][" + (i + 1) + "] es cero o indeterminado.")
                        .build();
            }
        }

        double[] xNuevo = new double[n];
        boolean convergio = false;

        // Paso 2: Ciclo iterativo del Algoritmo de Jacobi
        for (int k = 1; k <= maxIter; k++) {
            xNuevo = new double[n];
            double[] errorPorVariable = new double[n];
            double errorMaximo = 0.0;

            // a) Cálculo simultáneo de xNuevo usando ÚNICAMENTE valores de la iteración previa (xActual)
            for (int i = 0; i < n; i++) {
                double suma = 0.0;
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        suma += a[i][j] * xActual[j];
                    }
                }
                xNuevo[i] = (b[i] - suma) / a[i][i];

                // Manejo defensivo: Control de divergencia o desbordamiento numérico
                if (Double.isNaN(xNuevo[i]) || Double.isInfinite(xNuevo[i])) {
                    log.error("Divergencia numérica detectada en la iteración {}", k);
                    return JacobiResultado.builder()
                            .historial(historial)
                            .convergio(false)
                            .variablesFinales(xActual)
                            .mensajeError("El sistema ha divergió debido a un desbordamiento numérico. Verifique que la matriz sea estrictamente diagonalmente dominante.")
                            .build();
                }
            }

            // b) Cálculo del error relativo por variable y determinación del error máximo
            for (int i = 0; i < n; i++) {
                if (k == 1) {
                    errorPorVariable[i] = 100.0;
                } else {
                    errorPorVariable[i] = Math.abs(xNuevo[i]) < 1e-12 ? 0.0 :
                            Math.abs((xNuevo[i] - xActual[i]) / xNuevo[i]) * 100.0;
                }

                if (errorPorVariable[i] > errorMaximo) {
                    errorMaximo = errorPorVariable[i];
                }
            }

            // c) Registrar paso actual en el historial
            historial.add(JacobiRespuesta.builder()
                    .iteracion(k)
                    .valoresX(xNuevo.clone())
                    .errorPorVariable(errorPorVariable.clone())
                    .errorMaximo(errorMaximo)
                    .build());

            // d) Evaluar criterio de convergencia global
            if (errorMaximo < erObjetivo) {
                convergio = true;
                break;
            }

            // e) Actualizar vector para la siguiente iteración
            xActual = xNuevo.clone();
        }

        // Paso 3 & 4: Construir respuesta final
        return JacobiResultado.builder()
                .historial(historial)
                .convergio(convergio)
                .variablesFinales(xNuevo)
                .mensajeError(convergio ? null : "Se alcanzó el límite máximo de " + maxIter + " iteraciones sin lograr el nivel de tolerancia requerido (ER < " + erObjetivo + "%).")
                .build();
    }

    @Override
    public GaussSeidelResultado gaussSeidel(SistemaIterativo request) {
        int n = request.getTamano(); //
        double[][] a = request.getMatriz(); //
        double[] b = request.getVector(); //
        double[] x = request.getValoresIniciales().clone(); //
        double erObjetivo = request.getEr(); //[cite: 3]
        int maxIter = request.getMaximoIteraciones(); //[cite: 3]

        List<GaussSeidelRespuesta> historial = new ArrayList<>();

        // Paso 1: Manejo defensivo - Validación de ceros en la diagonal principal[cite: 3]
        for (int i = 0; i < n; i++) {
            if (Math.abs(a[i][i]) < 1e-10) {
                log.warn("Elemento nulo detectado en la diagonal principal A[{}][{}]", i, i);
                return GaussSeidelResultado.builder()
                        .historial(historial)
                        .convergio(false)
                        .variablesFinales(x)
                        .mensajeError("El método no puede aplicarse: hay un cero en la diagonal principal (A[" + (i + 1) + "][" + (i + 1) + "]).") //[cite: 3]
                        .build();
            }
        }

        boolean convergio = false;

        // Paso 2: Ciclo iterativo de Gauss-Seidel[cite: 3]
        for (int k = 1; k <= maxIter; k++) {
            double[] xAnterior = x.clone(); //[cite: 3]
            double[] errorPorVariable = new double[n];
            double errorMaximo = 0.0;

            // a) Cálculo in-place del vector X utilizando los valores más recientes[cite: 3]
            for (int i = 0; i < n; i++) {
                double suma = 0.0;
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        suma += a[i][j] * x[j]; //[cite: 3]
                    }
                }
                x[i] = (b[i] - suma) / a[i][i]; //[cite: 3]

                // Manejo defensivo por divergencia[cite: 3]
                if (Double.isNaN(x[i]) || Double.isInfinite(x[i])) {
                    log.error("Divergencia numérica en la iteración {}", k);
                    return GaussSeidelResultado.builder()
                            .historial(historial)
                            .convergio(false)
                            .variablesFinales(xAnterior)
                            .mensajeError("El sistema ha divergido. Verifique que la matriz sea diagonalmente dominante.") //[cite: 3]
                            .build();
                }
            }

            // b) Cálculo del error relativo[cite: 3]
            for (int i = 0; i < n; i++) {
                if (k == 1) {
                    errorPorVariable[i] = 100.0; //[cite: 3]
                } else {
                    errorPorVariable[i] = Math.abs(x[i]) < 1e-12 ? 0.0 :
                            Math.abs((x[i] - xAnterior[i]) / x[i]) * 100.0; //[cite: 3]
                }

                if (errorPorVariable[i] > errorMaximo) {
                    errorMaximo = errorPorVariable[i]; //[cite: 3]
                }
            }

            // c) Registrar en el historial[cite: 3]
            historial.add(GaussSeidelRespuesta.builder()
                    .iteracion(k)
                    .valoresAnteriores(xAnterior) //[cite: 3]
                    .valoresNuevos(x.clone()) //[cite: 3]
                    .errorPorVariable(errorPorVariable)
                    .errorMaximo(errorMaximo) //[cite: 3]
                    .build());

            // d) Evaluación del criterio de convergencia global[cite: 3]
            if (errorMaximo < erObjetivo) {
                convergio = true; //[cite: 3]
                break;
            }
        }

        // Paso 3 y 4: Retorno del resultado[cite: 3]
        return GaussSeidelResultado.builder()
                .historial(historial)
                .convergio(convergio)
                .variablesFinales(x) //[cite: 3]
                .mensajeError(convergio ? null : "Se alcanzó el límite máximo de iteraciones sin cumplir el criterio de tolerancia.") //[cite: 3]
                .build();
    }

    @Override
    public PuntoFijoSistemaResultado puntoFijoSistema(PuntoFijoSistema request) {
        // 0: Inicialización de variables, evaluador Symja y contador de iteraciones
        double x = request.getX0();
        double y = request.getY0();
        ExprEvaluator evaluator = new ExprEvaluator();
        List<PuntoFijoSistemaRespuesta> historial = new ArrayList<>();
        
        boolean convergio = false;
        String mensajeError = null;
        double errorMaximo = 100.0;
        
        // 1: Ciclo iterativo limitado por el máximo de iteraciones permitido
        for (int i = 0; i < request.getMaximoIteraciones(); i++) {
            // a) Guardar valores de entrada de la iteración
            double xAnterior = x;
            double yAnterior = y;
            
            try {
                // b) Evaluar G1 usando los valores viejos (xAnterior, yAnterior) para obtener el nuevo X
                evaluator.defineVariable("x", xAnterior);
                evaluator.defineVariable("y", yAnterior);
                double xNuevo = evaluator.eval(request.getG1()).evalDouble();
                x = xNuevo; 
                
                // c) Evaluar G2 usando el X recién actualizado y el Y viejo (yAnterior) para obtener el nuevo Y[cite: 3]
                evaluator.defineVariable("x", x); 
                evaluator.defineVariable("y", yAnterior);
                double yNuevo = evaluator.eval(request.getG2()).evalDouble();
                y = yNuevo;
                
                // d) Manejo defensivo contra desbordamiento o dominio inválido[cite: 3]
                if (Double.isInfinite(xNuevo) || Double.isNaN(xNuevo) || 
                    Double.isInfinite(yNuevo) || Double.isNaN(yNuevo)) {
                    mensajeError = "El sistema diverge o alguna función se evaluó fuera de su dominio válido.";
                    break;
                }
                
                // e) Cálculo de errores relativos (100% para la primera iteración)[cite: 3]
                double errorX = (i == 0) ? 100.0 : Math.abs((xNuevo - xAnterior) / xNuevo) * 100.0;
                double errorY = (i == 0) ? 100.0 : Math.abs((yNuevo - yAnterior) / yNuevo) * 100.0;
                
                // f) Calcular el error máximo[cite: 3]
                errorMaximo = Math.max(errorX, errorY);
                
                // g) Almacenar los resultados en el historial[cite: 3]
                historial.add(PuntoFijoSistemaRespuesta.builder()
                        .iteracion(i)
                        .x(xAnterior)
                        .y(yAnterior)
                        .xNuevo(xNuevo)
                        .yNuevo(yNuevo)
                        .errorX(errorX)
                        .errorY(errorY)
                        .errorMaximo(errorMaximo)
                        .build());
                
                // h) Evaluar criterio de convergencia[cite: 3]
                if (errorMaximo < request.getEr()) {
                    convergio = true;
                    x = xNuevo;
                    y = yNuevo;
                    break;
                }
            } catch (Exception e) {
                log.error("Error al evaluar expresiones con Symja: {}", e.getMessage());
                mensajeError = "Error de sintaxis en las funciones G1 o G2.";
                break;
            }
        }
        
        // 2 y 3: Configurar el resultado final y retornarlo[cite: 3]
        if (!convergio && mensajeError == null) {
            mensajeError = "No se alcanzó la convergencia en el máximo de iteraciones permitido.";
        }
        
        return PuntoFijoSistemaResultado.builder()
                .historial(historial)
                .convergio(convergio)
                .xFinal(x)
                .yFinal(y)
                .mensajeError(mensajeError)
                .build();
    }
    @Override
    public NewtonSistemaResultado newtonSistema(NewtonSistema request) {
        NewtonSistemaResultado resultado = new NewtonSistemaResultado();
        List<NewtonSistemaRespuesta> historial = new ArrayList<>();
        resultado.setHistorial(historial);

        double x = request.getX0();
        double y = request.getY0();
        
        ExprEvaluator util = new ExprEvaluator();

        try {
            // 1: Derivadas parciales simbólicas (calculadas una sola vez)
            IExpr dUdx_expr = util.eval("D(" + request.getU() + ", x)");
            IExpr dUdy_expr = util.eval("D(" + request.getU() + ", y)");
            IExpr dVdx_expr = util.eval("D(" + request.getV() + ", x)");
            IExpr dVdy_expr = util.eval("D(" + request.getV() + ", y)");

            for (int iteracion = 0; iteracion <= request.getMaximoIteraciones(); iteracion++) {
                double xi = x;
                double yi = y;

                // Definir variables en el entorno de Symja para la evaluación en el punto actual
                util.defineVariable("x", xi);
                util.defineVariable("y", yi);

                // 2b: Evaluación de funciones y derivadas
                double ui = util.eval(request.getU()).evalDouble();
                double vi = util.eval(request.getV()).evalDouble();
                double duDx = util.eval(dUdx_expr).evalDouble();
                double duDy = util.eval(dUdy_expr).evalDouble();
                double dvDx = util.eval(dVdx_expr).evalDouble();
                double dvDy = util.eval(dVdy_expr).evalDouble();

                // 2c: Cálculo del Jacobiano
                double jacobiano = (duDx * dvDy) - (duDy * dvDx);

                // 2d: Manejo defensivo (Jacobiano singular)
                if (Math.abs(jacobiano) < 1e-12) {
                    resultado.setConvergio(false);
                    resultado.setMensajeError("La matriz Jacobiana es singular (0) en este punto. El método no puede continuar.");
                    resultado.setXFinal(xi);
                    resultado.setYFinal(yi);
                    return resultado;
                }

                // 2e: Nuevos valores mediante la fórmula matricial
                double xiNuevo = xi - ((ui * dvDy) - (vi * duDy)) / jacobiano;
                double yiNuevo = yi - ((vi * duDx) - (ui * dvDx)) / jacobiano;

                // 2f: Manejo defensivo (Divergencia)
                if (Double.isInfinite(xiNuevo) || Double.isNaN(xiNuevo) || Double.isInfinite(yiNuevo) || Double.isNaN(yiNuevo)) {
                    resultado.setConvergio(false);
                    resultado.setMensajeError("El método diverge numéricamente desde el punto inicial.");
                    resultado.setXFinal(xi);
                    resultado.setYFinal(yi);
                    return resultado;
                }

                // 2g: Cálculo del error relativo
                double errorX = (iteracion == 0) ? 100.0 : Math.abs((xiNuevo - xi) / xiNuevo) * 100.0;
                double errorY = (iteracion == 0) ? 100.0 : Math.abs((yiNuevo - yi) / yiNuevo) * 100.0;

                // 2h: Almacenar en historial
                NewtonSistemaRespuesta respuesta = NewtonSistemaRespuesta.builder()
                        .iteracion(iteracion)
                        .xi(xi).yi(yi)
                        .xiNuevo(xiNuevo).yiNuevo(yiNuevo)
                        .ui(ui).vi(vi)
                        .duDx(duDx).duDy(duDy).dvDx(dvDx).dvDy(dvDy)
                        .jacobiano(jacobiano)
                        .errorX(errorX).errorY(errorY)
                        .build();
                
                historial.add(respuesta);

                // 2i: Actualizar variables para siguiente iteración
                x = xiNuevo;
                y = yiNuevo;

                // 2j: Evaluar convergencia
                if (errorX < request.getEr() && errorY < request.getEr() && iteracion > 0) {
                    resultado.setConvergio(true);
                    resultado.setXFinal(x);
                    resultado.setYFinal(y);
                    return resultado;
                }
            }

            // 3: Terminación sin converger por límite de iteraciones
            resultado.setConvergio(false);
            resultado.setMensajeError("Se alcanzó el límite de iteraciones sin lograr convergencia.");
            resultado.setXFinal(x);
            resultado.setYFinal(y);

        } catch (Exception e) {
            log.error("Error al evaluar expresiones en Newton Sistemas: ", e);
            resultado.setConvergio(false);
            resultado.setMensajeError("Error de sintaxis en las funciones o en la evaluación: " + e.getMessage());
        }

        return resultado;
    }
}