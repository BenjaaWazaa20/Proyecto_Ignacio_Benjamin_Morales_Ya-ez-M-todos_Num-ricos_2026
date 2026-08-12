package mx.edu.itses.ibmy.metodosnumericos.controller;

import lombok.extern.slf4j.Slf4j;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.DeterminantesRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.EliminacionGaussianaRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.GaussJordanRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.GaussSeidelResultado;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.JacobiResultado;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.NewtonSistema;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.NewtonSistemaResultado;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.PuntoFijoSistema;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.PuntoFijoSistemaResultado;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.SistemaIterativo;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.SistemaLineal;
import mx.edu.itses.ibmy.metodosnumericos.service.SistemasEcuaciones;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/sistemas")
public class SistemasController {

    public SistemasController(SistemasEcuaciones sistemasEcuacionesService) {
        this.sistemasEcuacionesService = sistemasEcuacionesService;
    }

    /**
     * Endpoint para el menú principal de Sistemas de Ecuaciones.
     */
    @GetMapping
    public String index(Model model) {
        log.info("Accediendo al menú principal de Sistemas de Ecuaciones Lineales");
        model.addAttribute("title", "Sistemas lineales");
        return "views/sistemas/index";
    }

    // ==========================================
    // MÉTODOS DIRECTOS
    // ==========================================

    @GetMapping("/determinantes")
    public String metodoDeterminantes(Model model) {
        model.addAttribute("seccion", "Sistemas lineales");
        model.addAttribute("sistemaLineal", new SistemaLineal());
        return "views/sistemas/determinantes/form";
    }

    @PostMapping("/determinantes")
    public String algoritmoDeterminantes(@ModelAttribute SistemaLineal request, Model model) {
        log.info("Procesando método de Determinantes para sistema {}x{}", request.getTamano(), request.getTamano());
        log.info("Matriz A capturada: {}", Arrays.deepToString(request.getMatriz()));
        log.info("Vector B capturado: {}", Arrays.toString(request.getVector()));

        DeterminantesRespuesta resultado = sistemasEcuacionesService.determinante(request);
        
        model.addAttribute("resultado", resultado);
        model.addAttribute("titulo", "Solución por Regla de Cramer (Determinantes)");
        
        return "views/sistemas/determinantes/solucion";
    }

    @GetMapping("/eliminacion-gaussiana")
    public String metodoEliminacionGaussiana(Model model) {
        SistemaLineal sistemaLineal = new SistemaLineal();
        sistemaLineal.setTamano(3);
        model.addAttribute("sistemaLineal", sistemaLineal);
        return "views/sistemas/eliminaciongaussiana/form";
    }

   @PostMapping("/eliminacion-gaussiana")
    public String algoritmoEliminacionGaussiana(@ModelAttribute("sistema") SistemaLineal sistema, Model model) {
        log.info("Procesando Eliminación Gaussiana - Tamaño: {}", sistema.getTamano());
        log.info("Matriz de Coeficientes A: {}", Arrays.deepToString(sistema.getMatriz()));
        log.info("Vector Términos Independientes b: {}", Arrays.toString(sistema.getVector()));

        EliminacionGaussianaRespuesta resultado = sistemasEcuacionesService.eliminacionGaussiana(sistema);
        model.addAttribute("resultado", resultado);

        return "views/sistemas/eliminaciongaussiana/solucion";
    }

 @GetMapping("/gauss-jordan")
    public String metodoGaussJordan(Model model) {
        log.info("Desplegando formulario para el método de Gauss-Jordan");
        model.addAttribute("sistemaLineal", new SistemaLineal());
        return "views/sistemas/gaussjordan/form";
    }


    @Autowired
private SistemasEcuaciones sistemasEcuacionesService;

@PostMapping("/gauss-jordan")
    public String algoritmoGaussJordan(@ModelAttribute SistemaLineal sistemaLineal, Model model) {
        log.info("Procesando Gauss-Jordan - Tamaño: {}", sistemaLineal.getTamano());
        log.info("Matriz recibida: {}", Arrays.deepToString(sistemaLineal.getMatriz()));
        log.info("Vector recibido: {}", Arrays.toString(sistemaLineal.getVector()));

        // Manejo defensivo: Validación estructural antes de delegar al servicio
        if (sistemaLineal.getMatriz() == null || sistemaLineal.getVector() == null ||
            sistemaLineal.getMatriz().length != sistemaLineal.getTamano() ||
            sistemaLineal.getVector().length != sistemaLineal.getTamano()) {
            
            log.error("Dimensiones de matriz o vector inconsistentes con el tamaño especificado.");
            model.addAttribute("error", "Los datos ingresados están incompletos o son inconsistentes.");
            model.addAttribute("sistemaLineal", sistemaLineal);
            return "views/sistemas/gaussjordan/form";
        }

        GaussJordanRespuesta respuesta = sistemasEcuacionesService.gaussJordan(sistemaLineal);
        
        model.addAttribute("resultado", respuesta);
        model.addAttribute("titulo", "Solución por Gauss-Jordan");
        model.addAttribute("seccion", "Sistemas Lineales");

        return "views/sistemas/gaussjordan/solucion";
    }

    // ==========================================
    // MÉTODOS ITERATIVOS
    // ==========================================

  @GetMapping("/jacobi")
    public String metodoJacobi(Model model) {
        SistemaIterativo sistemaIterativo = SistemaIterativo.builder()
                .tamano(3)
                .er(0.0001)
                .maximoIteraciones(100)
                .build();
                
        model.addAttribute("sistemaIterativo", sistemaIterativo);
        model.addAttribute("titulo", "Método de Jacobi");
        model.addAttribute("seccion", "Sistemas Lineales");
        return "views/sistemas/jacobi/form";
    }

    @PostMapping("/jacobi")
    public String algoritmoJacobi(@ModelAttribute SistemaIterativo sistemaIterativo, Model model) {
        log.info("Procesando Método de Jacobi - Tamaño: {}", sistemaIterativo.getTamano());
        log.info("Matriz de coeficientes A: {}", Arrays.deepToString(sistemaIterativo.getMatriz()));
        log.info("Vector de términos b: {}", Arrays.toString(sistemaIterativo.getVector()));
        log.info("Aproximación inicial X0: {}", Arrays.toString(sistemaIterativo.getValoresIniciales()));
        log.info("Tolerancia ER: {}%", sistemaIterativo.getEr());
        log.info("Máximo iteraciones: {}", sistemaIterativo.getMaximoIteraciones());

        // Manejo defensivo: Validación de consistencia estructural
        if (sistemaIterativo.getMatriz() == null || sistemaIterativo.getVector() == null ||
            sistemaIterativo.getValoresIniciales() == null ||
            sistemaIterativo.getMatriz().length != sistemaIterativo.getTamano()) {
            
            log.error("Estructura de datos incompleta o inconsistente enviada al método de Jacobi.");
            model.addAttribute("error", "Los datos ingresados para la matriz o vectores están incompletos.");
            model.addAttribute("sistemaIterativo", sistemaIterativo);
            return "views/sistemas/jacobi/form";
        }

        JacobiResultado resultado = sistemasEcuacionesService.jacobi(sistemaIterativo);

        model.addAttribute("resultado", resultado);
        model.addAttribute("sistemaIterativo", sistemaIterativo);
        model.addAttribute("titulo", "Resultado Método de Jacobi");
        model.addAttribute("seccion", "Sistemas Lineales");

        return "views/sistemas/jacobi/solucion";
    }

    @GetMapping("/gauss-seidel")
    public String metodoGaussSeidel(Model model) {
        model.addAttribute("sistemaIterativo", new SistemaIterativo()); //[cite: 3]
        return "views/sistemas/gaussseidel/form"; //[cite: 3]
    }

    @PostMapping("/gauss-seidel")
    public String algoritmoGaussSeidel(@ModelAttribute SistemaIterativo request, Model model) {
        log.info("Procesando Gauss-Seidel - Tamaño: {}", request.getTamano());
        // (Tus logs existentes de la fase 24 van aquí)

        int n = request.getTamano();
        if (request.getMatriz() == null || request.getMatriz().length != n ||
            request.getVector() == null || request.getVector().length != n ||
            request.getValoresIniciales() == null || request.getValoresIniciales().length != n) {
            
            model.addAttribute("error", "Las dimensiones capturadas no coinciden con el tamaño del sistema.");
            return "views/sistemas/gaussseidel/form";
        }

        // Ejecutar algoritmo[cite: 3]
        GaussSeidelResultado resultado = sistemasEcuacionesService.gaussSeidel(request); //[cite: 3]

        // Pasar modelo a la vista[cite: 3]
        model.addAttribute("resultado", resultado);
        model.addAttribute("sistemaIterativo", request);

        return "views/sistemas/gaussseidel/solucion"; //[cite: 3]
    }

    // ==========================================
    // SISTEMAS DE ECUACIONES NO LINEALES
    // ==========================================

    @GetMapping("/punto-fijo")
public String metodoPuntoFijoSistemas(Model model) {
    model.addAttribute("puntoFijoSistema", new PuntoFijoSistema()); //[cite: 3]
    return "views/sistemas/puntofijosistema/form"; //[cite: 3]
}

@PostMapping("/punto-fijo")
    public String algoritmoPuntoFijoSistemas(@ModelAttribute PuntoFijoSistema request, Model model) {
        log.info("Punto Fijo Sistemas -> X0: {}, Y0: {}, F1: {}, F2: {}, G1: {}, G2: {}, ER: {}, MaxIter: {}",
                request.getX0(), request.getY0(), request.getF1(), request.getF2(), 
                request.getG1(), request.getG2(), request.getEr(), request.getMaximoIteraciones());

        PuntoFijoSistemaResultado resultado = sistemasEcuacionesService.puntoFijoSistema(request);
        
        model.addAttribute("resultado", resultado);
        model.addAttribute("seccion", "Sistemas lineales");
        
        return "views/sistemas/puntofijosistema/solucion";
    }

    @GetMapping("/newton-raphson")
    public String metodoNewtonRaphsonSistemas(Model model) {
        model.addAttribute("newtonSistema", new NewtonSistema());
        return "views/sistemas/newtonraphsonsistema/form";
    }

    @PostMapping("/newton-raphson")
    public String algoritmoNewtonSistemas(@ModelAttribute NewtonSistema request, Model model) {
        log.info("Calculando Newton-Raphson Sistemas -> X0: {}, Y0: {}, U: {}, V: {}, ER: {}, MaximoIteraciones: {}",
                request.getX0(), request.getY0(), request.getU(), request.getV(), request.getEr(), request.getMaximoIteraciones());
        
        NewtonSistemaResultado resultado = sistemasEcuacionesService.newtonSistema(request);
        
        model.addAttribute("resultado", resultado);
        return "views/sistemas/newtonsistema/solucion";
    }
}