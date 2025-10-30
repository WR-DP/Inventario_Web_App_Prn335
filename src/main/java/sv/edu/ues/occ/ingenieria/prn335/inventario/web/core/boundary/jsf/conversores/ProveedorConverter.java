package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf.conversores;

import jakarta.enterprise.context.Dependent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProveedorDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;

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
            Object key = null;
            try {
                key = java.util.UUID.fromString(value);
            } catch (IllegalArgumentException ex1) {
                try {
                    key = Long.valueOf(value);
                } catch (NumberFormatException ex2) {
                    try {
                        key = Integer.valueOf(value);
                    } catch (NumberFormatException ex3) {
                        key = value;
                    }
                }
            }
            if (proveedorDAO != null) {
                return proveedorDAO.findById(key);
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Proveedor value) {
        if (value == null) return "";
        try {
            Object id = value.getId();
            return (id == null) ? "" : id.toString();
        } catch (Exception ex) {
            return "";
        }
    }
}
