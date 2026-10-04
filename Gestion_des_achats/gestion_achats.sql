-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : mer. 23 sep. 2026 à 02:16
-- Version du serveur : 8.4.3
-- Version de PHP : 8.3.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gestion_achats`
--

-- --------------------------------------------------------

--
-- Structure de la table `categorie`
--

CREATE TABLE `categorie` (
  `id_C` int NOT NULL,
  `nom_C` varchar(100) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `categorie`
--

INSERT INTO `categorie` (`id_C`, `nom_C`) VALUES
(1, 'Ouils de bureaux'),
(2, 'Fournitures scolaire');

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

CREATE TABLE `commande` (
  `num_Cm` int NOT NULL,
  `date_Cm` date NOT NULL,
  `date_prevue` date NOT NULL,
  `statut_Cm` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'En attente',
  `id_F` int NOT NULL
) ;

--
-- Déchargement des données de la table `commande`
--

INSERT INTO `commande` (`num_Cm`, `date_Cm`, `date_prevue`, `statut_Cm`, `id_F`) VALUES
(1, '2026-09-22', '2026-09-25', 'Annulee', 1),
(2, '2026-09-22', '2026-09-29', 'Annulee', 2),
(3, '2026-09-22', '2026-10-03', 'En attente', 2),
(4, '2026-09-22', '2026-09-24', 'Livree', 2),
(5, '2026-09-22', '2026-09-24', 'Annulee', 1),
(6, '2026-09-22', '2026-09-25', 'Annulee', 1),
(7, '2026-09-22', '2026-09-29', 'Livree', 2),
(8, '2026-09-23', '2026-09-27', 'En attente', 3),
(9, '2026-09-23', '2026-10-03', 'En attente', 1);

-- --------------------------------------------------------

--
-- Structure de la table `fournir`
--

CREATE TABLE `fournir` (
  `id_F` int NOT NULL,
  `id_P` int NOT NULL,
  `prix_fournisseur` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `fournir`
--

INSERT INTO `fournir` (`id_F`, `id_P`, `prix_fournisseur`) VALUES
(1, 1, 12000.00),
(1, 2, 1300.00),
(1, 3, 18000.00),
(2, 2, 1300.00),
(3, 2, 1200.00),
(3, 4, 500.00),
(3, 5, 6000.00),
(4, 1, 3.00);

-- --------------------------------------------------------

--
-- Structure de la table `fournisseur`
--

CREATE TABLE `fournisseur` (
  `id_F` int NOT NULL,
  `nom_F` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `adr_F` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `tel_F` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email_F` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ;

--
-- Déchargement des données de la table `fournisseur`
--

INSERT INTO `fournisseur` (`id_F`, `nom_F`, `adr_F`, `tel_F`, `email_F`) VALUES
(1, 'Chandarana', 'Analakely', '0348966453', 'chandarana@gmail.com'),
(2, 'DELI', 'Behoririka', '0325677654', 'Deli@gmail.com'),
(3, 'MG', 'Soarano', '0328977654', 'MG@gmail.com'),
(4, 'Maped', 'Ivato', '0348977654', 'Maped@gmail.com');

-- --------------------------------------------------------

--
-- Structure de la table `ligne_commande`
--

CREATE TABLE `ligne_commande` (
  `id_Lc` int NOT NULL,
  `num_Cm` int NOT NULL,
  `id_P` int NOT NULL,
  `qte` int NOT NULL,
  `montant` decimal(10,2) NOT NULL
) ;

--
-- Déchargement des données de la table `ligne_commande`
--

INSERT INTO `ligne_commande` (`id_Lc`, `num_Cm`, `id_P`, `qte`, `montant`) VALUES
(1, 1, 1, 4, 48000.00),
(2, 1, 2, 5, 6500.00),
(3, 2, 2, 5, 6500.00),
(4, 3, 2, 1, 1300.00),
(5, 4, 2, 4, 5200.00),
(6, 1, 1, 5, 12000.00),
(7, 1, 2, 6, 1300.00),
(8, 7, 2, 1, 1300.00),
(9, 8, 5, 10, 60000.00),
(10, 9, 3, 4, 72000.00),
(11, 9, 2, 5, 6500.00);

-- --------------------------------------------------------

--
-- Structure de la table `paiement`
--

CREATE TABLE `paiement` (
  `id_paiement` int NOT NULL,
  `num_Cm` int NOT NULL,
  `montant_paye` decimal(10,2) NOT NULL,
  `date_paiement` date NOT NULL,
  `mode_paiement` varchar(30) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `paiement`
--

INSERT INTO `paiement` (`id_paiement`, `num_Cm`, `montant_paye`, `date_paiement`, `mode_paiement`) VALUES
(1, 7, 1300.00, '2026-09-22', 'Especes'),
(2, 4, 1200.00, '2026-09-22', 'Virement'),
(3, 4, 4000.00, '2026-09-22', 'Mobile Money'),
(4, 9, 12300.00, '2026-09-23', 'Especes');

-- --------------------------------------------------------

--
-- Structure de la table `produit`
--

CREATE TABLE `produit` (
  `id_P` int NOT NULL,
  `nom_P` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `id_C` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `produit`
--

INSERT INTO `produit` (`id_P`, `nom_P`, `id_C`) VALUES
(1, 'Cahiers GM', 2),
(2, 'stylos bleu', 2),
(3, 'classeurs', 1),
(4, 'Gomme', 2),
(5, 'Compas', 2),
(6, 'Feutres', 2);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `categorie`
--
ALTER TABLE `categorie`
  ADD PRIMARY KEY (`id_C`);

--
-- Index pour la table `commande`
--
ALTER TABLE `commande`
  ADD PRIMARY KEY (`num_Cm`),
  ADD KEY `fk_commande_fournisseur` (`id_F`);

--
-- Index pour la table `fournir`
--
ALTER TABLE `fournir`
  ADD PRIMARY KEY (`id_F`,`id_P`),
  ADD KEY `fk_fournir_produit` (`id_P`);

--
-- Index pour la table `fournisseur`
--
ALTER TABLE `fournisseur`
  ADD PRIMARY KEY (`id_F`);

--
-- Index pour la table `ligne_commande`
--
ALTER TABLE `ligne_commande`
  ADD PRIMARY KEY (`id_Lc`),
  ADD KEY `fk_ligne_commande_commande` (`num_Cm`),
  ADD KEY `fk_ligne_commande_produit` (`id_P`);

--
-- Index pour la table `paiement`
--
ALTER TABLE `paiement`
  ADD PRIMARY KEY (`id_paiement`),
  ADD KEY `fk_paiement_commande` (`num_Cm`);

--
-- Index pour la table `produit`
--
ALTER TABLE `produit`
  ADD PRIMARY KEY (`id_P`),
  ADD KEY `fk_produit_categorie` (`id_C`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `categorie`
--
ALTER TABLE `categorie`
  MODIFY `id_C` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `commande`
--
ALTER TABLE `commande`
  MODIFY `num_Cm` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `fournisseur`
--
ALTER TABLE `fournisseur`
  MODIFY `id_F` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `ligne_commande`
--
ALTER TABLE `ligne_commande`
  MODIFY `id_Lc` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `paiement`
--
ALTER TABLE `paiement`
  MODIFY `id_paiement` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `produit`
--
ALTER TABLE `produit`
  MODIFY `id_P` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `commande`
--
ALTER TABLE `commande`
  ADD CONSTRAINT `fk_commande_fournisseur` FOREIGN KEY (`id_F`) REFERENCES `fournisseur` (`id_F`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Contraintes pour la table `fournir`
--
ALTER TABLE `fournir`
  ADD CONSTRAINT `fk_fournir_fournisseur` FOREIGN KEY (`id_F`) REFERENCES `fournisseur` (`id_F`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_fournir_produit` FOREIGN KEY (`id_P`) REFERENCES `produit` (`id_P`) ON DELETE CASCADE;

--
-- Contraintes pour la table `ligne_commande`
--
ALTER TABLE `ligne_commande`
  ADD CONSTRAINT `fk_ligne_commande_commande` FOREIGN KEY (`num_Cm`) REFERENCES `commande` (`num_Cm`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_ligne_commande_produit` FOREIGN KEY (`id_P`) REFERENCES `produit` (`id_P`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Contraintes pour la table `paiement`
--
ALTER TABLE `paiement`
  ADD CONSTRAINT `fk_paiement_commande` FOREIGN KEY (`num_Cm`) REFERENCES `commande` (`num_Cm`) ON DELETE CASCADE;

--
-- Contraintes pour la table `produit`
--
ALTER TABLE `produit`
  ADD CONSTRAINT `fk_produit_categorie` FOREIGN KEY (`id_C`) REFERENCES `categorie` (`id_C`) ON DELETE RESTRICT ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
