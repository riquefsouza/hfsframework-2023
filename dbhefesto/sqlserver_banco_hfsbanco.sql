

CREATE TABLE dbo.adm_usuario (
	usu_matricula bigint NOT NULL,
	usu_ip varchar(15) NOT NULL,
	usu_login varchar(60) NOT NULL,
	usu_nome varchar(60) NOT NULL,
	usu_data smalldatetime NOT NULL,
	usu_cpf decimal(18,0) NULL,
	usu_email varchar(255) NULL,
	usu_ldap_dn varchar(255) NULL,
	CONSTRAINT adm_usuario_PK PRIMARY KEY (usu_matricula,usu_ip)
)

CREATE TABLE dbo.adm_auditoria_revisao (
	aud_id bigint NOT NULL,
	aud_data_hora bigint NULL,
	aud_ip varchar(15) NOT NULL,
	aud_login varchar(60) NOT NULL,
	CONSTRAINT adm_auditoria_revisao_pkey PRIMARY KEY (aud_id)
)

CREATE TABLE dbo.adm_pagina (
	pag_seq bigint NOT NULL,
	pag_url varchar(255) NOT NULL,
	pag_descricao varchar(255) NOT NULL,
	CONSTRAINT adm_pagina_pkey PRIMARY KEY (pag_seq)
) 

CREATE TABLE dbo.adm_menu (
	mnu_seq bigint NOT NULL,
	mnu_descricao varchar(255) NOT NULL,
	mnu_pai_seq bigint NULL,
	mnu_pag_seq bigint NULL,
	mnu_ordem decimal(3,0) NULL,
	CONSTRAINT adm_menu_pkey PRIMARY KEY (mnu_seq),
	CONSTRAINT adm_menu_un UNIQUE (mnu_descricao),
	CONSTRAINT adm_menu_adm_pagina_fk FOREIGN KEY (mnu_pag_seq) REFERENCES dbo.adm_pagina(pag_seq),
	CONSTRAINT adm_menu_mnu_pai_seq_fkey FOREIGN KEY (mnu_pai_seq) REFERENCES dbo.adm_menu(mnu_seq)
)

CREATE TABLE dbo.adm_parametro_categoria (
	pmc_seq bigint NOT NULL,
	pmc_descricao varchar(100) NOT NULL,
	pmc_ordem numeric(2) NULL,
	CONSTRAINT adm_parametro_categoria_pkey PRIMARY KEY (pmc_seq),
	CONSTRAINT adm_parametro_categoria_un UNIQUE (pmc_descricao)
)

CREATE TABLE dbo.adm_parametro (
	par_seq bigint NOT NULL,
	par_valor varchar(4000) NULL,
	par_descricao varchar(255) NOT NULL,
	par_codigo varchar(255) NOT NULL,
	par_pmc_seq bigint NOT NULL,
	CONSTRAINT adm_parametro_pkey PRIMARY KEY (par_seq),
	CONSTRAINT adm_parametro_un UNIQUE (par_descricao),
	CONSTRAINT uk_adm_parametro UNIQUE (par_codigo),
	CONSTRAINT adm_parametro_par_pmc_seq_fkey FOREIGN KEY (par_pmc_seq) REFERENCES dbo.adm_parametro_categoria(pmc_seq)
)

CREATE TABLE dbo.adm_perfil (
	prf_seq bigint NOT NULL,
	prf_descricao varchar(255) NULL,
	prf_geral char(1) NULL DEFAULT 'N',
	prf_administrador char(1) NULL DEFAULT 'N',
	prf_intersecao char(1) NOT NULL DEFAULT 'N',
	CONSTRAINT adm_perfil_pkey PRIMARY KEY (prf_seq),
	CONSTRAINT adm_perfil_un UNIQUE (prf_descricao)
)

CREATE TABLE dbo.adm_pagina_perfil (
	pgl_pag_seq bigint NOT NULL,
	pgl_prf_seq bigint NOT NULL,
	CONSTRAINT adm_pagina_perfil_pkey PRIMARY KEY (pgl_pag_seq, pgl_prf_seq),
	CONSTRAINT adm_pagina_perfil_pgl_pag_seq_fkey FOREIGN KEY (pgl_pag_seq) REFERENCES dbo.adm_pagina(pag_seq),
	CONSTRAINT adm_pagina_perfil_pgl_prf_seq_fkey FOREIGN KEY (pgl_prf_seq) REFERENCES dbo.adm_perfil(prf_seq)
)

CREATE TABLE dbo.adm_setor_perfil (
	spf_setor varchar(15) NOT NULL,
	spf_prf_seq bigint NOT NULL,
	CONSTRAINT adm_setor_perfil_pkey PRIMARY KEY (spf_setor, spf_prf_seq),
	CONSTRAINT adm_setor_perfil_spf_prf_seq_fkey FOREIGN KEY (spf_prf_seq) REFERENCES dbo.adm_perfil(prf_seq)
)

CREATE TABLE dbo.adm_cargo_perfil (
	cgp_cod_cargo bigint NOT NULL,
	cgp_prf_seq bigint NOT NULL,
	CONSTRAINT adm_cargo_perfil_pkey PRIMARY KEY (cgp_cod_cargo, cgp_prf_seq),
	CONSTRAINT adm_cargo_perfil_cgp_prf_seq_fkey FOREIGN KEY (cgp_prf_seq) REFERENCES dbo.adm_perfil(prf_seq)
)

CREATE TABLE dbo.adm_funcionario_perfil (
	usp_prf_seq bigint NOT NULL,
	usp_cod_funcionario bigint NOT NULL,
	usp_excluido char(1) NOT NULL DEFAULT 'N',
	CONSTRAINT adm_funcionario_perfil_pkey PRIMARY KEY (usp_prf_seq, usp_cod_funcionario),
	CONSTRAINT adm_funcionario_perfil_usp_prf_seq_fkey FOREIGN KEY (usp_prf_seq) REFERENCES dbo.adm_perfil(prf_seq)
)





CREATE SEQUENCE dbo.adm_auditoria_revisao_seq
as [bigint]
start with 1
increment by 1
minvalue 1
maxvalue 9223372036854775807
cycle
cache


CREATE SEQUENCE dbo.adm_menu_seq
as [bigint]
start with 1
increment by 1
minvalue 1
maxvalue 9223372036854775807
cycle
cache


CREATE SEQUENCE dbo.adm_pagina_seq
as [bigint]
start with 1
increment by 1
minvalue 1
maxvalue 9223372036854775807
cycle
cache


CREATE SEQUENCE dbo.adm_parametro_categoria_seq
as [bigint]
start with 1
increment by 1
minvalue 1
maxvalue 9223372036854775807
cycle
cache


CREATE SEQUENCE dbo.adm_parametro_seq
as [bigint]
start with 1
increment by 1
minvalue 1
maxvalue 9223372036854775807
cycle
cache


CREATE SEQUENCE dbo.adm_perfil_seq
as [bigint]
start with 1
increment by 1
minvalue 1
maxvalue 9223372036854775807
cycle
cache




CREATE PROCEDURE dbo.pkg_adm_drop_auditoria(@p_esquema VARCHAR(128))
AS
BEGIN
	DECLARE @v_cr CHAR(2);
	DECLARE @v_tabela VARCHAR(128);

   SET @v_cr = char(13) + char(10);  
       
   DECLARE c_tabela cursor LOCAL FAST_FORWARD READ_ONLY FOR
	select t.table_name
	from information_schema.tables t
	where  t.table_catalog = @p_esquema
	and t.table_schema='dbo' 
	and t.table_type='BASE TABLE'
	and t.table_name <> 'adm_usuario'
	and t.table_name <> 'adm_auditoria_revisao'
	and t.table_name not like 'log_%'
	and t.table_name not like 'vw_%'
	and t.table_name not like '%_aud'
	and (t.table_name like 'adm_%'
	or t.table_name like 'prd_%')
	ORDER BY T.TABLE_NAME;

	OPEN c_tabela;
	FETCH NEXT FROM c_tabela INTO @v_tabela;
	
	WHILE (@@FETCH_STATUS = 0)
	BEGIN
		PRINT 'DROP TRIGGER trg_' + lower(@v_tabela) + '_auid ON log_' + lower(@v_tabela) + ';' + @v_cr;
		PRINT 'DROP FUNCTION trg_' + lower(@v_tabela) + '_aiud();' + @v_cr;
		PRINT 'DROP SEQUENCE log_' + lower(@v_tabela) + '_seq;' + @v_cr;
		PRINT '/*--------------------------------------------------------------*/' + @v_cr;
        
        FETCH NEXT FROM c_tabela INTO @v_tabela;
	END
	
	CLOSE c_tabela;
	DEALLOCATE c_tabela;
	  
	PRINT 'DROP TABLE log_ADM_PARAMETRO' + @v_cr;
	PRINT 'DROP TABLE log_ADM_PARAMETRO_CATEGORIA' + @v_cr;
	PRINT 'DROP TABLE log_ADM_PAGINA_PERFIL' + @v_cr;
	PRINT 'DROP TABLE log_ADM_MENU' + @v_cr;
	PRINT 'DROP TABLE log_ADM_FUNCIONARIO_PERFIL' + @v_cr;
	PRINT 'DROP TABLE log_ADM_PAGINA' + @v_cr;
	PRINT 'DROP TABLE log_ADM_CARGO_PERFIL' + @v_cr;
	PRINT 'DROP TABLE log_ADM_PERFIL' + @v_cr;

END




CREATE FUNCTION dbo.pkg_adm_obter_estrutura_tabela(@p_esquema VARCHAR(128), 
@p_tabela VARCHAR(128), @p_prefixo_coluna VARCHAR(128))
RETURNS VARCHAR(8000)
AS
BEGIN
	DECLARE @v_resultado VARCHAR(8000);
    DECLARE @v_coluna VARCHAR(128);
	DECLARE @v_tipo VARCHAR(128);
	DECLARE @v_tamanho INTEGER;
	DECLARE @v_decimais INTEGER;
	DECLARE @v_tamanho_char INTEGER;
    DECLARE @v_cr CHAR(2);
 
    SET @v_cr = char(13) + char(10);  
    SET @v_resultado = '';

    DECLARE c_tabela cursor LOCAL FAST_FORWARD READ_ONLY FOR
		SELECT c.COLUMN_NAME coluna, c.data_type tipo, 
		(case c.data_type when 'bigint' then null else c.numeric_precision end) tamanho, 
		c.numeric_scale decimais, c.character_maximum_length tamanho_char
		FROM INFORMATION_SCHEMA.COLUMNS c, information_schema.tables t
		WHERE c.table_catalog = @p_esquema
		and c.table_schema='dbo'
		AND C.TABLE_SCHEMA=T.TABLE_SCHEMA
		AND c.TABLE_NAME=t.TABLE_NAME 
		AND t.TABLE_NAME = @p_tabela
		AND t.table_type='BASE TABLE'
		AND c.data_type NOT IN ('bytea','text')
		ORDER BY c.TABLE_NAME, c.ORDINAL_POSITION; 
	
	OPEN c_tabela;
	FETCH NEXT FROM c_tabela INTO @v_coluna, @v_tipo, @v_tamanho, @v_decimais, @v_tamanho_char;
	
	WHILE (@@FETCH_STATUS = 0)
	BEGIN

      IF (@v_resultado IS NULL) BEGIN
        SET @v_resultado = @p_prefixo_coluna + @v_coluna;
      end else begin
        SET @v_resultado = @v_resultado + ',' + @p_prefixo_coluna + @v_coluna;
      END;

      IF (@v_tipo = 'timestamp') BEGIN
        SET @v_resultado = @v_resultado + ' timestamp(0)';
      END ELSE IF (@v_tipo='char' OR @v_tipo='varchar') BEGIN
        SET @v_resultado = @v_resultado + ' ' + @v_tipo + '(' + @v_tamanho_char + ')';
      END ELSE IF (@v_decimais IS NOT NULL) BEGIN
        SET @v_resultado = @v_resultado + ' ' + @v_tipo + '(' + @v_tamanho + ',' + @v_decimais + ')';
      END ELSE IF (@v_tamanho IS NOT NULL) BEGIN
        SET @v_resultado = @v_resultado + ' ' + @v_tipo + '(' + @v_tamanho + ')';
      END ELSE BEGIN
        SET @v_resultado = @v_resultado + ' ' + @v_tipo;
      END;

	  FETCH NEXT FROM c_tabela INTO @v_coluna, @v_tipo, @v_tamanho, @v_decimais, @v_tamanho_char;
    END

	CLOSE c_tabela;
	DEALLOCATE c_tabela;

    RETURN @v_resultado;
END



CREATE FUNCTION dbo.pkg_adm_criar_tabela(@p_esquema VARCHAR(128), @p_tabela VARCHAR(128))
RETURNS VARCHAR(8000)
AS
BEGIN
	DECLARE @v_comando VARCHAR(8000);
	DECLARE @v_cr CHAR(2);

	SET @v_cr = char(13) + char(10);   

	SET @v_comando = 'CREATE TABLE ' + @p_esquema + '.log_' + @p_tabela + ' (' + @v_cr;
	SET @v_comando = @v_comando + '   id         BIGINT not null primary key,' + @v_cr;
	SET @v_comando = @v_comando + '   usuario    varchar(30),' + @v_cr;
	SET @v_comando = @v_comando + '   data       smalldatetime,' + @v_cr;
	SET @v_comando = @v_comando + '   operacao   char(1),' + @v_cr;
	SET @v_comando = @v_comando + '   ip         varchar(50),' + @v_cr;
	SET @v_comando = @v_comando + '   ' + dbo.pkg_adm_obter_estrutura_tabela(@p_esquema, @p_tabela, 'X') + ',' + @v_cr;
	SET @v_comando = @v_comando + '   ' + dbo.pkg_adm_obter_estrutura_tabela(@p_esquema, @p_tabela, '') +');' + @v_cr;

RETURN @v_comando;
END


CREATE FUNCTION dbo.pkg_adm_criar_sequence(@p_esquema VARCHAR(128), @p_tabela VARCHAR(128))
RETURNS VARCHAR(8000)
AS
BEGIN
	DECLARE @v_comando VARCHAR(8000);
	DECLARE @v_cr CHAR(2);

	SET @v_cr = char(13) + char(10);  

  SET @v_comando = 'CREATE SEQUENCE dbo.log_' + @p_tabela
  + '_seq as [bigint] minvalue 1 maxvalue 9223372036854775807 start with 1 increment by 1 CYCLE CACHE GO' + @v_cr;

  RETURN @v_comando;
END




CREATE FUNCTION dbo.pkg_adm_auditar_tabela(@p_esquema VARCHAR(128), @p_tabela VARCHAR(128))
RETURNS VARCHAR(8000)
AS
BEGIN
	DECLARE @v_comando VARCHAR(8000);
	DECLARE @v_cr CHAR(2);

	SET @v_cr = char(13) + char(10);   

    SET @v_comando = dbo.pkg_adm_criar_tabela(@p_esquema, @p_tabela) + @v_cr;
    SET @v_comando = @v_comando + dbo.pkg_adm_criar_sequence(@p_esquema, @p_tabela) + @v_cr;
    --v_comando := v_comando || pkg_adm_gerar_function_after(p_esquema, p_tabela) || v_cr;
    --v_comando := v_comando || pkg_adm_gerar_trigger_after(p_esquema, p_tabela) || v_cr;
    --v_comando := v_comando || pkg_adm_habilita_trigger_after(p_tabela, true) || v_cr;

    RETURN @v_comando;
END



CREATE PROCEDURE dbo.pkg_adm_montar_auditoria(@p_esquema VARCHAR(128))
AS
BEGIN                       
	DECLARE @v_cr CHAR(2);
	DECLARE @v_tabela VARCHAR(128);

	SET @v_cr = char(13) + char(10); 
  
	DECLARE c_tabela cursor LOCAL FAST_FORWARD READ_ONLY FOR
	select t.table_name
	from information_schema.tables t
	where t.table_catalog = @p_esquema
	and t.table_schema='dbo' 
	and t.table_type='BASE TABLE'
      and t.table_name <> 'adm_usuario'
      and t.table_name <> 'adm_auditoria_revisao'
      and t.table_name not like 'log_%'
      and t.table_name not like 'vw_%'
      and t.table_name not like '%_aud'
      and (t.table_name like 'adm_%'
      or t.table_name like 'prd_%')
      ORDER BY T.TABLE_NAME
     
	OPEN c_tabela;
	FETCH NEXT FROM c_tabela INTO @v_tabela;
	
	WHILE (@@FETCH_STATUS = 0)
	BEGIN
        PRINT dbo.pkg_adm_auditar_tabela(@p_esquema, @v_tabela);
		PRINT '/*--------------------------------------------------------------*/' + @v_cr;
        
        FETCH NEXT FROM c_tabela INTO @v_tabela;
	END
	
	CLOSE c_tabela;
	DEALLOCATE c_tabela
END



CREATE TABLE dbo.log_adm_menu (
	id bigint NOT NULL,
	usuario varchar(30) NULL,
	data smalldatetime NULL,
	operacao char(1) NULL,
	ip varchar(50) NULL,
	xmnu_seq bigint NULL,
	xmnu_descricao varchar(255) NULL,
	xmnu_pai_seq bigint NULL,
	xmnu_pag_seq bigint NULL,
	xmnu_ordem numeric(3,0) NULL,
	mnu_seq bigint NULL,
	mnu_descricao varchar(255) NULL,
	mnu_pai_seq bigint NULL,
	mnu_pag_seq bigint NULL,
	mnu_ordem numeric(3,0) NULL,
	CONSTRAINT log_adm_menu_pkey PRIMARY KEY (id)
)


CREATE SEQUENCE dbo.log_adm_menu_seq
as [bigint]
start with 1
increment by 1
minvalue 1
maxvalue 9223372036854775807
cycle
cache


CREATE FUNCTION dbo.pkg_adm_obter_usuario()
RETURNS varchar(255)
AS
BEGIN
	DECLARE @v_usuario_aplicacao VARCHAR(30);

	SELECT  @v_usuario_aplicacao = nt_username
	FROM    sys.sysprocesses AS S
	INNER JOIN    sys.dm_exec_connections AS decc ON S.spid = decc.session_id
	WHERE   spid = @@SPID;

	IF @v_usuario_aplicacao IS NULL 
	BEGIN
		SET @v_usuario_aplicacao = 'sem usuario';
	END;

	RETURN @v_usuario_aplicacao;
END




CREATE FUNCTION dbo.pkg_adm_obter_ip_usuario()
RETURNS varchar(255)
AS
BEGIN
	DECLARE @v_ip_usuario_aplicacao VARCHAR(30);

	SELECT  @v_ip_usuario_aplicacao = hostname
	FROM    sys.sysprocesses AS S
	INNER JOIN    sys.dm_exec_connections AS decc ON S.spid = decc.session_id
	WHERE   spid = @@SPID;

	IF @v_ip_usuario_aplicacao IS NULL 
	BEGIN
		SET @v_ip_usuario_aplicacao = '127.0.0.1';
	END;

	RETURN @v_ip_usuario_aplicacao;
END



CREATE FUNCTION dbo.GetCurrentIP()
RETURNS varchar(255)
AS
BEGIN
    DECLARE @IP_Address varchar(255);

    SELECT @IP_Address = client_net_address
    FROM sys.dm_exec_connections
    WHERE Session_id = @@SPID;

    Return @IP_Address;
END

SELECT  nt_username,
		hostname,
        net_library,
        net_address,
        client_net_address
FROM    sys.sysprocesses AS S
INNER JOIN    sys.dm_exec_connections AS decc ON S.spid = decc.session_id
WHERE   spid = @@SPID


CREATE TRIGGER dbo.TRG_MENU_AUID
ON dbo.adm_menu
AFTER INSERT, UPDATE, DELETE
AS
BEGIN

    IF EXISTS(SELECT * FROM inserted) AND EXISTS(SELECT * FROM deleted) 
	BEGIN
        INSERT INTO dbo.log_adm_menu(id,data,operacao,usuario,ip,
		xmnu_seq,xmnu_descricao,xmnu_pai_seq,xmnu_pag_seq,xmnu_ordem,
        mnu_seq,mnu_descricao,mnu_pai_seq,mnu_pag_seq,mnu_ordem)
		SELECT NEXT VALUE FOR dbo.log_adm_menu_seq, GETDATE(), 'U', 
		ISNULL(dbo.pkg_adm_obter_usuario(), 'no user'),ISNULL(dbo.pkg_adm_obter_ip_usuario(), '127.0.0.1'),
		mnu_seq, mnu_descricao, mnu_pai_seq, mnu_pag_seq, mnu_ordem,
		i.mnu_seq, i.mnu_descricao, i.mnu_pai_seq, i.mnu_pag_seq, i.mnu_ordem FROM inserted i;
    END 
	ELSE IF EXISTS(SELECT * FROM inserted) 
    BEGIN
        INSERT INTO dbo.log_adm_menu(id,data,operacao,usuario,ip,
		mnu_seq,mnu_descricao,mnu_pai_seq,mnu_pag_seq,mnu_ordem) 
		SELECT NEXT VALUE FOR dbo.log_adm_menu_seq, GETDATE(), 'I', ISNULL(dbo.pkg_adm_obter_usuario(), 'no user'),
		ISNULL(dbo.pkg_adm_obter_ip_usuario(), '127.0.0.1'),
        i.mnu_seq, i.mnu_descricao, i.mnu_pai_seq, i.mnu_pag_seq, i.mnu_ordem FROM inserted i;    
	END
    ELSE IF EXISTS(SELECT * FROM deleted)
	BEGIN
        INSERT INTO dbo.log_adm_menu(id,data,operacao,usuario,ip,
		mnu_seq,mnu_descricao,mnu_pai_seq,mnu_pag_seq,mnu_ordem) 
		SELECT NEXT VALUE FOR dbo.log_adm_menu_seq, GETDATE(), 'D', ISNULL(dbo.pkg_adm_obter_usuario(), 'no user'),
		ISNULL(dbo.pkg_adm_obter_ip_usuario(), '127.0.0.1'),
        d.mnu_seq, d.mnu_descricao, d.mnu_pai_seq, d.mnu_pag_seq, d.mnu_ordem FROM deleted d;
    END;

END


INSERT INTO adm_pagina (pag_seq, pag_url, pag_descricao) VALUES(NEXT VALUE FOR dbo.adm_pagina_seq, 'admin/admParametroCategoria/listarAdmParametroCategoria.xhtml', 'Categoria dos Parâmetros de Configuração');
INSERT INTO adm_pagina (pag_seq, pag_url, pag_descricao) VALUES(NEXT VALUE FOR dbo.adm_pagina_seq, 'admin/admParametroCategoria/editarAdmParametroCategoria.xhtml', 'Editar Categoria dos Parâmetros de Configuração');
INSERT INTO adm_pagina (pag_seq, pag_url, pag_descricao) VALUES(NEXT VALUE FOR dbo.adm_pagina_seq, 'admin/admParametro/listarAdmParametro.xhtml', 'Parâmetros de Configuração');
INSERT INTO adm_pagina (pag_seq, pag_url, pag_descricao) VALUES(NEXT VALUE FOR dbo.adm_pagina_seq, 'admin/admParametro/editarAdmParametro.xhtml', 'Editar Parâmetros de Configuração');
INSERT INTO adm_pagina (pag_seq, pag_url, pag_descricao) VALUES(NEXT VALUE FOR dbo.adm_pagina_seq, 'admin/admPerfil/listarAdmPerfil.xhtml', 'Administrar Perfil');
INSERT INTO adm_pagina (pag_seq, pag_url, pag_descricao) VALUES(NEXT VALUE FOR dbo.adm_pagina_seq, 'admin/admPerfil/editarAdmPerfil.xhtml', 'Editar Administrar Perfil');
INSERT INTO adm_pagina (pag_seq, pag_url, pag_descricao) VALUES(NEXT VALUE FOR dbo.adm_pagina_seq, 'admin/admPagina/listarAdmPagina.xhtml', 'Administrar Página');
INSERT INTO adm_pagina (pag_seq, pag_url, pag_descricao) VALUES(NEXT VALUE FOR dbo.adm_pagina_seq, 'admin/admPagina/editarAdmPagina.xhtml', 'Editar Administrar Página');
INSERT INTO adm_pagina (pag_seq, pag_url, pag_descricao) VALUES(NEXT VALUE FOR dbo.adm_pagina_seq, 'admin/admMenu/listarAdmMenu.xhtml', 'Administrar Menu');
INSERT INTO adm_pagina (pag_seq, pag_url, pag_descricao) VALUES(NEXT VALUE FOR dbo.adm_pagina_seq, 'admin/admMenu/editarAdmMenu.xhtml', 'Editar Administrar Menu');



INSERT INTO adm_parametro_categoria (pmc_seq, pmc_descricao, pmc_ordem) VALUES(NEXT VALUE FOR dbo.adm_parametro_categoria_seq, 'Parâmetros do Tribunal', 1);
INSERT INTO adm_parametro_categoria (pmc_seq, pmc_descricao, pmc_ordem) VALUES(NEXT VALUE FOR dbo.adm_parametro_categoria_seq, 'Parâmetros de Login', 2);
INSERT INTO adm_parametro_categoria (pmc_seq, pmc_descricao, pmc_ordem) VALUES(NEXT VALUE FOR dbo.adm_parametro_categoria_seq, 'Parâmetros de E-mail', 3);
INSERT INTO adm_parametro_categoria (pmc_seq, pmc_descricao, pmc_ordem) VALUES(NEXT VALUE FOR dbo.adm_parametro_categoria_seq, 'Parâmetros de Conexão de Rede', 4);
INSERT INTO adm_parametro_categoria (pmc_seq, pmc_descricao, pmc_ordem) VALUES(NEXT VALUE FOR dbo.adm_parametro_categoria_seq, 'Parâmetros do Sistema', NULL);



INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, 'Tribunal Regional do Trabalho da 1a. Região', 'Nome do tribunal onde o sistema está instalado.', 'NOME_TRIBUNAL', 1);
INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, 'TRT1', 'Sigla do tribunal onde o sistema está instalado.', 'SIGLA_TRIBUNAL', 1);
INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, '080009', 'Código númérico de 6 dígitos que identifica o órgão no SIAFI.', 'CODIGO_UNIDADE_GESTORA', 1);
INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, '102', 'Código númérico de 3 dígitos da UG no código de barras da GRU.', 'APELIDO_UNIDADE_GESTORA', 1);
INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, 'false', 'Bloquear o sistema para que os usuários, exceto do administador, não façam login', 'BLOQUEAR_LOGIN', 2);
INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, 'Produção', 'Define o ambiente onde o sistema está instalado. Este parâmetro pode ser preenchido com: desenvolvimento, homologação ou produção', 'AMBIENTE_SISTEMA', 2);
INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, 'NOME_USUARIO', 'Define o atributo usado para efetuar login no sistema. Este parâmetro pode ser preenchido com: NOME_USUARIO ou CPF', 'ATRIBUTO_LOGIN', 2);
INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, 'smtp.teste', 'Servidor SMTP para que o sistema envie e-mail.', 'SMTP_SERVIDOR', 3);
INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, '25', 'Porta do servidor SMTP para que o sistema envie e-mail.', 'SMTP_PORTA', 3);
INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, NULL, 'Usuário para login no servidor SMTP.', 'SMTP_USERNAME', 3);
INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, NULL, 'Senha para login no servidor SMTP.', 'SMTP_PASSWORD', 3);
INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, 'sistema@teste', 'E-mail do sistema.', 'SMTP_EMAIL_FROM', 3);
INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, 'teste.br', 'Servidor do Proxy.', 'PROXY_SERVIDOR', 4);
INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, '8080', 'Porta do Proxy.', 'PROXY_PORTA', 4);
INSERT INTO adm_parametro (par_seq, par_valor, par_descricao, par_codigo, par_pmc_seq) VALUES(NEXT VALUE FOR dbo.adm_parametro_seq, '[ 
{ "ativo": "false", "login" : "henrique.souza", "setor" : "SAM", "cargo": "15426", "loginVirtual": "" }
]', 'Habilitar o modo de teste por login, esquema do json [  { "ativo": "false", "login" : "fulano", "setor" : "DISAD", "cargo": "15426" } ]', 'MODO_TESTE', 2);








