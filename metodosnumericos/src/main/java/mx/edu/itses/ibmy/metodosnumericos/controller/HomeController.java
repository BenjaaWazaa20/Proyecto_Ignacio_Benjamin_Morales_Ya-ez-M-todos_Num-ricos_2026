package mx.edu.itses.ibmy.metodosnumericos.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        log.info("Accediendo a la página principal de Métodos Numéricos");
        model.addAttribute("titulo", "Bienvenido a Métodos Numéricos");
        return "home/index";
    }
}