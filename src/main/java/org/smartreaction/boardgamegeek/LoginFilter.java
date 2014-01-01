package org.smartreaction.boardgamegeek;
import org.apache.commons.lang.StringUtils;
import org.smartreaction.boardgamegeek.view.UserSession;

import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String requestURI = httpRequest.getRequestURI();
        if (!requestURI.startsWith(httpRequest.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) {
            UserSession userSession = null;
            if (httpRequest.getSession().getAttribute("userSession") != null) {
                userSession = (UserSession) httpRequest.getSession().getAttribute("userSession");
            }

            boolean usernameSet = userSession != null && userSession.isUsernameSet();
            if (!usernameSet) {
                String bggUsername = null;
                String bggPassword = null;
                for (Cookie cookie : httpRequest.getCookies()) {
                    if (cookie.getName().equalsIgnoreCase("bggusername")) {
                        bggUsername = cookie.getValue();
                    }
                    else if (cookie.getName().equalsIgnoreCase("bggpassword")) {
                        bggPassword = cookie.getValue();
                    }
                }

                if (!StringUtils.isEmpty(bggUsername) && !StringUtils.isEmpty(bggPassword)) {
                    httpRequest.getSession().setAttribute("bggUsername", bggUsername);
                    httpRequest.getSession().setAttribute("bggPasswordCookie", bggPassword);

                    httpRequest.login(bggUsername, null);
                    if (userSession != null) {
                        userSession.loggedInFromCookies(httpRequest);
                    }
                    else {
                        httpRequest.getSession().setAttribute("loggedInFromCookies", true);
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        //do nothing
    }

    @Override
    public void destroy()
    {
        //do nothing
    }
}
