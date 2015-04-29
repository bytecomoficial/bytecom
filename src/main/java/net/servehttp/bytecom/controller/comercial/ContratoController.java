package net.servehttp.bytecom.controller.comercial;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.servehttp.bytecom.controller.extra.GenericoController;
import net.servehttp.bytecom.persistence.jpa.entity.comercial.Cliente;
import net.servehttp.bytecom.persistence.jpa.entity.comercial.Contrato;
import net.servehttp.bytecom.persistence.jpa.entity.comercial.Plano;
import net.servehttp.bytecom.persistence.jpa.entity.estoque.Equipamento;
import net.servehttp.bytecom.service.comercial.ClienteService;
import net.servehttp.bytecom.service.comercial.ContratoService;
import net.servehttp.bytecom.service.comercial.PlanoService;
import net.servehttp.bytecom.service.estoque.EquipamentoService;
import net.servehttp.bytecom.util.web.AlertaUtil;
import net.servehttp.bytecom.util.web.WebUtil;

/**
 * 
 * @author clairton
 */
@Named
@ViewScoped
public class ContratoController extends GenericoController implements Serializable {

  private static final long serialVersionUID = -5226446405193705169L;
  private List<Contrato> listContratos;
  private List<Plano> listPlanos;
  private List<Equipamento> listEquipamentos;
  private List<Equipamento> listEquipamentosWifi;

  private Cliente cliente;
  private int clienteId;
  @Inject
  private PlanoService planoBusiness;
  @Inject
  private EquipamentoService equipamentoBusiness;
  @Inject
  private ClienteService clientBusiness;
  @Inject
  private ContratoService contratoBusiness;

  @PostConstruct
  public void load() {
    listPlanos = planoBusiness.findAll();
    listEquipamentos = equipamentoBusiness.buscarEquipamentosInstalacaoNaoUtilizados();
    listEquipamentosWifi = equipamentoBusiness.buscarEquipamentosWifiNaoUtilizados();
    getParameters();
  }

  private void getParameters() {
    String clienteId = WebUtil.getParameters("clienteId");
    if (clienteId != null && !clienteId.isEmpty()) {
      setCliente(clientBusiness.buscarPorId(Integer.parseInt(clienteId)));
      if (getCliente().getContrato() == null) {
        gerarNovoContrato();
      }

      Equipamento e = getCliente().getContrato().getEquipamento();
      if (e != null && !listEquipamentos.contains(e)) {
        listEquipamentos.add(e);
      }

      e = getCliente().getContrato().getEquipamentoWifi();
      if (e != null && !listEquipamentosWifi.contains(e)) {
        listEquipamentosWifi.add(e);
      }
    }
  }

  private void gerarNovoContrato() {
    Contrato c = new Contrato();
    c.setCliente(getCliente());
    c.setDataInstalacao(LocalDate.now());
    getCliente().setContrato(c);
  }

  public List<Equipamento> getListEquipamentosWifi() {
    return listEquipamentosWifi;
  }

  public void setListEquipamentosWifi(List<Equipamento> listEquipamentosWifi) {
    this.listEquipamentosWifi = listEquipamentosWifi;
  }

  public List<Equipamento> getListEquipamentos() {
    return listEquipamentos;
  }

  public void setListEquipamentos(List<Equipamento> listEquipamentos) {
    this.listEquipamentos = listEquipamentos;
  }

  public List<Plano> getListPlanos() {
    return listPlanos;
  }

  public void setListPlanos(List<Plano> listPlanos) {
    this.listPlanos = listPlanos;
  }

  public List<Contrato> getListContratos() {
    return listContratos;
  }

  public void setListContratos(List<Contrato> listContratos) {
    this.listContratos = listContratos;
  }

  public String salvar() {
    jpa.salvar(cliente.getContrato());
    load();
    AlertaUtil.info("salvo com sucesso!");
    return "edit";
  }

  public void remover() {
    contratoBusiness.remover(cliente.getContrato());
    gerarNovoContrato();
    load();
    AlertaUtil.info("Contrato removido com sucesso!");
  }

  public int getClienteId() {
    return clienteId;
  }

  public void setClienteId(int clienteId) {
    this.clienteId = clienteId;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

}