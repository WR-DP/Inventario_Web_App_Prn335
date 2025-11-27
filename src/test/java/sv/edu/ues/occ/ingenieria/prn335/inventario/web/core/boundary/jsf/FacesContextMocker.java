package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.context.FacesContext;

public final class FacesContextMocker {

    private FacesContextMocker() {}

    public static void setCurrentInstance(FacesContext ctx) {
        try {
            var field = FacesContext.class.getDeclaredField("instance");
            field.setAccessible(true);

            ThreadLocal<FacesContext> threadLocal =
                    (ThreadLocal<FacesContext>) field.get(null);

            threadLocal.set(ctx);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void release() {
        try {
            var field = FacesContext.class.getDeclaredField("instance");
            field.setAccessible(true);

            ThreadLocal<FacesContext> threadLocal =
                    (ThreadLocal<FacesContext>) field.get(null);

            threadLocal.remove();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
