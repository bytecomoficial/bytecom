package net.servehttp.bytecom.model.jpa.entity.financeiro.retorno;

import net.servehttp.bytecom.model.jpa.entity.extra.EntityGeneric;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "trailer")
public class Trailer extends EntityGeneric implements Serializable {

    private static final long serialVersionUID = -4486342321787747170L;


    private int quantidadeLotes;
    private int quantidadeRegistros;

    @OneToOne
    @JoinColumn(name = "header_id")
    private Header header;

    public int getQuantidadeLotes() {
        return quantidadeLotes;
    }

    public void setQuantidadeLotes(int quantidadeLotes) {
        this.quantidadeLotes = quantidadeLotes;
    }

    public int getQuantidadeRegistros() {
        return quantidadeRegistros;
    }

    public void setQuantidadeRegistros(int quantidadeRegistros) {
        this.quantidadeRegistros = quantidadeRegistros;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

}