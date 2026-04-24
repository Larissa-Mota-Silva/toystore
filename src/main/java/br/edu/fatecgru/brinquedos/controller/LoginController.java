package br.edu.fatecgru.brinquedos.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.fatecgru.brinquedos.model.Usuario;
import br.edu.fatecgru.brinquedos.dao.UsuarioDAO;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    UsuarioDAO dao = new UsuarioDAO();

    // Código secreto exigido para cadastro de funcionário
    private static final String CODIGO_FUNCIONARIO = "FUNCIONARIO2024";

    // ================= LOGIN =================

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String fazerLogin(@RequestParam String email,
                             @RequestParam String senha,
                             HttpSession session,
                             RedirectAttributes redirect) {

        Usuario usuario = dao.autenticar(email, senha);

        if (usuario != null) {
            session.setAttribute("usuarioLogado", usuario);

            if ("FUNCIONARIO".equals(usuario.getTipo())) {
                return "redirect:/admin";
            }
            return "redirect:/";
        }

        redirect.addFlashAttribute("erro", "Email ou senha inválidos!");
        return "redirect:/login";
    }

    // ================= PERFIL =================

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfilPage(HttpSession session, Model model) {
        model.addAttribute("usuario", session.getAttribute("usuarioLogado"));
        return "editar";
    }

    @PostMapping("/perfil/editar")
    public String editarPerfil(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String dataNascimento,
            @RequestParam(required = false) String cargo,
            @RequestParam(required = false) String dataContratacao,
            HttpSession session) {

        Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
        
        if (logado == null) {
            return "redirect:/login";
        }

        // 🔥 CRIA E ATUALIZA NO BANCO
        Usuario usuario = new Usuario();
        usuario.setId(logado.getId());
        usuario.setTipo(logado.getTipo());
        usuario.setSenha(logado.getSenha());
        usuario.setAtivo(logado.isAtivo());
        
        usuario.setNome(nome);
        usuario.setEmail(email);

        // CPF: strip máscara (só para clientes)
        if ("CLIENTE".equals(logado.getTipo()) && cpf != null) {
            usuario.setCpf(cpf.replaceAll("[^0-9]", ""));
        }
        usuario.setTelefone(telefone);
        usuario.setCargo(cargo);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date hoje = new Date();

            if (dataNascimento != null && !dataNascimento.isEmpty()) {
                Date dn = sdf.parse(dataNascimento);
                if (dn.after(hoje)) {
                    dn = null; // ignora data futura silenciosamente
                }
                usuario.setDataNascimento(dn);
            }
            if (dataContratacao != null && !dataContratacao.isEmpty()) {
                Date dc = sdf.parse(dataContratacao);
                if (dc.after(hoje)) {
                    dc = null;
                }
                usuario.setDataContratacao(dc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dao.atualizar(usuario);
        
        // 🔥 RECARREGA DO BANCO para ter dados atualizados e consistentes
        Usuario usuarioAtualizado = dao.buscarPorId(logado.getId());
        session.setAttribute("usuarioLogado", usuarioAtualizado);

        return "redirect:/perfil";
    }

    // ================= LOGOUT =================

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ================= CADASTRO =================

    @GetMapping("/cadastro")
    public String cadastro() {
        return "redirect:/login?tab=cadastro";
    }

    @PostMapping("/cadastro")
    public String salvarCadastro(@RequestParam String tipo,
                                 @RequestParam String nome,
                                 @RequestParam String email,
                                 @RequestParam String senha,
                                 @RequestParam(required = false) String cpf,
                                 @RequestParam(required = false) String telefone,
                                 @RequestParam(required = false) String dataNascimento,
                                 @RequestParam(required = false) String cargo,
                                 @RequestParam(required = false) String dataContratacao,
                                 @RequestParam(required = false) String codigoFuncionario,
                                 RedirectAttributes redirect) {

        // --- validação de email ---
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            redirect.addFlashAttribute("erro", "Email inválido!");
            return "redirect:/login?tab=cadastro";
        }

        // --- validação de senha (mínimo 6 caracteres) ---
        if (senha == null || senha.length() < 6) {
            redirect.addFlashAttribute("erro", "A senha deve ter pelo menos 6 caracteres!");
            return "redirect:/login?tab=cadastro";
        }

        // --- email duplicado ---
        if (dao.emailExiste(email)) {
            redirect.addFlashAttribute("erro", "Email já cadastrado!");
            return "redirect:/login?tab=cadastro";
        }

        // --- validações específicas por tipo ---
        if ("CLIENTE".equals(tipo)) {
            // CPF: obrigatório, exatamente 11 dígitos numéricos
            String cpfLimpo = cpf == null ? "" : cpf.replaceAll("[^0-9]", "");
            if (cpfLimpo.length() != 11) {
                redirect.addFlashAttribute("erro", "CPF inválido! Informe os 11 dígitos.");
                return "redirect:/login?tab=cadastro";
            }
            if (dao.cpfExiste(cpfLimpo)) {
                redirect.addFlashAttribute("erro", "CPF já cadastrado!");
                return "redirect:/login?tab=cadastro";
            }
            cpf = cpfLimpo;

        } else if ("FUNCIONARIO".equals(tipo)) {
            // Código secreto obrigatório
            if (!CODIGO_FUNCIONARIO.equals(codigoFuncionario)) {
                redirect.addFlashAttribute("erro", "Código de funcionário inválido!");
                return "redirect:/login?tab=cadastro";
            }
        } else {
            redirect.addFlashAttribute("erro", "Tipo de conta inválido!");
            return "redirect:/login?tab=cadastro";
        }

        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setSenha(senha);
        u.setTipo(tipo);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date hoje = new Date();

            if ("CLIENTE".equals(tipo)) {
                u.setCpf(cpf);
                u.setTelefone(telefone);

                if (dataNascimento != null && !dataNascimento.isEmpty()) {
                    Date dn = sdf.parse(dataNascimento);
                    if (dn.after(hoje)) {
                        redirect.addFlashAttribute("erro", "Data de nascimento não pode ser no futuro!");
                        return "redirect:/login?tab=cadastro";
                    }
                    u.setDataNascimento(dn);
                }

            } else {
                u.setCargo(cargo);

                if (dataContratacao != null && !dataContratacao.isEmpty()) {
                    Date dc = sdf.parse(dataContratacao);
                    if (dc.after(hoje)) {
                        redirect.addFlashAttribute("erro", "Data de contratação não pode ser no futuro!");
                        return "redirect:/login?tab=cadastro";
                    }
                    u.setDataContratacao(dc);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        dao.salvar(u);

        redirect.addFlashAttribute("sucesso", "Cadastro realizado com sucesso! Faça login.");
        return "redirect:/login";
    }
    
    
}