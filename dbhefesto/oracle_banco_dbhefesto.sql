docker pull gvenzl/oracle-xe:latest

docker run -p 1521:1521 --name oracledocker -e ORACLE_PASSWORD=abcd1234 -d gvenzl/oracle-xe:latest -h 127.0.0.1

docker run -d -p 1521:1521 --name oracledocker -e ORACLE_PASSWORD=abcd1234 -v oracle-volume:/opt/oracle/oradata gvenzl/oracle-xe:latest -h 127.0.0.1

Reset database SYS and SYSTEM passwords:

docker exec oracledocker resetPassword abcd1234

docker exec -it oracledocker bash

sqlplus

connect

username: system
password: abcd1234


create user dbhefesto identified by abcd1234;

grant CREATE SESSION, ALTER SESSION, CREATE DATABASE LINK, CREATE MATERIALIZED VIEW, CREATE PROCEDURE, CREATE PUBLIC SYNONYM, CREATE ROLE, CREATE SEQUENCE, CREATE SYNONYM, CREATE TABLE, CREATE TRIGGER, CREATE TYPE, CREATE VIEW, UNLIMITED TABLESPACE to dbhefesto;

docker container start oracledocker

CREATE TABLE adm_parameter_category (
	pmc_seq number NOT NULL,
	pmc_description varchar2(64) NOT NULL,
	pmc_order number NULL,
	CONSTRAINT adm_parameter_category_pkey PRIMARY KEY (pmc_seq),
	CONSTRAINT adm_pmc_uk UNIQUE (pmc_description)
);

CREATE SEQUENCE ADM_PARAMETER_CATEGORY_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 9999999999999999999999999999 NOCYCLE NOCACHE NOORDER;

CREATE TABLE adm_parameter (
	par_seq number NOT NULL,
	par_code varchar2(64) NOT NULL,
	par_description varchar2(255) NOT NULL,
	par_pmc_seq number NOT NULL,
	par_value varchar2(4000) NULL,
	CONSTRAINT adm_parameter_pkey PRIMARY KEY (par_seq),
	CONSTRAINT adm_parameter_uk UNIQUE (par_description),
	CONSTRAINT adm_parameter_pmc_fkey FOREIGN KEY (par_pmc_seq) REFERENCES adm_parameter_category(pmc_seq)
);

CREATE SEQUENCE ADM_PARAMETER_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 9999999999999999999999999999 NOCYCLE NOCACHE NOORDER;

CREATE TABLE adm_profile (
	prf_seq number NOT NULL,
	prf_administrator char(1) DEFAULT 'N',
	prf_description varchar2(255) NOT NULL,
	prf_general char(1) DEFAULT 'N',
	CONSTRAINT adm_profile_pkey PRIMARY KEY (prf_seq),
	CONSTRAINT adm_profile_uk UNIQUE (prf_description)
);

CREATE SEQUENCE ADM_PROFILE_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 9999999999999999999999999999 NOCYCLE NOCACHE NOORDER;

CREATE TABLE adm_user (
	usu_seq number NOT NULL,
	usu_active char(1) DEFAULT 'N',
	usu_email varchar2(255) NULL,
	usu_login varchar2(64) NOT NULL,
	usu_name varchar2(64) NULL,
	usu_password varchar2(128) NOT NULL,
	CONSTRAINT adm_user_email_uk UNIQUE (usu_email),
	CONSTRAINT adm_user_login_uk UNIQUE (usu_login),
	CONSTRAINT adm_user_name_uk UNIQUE (usu_name),
	CONSTRAINT adm_user_password_uk UNIQUE (usu_password),
	CONSTRAINT adm_user_pkey PRIMARY KEY (usu_seq)
);

CREATE SEQUENCE ADM_USER_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 9999999999999999999999999999 NOCYCLE NOCACHE NOORDER;

CREATE TABLE adm_user_profile (
	usp_seq number NOT NULL,
	usp_prf_seq number NOT NULL,
	usp_use_seq number NOT NULL,
	CONSTRAINT adm_user_profile_pkey PRIMARY KEY (usp_seq),
	CONSTRAINT adm_user_profile_uk UNIQUE (usp_prf_seq, usp_use_seq),
	CONSTRAINT adm_usp_page_fkey FOREIGN KEY (usp_prf_seq) REFERENCES adm_profile(prf_seq),
	CONSTRAINT adm_usp_profile_fkey FOREIGN KEY (usp_use_seq) REFERENCES adm_user(usu_seq)
);

CREATE SEQUENCE ADM_USER_PROFILE_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 9999999999999999999999999999 NOCYCLE NOCACHE NOORDER;

CREATE TABLE adm_page (
	pag_seq number NOT NULL,
	pag_description varchar2(255) NOT NULL,
	pag_url varchar2(255) NOT NULL,
	CONSTRAINT adm_page_description_uk UNIQUE (pag_description),
	CONSTRAINT adm_page_pkey PRIMARY KEY (pag_seq),
	CONSTRAINT adm_page_url_uk UNIQUE (pag_url)
);

CREATE SEQUENCE ADM_PAGE_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 9999999999999999999999999999 NOCYCLE NOCACHE NOORDER;

CREATE TABLE adm_page_profile (
	pgl_seq number NOT NULL,
	pgl_prf_seq number NOT NULL,
	pgl_pag_seq number NOT NULL,
	CONSTRAINT adm_page_profile_pkey PRIMARY KEY (pgl_seq),
	CONSTRAINT adm_page_profile_uk UNIQUE (pgl_pag_seq, pgl_prf_seq),
	CONSTRAINT adm_pgl_page_fkey FOREIGN KEY (pgl_pag_seq) REFERENCES adm_page(pag_seq),
	CONSTRAINT adm_pgl_profile_fkey FOREIGN KEY (pgl_prf_seq) REFERENCES adm_profile(prf_seq)
);

CREATE SEQUENCE ADM_PAGE_PROFILE_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 9999999999999999999999999999 NOCYCLE NOCACHE NOORDER;

CREATE TABLE adm_menu (
	mnu_seq number NOT NULL,
	mnu_description varchar(255) NOT NULL,
	mnu_parent_seq number NULL,
	mnu_pag_seq number NULL,
	mnu_order int NULL,
	CONSTRAINT adm_menu_pkey PRIMARY KEY (mnu_seq),
	CONSTRAINT adm_menu_uk UNIQUE (mnu_description),
	CONSTRAINT adm_menu_page_fkey FOREIGN KEY (mnu_pag_seq) REFERENCES adm_page(pag_seq),
	CONSTRAINT adm_menu_parent_fkey FOREIGN KEY (mnu_parent_seq) REFERENCES adm_menu(mnu_seq)
);

CREATE SEQUENCE ADM_MENU_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 9999999999999999999999999999 NOCYCLE NOCACHE NOORDER;

INSERT INTO adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(adm_parameter_category_seq.nextval, 'Login Parameters', 2);
INSERT INTO adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(adm_parameter_category_seq.nextval, 'E-mail Parameters', 3);
INSERT INTO adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(adm_parameter_category_seq.nextval, 'Network connection Parameters', 4);
INSERT INTO adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(adm_parameter_category_seq.nextval, 'System Parameters', NULL);
INSERT INTO adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(adm_parameter_category_seq.nextval, 'teste 2345', NULL);


INSERT INTO adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(adm_parameter_seq.nextval, 'BLOQUEAR_LOGIN', 'Bloquear o sistema para que os usuários, exceto do administador, não façam login', 2, 'false');
INSERT INTO adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(adm_parameter_seq.nextval, 'ATRIBUTO_LOGIN', 'Define o atributo usado para efetuar login no sistema. Este parâmetro pode ser preenchido com: NOME_USUARIO ou CPF', 2, 'NOME_USUARIO');
INSERT INTO adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(adm_parameter_seq.nextval, 'SMTP_SERVIDOR', 'Servidor SMTP para que o sistema envie e-mail.', 3, 'smtp.teste');
INSERT INTO adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(adm_parameter_seq.nextval, 'SMTP_PORTA', 'Porta do servidor SMTP para que o sistema envie e-mail.', 3, '25');
INSERT INTO adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(adm_parameter_seq.nextval, 'SMTP_USERNAME', 'Usuário para login no servidor SMTP.', 3, NULL);
INSERT INTO adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(adm_parameter_seq.nextval, 'SMTP_PASSWORD', 'Senha para login no servidor SMTP.', 3, NULL);
INSERT INTO adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(adm_parameter_seq.nextval, 'SMTP_EMAIL_FROM', 'E-mail do sistema.', 3, 'sistema@teste');
INSERT INTO adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(adm_parameter_seq.nextval, 'PROXY_SERVIDOR', 'Servidor do Proxy.', 4, 'proxy.teste');
INSERT INTO adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(adm_parameter_seq.nextval, 'PROXY_PORTA', 'Porta do Proxy.', 4, '8080');
INSERT INTO adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(adm_parameter_seq.nextval, 'AMBIENTE_SISTEMA', 'Define o ambiente onde o sistema está instalado. Este parâmetro pode ser preenchido com: desenvolvimento, homologação ou produção', 2, 'Produção');
INSERT INTO adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(adm_parameter_seq.nextval, 'MODO_TESTE', 'Habilitar o modo de teste por login, esquema do json [ { "ativo": "false", "login" : "henrique.souza", "setor" : "SAM", "cargo": "15426", "loginVirtual": "" }]');

INSERT INTO adm_profile (prf_seq,prf_administrator,prf_description,prf_general) VALUES(adm_profile_seq.nextval,'S','ADMIN','N');
INSERT INTO adm_profile (prf_seq,prf_administrator,prf_description,prf_general) VALUES(adm_profile_seq.nextval,'N','USER','S');
	 
INSERT INTO adm_page (pag_seq,pag_description,pag_url) VALUES
(adm_page_seq.nextval,'Category of Configuration Parameters','admin/admParameterCategory/listAdmParameterCategory.xhtml');
INSERT INTO adm_page (pag_seq,pag_description,pag_url) VALUES
(adm_page_seq.nextval,'Edit Category of Configuration Parameters','admin/admParameterCategory/editAdmParameterCategory.xhtml');
INSERT INTO adm_page (pag_seq,pag_description,pag_url) VALUES	 
(adm_page_seq.nextval,'Configuration Parameters','admin/admParameter/listAdmParameter.xhtml');
INSERT INTO adm_page (pag_seq,pag_description,pag_url) VALUES	 
(adm_page_seq.nextval,'Edit Configuration Parameters','admin/admParameter/editAdmParameter.xhtml');
INSERT INTO adm_page (pag_seq,pag_description,pag_url) VALUES	 
(adm_page_seq.nextval,'Administer Profile','admin/admProfile/listAdmProfile.xhtml');
INSERT INTO adm_page (pag_seq,pag_description,pag_url) VALUES	 
(adm_page_seq.nextval,'Edit Administer Profile','admin/admProfile/editAdmProfile.xhtml');
INSERT INTO adm_page (pag_seq,pag_description,pag_url) VALUES	 
(adm_page_seq.nextval,'Administer Page','admin/admPage/listAdmPage.xhtml');
INSERT INTO adm_page (pag_seq,pag_description,pag_url) VALUES	 
(adm_page_seq.nextval,'Edit Administer Page','admin/admPage/editAdmPage.xhtml');
INSERT INTO adm_page (pag_seq,pag_description,pag_url) VALUES	 
(adm_page_seq.nextval,'Administer Menu','admin/admMenu/listAdmMenu.xhtml');
INSERT INTO adm_page (pag_seq,pag_description,pag_url) VALUES	 
(adm_page_seq.nextval,'Administer User','admin/admUser/listAdmUser.xhtml');
INSERT INTO adm_page (pag_seq,pag_description,pag_url) VALUES	 
(adm_page_seq.nextval,'Edit Administer User','admin/admUser/editAdmUser.xhtml');
INSERT INTO adm_page (pag_seq,pag_description,pag_url) VALUES	 
(adm_page_seq.nextval,'Change Password','admin/changePassword/editChangePassword.xhtml');

INSERT INTO adm_user (usu_seq,usu_active,usu_email,usu_login,usu_name,usu_password) VALUES
(adm_user_seq.nextval,'S','nao_responda@gmail.com','admin','Henrique','$2a$10$5ZuQHe40rPOxrvQLV4AGluoEnsHQ3vRDI9HdsM4jGNcRF6Ve21nJC');


INSERT INTO adm_menu (mnu_seq,mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES
(adm_menu_seq.nextval,'Administrative',NULL,NULL,1);
INSERT INTO adm_menu (mnu_seq,mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES	 
(adm_menu_seq.nextval,'Category of Configuration Parameters',1,1,2);
INSERT INTO adm_menu (mnu_seq,mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES	 
(adm_menu_seq.nextval,'Configuration Parameters',1,3,3);
INSERT INTO adm_menu (mnu_seq,mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES	 
(adm_menu_seq.nextval,'Administer Profile',1,5,4);
INSERT INTO adm_menu (mnu_seq,mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES	 
(adm_menu_seq.nextval,'Administer Page',1,7,5);
INSERT INTO adm_menu (mnu_seq,mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES	 
(adm_menu_seq.nextval,'Administer Menu',1,9,6);
INSERT INTO adm_menu (mnu_seq,mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES	 
(adm_menu_seq.nextval,'Administer User',1,10,7);
INSERT INTO adm_menu (mnu_seq,mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES	 
(adm_menu_seq.nextval,'Change Password',1,12,8);

INSERT INTO adm_user_profile (usp_seq,usp_prf_seq,usp_use_seq) VALUES(1,1,1);
INSERT INTO adm_page_profile (pgl_seq,pgl_prf_seq,pgl_pag_seq) VALUES(1,1,1);
INSERT INTO adm_page_profile (pgl_seq,pgl_prf_seq,pgl_pag_seq) VALUES(2,1,2);
INSERT INTO adm_page_profile (pgl_seq,pgl_prf_seq,pgl_pag_seq) VALUES(3,1,3);
INSERT INTO adm_page_profile (pgl_seq,pgl_prf_seq,pgl_pag_seq) VALUES(4,1,4);
INSERT INTO adm_page_profile (pgl_seq,pgl_prf_seq,pgl_pag_seq) VALUES(5,1,5);
INSERT INTO adm_page_profile (pgl_seq,pgl_prf_seq,pgl_pag_seq) VALUES(6,1,6);
INSERT INTO adm_page_profile (pgl_seq,pgl_prf_seq,pgl_pag_seq) VALUES(7,1,7);
INSERT INTO adm_page_profile (pgl_seq,pgl_prf_seq,pgl_pag_seq) VALUES(8,1,8);
INSERT INTO adm_page_profile (pgl_seq,pgl_prf_seq,pgl_pag_seq) VALUES(9,1,9);
INSERT INTO adm_page_profile (pgl_seq,pgl_prf_seq,pgl_pag_seq) VALUES(10,1,10);
INSERT INTO adm_page_profile (pgl_seq,pgl_prf_seq,pgl_pag_seq) VALUES(11,1,11);
INSERT INTO adm_page_profile (pgl_seq,pgl_prf_seq,pgl_pag_seq) VALUES(12,1,12);