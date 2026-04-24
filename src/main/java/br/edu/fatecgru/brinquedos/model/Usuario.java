package br.edu.fatecgru.brinquedos.model;

import java.util.Date;

public class Usuario {
    private Long id;
    private String email;
    private String senha;
    private String tipo; // "CLIENTE" ou "FUNCIONARIO"
    private String nome;
    private String cpf;
    private Date dataNascimento;
    private String telefone;
    private String cargo;
    private Date dataContratacao;
    private boolean ativo;
    private Date criadoEm;

    // GETTERS E SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public Date getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public Date getDataContratacao() { return dataContratacao; }
    public void setDataContratacao(Date dataContratacao) { this.dataContratacao = dataContratacao; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public Date getCriadoEm() { return criadoEm; }
    public void setCriadoEm(Date criadoEm) { this.criadoEm = criadoEm; }
}