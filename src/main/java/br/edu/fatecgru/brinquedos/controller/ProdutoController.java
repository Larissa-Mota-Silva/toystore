package br.edu.fatecgru.brinquedos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import br.edu.fatecgru.brinquedos.model.Produto;
import br.edu.fatecgru.brinquedos.model.Usuario;
import br.edu.fatecgru.brinquedos.dao.ProdutoDAO;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProdutoController {

    ProdutoDAO dao = new ProdutoDAO();
    
    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        model.addAttribute("categorias", dao.listarCategorias());
        return "catalogo";
    }
    
    // ===== HOME =====
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("produtos", dao.listarUltimos(6));
        return "home";
    }

    @GetMapping("/detalhe/{id}")
    public String detalhe(@PathVariable Long id, Model model) {

        Produto produto = dao.buscarPorId(id);

        // 🔥 busca produtos da mesma categoria
        List<Produto> relacionados = dao.listarPorCategoria(produto.getCategoria());

        // 🚫 remove o próprio produto da lista
        relacionados.removeIf(p -> p.getId().equals(produto.getId()));

        model.addAttribute("produto", produto);
        model.addAttribute("relacionados", relacionados);

        return "detalhe";
    }

    // ===== ADMIN =====
    @GetMapping("/admin")
    public String admin(Model model, HttpSession session) {
        Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
        if (logado == null || !"FUNCIONARIO".equals(logado.getTipo())) {
            return "redirect:/login";
        }
        model.addAttribute("produtos", dao.listar());
        return "admin";
    }

    // ===== NOVO =====
    @GetMapping("/admin/novo")
    public String novo(Model model, HttpSession session) {
        Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
        if (logado == null || !"FUNCIONARIO".equals(logado.getTipo())) {
            return "redirect:/login";
        }
        model.addAttribute("produto", new Produto());
        return "form";
    }

    // ===== EDITAR =====
    @GetMapping("/admin/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
        if (logado == null || !"FUNCIONARIO".equals(logado.getTipo())) {
            return "redirect:/login";
        }
        Produto produto = dao.buscarPorId(id);
        model.addAttribute("produto", produto);
        return "form";
    }

    // ===== SALVAR (NOVO + EDITAR) =====
    @PostMapping("/admin/salvar")
    public String salvar(Produto produto,
                         @RequestParam("imagemFile") MultipartFile imagemFile,
                         Model model,
                         HttpSession session) {

        Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
        if (logado == null || !"FUNCIONARIO".equals(logado.getTipo())) {
            return "redirect:/login";
        }

        try {

            // código: obrigatório e 4 dígitos
            if (produto.getCodigo() == null || !produto.getCodigo().matches("\\d{4}")) {
                model.addAttribute("erro", "O código deve ter exatamente 4 números.");
                model.addAttribute("produto", produto);
                return "form";
            }

            // verificar duplicado
            Produto existenteCodigo = dao.buscarPorCodigo(produto.getCodigo());
            if (existenteCodigo != null && !existenteCodigo.getId().equals(produto.getId())) {
                model.addAttribute("erro", "Já existe um produto com esse código.");
                model.addAttribute("produto", produto);
                return "form";
            }

            // descrição obrigatória
            if (produto.getDescricao() == null || produto.getDescricao().trim().isEmpty()) {
                model.addAttribute("erro", "Descrição é obrigatória.");
                model.addAttribute("produto", produto);
                return "form";
            }

            // valor válido
            if (produto.getValor() == null || produto.getValor() <= 0) {
                model.addAttribute("erro", "Valor deve ser maior que zero.");
                model.addAttribute("produto", produto);
                return "form";
            }

         // ===== IMAGEM =====

         // se for NOVO produto → imagem obrigatória
         if (produto.getId() == null && imagemFile.isEmpty()) {
             model.addAttribute("erro", "A imagem é obrigatória.");
             model.addAttribute("produto", produto);
             return "form";
         }

         if (!imagemFile.isEmpty()) {

             String nomeArquivo = System.currentTimeMillis() + "_" + imagemFile.getOriginalFilename();
             String caminho = System.getProperty("user.dir") + "/uploads/";

             File pasta = new File(caminho);
             if (!pasta.exists()) pasta.mkdirs();

             File destino = new File(caminho + nomeArquivo);
             imagemFile.transferTo(destino);

             produto.setImagem(nomeArquivo);

         } else {

        	    // se não enviou nova imagem, mantém a antiga do banco
        	    if (produto.getId() != null) {
        	        Produto existente = dao.buscarPorId(produto.getId());

        	        if (existente != null) {
        	            produto.setImagem(existente.getImagem());
        	        }
        	    }
        	}

            // ===== SALVAR =====
            if (produto.getId() == null) {
                dao.salvar(produto);
            } else {
                dao.atualizar(produto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin";
    }

    // ===== EXCLUIR =====
    @GetMapping("/admin/excluir/{id}")
    public String excluir(@PathVariable Long id, HttpSession session) {
        Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
        if (logado == null || !"FUNCIONARIO".equals(logado.getTipo())) {
            return "redirect:/login";
        }
        dao.excluir(id);
        return "redirect:/admin";
    }
    
    
}