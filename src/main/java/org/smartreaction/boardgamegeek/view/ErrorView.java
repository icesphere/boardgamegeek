package org.smartreaction.boardgamegeek.view;

import org.omnifaces.util.Faces;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class ErrorView implements Serializable
{
    public String invalidateSession()
    {
        Faces.invalidateSession();
        return "";
    }
}
