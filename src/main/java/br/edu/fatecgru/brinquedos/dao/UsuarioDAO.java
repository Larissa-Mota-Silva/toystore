package br.edu.fatecgru.brinquedos.dao;

import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.edu.fatecgru.brinquedos.model.Usuario;

public class UsuarioDAO {
    private final String URL = "jdbc:mysql://localhost:3306/brinquedos_db";
    private final String USER = "root";
    private final String PASS = "";

    // Hash MD5 simples (SEM dependências externas)
    private String hashSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(senha.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return senha; // fallback
        }
    }

    private Connection conectar() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public void salvar(Usuario u) {
        String sql = """
            INSERT INTO usuario (email, senha, tipo, nome, cpf, data_nascimento, telefone, cargo, data_contratacao) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getEmail());
            stmt.setString(2, hashSenha(u.getSenha())); // ✅ Hash MD5
            stmt.setString(3, u.getTipo());
            stmt.setString(4, u.getNome());
            stmt.setString(5, u.getCpf());
            stmt.setDate(6, u.getDataNascimento() != null ? new Date(u.getDataNascimento().getTime()) : null);
            stmt.setString(7, u.getTelefone());
            stmt.setString(8, u.getCargo());
            stmt.setDate(9, u.getDataContratacao() != null ? new Date(u.getDataContratacao().getTime()) : null);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Usuario autenticar(String email, String senha) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND ativo = true";
        Usuario u = null;

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && hashSenha(senha).equals(rs.getString("senha"))) {
                u = new Usuario();

                u.setId(rs.getLong("id"));
                u.setEmail(rs.getString("email"));
                u.setTipo(rs.getString("tipo"));
                u.setNome(rs.getString("nome"));

                u.setCpf(rs.getString("cpf"));
                u.setTelefone(rs.getString("telefone"));
                u.setCargo(rs.getString("cargo"));

                u.setDataNascimento(rs.getDate("data_nascimento"));
                u.setDataContratacao(rs.getDate("data_contratacao"));

                u.setAtivo(rs.getBoolean("ativo"));
                u.setCriadoEm(rs.getDate("criado_em"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;
    }

    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE email = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cpfExiste(String cpf) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE cpf = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void atualizar(Usuario u) {
        String sql = """
            UPDATE usuario
            SET email=?, nome=?, cpf=?, telefone=?, cargo=?, data_nascimento=?, data_contratacao=?
            WHERE id=?
        """;

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getEmail());
            stmt.setString(2, u.getNome());
            stmt.setString(3, u.getCpf());
            stmt.setString(4, u.getTelefone());
            stmt.setString(5, u.getCargo());
            stmt.setDate(6, u.getDataNascimento() != null ? new Date(u.getDataNascimento().getTime()) : null);
            stmt.setDate(7, u.getDataContratacao() != null ? new Date(u.getDataContratacao().getTime()) : null);
            stmt.setLong(8, u.getId());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Usuario buscarPorId(Long id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getLong("id"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha")); // hash
                u.setTipo(rs.getString("tipo"));
                u.setNome(rs.getString("nome"));
                u.setCpf(rs.getString("cpf"));
                u.setTelefone(rs.getString("telefone"));
                u.setCargo(rs.getString("cargo"));
                u.setDataNascimento(rs.getDate("data_nascimento"));
                u.setDataContratacao(rs.getDate("data_contratacao"));
                u.setAtivo(rs.getBoolean("ativo"));
                u.setCriadoEm(rs.getDate("criado_em"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}