package com.danzir.scambio.figurine.scrapper.navigation;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaywrightFactory {

    private static final Logger logger = LoggerFactory.getLogger(PlaywrightFactory.class);

    public static final ThreadLocal<Playwright> playwrightThread = new ThreadLocal<>();
    public static final ThreadLocal<Page> pageThread = new ThreadLocal<>();

    public static synchronized Page getPage(boolean headless, long timeout) {
        if(playwrightThread.get() == null){
            Playwright playwright = Playwright.create();
            playwrightThread.set(playwright);
            Page page = createPage(playwright, headless, timeout);
            pageThread.set(page);
        }
        return pageThread.get();
    }

    private static synchronized Page createPage(Playwright playwright, boolean headless, long timeout) {
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        Page page = browser.newPage();
        page.context().setDefaultTimeout(timeout);
        logger.debug("New page {} created", page.hashCode());
        return page;
    }

    public static synchronized void closePage(){
        Playwright playwright = playwrightThread.get();
        Page page = pageThread.get();
        int pageHash = page.hashCode();
        if(playwright != null){
            page.close();
            pageThread.remove();
            playwright.close();
            playwrightThread.remove();
        }
        logger.debug("Page {} closed", pageHash);
    }

}
