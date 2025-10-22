package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;

import java.io.IOException;
import java.io.Serializable;

@Named
@ViewScoped
public class MenuFrm implements Serializable {
    @Inject
    FacesContext facesContext;
    DefaultMenuModel model;

    @PostConstruct
    public void init() {

        //voy a agregar los de mas items del menu, hacer parte de los especificos
        model = new DefaultMenuModel();
        DefaultSubMenu tipos = DefaultSubMenu.builder().label("Tipos").expanded(true).build();
        DefaultMenuItem itemAlmacen = DefaultMenuItem.builder()
                .value("Almacén")
                .icon("pi pi-inbox")
                .ajax(true)
                .command("#{menuFrm.navegar('TipoAlmacen.jsf')}")
                .build();
        DefaultMenuItem itemProducto = DefaultMenuItem.builder()
                .value("Producto")
                .icon("pi pi-box")
                .ajax(true)
                .command("#{menuFrm.navegar('TipoProducto.jsf')}")
                .build();


        tipos.getElements().add(itemAlmacen);
        tipos.getElements().add(itemProducto);


        model.getElements().add(tipos);

    }

    public void navegar(String pagina) throws IOException {
            facesContext.getExternalContext().redirect(pagina);
    }

    public DefaultMenuModel getModel() {
        return model;
    }

}
