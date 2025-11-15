package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf.conversores;

import jakarta.enterprise.context.Dependent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProveedorDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;

import java.util.logging.Level;
import java.util.logging.Logger;

@FacesConverter(value = "proveedorConverter", managed = true)
@Dependent
public class ProveedorConverter implements Converter<Proveedor> {

    @Inject
    private ProveedorDAO proveedorDAO;

    @Override
    public Proveedor getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            Integer id = Integer.valueOf(value);  // solo integer
            return proveedorDAO.findById(id);
        } catch (NumberFormatException e) {
            Logger.getLogger(ProveedorConverter.class.getName())
                    .log(Level.SEVERE, "ID inválido para Proveedor: " + value, e);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Proveedor value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return value.getId().toString(); // <-- RETORNA INTEGER COMO STRING
    }
}
