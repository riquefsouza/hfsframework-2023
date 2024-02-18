package br.com.hfs.hfsfullstack.model;

import java.util.Date;
import java.util.Objects;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import br.com.hfs.util.CPFCNPJUtil;
import br.com.hfs.util.DateUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity(name = "Funcionario")
@Table(name = "vw_adm_funcionario")
@NamedQueries({
	@NamedQuery(name = "Funcionario.findByNomeLike", query = "SELECT DISTINCT f FROM Funcionario f WHERE f.nome LIKE ?1")
})
public class Funcionario {

    @Id
	@GenericGenerator(name = "vw_adm_funcionario_ID_GENERATOR",
	type = org.hibernate.id.enhanced.SequenceStyleGenerator.class,
    parameters = {
    	@Parameter(name = "sequence_name", value = "vw_adm_funcionario_seq"),
        @Parameter(name = "initial_value", value = "1"),
        @Parameter(name = "increment_size", value = "1")
	})		
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="vw_adm_funcionario_ID_GENERATOR")
    @Column(name = "cod_funcionario", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cpf", nullable = false)
    private Long cpf;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "celular")
    private String celular;

    @Column(name = "setor")
    private String setor;

    @Column(name = "cod_cargo")
    private Long codCargo;

    @Column(name = "cargo")
    private String cargo;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_admissao", nullable = true)
    private Date dataAdmissao;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_saida", nullable = true)
    private Date dataSaida;

    @Column(name = "ativo")
    private Character ativo;

    public Funcionario() {
    }

    public Funcionario(Long id) {
        super();
        this.id = id;
    }

    public Funcionario(Long id, String nome, Long cpf, String email, String telefone, 
            String celular, String setor, Long codCargo, String cargo, 
            Date dataAdmissao, Date dataSaida, Character ativo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.celular = celular;
        this.setor = setor;
        this.codCargo = codCargo;
        this.cargo = cargo;
        this.dataAdmissao = dataAdmissao;
        this.dataSaida = dataSaida;
        this.ativo = ativo;
    }

    public static Funcionario with(Long id, String nome, Long cpf, String email, String telefone, 
            String celular, String setor, Long codCargo, String cargo, 
            Date dataAdmissao, Date dataSaida, Boolean ativo) { 
        return new Funcionario(id, nome, cpf, email, telefone, celular, setor, codCargo, cargo, dataAdmissao,
            dataSaida, ativo ? 'S' : 'N');
    }

	public void clean() {
		this.id = null;
        this.nome = null;
        this.cpf = null;
        this.email = null;
        this.telefone = null;
        this.celular = null;
        this.setor = null;
        this.codCargo = null;
        this.cargo = null;
        this.dataAdmissao = null;
        this.dataSaida = null;
        this.ativo = 'N';
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public Long getCodCargo() {
        return codCargo;
    }

    public void setCodCargo(Long codCargo) {
        this.codCargo = codCargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Date getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Character getAtivo() {
        return ativo;
    }

    public void setAtivo(Character ativo) {
        this.ativo = ativo;
    }
    
    public Boolean getAtivoBooleano() {
    	return ativo.equals('S');
    }

    public void setAtivoBooleano(Boolean ativoBooleano) {
    	this.ativo = ativoBooleano ? 'S' : 'N';
    }

    public String getCpfFormatado() {
        return CPFCNPJUtil.formatCPForCPNJ(this.cpf, false);
    }
    
    public String getDataAdmissaoFormatada() {    	
        return DateUtil.Format(dataAdmissao, DateUtil.DATE_STANDARD);
    }
    
    public String getDataSaidaFormatada() {
    	return DateUtil.Format(dataSaida, DateUtil.DATE_STANDARD);
    }

	@Override
	public int hashCode() {
		return Objects.hash(ativo, cargo, celular, codCargo, cpf, dataAdmissao, dataSaida, email, id, nome, setor,
				telefone);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Funcionario other = (Funcionario) obj;
		return Objects.equals(ativo, other.ativo) && Objects.equals(cargo, other.cargo)
				&& Objects.equals(celular, other.celular) && Objects.equals(codCargo, other.codCargo)
				&& Objects.equals(cpf, other.cpf) && Objects.equals(dataAdmissao, other.dataAdmissao)
				&& Objects.equals(dataSaida, other.dataSaida) && Objects.equals(email, other.email)
				&& Objects.equals(id, other.id) && Objects.equals(nome, other.nome)
				&& Objects.equals(setor, other.setor) && Objects.equals(telefone, other.telefone);
	}
	
}

