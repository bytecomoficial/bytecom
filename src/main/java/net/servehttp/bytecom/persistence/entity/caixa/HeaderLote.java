package net.servehttp.bytecom.persistence.entity.caixa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "header_lote")
public class HeaderLote implements Serializable {

	private static final long serialVersionUID = 1073955464009876576L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="tipo_operacao")
	private char tipoOperacao;
	@Column(name="tipo_servico")
	private int tipoServico;
	@Column(name="numero_remessa_retorno")
	private int numeroRemessaRetorno;
	@Column(name="data_gravacao_remessa_retorno")
	private Date dataGravacaoRemessaRetorno;
	@Column(name="data_credito")
	private Date dataCredito;

	@OneToOne
	@JoinColumn(name = "header_id")
	private Header header;
	@OneToMany(mappedBy="headerLote")
	private List<Registro> registros;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public char getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(char tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public int getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(int tipoServico) {
		this.tipoServico = tipoServico;
	}

	public int getNumeroRemessaRetorno() {
		return numeroRemessaRetorno;
	}

	public void setNumeroRemessaRetorno(int numeroRemessaRetorno) {
		this.numeroRemessaRetorno = numeroRemessaRetorno;
	}

	public Date getDataGravacaoRemessaRetorno() {
		return dataGravacaoRemessaRetorno;
	}

	public void setDataGravacaoRemessaRetorno(Date dataGravacaoRemessaRetorno) {
		this.dataGravacaoRemessaRetorno = dataGravacaoRemessaRetorno;
	}

	public Date getDataCredito() {
		return dataCredito;
	}

	public void setDataCredito(Date dataCredito) {
		this.dataCredito = dataCredito;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public List<Registro> getRegistros() {
		return registros;
	}

	public void setRegistros(List<Registro> registros) {
		this.registros = registros;
	}
	
}