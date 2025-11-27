package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.*;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.*;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DespachoKardexFrmTest {

    @InjectMocks
    DespachoKardexFrm frm;

    @Mock
    VentaDAO ventaDAO;

    @Mock
    FacesContext facesContext;

    @Mock
    VentaDetalleDAO ventaDetalleDAO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        frm = new DespachoKardexFrm();

        frm.facesContext = facesContext;
        frm.ventaDAO = ventaDAO;
        frm.ventaDetalleDAO = ventaDetalleDAO;

        // LazyDataModel requerido por DefaultFrm (PrimeFaces 15)
        frm.modelo = new LazyDataModel<Venta>() {

            @Override
            public List<Venta> load(int first,
                                    int pageSize,
                                    Map<String, SortMeta> sortBy,
                                    Map<String, FilterMeta> filterBy) {
                return List.of();
            }

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return 0;
            }
        };
    }

    @Test
    void testGetIdAsText() {
        Venta v = new Venta();
        UUID id = UUID.randomUUID();
        v.setId(id);

        assertEquals(id.toString(), frm.getIdAsText(v));
    }

    @Test
    void testGetIdAsTextNull() {
        assertEquals("", frm.getIdAsText(null));
    }

    @Test
    void testGetIdByTextFound() {
        UUID id = UUID.randomUUID();

        Venta venta = new Venta();
        venta.setId(id);

        frm.modelo.setWrappedData(List.of(venta));

        assertEquals(venta, frm.getIdByText(id.toString()));
    }

    @Test
    void testGetIdByTextNotFound() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.getIdByText(UUID.randomUUID().toString()));
    }

    @Test
    void testGetIdByTextInvalidUUID() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.getIdByText("BAD-UUID"));
    }

    @Test
    void testNuevoRegistro() {
        Venta v = frm.nuevoRegistro();
        assertEquals("ACTIVA", v.getEstado());
        assertNotNull(v.getFecha());
    }

    @Test
    void testSeleccionarRegistro() throws Exception {
        UUID id = UUID.randomUUID();

        Venta venta = new Venta();
        venta.setId(id);

        List<VentaDetalle> detalles = List.of(new VentaDetalle());

        when(ventaDetalleDAO.findByIdVenta(id, 0, Integer.MAX_VALUE))
                .thenReturn(detalles);

        frm.registro = venta;

        SelectEvent<Venta> evt = mock(SelectEvent.class);
        when(evt.getObject()).thenReturn(venta);

        frm.seleccionarRegistro(evt);

        assertEquals(detalles, frm.registro.getDetalles());
        assertEquals(ESTADO_CRUD.MODIFICAR, frm.getEstado()); // CORREGIDO
    }


    @Test
    void testCargarDatos() {
        List<Venta> lista = List.of(new Venta());

        when(ventaDAO.buscarVentasParaDespacho(0, 10)).thenReturn(lista);

        assertEquals(1, frm.cargarDatos(0, 10).size());
    }

    @Test
    void testContarDatos() {
        when(ventaDAO.contarVentasParaDespacho()).thenReturn(10L);
        assertEquals(10, frm.contarDatos());
    }

    @Test
    void testGetRandom() {
        String r = frm.getRandom();
        assertNotNull(r);
        assertFalse(r.isBlank());
    }

    @Test
    void testBtnCancelar() {
        frm.registro = new Venta();
        frm.setEstado(ESTADO_CRUD.MODIFICAR);

        frm.btnCancelarHandler(mock(ActionEvent.class));

        assertNull(frm.registro);
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }

}









