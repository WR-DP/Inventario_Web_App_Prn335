package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoUnidadMedida;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TipoUnidadMedidaDAOTest {

    private static final Integer ANY_ID = 999; // 🔥 ESTA ES LA CONSTANTE QUE NECESITAS

    @Mock
    EntityManager em;

    @InjectMocks
    TipoUnidadMedidaDAO dao;

    TipoUnidadMedida tipo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tipo = new TipoUnidadMedida();
        tipo.setId(ANY_ID);
    }

    @Test
    void testFindByIdOK() {
        when(em.find(TipoUnidadMedida.class, ANY_ID)).thenReturn(tipo);

        TipoUnidadMedida resultado = dao.findById(ANY_ID);

        assertNotNull(resultado);
        assertEquals(ANY_ID, resultado.getId());
    }

    @Test
    void testFindByIdInvalido() {
        assertThrows(IllegalArgumentException.class, () -> dao.findById(null));
    }

    @Test
    void testFindByIdException() {
        when(em.find(TipoUnidadMedida.class, ANY_ID))
                .thenThrow(new RuntimeException("DB ERROR"));

        assertThrows(RuntimeException.class, () -> dao.findById(ANY_ID));
    }

    @Test
    void testCountException() {

        when(em.getCriteriaBuilder())
                .thenThrow(new IllegalStateException("ERR"));

        assertThrows(IllegalStateException.class, () -> dao.count());
    }


}
