package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import org.junit.jupiter.api.Test;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.VentaDetalle;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VentaDetalleDAOTest {

    @Test
    void testCalcularMontoTotal() {
        VentaDetalleDAO dao = new VentaDetalleDAO();

        VentaDetalle d1 = new VentaDetalle();
        d1.setCantidad(new BigDecimal("2"));
        d1.setPrecio(new BigDecimal("10.50"));

        VentaDetalle d2 = new VentaDetalle();
        d2.setCantidad(new BigDecimal("3"));
        d2.setPrecio(new BigDecimal("5.00"));

        VentaDetalle d3 = new VentaDetalle();
        d3.setCantidad(new BigDecimal("1.5"));
        d3.setPrecio(new BigDecimal("4.00"));

        List<VentaDetalle> detalles = List.of(d1, d2, d3);

        BigDecimal esperado =
                new BigDecimal("10.50").multiply(new BigDecimal("2"))
                        .add(new BigDecimal("5.00").multiply(new BigDecimal("3")))
                        .add(new BigDecimal("4.00").multiply(new BigDecimal("1.5")));

        BigDecimal resultado = dao.calcularMontoTotal(detalles);

        assertEquals(esperado, resultado);
    }
}
