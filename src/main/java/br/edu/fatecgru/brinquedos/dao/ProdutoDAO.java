package br.edu.fatecgru.brinquedos.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.edu.fatecgru.brinquedos.model.Produto;

public class ProdutoDAO {

    private final String URL = "jdbc:mysql://localhost:3306/brinquedos_db";
    private final String USER = "root";
    private final String PASS = "";

    private Connection conectar() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // ===== INSERIR =====
    public void salvar(Produto p) {
        String sql = "INSERT INTO produto (codigo, descricao, categoria, marca, valor, imagem, detalhes) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getCodigo());
            stmt.setString(2, p.getDescricao());
            stmt.setString(3, p.getCategoria());
            stmt.setString(4, p.getMarca());
            stmt.setDouble(5, p.getValor());
            stmt.setString(6, p.getImagem());
            stmt.setString(7, p.getDetalhes());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== LISTAR =====
    public List<Produto> listar() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto";

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produto p = new Produto();

                p.setId(rs.getLong("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setDescricao(rs.getString("descricao"));
                p.setCategoria(rs.getString("categoria"));
                p.setMarca(rs.getString("marca"));
                p.setValor(rs.getDouble("valor"));
                p.setImagem(rs.getString("imagem"));
                p.setDetalhes(rs.getString("detalhes"));

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ===== BUSCAR POR ID =====
    public Produto buscarPorId(Long id) {
        String sql = "SELECT * FROM produto WHERE id = ?";
        Produto p = null;

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Produto();

                p.setId(rs.getLong("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setDescricao(rs.getString("descricao"));
                p.setCategoria(rs.getString("categoria"));
                p.setMarca(rs.getString("marca"));
                p.setValor(rs.getDouble("valor"));
                p.setImagem(rs.getString("imagem"));
                p.setDetalhes(rs.getString("detalhes"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    // ===== ATUALIZAR =====
    public void atualizar(Produto p) {
        String sql = "UPDATE produto SET codigo=?, descricao=?, categoria=?, marca=?, valor=?, imagem=?, detalhes=? WHERE id=?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getCodigo());
            stmt.setString(2, p.getDescricao());
            stmt.setString(3, p.getCategoria());
            stmt.setString(4, p.getMarca());
            stmt.setDouble(5, p.getValor());
            stmt.setString(6, p.getImagem());
            stmt.setString(7, p.getDetalhes());
            stmt.setLong(8, p.getId());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== EXCLUIR =====
    public void excluir(Long id) {
        String sql = "DELETE FROM produto WHERE id = ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Produto> listarPorCategoria(String categoria) {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE categoria = ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto p = new Produto();

                p.setId(rs.getLong("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setDescricao(rs.getString("descricao"));
                p.setCategoria(rs.getString("categoria"));
                p.setMarca(rs.getString("marca"));
                p.setValor(rs.getDouble("valor"));
                p.setImagem(rs.getString("imagem")); // ⭐ ESSA LINHA FALTAVA
                p.setDetalhes(rs.getString("detalhes"));

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    public List<String> listarCategorias() {
        List<String> categorias = new ArrayList<>();

        String sql = "SELECT DISTINCT categoria FROM produto";

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categorias.add(rs.getString("categoria"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return categorias;
    }
    
    public Produto buscarPrimeiroPorCategoria(String categoria) {

        String sql = "SELECT * FROM produto WHERE categoria = ? LIMIT 1";
        Produto p = null;

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Produto();

                p.setId(rs.getLong("id"));
                p.setDescricao(rs.getString("descricao"));
                p.setCategoria(rs.getString("categoria"));
                p.setImagem(rs.getString("imagem")); // ⭐ importante
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }
    
    public Produto buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM produto WHERE codigo = ?";
        Produto p = null;

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Produto();
                p.setId(rs.getLong("id"));
                p.setCodigo(rs.getString("codigo"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    public List<Produto> listarUltimos(int n) {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto ORDER BY id DESC LIMIT ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, n);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getLong("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setDescricao(rs.getString("descricao"));
                p.setCategoria(rs.getString("categoria"));
                p.setMarca(rs.getString("marca"));
                p.setValor(rs.getDouble("valor"));
                p.setImagem(rs.getString("imagem"));
                p.setDetalhes(rs.getString("detalhes"));
                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}

