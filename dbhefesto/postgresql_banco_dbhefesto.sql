docker pull postgres:latest

docker run -p 5433:5433 --expose 5433 --name postgresdocker -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=abcd1234 -v postgres-volume:/var/lib/postgresql/data -h 127.0.0.1 -d postgres:latest -p 5433

docker exec -it postgresdocker bash

psql -h 127.0.0.1 -p 5433 -U postgres

ALTER USER postgres with encrypted password 'abcd1234';

psql -h 127.0.0.1 -p 5433 -U postgres -W

docker container rm postgresdocker

docker container stop postgresdocker

docker container start postgresdocker


mkdir backup
cd backup
../bin/./pg_dump --verbose --host=localhost --port=5433 --username=postgres --format=c --file pg-localhost-dump-dbhefesto.backup -n "public" dbhefesto
../bin/./pg_dump --verbose --host=localhost --port=5433 --username=postgres --format=c --file pg-localhost-dump-contratos.backup -n "contratos" contratos
../bin/./pg_dump --verbose --host=localhost --port=5433 --username=postgres --format=c --file pg-localhost-dump-hfsbanco.backup -n "public" hfsbanco
../bin/./pg_dump --verbose --host=localhost --port=5433 --username=postgres --format=c --file pg-localhost-dump-imovel.backup -n "imovel" imovel
../bin/./pg_dump --verbose --host=localhost --port=5433 --username=postgres --format=c --file pg-localhost-dump-paa.backup -n "paa" paa
../bin/./pg_dump --verbose --host=localhost --port=5433 --username=postgres --format=c --file pg-localhost-dump-predial.backup -n "predial" predial

docker cp postgresdocker:/backup /home/hfs


CREATE TABLE public.adm_parameter_category (
	pmc_seq int8 NOT NULL,
	pmc_description varchar(64) NOT NULL,
	pmc_order int8 NULL,
	CONSTRAINT adm_parameter_category_pkey PRIMARY KEY (pmc_seq),
	CONSTRAINT adm_pmc_uk UNIQUE (pmc_description)
);

CREATE SEQUENCE public.adm_parameter_category_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
		
CREATE TABLE public.adm_parameter (
	par_seq int8 NOT NULL,
	par_code varchar(64) NOT NULL,
	par_description varchar(255) NOT NULL,
	par_pmc_seq int8 NOT NULL,
	par_value varchar(4000) NULL,
	CONSTRAINT adm_parameter_pkey PRIMARY KEY (par_seq),
	CONSTRAINT adm_parameter_uk UNIQUE (par_description),
	CONSTRAINT adm_parameter_pmc_fkey FOREIGN KEY (par_pmc_seq) REFERENCES public.adm_parameter_category(pmc_seq)
);

CREATE SEQUENCE public.adm_parameter_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;

CREATE TABLE public.adm_profile (
	prf_seq int8 NOT NULL,
	prf_administrator bpchar(1) NULL DEFAULT 'N'::bpchar,
	prf_description varchar(255) NOT NULL,
	prf_general bpchar(1) NULL DEFAULT 'N'::bpchar,
	CONSTRAINT adm_profile_pkey PRIMARY KEY (prf_seq),
	CONSTRAINT adm_profile_uk UNIQUE (prf_description)
);

CREATE SEQUENCE public.adm_profile_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;

CREATE TABLE public.adm_user (
	usu_seq int8 NOT NULL,
	usu_active bpchar(1) NULL DEFAULT 'N'::bpchar,
	usu_email varchar(255) NULL,
	usu_login varchar(64) NOT NULL,
	usu_name varchar(64) NULL,
	usu_password varchar(128) NOT NULL,
	CONSTRAINT adm_user_email_uk UNIQUE (usu_email),
	CONSTRAINT adm_user_login_uk UNIQUE (usu_login),
	CONSTRAINT adm_user_name_uk UNIQUE (usu_name),
	CONSTRAINT adm_user_password_uk UNIQUE (usu_password),
	CONSTRAINT adm_user_pkey PRIMARY KEY (usu_seq)
);

CREATE SEQUENCE public.adm_user_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;

CREATE TABLE public.adm_user_profile (
	usp_seq int8 NOT NULL,
	usp_prf_seq int8 NOT NULL,
	usp_use_seq int8 NOT NULL,
	CONSTRAINT adm_user_profile_pkey PRIMARY KEY (usp_seq),
	CONSTRAINT adm_user_profile_uk UNIQUE (usp_prf_seq, usp_use_seq),
	CONSTRAINT adm_usp_page_fkey FOREIGN KEY (usp_prf_seq) REFERENCES public.adm_profile(prf_seq),
	CONSTRAINT adm_usp_profile_fkey FOREIGN KEY (usp_use_seq) REFERENCES public.adm_user(usu_seq)
);

CREATE SEQUENCE public.adm_user_profile_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;

CREATE TABLE public.adm_page (
	pag_seq int8 NOT NULL,
	pag_description varchar(255) NOT NULL,
	pag_url varchar(255) NOT NULL,
	CONSTRAINT adm_page_description_uk UNIQUE (pag_description),
	CONSTRAINT adm_page_pkey PRIMARY KEY (pag_seq),
	CONSTRAINT adm_page_url_uk UNIQUE (pag_url)
);

CREATE SEQUENCE public.adm_page_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


CREATE TABLE public.adm_page_profile (
	pgl_seq int8 NOT NULL,
	pgl_prf_seq int8 NOT NULL,
	pgl_pag_seq int8 NOT NULL,
	CONSTRAINT adm_page_profile_pkey PRIMARY KEY (pgl_seq),
	CONSTRAINT adm_page_profile_uk UNIQUE (pgl_pag_seq, pgl_prf_seq),
	CONSTRAINT adm_pgl_page_fkey FOREIGN KEY (pgl_pag_seq) REFERENCES public.adm_page(pag_seq),
	CONSTRAINT adm_pgl_profile_fkey FOREIGN KEY (pgl_prf_seq) REFERENCES public.adm_profile(prf_seq)
);

CREATE SEQUENCE public.adm_page_profile_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;

CREATE TABLE public.adm_menu (
	mnu_seq int8 NOT NULL,
	mnu_description varchar(255) NOT NULL,
	mnu_parent_seq int8 NULL,
	mnu_pag_seq int8 NULL,
	mnu_order int4 NULL,
	CONSTRAINT adm_menu_pkey PRIMARY KEY (mnu_seq),
	CONSTRAINT adm_menu_uk UNIQUE (mnu_description),
	CONSTRAINT adm_menu_page_fkey FOREIGN KEY (mnu_pag_seq) REFERENCES public.adm_page(pag_seq),
	CONSTRAINT adm_menu_parent_fkey FOREIGN KEY (mnu_parent_seq) REFERENCES public.adm_menu(mnu_seq)
);

CREATE SEQUENCE public.adm_menu_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
	
INSERT INTO public.adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(nextval('public.adm_parameter_category_seq'), 'Login Parameters', 2);
INSERT INTO public.adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(nextval('public.adm_parameter_category_seq'), 'E-mail Parameters', 3);
INSERT INTO public.adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(nextval('public.adm_parameter_category_seq'), 'Network connection Parameters', 4);
INSERT INTO public.adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(nextval('public.adm_parameter_category_seq'), 'System Parameters', NULL);
INSERT INTO public.adm_parameter_category(pmc_seq, pmc_description, pmc_order) VALUES(nextval('public.adm_parameter_category_seq'), 'teste 2345', NULL);
	
INSERT INTO public.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(nextval('public.adm_parameter_seq'), 'BLOQUEAR_LOGIN', 'Bloquear o sistema para que os usuários, exceto do administador, não façam login', 2, 'false');
INSERT INTO public.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(nextval('public.adm_parameter_seq'), 'ATRIBUTO_LOGIN', 'Define o atributo usado para efetuar login no sistema. Este parâmetro pode ser preenchido com: NOME_USUARIO ou CPF', 2, 'NOME_USUARIO');
INSERT INTO public.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(nextval('public.adm_parameter_seq'), 'SMTP_SERVIDOR', 'Servidor SMTP para que o sistema envie e-mail.', 3, 'smtp.teste');
INSERT INTO public.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(nextval('public.adm_parameter_seq'), 'SMTP_PORTA', 'Porta do servidor SMTP para que o sistema envie e-mail.', 3, '25');
INSERT INTO public.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(nextval('public.adm_parameter_seq'), 'SMTP_USERNAME', 'Usuário para login no servidor SMTP.', 3, NULL);
INSERT INTO public.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(nextval('public.adm_parameter_seq'), 'SMTP_PASSWORD', 'Senha para login no servidor SMTP.', 3, NULL);
INSERT INTO public.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(nextval('public.adm_parameter_seq'), 'SMTP_EMAIL_FROM', 'E-mail do sistema.', 3, 'sistema@teste');
INSERT INTO public.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(nextval('public.adm_parameter_seq'), 'PROXY_SERVIDOR', 'Servidor do Proxy.', 4, 'proxy.teste');
INSERT INTO public.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(nextval('public.adm_parameter_seq'), 'PROXY_PORTA', 'Porta do Proxy.', 4, '8080');
INSERT INTO public.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(nextval('public.adm_parameter_seq'), 'AMBIENTE_SISTEMA', 'Define o ambiente onde o sistema está instalado. Este parâmetro pode ser preenchido com: desenvolvimento, homologação ou produção', 2, 'Produção');
INSERT INTO public.adm_parameter(par_seq, par_code, par_description, par_pmc_seq, par_value)
VALUES(nextval('public.adm_parameter_seq'), 'MODO_TESTE', 'Habilitar o modo de teste por login, esquema do json [ { "ativo": "false", "login" : "henrique.souza", "setor" : "SAM", "cargo": "15426", "loginVirtual": "" }]');

INSERT INTO public.adm_profile (prf_seq,prf_administrator,prf_description,prf_general) VALUES
	 (nextval('public.adm_profile_seq'),'S','ADMIN','N'),
	 (nextval('public.adm_profile_seq'),'N','USER','S');
	 
INSERT INTO public.adm_page (pag_seq,pag_description,pag_url) VALUES
	 (nextval('public.adm_page_seq'),'Category of Configuration Parameters','admin/admParameterCategory/listAdmParameterCategory.xhtml'),
	 (nextval('public.adm_page_seq'),'Edit Category of Configuration Parameters','admin/admParameterCategory/editAdmParameterCategory.xhtml'),
	 (nextval('public.adm_page_seq'),'Configuration Parameters','admin/admParameter/listAdmParameter.xhtml'),
	 (nextval('public.adm_page_seq'),'Edit Configuration Parameters','admin/admParameter/editAdmParameter.xhtml'),
	 (nextval('public.adm_page_seq'),'Administer Profile','admin/admProfile/listAdmProfile.xhtml'),
	 (nextval('public.adm_page_seq'),'Edit Administer Profile','admin/admProfile/editAdmProfile.xhtml'),
	 (nextval('public.adm_page_seq'),'Administer Page','admin/admPage/listAdmPage.xhtml'),
	 (nextval('public.adm_page_seq'),'Edit Administer Page','admin/admPage/editAdmPage.xhtml'),
	 (nextval('public.adm_page_seq'),'Administer Menu','admin/admMenu/listAdmMenu.xhtml'),
	 (nextval('public.adm_page_seq'),'Administer User','admin/admUser/listAdmUser.xhtml'),
	 (nextval('public.adm_page_seq'),'Edit Administer User','admin/admUser/editAdmUser.xhtml'),
	 (nextval('public.adm_page_seq'),'Change Password','admin/changePassword/editChangePassword.xhtml');

INSERT INTO public.adm_user (usu_seq,usu_active,usu_email,usu_login,usu_name,usu_password) VALUES
	 (nextval('public.adm_user_seq'),'S','nao_responda@gmail.com','admin','Henrique','$2a$10$5ZuQHe40rPOxrvQLV4AGluoEnsHQ3vRDI9HdsM4jGNcRF6Ve21nJC');


INSERT INTO public.adm_menu (mnu_seq,mnu_description,mnu_parent_seq,mnu_pag_seq,mnu_order) VALUES
	 (nextval('public.adm_menu_seq'),'Administrative',NULL,NULL,1),
	 (nextval('public.adm_menu_seq'),'Category of Configuration Parameters',1,1,2),
	 (nextval('public.adm_menu_seq'),'Configuration Parameters',1,3,3),
	 (nextval('public.adm_menu_seq'),'Administer Profile',1,5,4),
	 (nextval('public.adm_menu_seq'),'Administer Page',1,7,5),
	 (nextval('public.adm_menu_seq'),'Administer Menu',1,9,6),
	 (nextval('public.adm_menu_seq'),'Administer User',1,10,7),
	 (nextval('public.adm_menu_seq'),'Change Password',1,12,8);

INSERT INTO public.adm_user_profile (usp_seq,usp_prf_seq,usp_use_seq) VALUES
	 (1,1,1);
	 
INSERT INTO public.adm_page_profile (pgl_seq,pgl_prf_seq,pgl_pag_seq) VALUES
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
	 
