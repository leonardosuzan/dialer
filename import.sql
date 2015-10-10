-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema dialer
-- -----------------------------------------------------
-- DROP SCHEMA IF EXISTS `dialer` ;

-- -----------------------------------------------------
-- Schema dialer
-- -----------------------------------------------------
-- CREATE SCHEMA IF NOT EXISTS `dialer` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `dialer` ;

-- -----------------------------------------------------
-- Table `dialer`.`contatosListagens`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dialer`.`contatosListagens` ;

CREATE TABLE IF NOT EXISTS `dialer`.`contatosListagens` (
  `idContato` INT NOT NULL AUTO_INCREMENT,
  `idListagens` INT NOT NULL,
  `primeiroNome` VARCHAR(45) NULL,
  `sobrenome` VARCHAR(60) NULL,
  `ddd` VARCHAR(2) NOT NULL,
  `numeroTelefonico` VARCHAR(9) NOT NULL,
  `cidade` VARCHAR(45) NULL,
  `estado` VARCHAR(50) NULL,
  `endereco` VARCHAR(100) NULL,
  `numero` VARCHAR(10) NULL,
  `complemento` VARCHAR(45) NULL,
  `bairro` VARCHAR(45) NULL,
  `observacoes` VARCHAR(1000) NULL,
  PRIMARY KEY (`idContato`),
  INDEX `idListagens_fk_idx` (`idListagens` ASC))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;





-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';


-- -----------------------------------------------------
-- Table `dialer`.`disparosEfetuados`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dialer`.`disparosEfetuados` ;

CREATE TABLE IF NOT EXISTS `dialer`.`disparosEfetuados` (
  `idContato` INT NOT NULL,
  `idCampanha` INT NOT NULL,
  `data` DATETIME NULL DEFAULT NOW(),
  `Atendeu?` TINYINT(1) NULL DEFAULT 0,
  `resposta1` VARCHAR(45) NULL,
  `resposta2` VARCHAR(45) NULL,
  `resposta3` VARCHAR(45) NULL,
  `resposta4` VARCHAR(45) NULL,
  `resposta5` VARCHAR(45) NULL,
  `resposta6` VARCHAR(45) NULL,
  `resposta7` VARCHAR(45) NULL,
  `resposta8` VARCHAR(45) NULL,
  `transferiu1` TINYINT(1) NULL,
  `transferiu2` TINYINT(1) NULL,
  `transferiu3` TINYINT(1) NULL,
  `transferiu4` TINYINT(1) NULL,
  `transferiu5` TINYINT(1) NULL,
  `transferiu6` TINYINT(1) NULL,
  `transferiu7` TINYINT(1) NULL,
  `transferiu8` TINYINT(1) NULL,
  PRIMARY KEY (`idContato`, `idCampanha`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

