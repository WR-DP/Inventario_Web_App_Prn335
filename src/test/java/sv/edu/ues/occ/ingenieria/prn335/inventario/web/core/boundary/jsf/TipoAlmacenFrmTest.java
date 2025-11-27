package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.primefaces.model.LazyDataModel;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoAlmacenDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoAlmacen;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TipoAlmacenFrmTest {

    @InjectMocks
    TipoAlmacenFrm frm;

    @Mock
    FacesContext facesContext;

    @Mock
    TipoAlmacenDAO tipoAlmacenDAO;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        frm = new TipoAlmacenFrm();
        frm.facesContext = facesContext;
        frm.tipoAlmacenDAO = tipoAlmacenDAO;

        // Modelo necesario para el DefaultFrm
        frm.modelo = new LazyDataModel<TipoAlmacen>() {
            @Override
            public List<TipoAlmacen> load(int first, int pageSize, Map sort, Map filter) {
                return List.of();
            }

            @Override
            public int count(Map filter) {
                return 0;
            }
        };
    }

    @Test
    void testGetIdAsText() {
        TipoAlmacen t = new TipoAlmacen();
        t.setId(7);

        assertEquals("7", frm.getIdAsText(t));
    }

    @Test
    void testGetIdAsTextNull() {
        assertNull(frm.getIdAsText(null));
    }

    @Test
    void testGetIdByTextFound() {
        TipoAlmacen t = new TipoAlmacen();
        t.setId(3);

        frm.modelo.setWrappedData(List.of(t));

        TipoAlmacen result = frm.getIdByText("3");

        assertEquals(t, result);
    }

    @Test
    void testGetIdByTextNotFound() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.getIdByText("100"));
    }

    @Test
    void testGetIdByTextFormatoInvalido() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.getIdByText("NO-VALIDO"));
    }

    @Test
    void testBuscarRegistroPorId() {
        TipoAlmacen t = new TipoAlmacen();
        t.setId(15);

        frm.modelo.setWrappedData(List.of(t));

        assertEquals(t, frm.buscarRegistroPorId(15));
    }

    @Test
    void testBuscarRegistroPorIdNotFound() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.buscarRegistroPorId(9));
    }

    @Test
    void testNuevoRegistro() {
        TipoAlmacen t = frm.nuevoRegistro();

        assertNotNull(t);
        assertNull(t.getId());
        assertNull(t.getNombre());
        assertNull(t.getActivo());
        assertNull(t.getObsevaciones());
    }

    @Test
    void testInicializar() {
        List<TipoAlmacen> lista = List.of(new TipoAlmacen());

        when(tipoAlmacenDAO.findRange(0, Integer.MAX_VALUE))
                .thenReturn(lista);

        frm.inicializar();

        assertNotNull(frm.getListaTipoAlmacen());
        assertEquals(1, frm.getListaTipoAlmacen().size());
    }

    @Test
    void testNombreBean() {
        assertEquals("page.tipoAlmacen", frm.getNombreBean());

        frm.setNombreBean("nuevoNombre");

        assertEquals("nuevoNombre", frm.getNombreBean());
    }

    @Test
    void testListaTipoAlmacenSetterGetter() {
        List<TipoAlmacen> lista = List.of(new TipoAlmacen());

        frm.setListaTipoAlmacen(lista);

        assertEquals(lista, frm.getListaTipoAlmacen());
    }
}
