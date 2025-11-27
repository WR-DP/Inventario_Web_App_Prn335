package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoUnidadMedidaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoUnidadMedida;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TipoUnidadMedidaFrmTest {

    TipoUnidadMedidaFrm frm;

    FacesContext facesContext;
    TipoUnidadMedidaDAO tipoUnidadMedidaDAO;
    UnidadMedidaFrm unidadMedidaFrm;


    private void inject(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        facesContext = mock(FacesContext.class);
        tipoUnidadMedidaDAO = mock(TipoUnidadMedidaDAO.class);
        unidadMedidaFrm = mock(UnidadMedidaFrm.class);

        frm = spy(new TipoUnidadMedidaFrm());

        frm.modelo = new LazyDataModel<TipoUnidadMedida>() {
            @Override
            public List<TipoUnidadMedida> load(int first, int pageSize,
                                               Map<String, SortMeta> sortBy,
                                               Map<String, FilterMeta> filterBy) {
                return List.of();
            }

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return 0;
            }
        };

        inject(frm, "facesContext", facesContext);
        inject(frm, "tipoUnidadMedidaDAO", tipoUnidadMedidaDAO);
        inject(frm, "unidadMedidaFrm", unidadMedidaFrm);

        // Mock de métodos heredados
        doReturn(facesContext).when(frm).getFacesContext();
        doReturn(tipoUnidadMedidaDAO).when(frm).getDao();
        doReturn(tipoUnidadMedidaDAO).when(frm).getDataAccess();
    }

    @Test
    void testInicializar() {
        when(tipoUnidadMedidaDAO.findRange(0, Integer.MAX_VALUE))
                .thenReturn(List.of(new TipoUnidadMedida()));

        frm.inicializar();

        assertNotNull(frm.getListaTipos());
        assertEquals(1, frm.getListaTipos().size());
    }

    @Test
    void testNuevoRegistro() {
        TipoUnidadMedida t = frm.nuevoRegistro();

        assertNotNull(t);
        assertEquals("", t.getNombre());
        assertEquals("", t.getUnidadBase());
        assertTrue(t.getActivo());
        assertEquals("", t.getComentarios());
    }

    @Test
    void testGetIdAsText() {
        TipoUnidadMedida t = new TipoUnidadMedida();
        t.setId(10);

        assertEquals("10", frm.getIdAsText(t));
    }

    @Test
    void testGetIdAsTextNull() {
        assertNull(frm.getIdAsText(null));
    }

    @Test
    void testGetIdByTextFound() {
        TipoUnidadMedida t = new TipoUnidadMedida();
        t.setId(5);

        frm.modelo.setWrappedData(List.of(t));

        assertEquals(t, frm.getIdByText("5"));
    }

    @Test
    void testGetIdByTextInvalid() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.getIdByText("xx"));
    }

    @Test
    void testBuscarRegistroPorId() {
        TipoUnidadMedida t = new TipoUnidadMedida();
        t.setId(3);

        frm.modelo.setWrappedData(List.of(t));

        assertEquals(t, frm.buscarRegistroPorId(3));
    }

    @Test
    void testBuscarRegistroPorIdNotFound() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.buscarRegistroPorId(99));
    }
}
