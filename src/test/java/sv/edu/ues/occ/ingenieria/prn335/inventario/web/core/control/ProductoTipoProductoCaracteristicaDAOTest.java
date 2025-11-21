package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.ProductoTipoProductoCaracteristica;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoTipoProductoCaracteristicaDAOTest {

    private EntityManager emMock;
    private TypedQuery<ProductoTipoProductoCaracteristica> queryMock;
    private ProductoTipoProductoCaracteristicaDAO dao;

    @BeforeEach
    void setUp() throws Exception {

        emMock = Mockito.mock(EntityManager.class);
        queryMock = Mockito.mock(TypedQuery.class);

        dao = new ProductoTipoProductoCaracteristicaDAO();

        // Inyección del entityManager vía reflexión
        var field = ProductoTipoProductoCaracteristicaDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(dao, emMock);
    }

    // -------------------------------------------------------------------------
    // TEST findByProductoTipoProductoId() válido
    // -------------------------------------------------------------------------
    @Test
    void testFindByProductoTipoProductoIdValido() {

        UUID id = UUID.randomUUID();
        ProductoTipoProductoCaracteristica obj = new ProductoTipoProductoCaracteristica();
        List<ProductoTipoProductoCaracteristica> resultado = List.of(obj);

        when(emMock.createNamedQuery(
                "ProductoTipoProductoCaracteristica.findByProductoTipoProductoId",
                ProductoTipoProductoCaracteristica.class))
                .thenReturn(queryMock);

        when(queryMock.setParameter("idProductoTipoProducto", id)).thenReturn(queryMock);
        when(queryMock.setFirstResult(0)).thenReturn(queryMock);
        when(queryMock.setMaxResults(10)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(resultado);

        List<ProductoTipoProductoCaracteristica> lista =
                dao.findByProductoTipoProductoId(id, 0, 10);

        assertEquals(1, lista.size());
        assertSame(obj, lista.get(0));
    }

    // -------------------------------------------------------------------------
    // TEST findByProductoTipoProductoId() id null → lista vacía
    // -------------------------------------------------------------------------
    @Test
    void testFindByProductoTipoProductoIdNull() {

        List<ProductoTipoProductoCaracteristica> lista =
                dao.findByProductoTipoProductoId(null, 0, 10);

        assertTrue(lista.isEmpty());
        verify(emMock, never()).createNamedQuery(any(), any());
    }

    // -------------------------------------------------------------------------
    // TEST findByProductoTipoProductoId() excepción → IllegalArgumentException
    // -------------------------------------------------------------------------
    @Test
    void testFindByProductoTipoProductoIdException() {

        UUID id = UUID.randomUUID();

        when(emMock.createNamedQuery(
                "ProductoTipoProductoCaracteristica.findByProductoTipoProductoId",
                ProductoTipoProductoCaracteristica.class))
                .thenThrow(new RuntimeException("Error DB"));

        assertThrows(IllegalArgumentException.class,
                () -> dao.findByProductoTipoProductoId(id, 0, 10));
    }

    // -------------------------------------------------------------------------
    // TEST exists() id válido, existe → true
    // -------------------------------------------------------------------------
    @Test
    void testExistsTrue() {

        UUID id = UUID.randomUUID();
        ProductoTipoProductoCaracteristica entity = new ProductoTipoProductoCaracteristica();

        when(emMock.find(ProductoTipoProductoCaracteristica.class, id))
                .thenReturn(entity);

        assertTrue(dao.exists(id));
    }

    // -------------------------------------------------------------------------
    // TEST exists() id válido, NO existe → false
    // -------------------------------------------------------------------------
    @Test
    void testExistsFalse() {

        UUID id = UUID.randomUUID();

        when(emMock.find(ProductoTipoProductoCaracteristica.class, id))
                .thenReturn(null);

        assertFalse(dao.exists(id));
    }

    // -------------------------------------------------------------------------
    // TEST exists() id null → false
    // -------------------------------------------------------------------------
    @Test
    void testExistsNull() {

        assertFalse(dao.exists(null));
    }
}
