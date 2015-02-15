package net.servehttp.bytecom.percistence;

import java.util.List;

import javax.persistence.EntityManager;

import net.servehttp.bytecom.comercial.jpa.entity.Plano;
import net.servehttp.bytecom.extra.jpa.GenericoJPA;
import net.servehttp.bytecom.facede.CreateEntityManager;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class GenericoJPATest {
  private static EntityManager em;
  private static GenericoJPA genericoJPA;

  @BeforeClass
  public static void setUp() {
    em = CreateEntityManager.INSTANCE.getEntityManager();
    genericoJPA = new GenericoJPA();
    genericoJPA.setEntityManager(em);
  }

  @Test
  public void deveriaDeveriaBuscarTodos() {
    List<Plano> list = genericoJPA.buscarTodos(Plano.class);
    Assert.assertNotNull(list);
  }
  
  @Test
  public void deveriaDeveriaBuscarTodosComParametroString() {
    genericoJPA.buscarTodos("id", 1, Plano.class);
  }

  @Test
  public void deveriaDeveriaBuscarTodosComParametroInteiro() {
    List<Plano> list = genericoJPA.buscarTodos("id", "1", Plano.class);
    Assert.assertNotNull(list);
  }

  @Test
  public void deveriaDeveriaBuscarPorId() {
    genericoJPA.buscarPorId(Plano.class, 1);
  }

  @AfterClass
  public static void closeUp() {
    em.close();
  }

}
