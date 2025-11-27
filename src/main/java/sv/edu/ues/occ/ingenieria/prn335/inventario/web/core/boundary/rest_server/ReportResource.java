package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;


import javax.sql.DataSource;
import jakarta.annotation.Resource;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import net.sf.jasperreports.engine.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Path("reporte")
public class ReportResource implements Serializable {


    @Resource(lookup = "jdbc/pgdb")
    DataSource datasource;

    @GET
    @Path("{nombreReporte}")
    public Response getReport(@PathParam("nombreReporte") String nombreReporte) throws SQLException, JRException {
        String pathReporte = null;
        switch (nombreReporte) {
            case "KardexProductos":
                pathReporte = "/reports/KardexProductos.jasper";
                break;
            default:
                return Response.status(Response.Status.NOT_FOUND).entity("Reporte no encontrado").build();
        }

        //buscar el path real del reporte y generar el reporte
        InputStream report = this.getClass().getClassLoader().getResourceAsStream(pathReporte);
        Map<String, Object> parametros = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parametros, datasource.getConnection());
        StreamingOutput stream = new StreamingOutput() {


            @Override
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                try {
                    outputStream.write(JasperExportManager.exportReportToPdf(jasperPrint));
                } catch (JRException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        return Response.ok(stream, "applicaton/pdf")
                .header("content-dispotition", "attachment; filename=\"" + nombreReporte + UUID.randomUUID().getMostSignificantBits() + " .pdf")
                    .build();
    }
}