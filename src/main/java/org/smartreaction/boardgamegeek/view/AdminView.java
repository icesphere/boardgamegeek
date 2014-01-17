package org.smartreaction.boardgamegeek.view;

import org.smartreaction.boardgamegeek.db.dao.UserDao;
import org.smartreaction.boardgamegeek.db.entities.User;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ManagedBean
@ViewScoped
public class AdminView
{
    @ManagedProperty(value = "#{userSession}")
    UserSession userSession;

    @EJB
    UserDao userDao;

    private List<User> recentlyLoggedInUsers;

    @PostConstruct
    public void setup()
    {
        if (userSession.getUser().isAdmin()) {
            recentlyLoggedInUsers = userDao.getRecentlyLoggedInUsers();
        }
    }

    public List<User> getRecentlyLoggedInUsers()
    {
        return recentlyLoggedInUsers;
    }

    public void setUserSession(UserSession userSession)
    {
        this.userSession = userSession;
    }
}
