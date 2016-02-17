package br.com.clairtonluz.sicoba.api.comercial;

import br.com.clairtonluz.sicoba.model.entity.comercial.Cliente;
import br.com.clairtonluz.sicoba.model.entity.comercial.Conexao;
import br.com.clairtonluz.sicoba.service.comercial.conexao.ConexaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by clairtonluz on 19/12/15.
 */
@RestController
@RequestMapping("api/conexoes")
public class ConexaoAPI {

    @Autowired
    private ConexaoService conexaoService;

    @RequestMapping(value = "/cliente/{id}", method = RequestMethod.GET)
    public Conexao getPorCliente(@PathVariable Integer id) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        return conexaoService.buscarOptionalPorCliente(cliente);
    }

    @RequestMapping(value = "/ip/{ip}", method = RequestMethod.GET)
    public Conexao getPorIp(@PathParam("ip") String ip) {
        return conexaoService.buscarPorIp(ip);
    }

    @RequestMapping(value = "/ip/livre", method = RequestMethod.GET)
    public Map<String, String> getIpLivre() {
        String ip = conexaoService.buscarIpLivre();
        Map<String, String> map = new HashMap<>();
        map.put("ip", ip);
        return map;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Conexao save(Conexao conexao) throws Exception {
        return conexaoService.save(conexao);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public Conexao update(Conexao conexao) throws Exception {
        return conexaoService.save(conexao);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void remove(@PathVariable Integer id) {
        conexaoService.remove(id);
    }


}
