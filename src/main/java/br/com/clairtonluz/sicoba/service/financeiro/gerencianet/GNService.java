package br.com.clairtonluz.sicoba.service.financeiro.gerencianet;

import br.com.clairtonluz.sicoba.config.MyEnvironment;
import br.com.clairtonluz.sicoba.exception.BadRequestException;
import br.com.clairtonluz.sicoba.exception.ConflitException;
import br.com.clairtonluz.sicoba.model.entity.comercial.Cliente;
import br.com.clairtonluz.sicoba.model.entity.comercial.Endereco;
import br.com.clairtonluz.sicoba.model.entity.financeiro.gerencianet.GerencianetAccount;
import br.com.clairtonluz.sicoba.model.entity.financeiro.gerencianet.carnet.Carnet;
import br.com.clairtonluz.sicoba.model.entity.financeiro.gerencianet.charge.Charge;
import br.com.clairtonluz.sicoba.util.StringUtil;
import br.com.gerencianet.gnsdk.Gerencianet;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by clairton on 09/11/16.
 */
@Service
public class GNService {

    private static final JSONObject BODY_EMPTY = new JSONObject();
    private static final HashMap<String, String> PARAMS_EMPTY = new HashMap<>();
    private static final double FINE_RATE = 0.05;
    private static final double INTEREST_RATE = 0.006677;
    private final MyEnvironment myEnvironment;

    @Autowired
    public GNService(MyEnvironment myEnvironment) {
        this.myEnvironment = myEnvironment;
    }

    public static JSONObject createItem(String name, Double value) {
        return createItem(name, value, null);
    }

    public static JSONObject createItem(String name, Double value, Integer amount) {
        int valueInt = retirarCasasDecimais(value); // retirando as casa decimais
        JSONObject item = new JSONObject()
                .put("name", name)
                .put("value", valueInt);
        if (amount != null) {
            item.put("amount", 1); // não é obrigatório
        }

        return item;
    }

    public static JSONObject createMetadata(String notificationUrl) {
        return createMetadata(notificationUrl, null);
    }

    /**
     * Se passar @notificationUrl com o valor null irá remover a url definida para notificação.
     *
     * @param notificationUrl
     * @param customId
     * @return
     */
    public static JSONObject createMetadata(String notificationUrl, Integer customId) {
        JSONObject metadata = new JSONObject().put("notification_url", notificationUrl);
        if (customId != null) {
            metadata.put("custom_id", customId.toString());
        }
        return metadata;
    }

    public static JSONObject createConsumer(Cliente cliente, boolean notificarClientePorEmail) {
        JSONObject customer = new JSONObject();
        Endereco endereco = cliente.getEndereco();
        if (endereco != null) {
            JSONObject customerAddres = new JSONObject();
            customerAddres.put("street", endereco.getLogradouro());
            customerAddres.put("number", endereco.getNumero());
            customerAddres.put("neighborhood", endereco.getBairro().getNome());
            customerAddres.put("zipcode", endereco.getCep());
            customerAddres.put("city", endereco.getBairro().getCidade().getNome());
            customerAddres.put("complement", endereco.getComplemento());
            customerAddres.put("state", endereco.getBairro().getCidade().getEstado().getUf());
            customer.put("address", customerAddres); // opcional
        }

        if (StringUtil.isCnpj(cliente.getCpfCnpj())) {
            JSONObject juridicalPerson = new JSONObject();
            juridicalPerson.put("corporate_name", cliente.getNome());
            juridicalPerson.put("cnpj", cliente.getCpfCnpj());
            customer.put("juridical_person", juridicalPerson);
        } else {
            customer.put("name", cliente.getNome());
            customer.put("cpf", cliente.getCpfCnpj());
        }
        customer.put("phone_number", cliente.getFoneTitular());
        if (notificarClientePorEmail && !StringUtil.isEmpty(cliente.getEmail())) {
            customer.put("email", cliente.getEmail());
        }
        return customer;
    }

    public static JSONArray createInstructions(Charge charge) {
        return createInstructions(charge.getValue(), charge.getDiscount());
    }

    public static JSONArray createInstructions(Carnet carnet) {
        return createInstructions(carnet.getValue(), carnet.getDiscountSplit());
    }

    private static JSONArray createInstructions(Double valor, Double discount) {
        Double fine = valor * FINE_RATE;
        Double interest = valor * INTEREST_RATE;
        JSONArray instructions = new JSONArray()
                .put("Não receber após 60 dias do vencimento");
//                .put(String.format("Sr. Caixa, cobrar multa de %s após o vencimento", StringUtil.formatCurrence(fine)))
//                .put(String.format("Sr. Caixa, cobrar juros de %s ao dia após o vencimento", StringUtil.formatCurrence(interest)));
        if (discount != null && discount > 0) {
            instructions.put(String.format("Até o dia do vencimento conceder desconto de R$%s", StringUtil.formatCurrence(discount)));
        }
        return instructions;
    }

    public static JSONObject createConfigurations(GerencianetAccount gerencianetAccount) {
        JSONObject configurations = new JSONObject();
        if (gerencianetAccount.getFine() != null)
            configurations.put("fine", retirarCasasDecimais(gerencianetAccount.getFine()));

        if (gerencianetAccount.getInterest() != null)
            configurations.put("interest", 330);

        return configurations;
    }

    public static JSONObject createDiscount(Double value) {
        JSONObject discount = null;
        if (value != null && value > 0) {
            int discountValue = retirarCasasDecimais(value);
            discount = new JSONObject()
                    .put("type", "currency")
                    .put("value", discountValue);
        }

        return discount;
    }

    private static int retirarCasasDecimais(Double value) {
        return (int) (value * 100);
    }

    public static boolean isOk(JSONObject response) {
        return response.getInt("code") == HttpStatus.OK.value();
    }


    public JSONObject call(GerencianetAccount account, String method, Map<String, String> params) {
        return call(account, method, params, BODY_EMPTY);
    }

    public JSONObject call(GerencianetAccount account, String method, JSONObject body) {
        return call(account, method, PARAMS_EMPTY, body);
    }

    public JSONObject call(GerencianetAccount account, String method, Map<String, String> params, JSONObject body) {
        try {
            Gerencianet gn = new Gerencianet(createOptions(account));
            JSONObject response = gn.call(method, params, body);
            return response;
        } catch (GerencianetException e) {
            e.printStackTrace();
            throw new ConflitException(String.format("%d - %s - %s - %s", e.getCode(), e.getErrorDescription(), e.getMessage(), e.getError()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConflitException(e.getMessage());
        }
    }


    public JSONObject createOptions(GerencianetAccount account) {
        JSONObject options = new JSONObject();
        options.put("client_id", account.getClientId());
        options.put("client_secret", account.getClientSecret());
        options.put("sandbox", !myEnvironment.isProduction());
        return options;
    }

    public boolean isNotifyClient(GerencianetAccount account) {
        return myEnvironment.isProduction() && account.isNotifyClient();

    }

    /**
     * O padrão de url a ser configurada: https://mydomain.com.br/api/gerencianet/%d/notification
     * onde o %d será substituido pelo o id da conta gerencianet configurada.
     *
     * @return
     * @param account
     */
    public String createNotificationUrl(GerencianetAccount account) {
        String url = null;
        if (account == null || account.getId() == null) {
            throw new BadRequestException("Conta gerencianet não foi cadastrada no sistema");
        }

        String notificationUrl = myEnvironment.getNotificationUrl();
        if (!StringUtil.isEmpty(notificationUrl)) {
            if (!notificationUrl.contains("%d")) {
                throw new BadRequestException("A url e notificação segue o padrão expecificado");
            }
            url = String.format(notificationUrl, account.getId());
        }
        return url;
    }

}
