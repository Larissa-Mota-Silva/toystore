package br.edu.fatecgru.brinquedos.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import br.edu.fatecgru.brinquedos.dao.ProdutoDAO;
import br.edu.fatecgru.brinquedos.model.Produto;

@Controller
public class CategoriaController {

    ProdutoDAO dao = new ProdutoDAO();

    @GetMapping("/categoria/{nome}")
    public String abrirCategoria(@PathVariable String nome,
                                 @RequestParam(required = false) String ordem,
                                 Model model) {

        List<Produto> produtos = dao.listarPorCategoria(nome);

        // 🔥 ORDENAÇÃO AQUI
        if (ordem != null) {
            switch (ordem) {
                case "menor":
                    produtos.sort((a, b) -> a.getValor().compareTo(b.getValor()));
                    break;

                case "maior":
                    produtos.sort((a, b) -> b.getValor().compareTo(a.getValor()));
                    break;

                case "az":
                    produtos.sort((a, b) -> a.getDescricao().compareToIgnoreCase(b.getDescricao()));
                    break;
            }
        }

        model.addAttribute("produtos", produtos);
        model.addAttribute("categoria", nome);

        return "categoria";
    }
}