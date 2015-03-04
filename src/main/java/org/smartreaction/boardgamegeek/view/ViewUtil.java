package org.smartreaction.boardgamegeek.view;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ApplicationScoped
@ManagedBean
public class ViewUtil
{
    public String replace(String s, String replace, String replaceWith)
    {
        return s.replaceAll(replace, replaceWith);
    }
}
