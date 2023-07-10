CREATE TABLE adm_parameter_category (
	pmc_seq INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	pmc_description TEXT(64) NOT NULL,
	pmc_order INTEGER NULL,
	CONSTRAINT adm_pmc_uk UNIQUE (pmc_description)
);

CREATE TABLE adm_parameter (
	par_seq INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	par_code TEXT(64) NOT NULL,
	par_description TEXT(255) NOT NULL,
	par_pmc_seq INTEGER NOT NULL,
	par_value TEXT(4000) NULL,
	CONSTRAINT adm_parameter_uk UNIQUE (par_description),
	CONSTRAINT adm_parameter_pmc_fkey FOREIGN KEY (par_pmc_seq) REFERENCES adm_parameter_category(pmc_seq)
);

CREATE TABLE adm_profile (
	prf_seq INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	prf_administrator TEXT(1) DEFAULT 'N',
	prf_description TEXT(255) NOT NULL,
	prf_general TEXT(1) DEFAULT 'N',
	CONSTRAINT adm_profile_uk UNIQUE (prf_description)
);

CREATE TABLE adm_user (
	usu_seq INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	usu_active TEXT(1) DEFAULT 'N',
	usu_email TEXT(255) NULL,
	usu_login TEXT(64) NOT NULL,
	usu_name TEXT(64) NULL,
	usu_password TEXT(128) NOT NULL,
	CONSTRAINT adm_user_email_uk UNIQUE (usu_email),
	CONSTRAINT adm_user_login_uk UNIQUE (usu_login),
	CONSTRAINT adm_user_name_uk UNIQUE (usu_name),
	CONSTRAINT adm_user_password_uk UNIQUE (usu_password)
);

CREATE TABLE adm_user_profile (
	usp_seq INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	usp_prf_seq INTEGER NOT NULL,
	usp_use_seq INTEGER NOT NULL,
	CONSTRAINT adm_user_profile_uk UNIQUE (usp_prf_seq, usp_use_seq),
	CONSTRAINT adm_usp_page_fkey FOREIGN KEY (usp_prf_seq) REFERENCES adm_profile(prf_seq),
	CONSTRAINT adm_usp_profile_fkey FOREIGN KEY (usp_use_seq) REFERENCES adm_user(usu_seq)
);

CREATE TABLE adm_page (
	pag_seq INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	pag_description TEXT(255) NOT NULL,
	pag_url TEXT(255) NOT NULL,
	CONSTRAINT adm_page_description_uk UNIQUE (pag_description),
	CONSTRAINT adm_page_url_uk UNIQUE (pag_url)
);

CREATE TABLE adm_page_profile (
	pgl_seq INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	pgl_prf_seq INTEGER NOT NULL,
	pgl_pag_seq INTEGER NOT NULL,
	CONSTRAINT adm_page_profile_uk UNIQUE (pgl_pag_seq, pgl_prf_seq),
	CONSTRAINT adm_pgl_page_fkey FOREIGN KEY (pgl_pag_seq) REFERENCES adm_page(pag_seq),
	CONSTRAINT adm_pgl_profile_fkey FOREIGN KEY (pgl_prf_seq) REFERENCES adm_profile(prf_seq)
);

CREATE TABLE adm_menu (
	mnu_seq INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	mnu_description TEXT(255) NOT NULL,
	mnu_parent_seq INTEGER NULL,
	mnu_pag_seq INTEGER NULL,
	mnu_order INTEGER NULL,
	CONSTRAINT adm_menu_uk UNIQUE (mnu_description),
	CONSTRAINT adm_menu_page_fkey FOREIGN KEY (mnu_pag_seq) REFERENCES adm_page(pag_seq),
	CONSTRAINT adm_menu_parent_fkey FOREIGN KEY (mnu_parent_seq) REFERENCES adm_menu(mnu_seq)
);

INSERT INTO adm_parameter_category(pmc_description, pmc_order) VALUES('Login Parameters', 2);
INSERT INTO adm_parameter_category(pmc_description, pmc_order) VALUES('E-mail Parameters', 3);
INSERT INTO adm_parameter_category(pmc_description, pmc_order) VALUES('Network connection Parameters', 4);
INSERT INTO adm_parameter_category(pmc_description, pmc_order) VALUES('System Parameters', NULL);
INSERT INTO adm_parameter_category(pmc_description, pmc_order) VALUES('teste 2345', NULL);


INSERT INTO adm_parameter(par_code, par_description, par_pmc_seq, par_value)
VALUES('BLOQUEAR_LOGIN', 'Bloquear o sistema para que os usuários, exceto do administador, não façam login', 2, 'false');
INSERT INTO adm_parameter(par_code, par_description, par_pmc_seq, par_value)
VALUES('ATRIBUTO_LOGIN', 'Define o atributo usado para efetuar login no sistema. Este parâmetro pode ser preenchido com: NOME_USUARIO ou CPF', 2, 'NOME_USUARIO');
INSERT INTO adm_parameter(par_code, par_description, par_pmc_seq, par_value)
VALUES('SMTP_SERVIDOR', 'Servidor SMTP para que o sistema envie e-mail.', 3, 'smtp.teste');
INSERT INTO adm_parameter(par_code, par_description, par_pmc_seq, par_value)
VALUES('SMTP_PORTA', 'Porta do servidor SMTP para que o sistema envie e-mail.', 3, '25');
INSERT INTO adm_parameter(par_code, par_description, par_pmc_seq, par_value)
VALUES('SMTP_USERNAME', 'Usuário para login no servidor SMTP.', 3, NULL);
INSERT INTO adm_parameter(par_code, par_description, par_pmc_seq, par_value)
VALUES('SMTP_PASSWORD', 'Senha para login no servidor SMTP.', 3, NULL);
INSERT INTO adm_parameter(par_code, par_description, par_pmc_seq, par_value)
VALUES('SMTP_EMAIL_FROM', 'E-mail do sistema.', 3, 'sistema@teste');
INSERT INTO adm_parameter(par_code, par_description, par_pmc_seq, par_value)
VALUES('PROXY_SERVIDOR', 'Servidor do Proxy.', 4, 'proxy.teste');
INSERT INTO adm_parameter(par_code, par_description, par_pmc_seq, par_value)
VALUES('PROXY_PORTA', 'Porta do Proxy.', 4, '8080');
INSERT INTO adm_parameter(par_code, par_description, par_pmc_seq, par_value)
VALUES('AMBIENTE_SISTEMA', 'Define o ambiente onde o sistema está instalado. Este parâmetro pode ser preenchido com: desenvolvimento, homologação ou produção', 2, 'Produção');
INSERT INTO adm_parameter(par_code, par_description, par_pmc_seq, par_value)
VALUES('MODO_TESTE', 'Habilitar o modo de teste por login, esquema do json [ { "ativo": "false", "login" : "henrique.souza", "setor" : "SAM", "cargo": "15426", "loginVirtual": "" }]');

INSERT INTO adm_profile (prf_administrator,prf_description,prf_general) VALUES('S','ADMIN','N');
INSERT INTO adm_profile (prf_administrator,prf_description,prf_general) VALUES('N','USER','S');
	 
INSERT INTO adm_page (pag_description,pag_url) VALUES
('Category of Configuration Parameters','admin/admParameterCategory/listAdmParameterCategory.xhtml');
INSERT INTO adm_page (pag_description,pag_url) VALUES
('Edit Category of Configuration Parameters','admin/admParameterCategory/editAdmParameterCategory.xhtml');
INSERT INTO adm_page (pag_description,pag_url) VALUES	 
('Configuration Parameters','admin/admParameter/listAdmParameter.xhtml');
INSERT INTO adm_page (pag_description,pag_url) VALUES	 
('Edit Configuration Parameters','admin/admParameter/editAdmParameter.xhtml');
INSERT INTO adm_page (pag_description,pag_url) VALUES	 
('Administer Profile','admin/admProfile/listAdmProfile.xhtml');
INSERT INTO adm_page (pag_description,pag_url) VALUES	 
('Edit Administer Profile','admin/admProfile/editAdmProfile.xhtml');
INSERT INTO adm_page (pag_description,pag_url) VALUES	 
('Administer Page','admin/admPage/listAdmPage.xhtml');
INSERT INTO adm_page (pag_description,pag_url) VALUES	 
('Edit Administer Page','admin/admPage/editAdmPage.xhtml');
INSERT INTO adm_page (pag_description,pag_url) VALUES	 
('Administer Menu','admin/admMenu/listAdmMenu.xhtml');
INSERT INTO adm_page (pag_description,pag_url) VALUES	 
('Administer User','admin/admUser/listAdmUser.xhtml');
INSERT INTO adm_page (pag_description,pag_url) VALUES	 
('Edit Administer User','admin/admUser/editAdmUser.xhtml');
INSERT INTO adm_page (pag_description,pag_url) VALUES	 
('Change Password','admin/changePassword/editChangePassword.xhtml');

INSERT INTO adm_user (usu_active,usu_email,usu_login,usu_name,usu_password) VALUES
('S','nao_responda@gmail.com','admin','Henrique','$2a$10$5ZuQHe40rPOxrvQLV4AGluoEnsHQ3vRDI9HdsM4jGNcRF6Ve21nJC');


INSERT INTO adm_menu (mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES
('Administrative',NULL,NULL,1);
INSERT INTO adm_menu (mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES	 
('Category of Configuration Parameters',1,1,2);
INSERT INTO adm_menu (mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES	 
('Configuration Parameters',1,3,3);
INSERT INTO adm_menu (mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES	 
('Administer Profile',1,5,4);
INSERT INTO adm_menu (mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES	 
('Administer Page',1,7,5);
INSERT INTO adm_menu (mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES	 
('Administer Menu',1,9,6);
INSERT INTO adm_menu (mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES	 
('Administer User',1,10,7);
INSERT INTO adm_menu (mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES	 
('Change Password',1,12,8);

INSERT INTO adm_user_profile (usp_prf_seq,usp_use_seq) VALUES(1,1);

INSERT INTO adm_page_profile (pgl_prf_seq,pgl_pag_seq) VALUES(1,1);
INSERT INTO adm_page_profile (pgl_prf_seq,pgl_pag_seq) VALUES(1,2);
INSERT INTO adm_page_profile (pgl_prf_seq,pgl_pag_seq) VALUES(1,3);
INSERT INTO adm_page_profile (pgl_prf_seq,pgl_pag_seq) VALUES(1,4);
INSERT INTO adm_page_profile (pgl_prf_seq,pgl_pag_seq) VALUES(1,5);
INSERT INTO adm_page_profile (pgl_prf_seq,pgl_pag_seq) VALUES(1,6);
INSERT INTO adm_page_profile (pgl_prf_seq,pgl_pag_seq) VALUES(1,7);
INSERT INTO adm_page_profile (pgl_prf_seq,pgl_pag_seq) VALUES(1,8);
INSERT INTO adm_page_profile (pgl_prf_seq,pgl_pag_seq) VALUES(1,9);
INSERT INTO adm_page_profile (pgl_prf_seq,pgl_pag_seq) VALUES(1,10);
INSERT INTO adm_page_profile (pgl_prf_seq,pgl_pag_seq) VALUES(1,11);
INSERT INTO adm_page_profile (pgl_prf_seq,pgl_pag_seq) VALUES(1,12);


