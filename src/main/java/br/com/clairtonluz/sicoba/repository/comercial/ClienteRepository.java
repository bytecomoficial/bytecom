package br.com.clairtonluz.sicoba.repository.comercial;

import br.com.clairtonluz.sicoba.model.entity.comercial.Cliente;
import br.com.clairtonluz.sicoba.model.entity.comercial.StatusCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by clairtonluz on 15/11/15.
 */

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    String QUERY_CLIENTE_SEM_TITULOS = "select c from Cliente c where c.status <> 2 " +
            "and (" +
            "(SELECT count(*) FROM Titulo t where t.cliente.id = c.id and t.dataVencimento > :date) " +
            "+ (SELECT count(*) FROM Charge t where t.cliente.id = c.id and t.status <> 'NEW' and (t.status <> 'CANCELED' or t.manualPayment = true) and t.expireAt > :date)" +
            ") < 2 " +
            "order by ( " +
            "(SELECT count(*) FROM Titulo t where t.cliente.id = c.id and t.dataVencimento > :date) + " +
            "(SELECT count(*) FROM Charge t where t.cliente.id = c.id and t.status <> 'NEW' and (t.status <> 'CANCELED' or t.manualPayment = true) and t.expireAt > :date) " +
            ")";

    List<Cliente> findByStatus(StatusCliente status);

    Cliente findOptionalByEmail(String email);

    Cliente findOptionalByCpfCnpj(String cpfCnpj);

    Cliente findOptionalByRg(String rg);

    List<Cliente> findTop20ByOrderByUpdatedAtDesc();

    List<Cliente> findByNomeLike(String nome);

    @Query(QUERY_CLIENTE_SEM_TITULOS)
    List<Cliente> findBySemTitulosDepoisDe(@Param("date") LocalDate date);

    List<Cliente> findByStatusAndUpdatedAtGreaterThanOrderByUpdatedAtDesc(StatusCliente status, LocalDateTime data);
}
