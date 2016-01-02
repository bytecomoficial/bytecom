package br.com.clairtonluz.bytecom.model.jpa.comercial;

import br.com.clairtonluz.bytecom.model.jpa.CrudJPA;
import br.com.clairtonluz.bytecom.model.jpa.entity.comercial.Cliente;
import br.com.clairtonluz.bytecom.model.jpa.entity.comercial.QCliente;
import br.com.clairtonluz.bytecom.model.jpa.entity.comercial.QConexao;
import br.com.clairtonluz.bytecom.model.jpa.entity.comercial.StatusCliente;
import br.com.clairtonluz.bytecom.model.jpa.entity.financeiro.QMensalidade;
import br.com.clairtonluz.bytecom.util.DateUtil;
import br.com.clairtonluz.bytecom.util.StringUtil;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author clairton
 */
@Transactional
public class ClienteJPA extends CrudJPA {

    private static final long serialVersionUID = 1857140370479772238L;
    private QCliente c = QCliente.cliente;

    @Inject
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }

    public List<Cliente> buscarTodosClientePorNomeIp(String nome, String ip, StatusCliente status) {
        BooleanExpression condicao = c.id.eq(c.id);
        QConexao conexao = QConexao.conexao;
        if (nome != null && !nome.trim().isEmpty()) {
            condicao = condicao.and(c.nome.like("%" + nome + "%"));
        }

        if (ip != null && !ip.isEmpty()) {
            condicao = condicao.and(conexao.ip.eq(ip));
        }

        if (status != null) {
            condicao = condicao.and(c.status.eq(status));
        }

        return new JPAQuery(entityManager).from(c).where(condicao).orderBy(c.nome.asc()).limit(200).list(c);
    }

    public List<Cliente> buscarSemMensalidade() {
        QMensalidade m = QMensalidade.mensalidade;
        Date data = DateUtil.toDate(LocalDate.now().plusMonths(2));
        return new JPAQuery(entityManager)
                .from(c)
                .where(
                        (c.status.eq(StatusCliente.ATIVO).or(c.status.eq(StatusCliente.INATIVO)).and(c.id
                                .notIn(new JPASubQuery().from(m).distinct().where(m.dataVencimento.gt(data))
                                        .list(m.cliente.id)))))
                .list(c);
    }

}
