package br.edu.fatecgru.brinquedos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    @GetMapping("/sobre")
    public String sobre() {
        return "sobre";
    }
}