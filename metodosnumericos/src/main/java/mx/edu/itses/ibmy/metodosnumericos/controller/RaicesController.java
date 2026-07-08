package mx.edu.itses.ibmy.metodosnumericos.controller;

import lombok.extern.slf4j.Slf4j;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.Biseccion;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.raices.BiseccionRespuesta;
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
    //@PostMapping("/biseccion")
    //public String algoritmoBiseccion(@ModelAttribute Biseccion biseccion) { // [cite: 78]
    //    log.info("--- Datos recibidos del formulario Bisección ---");
    //    log.info("XL: {}", biseccion.getXl()); // [cite: 79]
    //    log.info("XU: {}", biseccion.getXu()); // [cite: 80]
    //    log.info("FX: {}", biseccion.getFx()); // [cite: 81]
    //    log.info("ER: {}", biseccion.getEr()); // [cite: 82]
    //    log.info("MaximoIteraciones: {}", biseccion.getMaximoIteraciones()); // [cite: 83]
        
    //    return "views/raices/empty"; // [cite: 84]
   // }
    

    @GetMapping("/regla-falsa")
    public String metodoReglaFalsa() {
        log.info("Direccionamiento a Método de Regla Falsa");
        return "views/raices/empty";
    }

    @GetMapping("/punto-fijo")
    public String metodoPuntoFijo() {
        log.info("Direccionamiento a Método de Iteración de punto Fijo");
        return "views/raices/empty";
    }

    @GetMapping("/newton-raphson")
    public String metodoNewtonRaphson() {
        log.info("Direccionamiento a Método de Newton Raphson");
        return "views/raices/empty";
    }

    @GetMapping("/secante")
    public String metodoSecante() {
        log.info("Direccionamiento a Método de la Secante");
        return "views/raices/empty";
    }

    @GetMapping("/secante-modificada")
    public String metodoSecanteModificada() {
        log.info("Direccionamiento a Método de la Secante Modificada");
        return "views/raices/empty";
    }
}