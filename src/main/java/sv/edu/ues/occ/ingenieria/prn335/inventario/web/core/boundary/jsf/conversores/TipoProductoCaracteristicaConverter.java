package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf.conversores;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.convert.Converter;
import jakarta.inject.Inject;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoCaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProductoCaracteristica;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@FacesConverter(value = "tipoProductoCaracteristicaConverter")
public class TipoProductoCaracteristicaConverter implements Converter<TipoProductoCaracteristica>{

    @Override
    public TipoProductoCaracteristica getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            Long id = Long.parseLong(value);
            TipoProductoCaracteristicaDAO dao = CDI.current().select(TipoProductoCaracteristicaDAO.class).get();
            return dao.findById(id);
        } catch (NumberFormatException ex) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, TipoProductoCaracteristica tipoProductoCaracteristica) {
        if (tipoProductoCaracteristica == null) {
            return "";
        }
        Long id = tipoProductoCaracteristica.getId();
        return id == null ? "" : id.toString();
    }
}