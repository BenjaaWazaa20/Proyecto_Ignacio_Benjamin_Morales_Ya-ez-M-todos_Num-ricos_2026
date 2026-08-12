package mx.edu.itses.ibmy.metodosnumericos.controller;

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

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/raices")
public class RaicesController {

    @Autowired // <-- ¡Esta es la línea clave!
    private RaicesEcuaciones raicesEcuacionesService;

    @GetMapping
    public String index() {
        log.info("Accediendo a la página principal de Raíces de Ecuaciones");
        return "views/raices/index";
    }

    @GetMapping("/biseccion")
    public String metodoBiseccion(Model model) {
        log.info("Cargando formulario para Método de Bisección");
        model.addAttribute("biseccion", new Biseccion()); // [cite: 77]
        return "views/raices/biseccion/form"; // [cite: 77]
    }

    @PostMapping("/biseccion")
    public String algoritmoBiseccion(@ModelAttribute Biseccion biseccion, Model model) {
        log.info("Procesando cálculo de bisección para función: {}", biseccion.getFx());

        // Llamado a la capa de servicio para el algoritmo de Bisección
        BiseccionRespuesta[] resultados = raicesEcuacionesService.biseccion(biseccion); // [cite: 124]

        // Inyección del arreglo BiseccionRespuesta hacia la vista
        model.addAttribute("resultados", resultados); // [cite: 124]

        // Direcciona la salida a la vista de solución [cite: 124]
        return "views/raices/biseccion/solucion";
    }
    // 3.5 Nuevo método: Recibe el formulario, imprime en consola y redirige
    // @PostMapping("/biseccion")
    // public String algoritmoBiseccion(@ModelAttribute Biseccion biseccion) { //
    // [cite: 78]
    // log.info("--- Datos recibidos del formulario Bisección ---");
    // log.info("XL: {}", biseccion.getXl()); // [cite: 79]
    // log.info("XU: {}", biseccion.getXu()); // [cite: 80]
    // log.info("FX: {}", biseccion.getFx()); // [cite: 81]
    // log.info("ER: {}", biseccion.getEr()); // [cite: 82]
    // log.info("MaximoIteraciones: {}", biseccion.getMaximoIteraciones()); //
    // [cite: 83]

    // return "views/raices/empty"; // [cite: 84]
    // }

    @GetMapping("/regla-falsa")
    public String metodoReglaFalsa(Model model) { // [cite: 2]
        log.info("Cargando formulario para Método de Regla Falsa");
        model.addAttribute("reglaFalsa", new ReglaFalsa()); // [cite: 2]
        return "views/raices/reglafalsa/form"; // [cite: 2]
    }

    @PostMapping("/regla-falsa")
    public String algoritmoReglaFalsa(@ModelAttribute ReglaFalsa reglaFalsa, Model model) { // [cite: 2]
        log.info("Procesando cálculo numérico Regla Falsa para FX: {}", reglaFalsa.getFx());

        // Invoca a la lógica de negocio y recupera el arreglo de resultados
        ReglaFalsaRespuesta[] resultados = raicesEcuacionesService.reglaFalsa(reglaFalsa); // [cite: 2]

        // Inyecta el arreglo al modelo para ser renderizado por Thymeleaf
        model.addAttribute("iteraciones", resultados);

        // Envía la salida hacia la vista solucion.html
        return "views/raices/reglafalsa/solucion"; // [cite: 2]
    }

    @GetMapping("/punto-fijo")
    public String metodoPuntoFijo(Model model) {
        log.info("Renderizando formulario para Método de Punto Fijo");
        model.addAttribute("puntoFijo", new PuntoFijo());
        return "views/raices/puntofijo/form";
    }

    @PostMapping("/punto-fijo")
    public String algoritmoPuntoFijo(@ModelAttribute PuntoFijo puntoFijo, Model model) {
        log.info("Procesando Punto Fijo - X0: {}, FX: {}, GX: {}, ER: {}, MaxIter: {}",
                puntoFijo.getX0(), puntoFijo.getFx(), puntoFijo.getGx(),
                puntoFijo.getEr(), puntoFijo.getMaximoIteraciones());

        // Paso 6: Recibe el arreglo procesado y envía a solucion.html
        PuntoFijoRespuesta[] resultados = raicesEcuacionesService.puntoFijo(puntoFijo);
        model.addAttribute("iteraciones", resultados);

        return "views/raices/puntofijo/solucion";
    }

    @GetMapping("/newton-raphson")
    public String metodoNewtonRaphson(Model model) {
        log.info("Desplegando formulario para Método de Newton-Raphson");
        model.addAttribute("newton", new Newton());
        return "views/raices/newton/form";
    }

    @PostMapping("/newton-raphson")
    public String algoritmoNewtonRaphson(@ModelAttribute Newton newton, Model model) {
        log.info("Procesando Newton-Raphson - X0: {}, FX: {}, ER: {}, MaxIter: {}",
                newton.getX0(), newton.getFx(), newton.getEr(), newton.getMaximoIteraciones());

        // Paso 10: Invoca al servicio y envía el arreglo de respuestas hacia la vista
        // solucion.html
        NewtonRespuesta[] resultados = raicesEcuacionesService.newtonRaphson(newton);
        model.addAttribute("iteraciones", resultados);

        return "views/raices/newton/solucion";
    }

    @GetMapping("/secante")
    public String metodoSecante(Model model) {
        log.info("Desplegando formulario para Método de la Secante");
        model.addAttribute("secante", new Secante());
        return "views/raices/secante/form";
    }

    @PostMapping("/secante")
    public String algoritmoSecante(@ModelAttribute Secante secante, Model model) {
        log.info("Procesando método de Secante - X0: {}, X1: {}, FX: {}, ER: {}, MaxIter: {}",
                secante.getX0(), secante.getX1(), secante.getFx(), secante.getEr(), secante.getMaximoIteraciones());

        // Paso 10: Invoca al servicio, inyecta el array en el modelo y direcciona a la
        // vista solucion
        SecanteRespuesta[] resultados = raicesEcuacionesService.secante(secante);
        model.addAttribute("iteraciones", resultados);

        return "views/raices/secante/solucion";
    }

    @GetMapping("/secante-modificada")
    public String metodoSecanteModificada(Model model) {
        log.info("Desplegando formulario para Método de la Secante Modificada");
        model.addAttribute("secanteModificada", new SecanteModificada());
        return "views/raices/secantemodificada/form";
    }

    @PostMapping("/secante-modificada")
    public String algoritmoSecanteModificada(@ModelAttribute SecanteModificada secanteModificada, Model model) {
        log.info("Procesando Secante Modificada - X0: {}, FX: {}, sigma: {}, ER: {}, MaxIter: {}",
                secanteModificada.getX0(), secanteModificada.getFx(), secanteModificada.getSigma(),
                secanteModificada.getEr(), secanteModificada.getMaximoIteraciones());

        // Paso 11: Ejecutar el algoritmo y enviar el arreglo resultante a la vista
        // solucion.html
        SecanteModificadaRespuesta[] resultados = raicesEcuacionesService.secanteModificada(secanteModificada);
        model.addAttribute("iteraciones", resultados);

        return "views/raices/secantemodificada/solucion";
    }
}