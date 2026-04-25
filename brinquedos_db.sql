-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 24/04/2026 às 23:16
-- Versão do servidor: 10.4.32-MariaDB
-- Versão do PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `brinquedos_db`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `produto`
--

CREATE TABLE `produto` (
  `id` bigint(20) NOT NULL,
  `codigo` varchar(50) DEFAULT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `categoria` varchar(100) DEFAULT NULL,
  `marca` varchar(100) DEFAULT NULL,
  `valor` double DEFAULT NULL,
  `imagem` varchar(255) DEFAULT NULL,
  `detalhes` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `produto`
--

INSERT INTO `produto` (`id`, `codigo`, `descricao`, `categoria`, `marca`, `valor`, `imagem`, `detalhes`) VALUES
(9, '1234', 'Boneco do Luva ', 'Bonecos', 'Youtuber', 50.73, '1776371462495_luvadepedreiro.jpg', 'Boneco Receba'),
(13, '2345', 'Carros super original', 'Carrinhos', 'Disney', 60, '1776371476766_carrosoriginal.jpg', 'Katchaw'),
(17, '2333', 'Benny\'s love you', 'Pelúcia', 'BENNYS', 50.09, '1776931803990_thumb_A8444569-FB5D-4412-BDF2-70C380883016.jpg', 'Pelúcia totalmente normal.'),
(18, '7878', 'Barbie princesa', 'Bonecos', 'Matel', 90.7, '1776932117087_images (2).jpg', 'Boneca da barbie'),
(19, '2122', 'Carrinhos Hotweels', 'Carrinhos', 'Hotweels Inc', 30, '1776932307918_images (1).jpg', 'Carrinhos pequenininhos');

-- --------------------------------------------------------

--
-- Estrutura para tabela `usuario`
--

CREATE TABLE `usuario` (
  `id` bigint(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `tipo` enum('CLIENTE','FUNCIONARIO') NOT NULL,
  `nome` varchar(100) NOT NULL,
  `cpf` varchar(14) DEFAULT NULL,
  `data_nascimento` date DEFAULT NULL,
  `telefone` varchar(15) DEFAULT NULL,
  `cargo` varchar(50) DEFAULT NULL,
  `data_contratacao` date DEFAULT NULL,
  `ativo` tinyint(1) DEFAULT 1,
  `criado_em` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `usuario`
--

INSERT INTO `usuario` (`id`, `email`, `senha`, `tipo`, `nome`, `cpf`, `data_nascimento`, `telefone`, `cargo`, `data_contratacao`, `ativo`, `criado_em`) VALUES
(6, 'larissamotadavi@gmail.com', '5f48529f1e826995d258eaeef8c499e7', 'FUNCIONARIO', 'Larissa Mota', NULL, NULL, NULL, 'Líder', '2025-01-08', 1, '2026-04-23 07:23:05'),
(7, 'rafael@gmail.com', '5f48529f1e826995d258eaeef8c499e7', 'FUNCIONARIO', 'Rafael', NULL, NULL, NULL, 'Líder', '2026-03-26', 1, '2026-04-23 08:51:34'),
(8, 'jadir@gmail.com', '5f48529f1e826995d258eaeef8c499e7', 'CLIENTE', 'Jadir', '44990097845', '2026-01-08', '(11) 98881-8973', NULL, NULL, 1, '2026-04-23 08:57:46');

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `produto`
--
ALTER TABLE `produto`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Índices de tabela `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `produto`
--
ALTER TABLE `produto`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT de tabela `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
