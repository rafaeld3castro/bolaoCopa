package br.com.bolaoCopaDoMundo.util;

import java.io.IOException;

import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;

import org.primefaces.component.picklist.PickListRenderer;

public class PickListBugfixRenderer extends PickListRenderer {

    /*@Override
    protected void encodeClientBehaviors(FacesContext context, ClientBehaviorHolder component, WidgetBuilder wb) throws IOException {
        super.encodeClientBehaviors(context, component, wb);
        PickList pickList = (PickList) component;
        wb.attr("filterMatchMode", pickList.getFilterMatchMode(), null);
    }*/
    
    @Override
    protected void encodeClientBehaviors(FacesContext context, ClientBehaviorHolder component) throws IOException {
        super.encodeClientBehaviors(context, component);
        // PickList pickList = (PickList) component;
        //wb.attr("filterMatchMode", pickList.getFilterMatchMode(), null);
    }
}