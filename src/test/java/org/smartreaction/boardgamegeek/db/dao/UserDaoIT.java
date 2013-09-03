package org.smartreaction.boardgamegeek.db.dao;

import org.junit.Before;
import org.junit.Test;
import org.smartreaction.boardgamegeek.db.entities.User;
import org.smartreaction.test.BaseDatabaseTest;

import java.util.List;

import static org.junit.Assert.*;

public class UserDaoIT extends BaseDatabaseTest
{
    private UserDao userDao;

    @Before
    public void setup()
    {
        super.setup();
        userDao = new UserDao();
        userDao.em = entityManager;
    }

    @Test
    public void getUser_existing_user()
    {
        User user = userDao.getUser("admin");
        assertNotNull(user);
        assertEquals(1, user.getId());
    }

    @Test
    public void getUser_new_user_not_in_database()
    {
        assertNull(userDao.getUser("newuser"));
    }

    @Test
    public void getUsers()
    {
        List<User> users = userDao.getUsers();
        assertEquals(2, users.size());
    }

    @Test
    public void createUser()
    {
        User user = new User();
        user.setUsername("newuser");
        userDao.createUser(user);
    }
}
