package pl.exsio.plupload.client.ui;

import com.google.gwt.user.client.Event;
import com.vaadin.client.ui.VWindow;

/**
 *
 * @author exsio
 */
public class ModalFixVWindow extends VWindow {

    @Override
    public boolean onEventPreview(final Event event) {
        if (vaadinModality) {
            return true;
        }
        return super.onEventPreview(event);
    }
}
