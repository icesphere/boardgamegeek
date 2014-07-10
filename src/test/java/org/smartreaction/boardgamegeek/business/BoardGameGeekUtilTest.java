package org.smartreaction.boardgamegeek.business;

import org.junit.Before;
import org.junit.Test;
import org.smartreaction.boardgamegeek.BoardGameGeekConstants;
import org.smartreaction.boardgamegeek.db.entities.Game;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BoardGameGeekUtilTest
{
    private BoardGameGeekUtil boardGameGeekUtil;
    private BoardGameCache boardGameCache;

    @Before
    public void setup()
    {
        boardGameCache = mock(BoardGameCache.class);

        boardGameGeekUtil = new BoardGameGeekUtil(boardGameCache);
    }

    @Test
    public void convertBoardGameGeekXmlText_images_and_bold()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("[ImageID=1320300medium] [b]\"Modern Art for Dummies\"[/b] Even the Meeples need some education");
        assertEquals("<div style=\"display:inline\"><img src=\"http://cf.geekdo-images.com/images/pic1320300_t.jpg\"/></div> <span style=\"font-weight:bold\">\"Modern Art for Dummies\"</span> Even the Meeples need some education", result);
    }

    @Test
    public void convertBoardGameGeekXmlText_quotes()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("Then I quoted [q]some cool quote[/q] and it was cool and then I quoted a specific person [q=\"testuser\"]another quote[/q]");
        assertEquals("Then I quoted <span class=\"quote\">Quote: some cool quote</span> and it was cool and then I quoted a specific person <span class=\"quote\">testuser wrote: another quote</span>", result);
    }

    @Test
    public void convertBoardGameGeekXmlText_italics()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("This should be [i]italics[/i] just because");
        assertEquals("This should be <span style=\"font-style:italic\">italics</span> just because", result);
    }

    @Test
    public void convertBoardGameGeekXmlText_underline()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("This should be [u]underlined[/u] just because");
        assertEquals("This should be <span style=\"text-decoration:underline\">underlined</span> just because", result);
    }

    @Test
    public void convertBoardGameGeekXmlText_url()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("Click [url=http://www.google.com]here[/url] to go to google.");
        assertEquals("Click <a href=\"http://www.google.com\">here</a> to go to google.", result);
    }

    @Test
    public void convertBoardGameGeekXmlText_color()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("Do you like [COLOR=#0000FF]blue[/COLOR] colored stuff or [color=red]red[/color] colored stuff?");
        assertEquals("Do you like <span style=\"color:#0000FF\">blue</span> colored stuff or <span style=\"color:red\">red</span> colored stuff?", result);
    }

    @Test
    public void convertBoardGameGeekXmlText_bgcolor()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("Do you like [BGCOLOR=#0000FF]blue[/BGCOLOR] colored stuff or [bgcolor=red]red[/bgcolor] colored stuff?");
        assertEquals("Do you like <span style=\"background-color:#0000FF\">blue</span> colored stuff or <span style=\"background-color:red\">red</span> colored stuff?", result);
    }

    @Test
    public void convertBoardGameGeekXmlText_size()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("This is [size=20]big text[/size]");
        assertEquals("This is <span font-size=\"20px\">big text</span>", result);
    }

    @Test
    public void multipleImages_and_floats()
    {
        Game game1 = new Game();
        game1.setName("test game 1");
        when(boardGameCache.getGame(93260)).thenReturn(game1);

        Game game2 = new Game();
        game2.setName("test game 2");
        when(boardGameCache.getGame(39463)).thenReturn(game2);

        String text = "Here is as close to ideal as I could get in 10 games or less:\n" +
                "\n" +
                "[floatright][ImageID=923048][/floatright]\n" +
                "[size=12][b][thing=93260][/thing][/b][/size]\n" +
                "\n" +
                "I think every good collection needs a solid 2 player game, whether its a pure abstract or dripping with theme.  Summoner Wars fits that niche for me.  I tend to like a little bit of luck in the games I play, and Summoner War's does just that with the dice aspect.  The sheer enjoyment of hitting a clutch die roll at a key opportunity, or overcoming a string of bad luck rolls through planning is hard to beat.  I think luck in board games makes for a better narrative experience; there seems to be more of a story underlying the events that happen in a game that contains some luck.  \n" +
                "\n" +
                "[clear]\n" +
                "\n" +
                "[floatleft][imageid=354780][/floatleft]\n" +
                "[size=12][b][thing=39463][/thing][/b][/size]";

        String expected = "Here is as close to ideal as I could get in 10 games or less:\n" +
                "\n" +
                "<div style=\"float:right\"><div style=\"display:inline\"><img src=\"http://cf.geekdo-images.com/images/pic923048_t.jpg\"/></div></div>\n" +
                "<span font-size=\"12px\"><span style=\"font-weight:bold\"><a href=\"game.xhtml?id=93260\">test game 1</a></span></span>\n" +
                "\n" +
                "I think every good collection needs a solid 2 player game, whether its a pure abstract or dripping with theme.  Summoner Wars fits that niche for me.  I tend to like a little bit of luck in the games I play, and Summoner War's does just that with the dice aspect.  The sheer enjoyment of hitting a clutch die roll at a key opportunity, or overcoming a string of bad luck rolls through planning is hard to beat.  I think luck in board games makes for a better narrative experience; there seems to be more of a story underlying the events that happen in a game that contains some luck.  \n" +
                "\n" +
                "<div style=\"clear:both\"></div>\n" +
                "\n" +
                "<div style=\"float:left\"><div style=\"display:inline\"><img src=\"http://cf.geekdo-images.com/images/pic354780_t.jpg\"/></div></div>\n" +
                "<span font-size=\"12px\"><span style=\"font-weight:bold\"><a href=\"game.xhtml?id=39463\">test game 2</a></span></span>";

        String result = boardGameGeekUtil.convertBoardGameGeekXmlText(text);
        assertEquals(expected, result);
    }

    @Test
    public void convertGeekLists()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("Check out this geeklist [geeklist=45896]some geeklist text[/geeklist] it is cool, then check out this one [geeklist=345][/geeklist] too.");
        assertEquals("Check out this geeklist <a href=\"geeklist.xhtml?id=45896\">some geeklist text</a> it is cool, then check out this one <a href=\"geeklist.xhtml?id=345\">geeklist 345</a> too.", result);
    }

    @Test
    public void convertThings()
    {
        Game game1 = new Game();
        game1.setName("test game 1");
        when(boardGameCache.getGame(345)).thenReturn(game1);

        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("Check out this thing [thing=45896]some thing text[/thing] it is cool, then check out this one [thing=345][/thing] too.");
        assertEquals("Check out this thing <a href=\"game.xhtml?id=45896\">some thing text</a> it is cool, then check out this one <a href=\"game.xhtml?id=345\">test game 1</a> too.", result);
    }

    @Test
    public void convertGames()
    {
        Game game1 = new Game();
        game1.setName("test game 1");
        when(boardGameCache.getGame(45896)).thenReturn(game1);

        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("Check out this game [gameid=45896] it is cool");
        assertEquals("Check out this game <a href=\"game.xhtml?id=45896\">test game 1</a> it is cool", result);
    }

    @Test
    public void convertUsers()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("Check out this user [user=someusername]some user text[/user] it is cool, then check out this one [user=someusername][/user] too.");
        assertEquals("Check out this user <a href=\""+ BoardGameGeekConstants.BBG_WEBSITE + "/user/someusername\">some user text</a> it is cool, then check out this one <a href=\"" + BoardGameGeekConstants.BBG_WEBSITE + "/user/someusername\">someusername</a> too.", result);
    }

    @Test
    public void convertBoardGameGeekXmlText_strike()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("This should be [-]striked through[/-] just because");
        assertEquals("This should be <strike>striked through</strike> just because", result);
    }

    @Test
    public void convertBoardGameGeekXmlText_clear()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("There should be a clear div [clear] and then more stuff");
        assertEquals("There should be a clear div <div style=\"clear:both\"></div> and then more stuff", result);
    }

    @Test
    public void convertBoardGameGeekXmlText_hr()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("There should be a [hr] and then more stuff");
        assertEquals("There should be a <hr/> and then more stuff", result);
    }

    @Test
    public void convertThreads()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("Check out this thread [thread=1234]some thread text[/thread] it is cool, then check out this one [thread=456][/thread] too.");
        assertEquals("Check out this thread <a href=\""+ BoardGameGeekConstants.BBG_WEBSITE + "/thread/1234\">some thread text</a> it is cool, then check out this one <a href=\"" + BoardGameGeekConstants.BBG_WEBSITE + "/thread/456\">thread 456</a> too.", result);
    }

    @Test
    public void convertBoardGameGeekXmlText_star()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("There should be :star: :halfstar: :nostar: images");
        assertEquals("There should be <img src=\""+BoardGameGeekConstants.BBG_STATIC_IMAGE+"star_yellow.gif\"/> <img src=\""+BoardGameGeekConstants.BBG_STATIC_IMAGE+"star_yellowhalf.gif\"/> <img src=\""+BoardGameGeekConstants.BBG_STATIC_IMAGE+"star_white.gif\"/> images", result);
    }

    @Test
    public void convertBoardGameGeekXmlText_thumbs_up()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("There should be a :thumbsup: image");
        assertEquals("There should be a <img src=\""+BoardGameGeekConstants.BBG_STATIC_IMAGE+"thumbs-up.gif\"/> image", result);
    }

    @Test
    public void convertBoardGameGeekXmlText_dice()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("There should be :d10-7: :d10-5: images");
        assertEquals("There should be <img src=\""+BoardGameGeekConstants.BBG_STATIC_IMAGE+"d10-7.gif\"/> <img src=\""+BoardGameGeekConstants.BBG_STATIC_IMAGE+"d10-5.gif\"/> images", result);
    }

    @Test
    public void convertBoardGameGeekXmlText_smileys()
    {
        String result = boardGameGeekUtil.convertBoardGameGeekXmlText("There should be :) :( images");
        assertEquals("There should be <img src=\""+BoardGameGeekConstants.BBG_STATIC_IMAGE+"smile.gif\"/> <img src=\""+BoardGameGeekConstants.BBG_STATIC_IMAGE+"sad.gif\"/> images", result);
    }
}
