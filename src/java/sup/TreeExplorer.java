package sup;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static sup.ElemBindings.BREAD_CRUMB_SPAN_CSS;
import static sup.ElemBindings.CANVAS_XPATH;
import static sup.ElemBindings.DIV_VIEWPORT_INNER_CSS;
import testweb.CleanCache;

public class TreeExplorer {

    String testFolder;
    String logFile = "";
    Stack<Rows> branch = new Stack<>();
    List<Rows> listTree = new ArrayList<>();
    WebDriver driver;
    WebElement inner;
    WebElement breadCrumb;
    Rows parent = null;

    public TreeExplorer setTestFolder(String testFolder) {
        this.testFolder = testFolder;
        return this;
    }

    By[] arrContainer = new By[]{
        By.cssSelector("div[id='error-prompt']"),
        By.cssSelector("div[modal-render='true']"), //        By.cssSelector("div[class='modal-dlog modal-confirm"),
    };

    public TreeExplorer(WebDriver driver, Rows row, String logFile) {
        this.driver = driver;
        this.parent = row;
        this.logFile = logFile;
    }

    public TreeExplorer findTree() throws IOException, InterruptedException {
        modifyWait(By.cssSelector(DIV_VIEWPORT_INNER_CSS), WebConstants.WEBDRIVER_WAIT+5);
        List<WebElement> elList;
        String tmpStr;
        boolean flag = false;
        while (!flag) {
            elList = driver.findElements(By.cssSelector(DIV_VIEWPORT_INNER_CSS + "  div"));
            try {
                for (WebElement q : elList) {
                    tmpStr = q.getAttribute("data-column");
                    if (tmpStr != null && tmpStr.equals("FILE_NAME")) {
                        WebElement sibling = (WebElement) ((ChromeDriver) driver).executeScript("return arguments[0].nextSibling;", q);
                        Rows tmpRow = new Rows(driver, q, sibling);
                        this.listTree.add(tmpRow);
                        this.branch.push(tmpRow);
                    }
                }
                flag = true;
                //Ограничение на ввод
                if (parent.getPathText() == null) {
                    Rows r = null;
                    while (!branch.empty()) {
                        r = branch.pop();
                        if (r.getPathText().replace(" ", "").compareToIgnoreCase(testFolder.replace(" ", "")) == 0) {
                            break;
                        }
                    }
                    branch.clear();
                    branch.push(r);
                }
                //---------------------------
            } catch (StaleElementReferenceException ex) {
                System.out.println("StaleExcept in Constructor Rows");
                this.print();
                Thread.sleep(5000);
                listTree.clear();
                branch.clear();
            }
        }
        return this;
    }

    public void explore() throws IOException, InterruptedException, TryAgainException {
        Rows tmp = null;
        System.out.println("branch_size: " + branch.size());
        while (!branch.empty()) {
            List<WebElement> list;
            tmp = branch.pop();
            //folders
            if (tmp.chekFolder() == PathType.FOLDER) {
                Actions act = new Actions(driver);
                WebElement element = null;
                boolean mw = modifyWait(By.cssSelector(DIV_VIEWPORT_INNER_CSS), WebConstants.WEBDRIVER_WAIT + 5);
                if (mw) {
                    System.out.println(mw);
                }
                Thread.sleep(1500);
                //--------------------------------------------------
                boolean spanFlag = false;
                while (!spanFlag) {
                    try {
                        list = driver.findElements(By.cssSelector(DIV_VIEWPORT_INNER_CSS + " span[class='item-name']")); //Изменеия
                        for (WebElement elem : list) {
                            System.out.println("Aq: " + elem.getText());
                            if (elem.getText().replace(" ", "").compareToIgnoreCase(tmp.getPathText().replace(" ", "")) == 0) {
                                element = elem;
                            }
                        }
                        list.clear();
                        System.out.println("Element: " + element + " tmp: " + tmp.getPathText());

                        act.moveToElement(element).click().build().perform();
                        while (!findElementInText(By.cssSelector(BREAD_CRUMB_SPAN_CSS), tmp.getPathText()));
                        System.out.println("Зaшли: " + tmp.getPathText());
                        Thread.sleep(1000);

                        spanFlag = true;
                    } catch (Exception ex) {
                        ex.getMessage();
                        Thread.sleep(5000);
                    }
                }
                //--------------------------------------------------------------
//                mw = modifyWait(By.cssSelector("span[class'crumbname'"), WebConstants.WEBDRIVER_WAIT + 5);
                if (mw) {
                    System.out.println(mw);
                }
                new TreeExplorer(driver, tmp, this.logFile).findTree().print().explore();
                continue;
            }

            if (tmp.chekFolder() == PathType.SMART || tmp.chekFolder() == PathType.WEB) {
                StopWatch stp = new StopWatch();
                Actions act = new Actions(driver);
                WebDriverWait wait = new WebDriverWait(driver, 5400, 1000);
                WebElement clk = null;
                boolean mw = modifyWait(By.cssSelector(DIV_VIEWPORT_INNER_CSS), WebConstants.WEBDRIVER_WAIT + 5);
                if (mw) {
                    System.out.println(mw);
                }
                list = driver.findElements(By.cssSelector("span[class='item-name']"));
                for (WebElement elem : list) {
                    if (elem.getText().replace(" ", "").compareToIgnoreCase(tmp.getPathText().replace(" ", "")) == 0) {
                        clk = elem;
                    }
                }
                list.clear();
                if (Boolean.valueOf(System.getProperty("cacheclear"))) {
                    try {
                        CleanCache.cleanCapexYear();
                        Logger.write(this.logFile, "Кэш очищен."); // очистка Кэщ
                    } catch (ClassNotFoundException | SQLException ex) {
                        if (ex.getClass().equals(SQLException.class)) {
                            Logger.write(this.logFile, "Не удалось очистить кэш"); // очистка Кэщ
                        }
                    }
                }
                act.moveToElement(clk).click().build().perform();
//                System.out.println("Click: "+clk.getText());
                stp.start();
                try {
                    WebElement canvas = wait
//                            .ignoring(StaleElementReferenceException.class, NoSuchElementException.class)
                            .ignoreAll(WebConstants.exc)
                            .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CANVAS_XPATH))
                    );
                    stp.stop();
                    Logger.write(this.logFile, parent.getPathText() + ";отчет;" + tmp.getDataTypeText() + ";" + tmp.getPathText() + ";открытие;" + stp.getTime() + ";млсек");
                    stp.reset();
                    Thread.sleep(WebConstants.THREAD_WAIT);

                    ((ChromeDriver) driver).executeScript("document.querySelector(\"i[class='p-icon icon-close tab-close']\").click();");
                    stp.start();

                    mw = modifyWait(By.cssSelector(BREAD_CRUMB_SPAN_CSS), WebConstants.WEBDRIVER_WAIT + 60);
                    if (mw) {
                        System.out.println(mw);
                    }
                    stp.stop();
                    Logger.write(this.logFile, parent.getPathText() + ";отчет;" + tmp.getDataTypeText() + ";" + tmp.getPathText() + ";закрытие;" + stp.getTime() + ";млсек");
                    stp.reset();
                } catch (TimeoutException | InterruptedException ex) {
                    System.out.println("Time_out_Exception: " + ex.getMessage());
                    System.out.println("Тип обработчика: " + this.handlingTimeOutException());
                    switch (this.handlingTimeOutException()) {
                        case 1: {
                            this.closeError();
                            Logger.write(this.logFile, tmp.getPathText() + ";Ошибка сервера");
                            mw = modifyWait(By.cssSelector(BREAD_CRUMB_SPAN_CSS), WebConstants.WEBDRIVER_WAIT + 5);
                            if (mw) {
                                System.out.println(mw);
                            }
                            break;
                        }
                        case 2: {
                            this.closeWarning();
                            Logger.write(this.logFile, tmp.getPathText() + ";Куб не доступен!");
                            mw = modifyWait(By.cssSelector(BREAD_CRUMB_SPAN_CSS), WebConstants.WEBDRIVER_WAIT + 5);
                            if (mw) {
                                System.out.println(mw);
                            }
                            break;
                        }
                        case 0: {
//                            if (!this.existsElement(By.xpath(CANVAS_XPATH))) {
                            Logger.write(this.logFile, parent.getPathText() + ";отчет;" + tmp.getDataTypeText() + ";" + tmp.getPathText() + ";открытие;" + WebConstants.WEBDRIVER_WAIT + "000;млсек");
                            stp.stop();
                            ((ChromeDriver) driver).executeScript("document.querySelector(\"i[class='p-icon icon-close tab-close']\").click();");
                            mw = modifyWait(By.cssSelector(BREAD_CRUMB_SPAN_CSS), WebConstants.WEBDRIVER_WAIT + 5);
                            if (mw) {
                                System.out.println(mw);
                            }
                            break;
//                            }
//                            Logger.write( this.logFile, "Неисзвестное событие SMART; ");
//                            throw new TryAgainException("");//System.exit(0);
                        }
                        default: {
                            Logger.write(this.logFile, "Default сработал!!!");
                            System.exit(0); // не доступен!
                        }
                    }
                }
            }
        }
        Thread.sleep(WebConstants.THREAD_WAIT);
        boolean mw = modifyWait(By.cssSelector("div[class=breadcrumb-toolbar]"), WebConstants.WEBDRIVER_WAIT + 5);
        if (mw) {
            System.out.println(mw + " поднимаемся!");
        }
        ((ChromeDriver) driver).executeScript(""
                + "var el = document.querySelectorAll(\"span[class='crumbname']\"); "
                + "el.forEach((j)=>{ if (j.innerText == arguments[0] ) j.parentElement.previousSibling.click();})",
                parent.getPathText());
        System.out.println("Поднялись : " + parent.getPathText());
    }

    public TreeExplorer breadCrumbUpdate() throws IOException, InterruptedException, TryAgainException {
        WebDriverWait wait = new WebDriverWait(driver, 5400, 1000);
        this.breadCrumb = wait
                .ignoreAll(WebConstants.exc)
//                .ignoring(StaleElementReferenceException.class, NoSuchElementException.class)
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[class=breadcrumb-toolbar]")));
        Thread.sleep(WebConstants.THREAD_WAIT);
        if (this.breadCrumb == null) {
            Logger.write(this.logFile, "breadCrumb не найден");
        }
        return this;
    }

    public int handlingTimeOutException() {

        for (int i = 0; i < arrContainer.length; i++) {
            if (existsElement(arrContainer[i])) {
                String str = driver.findElement(arrContainer[i]).getAttribute("class");
                System.out.println("str: " + str);
                if (i == 0
                        && !str.isEmpty()) {
                    return 1;
                }
                if (i > 0) {
                    return i + 1;
                }
            }
        }
        return 0;
    }

    public boolean MyWait(By locator) throws InterruptedException {
        long i = WebConstants.WEBDRIVER_WAIT;
        while (--i > 0) {
            if (existsElement(locator)) {
                break;
            }
            Thread.sleep(1000);
        }
        return (i <= 0 ? false : true);
    }

    public boolean modifyWait(By locator, long wait) throws TimeoutException {
        
        new WebDriverWait(driver, wait, 1000)
//                .ignoring(StaleElementReferenceException.class, NoSuchElementException.class)
                .ignoreAll(WebConstants.exc)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        return true;
    }

    public boolean findElementInText(By locator, String text) throws InterruptedException {
        System.out.println("Ждем BreadCrumb: " + text);
        while (!existsElement(locator)) {
            System.out.println("BreadCrumb Не найден! " + text);
            Thread.sleep(5000);
        }
        boolean flag = false;
        while (!flag) {
            List<WebElement> list = driver.findElements(locator);
            for (WebElement el : list) {
                if (el.getText().compareToIgnoreCase(text) == 0) {
                    flag = true;
                    System.out.println("Дождались BreadCrumb: " + text);
                    Thread.sleep(WebConstants.THREAD_WAIT);
                    return true;
                }
            }
            System.out.println("BreadCrumbLast Не найден " + text);
            Thread.sleep(5000);
        }
        return false;
    }

    private boolean existsElement(By locator) {
        try {
            driver.findElement(locator);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public void closeError() {
        Actions actError = new Actions(driver);
        actError.moveToElement(driver.findElement(By.cssSelector("button[id='error-container-close-btn']"))).click().build().perform();

    }

    public void closeWarning() {
        Actions actError = new Actions(driver);
        actError.moveToElement(driver.findElement(By.cssSelector("div[class='modal-dialog modal-confirm']"))
                .findElement(By.tagName("button"))).click().build().perform();
    }

    public void dumpTree() {
        for (Rows ls : listTree) {
            System.out.println(ls.getPathText());
        }
    }

    public TreeExplorer print() {
        for (Rows el : this.listTree) {
            System.out.println(el.getPathText() + " : " + el.getDataTypeText());
        }
        return this;
    }
}
