package org.smartreaction.boardgamegeek.view;

import com.sun.jersey.api.client.Client;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class BoardGameGeekClient
{
    Client client = Client.create();

    public Client getClient()
    {
        return client;
    }
}
