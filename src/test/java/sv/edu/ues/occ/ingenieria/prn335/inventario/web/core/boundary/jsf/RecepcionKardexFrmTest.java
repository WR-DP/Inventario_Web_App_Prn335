package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.*;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Compra;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.CompraDetalle;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RecepcionKardexFrmTest {

    @InjectMocks
    RecepcionKardexFrm frm;

    @Mock
    FacesContext facesContext;

    @Mock
    CompraDAO compraDAO;

    @Mock
    ProductoDAO productoDAO;

    @Mock
    CompraDetalleDAO compraDetalleDAO;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        frm = new RecepcionKardexFrm();
        frm.facesContext = facesContext;
        frm.compraDAO = compraDAO;
        frm.productoDAO = productoDAO;
        frm.compraDetalleDAO = compraDetalleDAO;

        frm.modelo = new LazyDataModel<Compra>() {
            @Override
            public List<Compra> load(int first, int pageSize, Map sort, Map filter) {
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
        Compra c = new Compra();
        c.setId(10L);

        assertEquals("10", frm.getIdAsText(c));
    }

    @Test
    void testGetIdAsTextNull() {
        assertEquals("", frm.getIdAsText(null));
    }

    @Test
    void testGetIdByTextFound() {
        Compra c = new Compra();
        c.setId(5L);

        frm.modelo.setWrappedData(List.of(c));

        assertEquals(c, frm.getIdByText("5"));
    }

    @Test
    void testGetIdByTextNotFound() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.getIdByText("999"));
    }

    @Test
    void testGetIdByTextFormatoInvalido() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.getIdByText("NO-VALIDO"));
    }

    @Test
    void testNuevoRegistro() {
        Compra c = frm.nuevoRegistro();

        assertNotNull(c.getFecha());
        assertEquals("ACTIVO", c.getEstado());
    }

    @Test
    void testSeleccionarRegistroConDetalles() {
        Compra compra = new Compra();
        compra.setId(3L);

        List<CompraDetalle> detalles = List.of(new CompraDetalle());

        when(compraDetalleDAO.findByIdCompra(3L, 0, Integer.MAX_VALUE))
                .thenReturn(detalles);

        SelectEvent<Compra> evt = mock(SelectEvent.class);
        when(evt.getObject()).thenReturn(compra);

        frm.registro = compra;
        frm.seleccionarRegistro(evt);

        assertEquals(detalles, frm.registro.getDetalles());
        assertEquals(ESTADO_CRUD.MODIFICAR, frm.getEstado());
    }

    @Test
    void testBtnCancelar() {
        frm.registro = new Compra();
        frm.setEstado(ESTADO_CRUD.MODIFICAR);

        ActionEvent evt = mock(ActionEvent.class);

        frm.btnCancelarHandler(evt);

        assertNull(frm.registro);
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }

    @Test
    void testCargarDatos() {
        List<Compra> lista = List.of(new Compra());

        when(compraDAO.buscarLibrosParaRecepcion(0, 10))
                .thenReturn(lista);

        List<Compra> result = frm.cargarDatos(0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void testContarDatos() {
        when(compraDAO.contarLibrosParaRecepcion())
                .thenReturn(25L);

        assertEquals(25, frm.contarDatos());
    }

    @Test
    void testGetRandom() {
        String r1 = frm.getRandom();
        String r2 = frm.getRandom();

        assertNotNull(r1);
        assertNotEquals(r1, r2);
    }
}
