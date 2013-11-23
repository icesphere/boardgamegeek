package org.smartreaction.boardgamegeek.view;

import org.apache.commons.lang.StringUtils;
import org.omnifaces.util.Faces;
import org.smartreaction.boardgamegeek.services.BoardGameGeekService;
import org.smartreaction.boardgamegeek.xml.forum.Thread;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ManagedBean
@ViewScoped
public class ForumView {

    @EJB
    BoardGameGeekService boardGameGeekService;

    private List<Thread> threads;

    private long gameId;

    private long forumId;

    @PostConstruct
    public void setup() {
        forumId = Long.parseLong(Faces.getRequestParameter("id"));

        String gameIdParameter = Faces.getRequestParameter("gameId");
        if (!StringUtils.isEmpty(gameIdParameter)) {
            gameId = Long.parseLong(gameIdParameter);
        }

        threads = boardGameGeekService.getForumThreads(forumId);
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public long getGameId() {
        return gameId;
    }

    public long getForumId() {
        return forumId;
    }
}
