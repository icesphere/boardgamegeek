package org.smartreaction.boardgamegeek.view;

import org.apache.commons.lang.StringUtils;

import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(servletNames = {"Faces Servlet"})
public class FacesFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        if (!requestURI.startsWith(httpRequest.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) {
            UserSession userSession = null;
            if (httpRequest.getSession().getAttribute("userSession") != null) {
                userSession = (UserSession) httpRequest.getSession().getAttribute("userSession");
            }

            boolean usernameSet = userSession != null && userSession.isUsernameSet();
            if (userSession != null && !usernameSet) {
                for (Cookie cookie : httpRequest.getCookies()) {
                    if (cookie.getName().equalsIgnoreCase("bggpassword")) {
                        userSession.loginFromCookies(httpRequest.getCookies());
                        usernameSet = true;
                        break;
                    }
                }
            }

            if (!usernameSet && !requestURI.endsWith(httpRequest.getContextPath() + "/")
                    && !requestURI.endsWith("/login.xhtml") && !requestURI.endsWith("/loginWithPassword.xhtml")
                    && !requestURI.endsWith("/index.xhtml") && !requestURI.endsWith("/geeklists.xhtml")
                    && !requestURI.endsWith("/geeklist.xhtml") && !requestURI.endsWith("/game.xhtml")
                    && !requestURI.endsWith("/hotgames.xhtml") && !requestURI.endsWith("/topgames.xhtml")
                    && !requestURI.endsWith("/search.xhtml") && !requestURI.endsWith("/thread.xhtml")
                    && !requestURI.endsWith("/forum.xhtml")) {
                String redirectPage = requestURI.substring(requestURI.lastIndexOf("/") + 1);
                if (!StringUtils.isEmpty(redirectPage) && !redirectPage.startsWith("error")) {
                    httpRequest.getSession().setAttribute("redirectPage", redirectPage);
                }
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
                return;
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
