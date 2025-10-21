package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoAlmacenDAO;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name ="TIpoAlmacenServlet", urlPatterns="web/tipo_almacen")
public class TipoAlmacenServlet extends HttpServlet {

    @Inject
    TipoAlmacenDAO taDao;// tipoalmacendato

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

        String arriba= """
                <html>
                <head>
                </head>
                <body>
                """;
        String abajo= """
                
                </body>
                </html>
                """;
                StringBuilder sb= new StringBuilder();
//        if (req.getParameter("nombre") == null) {
//            //throw new ServletException("El nombre no puede ser nulo");
//            sb.append("<p>debe agregar un nombre al tipo almacen.</p>");
//        } else{
//            if(nombre !=null){
//                TipoAlmacenDAO tipoAlmacen = new TipoAlmacenDAO();
//                tipoAlmacen.setNombre(nombre);
//                tipoAlmacen.setActivo(true);
//                tipoAlmacen.setObservaciones("creado desde clase...");
//
//            }
//        }
//        try{
//            PrintWriter writer= resp.getWriter();
//            writer.println(arriba);
//            writer.println(abajo);
//            writer.flush();
//            writer.close();
//
//        }catch (IOException ex){
//            throw new ServletException("Error al procesar la respuesta", ex);
//    }
    }
}
