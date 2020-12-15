-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema WebApplicatie_DROVN_Bank
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `WebApplicatie_DROVN_Bank` DEFAULT CHARACTER SET utf8;
USE `WebApplicatie_DROVN_Bank`;

-- -----------------------------------------------------
-- Database user for Schema WebApplicatie_DROVN_Bank
-- -----------------------------------------------------
CREATE USER IF NOT EXISTS 'userWebApplicatie_DROVN_Bank'@'localhost'
    IDENTIFIED WITH caching_sha2_password
        BY 'pwWebApplicatie_DROVN_Bank' PASSWORD EXPIRE NEVER;
GRANT ALL ON WebApplicatie_DROVN_Bank.* TO 'userWebApplicatie_DROVN_Bank'@'localhost';

-- -----------------------------------------------------
-- Table `WebApplicatie_DROVN_Bank`.`Customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WebApplicatie_DROVN_Bank`.`Customer`
(
    `customerID`   BIGINT(10)  NOT NULL AUTO_INCREMENT,
    `userName`     VARCHAR(45) NOT NULL,
    `password`     VARCHAR(45) NOT NULL,
    `customerType` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`customerID`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `WebApplicatie_DROVN_Bank`.`NaturalPerson`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WebApplicatie_DROVN_Bank`.`NaturalPerson`
(
    `ID`                   BIGINT(10)  NOT NULL AUTO_INCREMENT,
    `initials`             VARCHAR(10) NOT NULL,
    `firstName`            VARCHAR(15) NOT NULL,
    `preposition`          VARCHAR(15) NULL,
    `surName`              VARCHAR(45) NOT NULL,
    `dateOfBirth`          DATE        NOT NULL,
    `socialSecurityNumber` BIGINT(10)  NOT NULL,
    `email`                VARCHAR(45) NOT NULL,
    `phone`                BIGINT(14)  NOT NULL,
    `postalCode`           VARCHAR(6)  NOT NULL,
    `homeNumber`           VARCHAR(10) NOT NULL,
    `street`               VARCHAR(45) NOT NULL,
    `residence`            VARCHAR(45) NOT NULL,
    PRIMARY KEY (`ID`),
    INDEX `fk_NaturalPerson_Customer_idx` (`ID` ASC) VISIBLE,
    UNIQUE INDEX `socialSecurityNumber_UNIQUE` (`socialSecurityNumber` ASC) VISIBLE,
    CONSTRAINT `fk_NaturalPerson_Customer`
        FOREIGN KEY (`ID`)
            REFERENCES `WebApplicatie_DROVN_Bank`.`Customer` (`customerID`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `WebApplicatie_DROVN_Bank`.`LegalPerson`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WebApplicatie_DROVN_Bank`.`LegalPerson`
(
    `companyID`        BIGINT(10)  NOT NULL AUTO_INCREMENT,
    `companyName`      VARCHAR(45) NOT NULL,
    `kvkNumber`        BIGINT(8)   NOT NULL,
    `sector`           VARCHAR(45) NULL,
    `vatNumber`        VARCHAR(20) NOT NULL,
    `postalCode`       VARCHAR(6)  NOT NULL,
    `homeNumber`       VARCHAR(10) NOT NULL,
    `street`           VARCHAR(45) NOT NULL,
    `residence`        VARCHAR(45) NOT NULL,
    `accountmanagerID` BIGINT(10)  NULL,
    PRIMARY KEY (`companyID`),
    INDEX `fk_LegalPerson_Customer_idx` (`companyID` ASC) VISIBLE,
    /* INDEX `fk_LegalPerson_Employee_idx` (`accountmanagerID` ASC) VISIBLE, */
    CONSTRAINT `fk_LegalPerson_Customer`
        FOREIGN KEY (`companyID`)
            REFERENCES `WebApplicatie_DROVN_Bank`.`Customer` (`customerID`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION/*,
  CONSTRAINT `fk_LegalPerson_Employee`
    FOREIGN KEY (`accountmanagerID`)
    REFERENCES `WebApplicatie_DROVN_Bank`.`Employee` (`employeeID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE*/)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `WebApplicatie_DROVN_Bank`.`Employee`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WebApplicatie_DROVN_Bank`.`Employee`
(
    `employeeID` BIGINT(10)  NOT NULL,
    `role`       VARCHAR(45) NOT NULL,
    PRIMARY KEY (`employeeID`),
    INDEX `fk_Employee_NaturalPerson_idx` (`employeeID` ASC) VISIBLE,
    CONSTRAINT `fk_Employee_NaturalPerson`
        FOREIGN KEY (`employeeID`)
            REFERENCES `WebApplicatie_DROVN_Bank`.`NaturalPerson` (`ID`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `WebApplicatie_DROVN_Bank`.`Account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WebApplicatie_DROVN_Bank`.`Account` (
  `accountID` BIGINT(10) NOT NULL,
  `iban` VARCHAR(45) NOT NULL,
  `balance` DECIMAL(64,2) NOT NULL,
  PRIMARY KEY (`accountID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `WebApplicatie_DROVN_Bank`.`BusinessAccount`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WebApplicatie_DROVN_Bank`.`BusinessAccount` (
  `accountID` BIGINT(10) NOT NULL,
  `contactPerson` BIGINT(10) NOT NULL,
  PRIMARY KEY (`accountID`),
  INDEX `fk_BusinessAccount_NaturalPerson1_idx` (`contactPerson` ASC) VISIBLE,
  CONSTRAINT `fk_BusinessAccount_Account1`
    FOREIGN KEY (`accountID`)
    REFERENCES `WebApplicatie_DROVN_Bank`.`Account` (`accountID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BusinessAccount_NaturalPerson1`
    FOREIGN KEY (`contactPerson`)
        REFERENCES `WebApplicatie_DROVN_Bank`.`NaturalPerson` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `WebApplicatie_DROVN_Bank`.`Customer_has_Account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WebApplicatie_DROVN_Bank`.`Customer_has_Account` (
  `customerID` BIGINT(10) NOT NULL,
  `accountID` BIGINT(10) NOT NULL,
  PRIMARY KEY (`accountID`, `customerID`),
  INDEX `fk_Customer_has_Account_Account1_idx` (`accountID` ASC) VISIBLE,
  INDEX `fk_Customer_has_Account_Customer1_idx` (`customerID` ASC) VISIBLE,
  CONSTRAINT `fk_Customer_has_Account_Customer1`
    FOREIGN KEY (`customerID`)
    REFERENCES `WebApplicatie_DROVN_Bank`.`Customer` (`customerID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Customer_has_Account_Account1`
    FOREIGN KEY (`accountID`)
    REFERENCES `WebApplicatie_DROVN_Bank`.`Account` (`accountID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
