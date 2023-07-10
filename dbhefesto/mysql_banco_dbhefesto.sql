docker pull mysql

docker run -p 3306:3306 --name mysqldocker -e MYSQL_ROOT_PASSWORD=abcd1234 -v mysql-volume:/var/lib/mysql -d mysql:latest --default-authentication-plugin=mysql_native_password -h 127.0.0.1

docker cp mysql-dump-dbhefesto.sql mysqldocker:/mysql-dump-dbhefesto.sql

docker exec -it mysqldocker bash

mysql -u root -p
create database dbhefesto;
show databases;
use dbhefesto;
source mysql-dump-dbhefesto.sql;
show tables;


docker container start mysqldocker

CREATE TABLE `adm_parameter_category` (
  `pmc_seq` bigint NOT NULL AUTO_INCREMENT,
  `pmc_description` varchar(64) NOT NULL,
  `pmc_order` bigint DEFAULT NULL,
  PRIMARY KEY (`pmc_seq`),
  UNIQUE KEY `adm_pmc_uk` (`pmc_description`)
);

CREATE TABLE `adm_parameter` (
  `par_seq` bigint NOT NULL AUTO_INCREMENT,
  `par_code` varchar(64) NOT NULL,
  `par_description` varchar(255) NOT NULL,
  `par_pmc_seq` bigint NOT NULL,
  `par_value` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`par_seq`),
  UNIQUE KEY `adm_parameter_uk` (`par_description`),
  KEY `adm_parameter_pmc_fkey` (`par_pmc_seq`),
  CONSTRAINT `adm_parameter_pmc_fkey` FOREIGN KEY (`par_pmc_seq`) REFERENCES `adm_parameter_category` (`pmc_seq`)
);

CREATE TABLE `adm_profile` (
  `prf_seq` bigint NOT NULL AUTO_INCREMENT,
  `prf_administrator` char(1) DEFAULT 'N',
  `prf_description` varchar(255) NOT NULL,
  `prf_general` char(1) DEFAULT 'N',
  PRIMARY KEY (`prf_seq`),
  UNIQUE KEY `adm_profile_uk` (`prf_description`)
);

CREATE TABLE `adm_user` (
  `usu_seq` bigint NOT NULL AUTO_INCREMENT,
  `usu_active` char(1) DEFAULT 'N',
  `usu_email` varchar(255) DEFAULT NULL,
  `usu_login` varchar(64) NOT NULL,
  `usu_name` varchar(64) DEFAULT NULL,
  `usu_password` varchar(128) NOT NULL,
  PRIMARY KEY (`usu_seq`),
  UNIQUE KEY `adm_user_login_uk` (`usu_login`),
  UNIQUE KEY `adm_user_password_uk` (`usu_password`),
  UNIQUE KEY `adm_user_name_uk` (`usu_name`),
  UNIQUE KEY `adm_user_email_uk` (`usu_email`)
);

CREATE TABLE `adm_user_profile` (
  `usp_seq` bigint NOT NULL AUTO_INCREMENT,
  `usp_prf_seq` bigint NOT NULL,
  `usp_use_seq` bigint NOT NULL,
  PRIMARY KEY (`usp_seq`),
  UNIQUE KEY `adm_user_profile_uk` (`usp_prf_seq`,`usp_use_seq`),
  KEY `adm_usp_profile_fkey` (`usp_use_seq`),
  CONSTRAINT `adm_usp_page_fkey` FOREIGN KEY (`usp_prf_seq`) REFERENCES `adm_profile` (`prf_seq`),
  CONSTRAINT `adm_usp_profile_fkey` FOREIGN KEY (`usp_use_seq`) REFERENCES `adm_user` (`usu_seq`)
);

CREATE TABLE `adm_page` (
  `pag_seq` bigint NOT NULL AUTO_INCREMENT,
  `pag_description` varchar(255) NOT NULL,
  `pag_url` varchar(255) NOT NULL,
  PRIMARY KEY (`pag_seq`),
  UNIQUE KEY `adm_page_description_uk` (`pag_description`),
  UNIQUE KEY `adm_page_url_uk` (`pag_url`)
);

CREATE TABLE `adm_page_profile` (
  `pgl_seq` bigint NOT NULL AUTO_INCREMENT,
  `pgl_prf_seq` bigint NOT NULL,
  `pgl_pag_seq` bigint NOT NULL,
  PRIMARY KEY (`pgl_seq`),
  UNIQUE KEY `adm_page_profile_uk` (`pgl_pag_seq`,`pgl_prf_seq`),
  KEY `adm_pgl_profile_fkey` (`pgl_prf_seq`),
  CONSTRAINT `adm_pgl_page_fkey` FOREIGN KEY (`pgl_pag_seq`) REFERENCES `adm_page` (`pag_seq`),
  CONSTRAINT `adm_pgl_profile_fkey` FOREIGN KEY (`pgl_prf_seq`) REFERENCES `adm_profile` (`prf_seq`)
);

CREATE TABLE `adm_menu` (
  `mnu_seq` bigint NOT NULL AUTO_INCREMENT,
  `mnu_description` varchar(255) NOT NULL,
  `mnu_parent_seq` bigint DEFAULT NULL,
  `mnu_pag_seq` bigint DEFAULT NULL,
  `mnu_order` int DEFAULT NULL,
  PRIMARY KEY (`mnu_seq`),
  UNIQUE KEY `adm_menu_uk` (`mnu_description`),
  KEY `adm_menu_parent_fkey` (`mnu_parent_seq`),
  KEY `adm_menu_page_fkey` (`mnu_pag_seq`),
  CONSTRAINT `adm_menu_page_fkey` FOREIGN KEY (`mnu_pag_seq`) REFERENCES `adm_page` (`pag_seq`),
  CONSTRAINT `adm_menu_parent_fkey` FOREIGN KEY (`mnu_parent_seq`) REFERENCES `adm_menu` (`mnu_seq`)
);


INSERT INTO dbhefesto.adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(1, 'Court Parameters', 1);
INSERT INTO dbhefesto.adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(2, 'Login Parameters', 2);
INSERT INTO dbhefesto.adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(3, 'E-mail Parameters', 3);
INSERT INTO dbhefesto.adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(4, 'Network connection Parameters', 4);
INSERT INTO dbhefesto.adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(5, 'System Parameters', NULL);


INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(1, 'NOME_TRIBUNAL', 'Nome do tribunal onde o sistema está instalado.', 1, 'Tribunal Regional do Trabalho da 1a. Região');
INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(2, 'SIGLA_TRIBUNAL', 'Sigla do tribunal onde o sistema está instalado.', 1, 'TRT1');
INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(3, 'CODIGO_UNIDADE_GESTORA', 'Código númérico de 6 dígitos que identifica o órgão no SIAFI.', 1, '080009');
INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(4, 'APELIDO_UNIDADE_GESTORA', 'Código númérico de 3 dígitos da UG no código de barras da GRU.', 1, '102');
INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(5, 'BLOQUEAR_LOGIN', 'Bloquear o sistema para que os usuários, exceto do administador, não façam login', 2, 'false');
INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(6, 'ATRIBUTO_LOGIN', 'Define o atributo usado para efetuar login no sistema. Este parâmetro pode ser preenchido com: NOME_USUARIO ou CPF', 2, 'NOME_USUARIO');
INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(7, 'SMTP_SERVIDOR', 'Servidor SMTP para que o sistema envie e-mail.', 3, 'smtp.teste');
INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(8, 'SMTP_PORTA', 'Porta do servidor SMTP para que o sistema envie e-mail.', 3, '25');
INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(9, 'SMTP_USERNAME', 'Usuário para login no servidor SMTP.', 3, NULL);
INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(10, 'SMTP_PASSWORD', 'Senha para login no servidor SMTP.', 3, NULL);
INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(11, 'SMTP_EMAIL_FROM', 'E-mail do sistema.', 3, 'sistema@teste');
INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(12, 'PROXY_SERVIDOR', 'Servidor do Proxy.', 4, 'teste');
INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(13, 'PROXY_PORTA', 'Porta do Proxy.', 4, '8080');
INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(14, 'AMBIENTE_SISTEMA', 'Define o ambiente onde o sistema está instalado. Este parâmetro pode ser preenchido com: desenvolvimento, homologação ou produção', 2, 'Produção');
INSERT INTO dbhefesto.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(15, 'MODO_TESTE', 'Habilitar o modo de teste por login, esquema do json [  { "ativo": "false", "login" : "henrique.souza", "setor" : "SAM", "cargo": "15426", "loginVirtual": "" }]');


INSERT INTO dbhefesto.adm_profile (prf_seq,prf_administrator,prf_description,prf_general) VALUES
	 (1,'S','ADMIN','N'),
	 (2,'N','USER','S');
	 
INSERT INTO dbhefesto.adm_page (pag_seq,pag_description,pag_url) VALUES
	 (1,'Category of Configuration Parameters','admin/admParameterCategory/listAdmParameterCategory.xhtml'),
	 (2,'Edit Category of Configuration Parameters','admin/admParameterCategory/editAdmParameterCategory.xhtml'),
	 (3,'Configuration Parameters','admin/admParameter/listAdmParameter.xhtml'),
	 (4,'Edit Configuration Parameters','admin/admParameter/editAdmParameter.xhtml'),
	 (5,'Administer Profile','admin/admProfile/listAdmProfile.xhtml'),
	 (6,'Edit Administer Profile','admin/admProfile/editAdmProfile.xhtml'),
	 (7,'Administer Page','admin/admPage/listAdmPage.xhtml'),
	 (8,'Edit Administer Page','admin/admPage/editAdmPage.xhtml'),
	 (9,'Administer Menu','admin/admMenu/listAdmMenu.xhtml'),
	 (10,'Administer User','admin/admUser/listAdmUser.xhtml'),
	 (11,'Edit Administer User','admin/admUser/editAdmUser.xhtml'),
	 (12,'Change Password','admin/changePassword/editChangePassword.xhtml');

INSERT INTO dbhefesto.adm_user (usu_seq,usu_active,usu_email,usu_login,usu_name,usu_password) VALUES
	 (1,'S','nao_responda@gmail.com','admin','Henrique','$2a$10$5ZuQHe40rPOxrvQLV4AGluoEnsHQ3vRDI9HdsM4jGNcRF6Ve21nJC');


INSERT INTO dbhefesto.adm_menu (mnu_seq,mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES
	 (1,'Administrative',NULL,NULL,1),
	 (2,'Category of Configuration Parameters',1,1,2),
	 (3,'Configuration Parameters',1,3,3),
	 (4,'Administer Profile',1,5,4),
	 (5,'Administer Page',1,7,5),
	 (6,'Administer Menu',1,9,6),
	 (7,'Administer User',1,10,7),
	 (8,'Change Password',1,12,8);

INSERT INTO dbhefesto.adm_user_profile (usp_seq,usp_prf_seq,usp_use_seq) VALUES
	 (1,1,1);
	 
INSERT INTO dbhefesto.adm_page_profile (pgl_seq,pgl_prf_seq,pgl_pag_seq) VALUES
	 (1,1,1),
	 (2,1,2),
	 (3,1,3),
	 (4,1,4),
	 (5,1,5),
	 (6,1,6),
	 (7,1,7),
	 (8,1,8),
	 (9,1,9),
	 (10,1,10),
	 (11,1,11),
	 (12,1,12);

