/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package load;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import sup.Logger;
import sup.Rows;
import sup.TreeExplorer;
import sup.TryAgainException;
import sup.WebConstants;
import static sup.WebConstants.LOG_FILE_NAME;

public class Debug {

    final long DELAY_ZOOM = 300;
    ChromeDriver driver;
    StopWatch stp = new StopWatch();
    String folder;
    String url = "";
    String host = "";
    String logFileName = null;

    public Debug() {
    }

    public Debug(String url, String host, String str) throws IOException {
        this.folder = str;
        this.url = url;
        this.host = host;
        this.logFileName = LOG_FILE_NAME + new SimpleDateFormat("yyyy_MM_dd_kk_mm_ss_").format(new Date()) + "_" + this.folder + ".txt";
        Logger.write(this.logFileName, "Start testing at " + new SimpleDateFormat("yyyy-MM-dd kk:mm:ss zzz").format(new Date()));
        this.printFields();
    }

    public void printFields() {
        System.out.println(String.format("folder: %s\nurl: %s\nhost: %s\nlogfilename:%s\n", this.folder, this.url, this.host, this.logFileName));
    }

    public void setPublicFolder() {
    }

    public int debug() throws ClassNotFoundException, InterruptedException, IllegalArgumentException, IllegalAccessException, IOException, Exception {
        setUpClass();
        System.out.println("before");

        System.out.println("after");
        PageObject page = new PageObject(driver);
        WebElement el;
        String log = "";
        Actions act = new Actions(driver);
        try {
            for (By item : page.getByMass()) {
                if (new TreeExplorer(driver, new Rows(), logFileName).MyWait(item)) {
                    el = driver.findElement(item);
                    Thread.sleep(WebConstants.THREAD_WAIT);
                    log = "Найден!:" + el.getText() + " : " + stp.getTime() + " млсек";
                    System.out.println(log);
                    act.moveToElement(el).click().perform();
                } else {
                    Logger.write(logFileName, "Ошибка основного открытия!");
                    throw new TryAgainException("");// System.exit(0);
                }
            }
            Thread.sleep(WebConstants.THREAD_WAIT);
            new TreeExplorer(driver, new Rows(), logFileName).setTestFolder(folder).findTree().print().explore();
            Logger.write(this.logFileName, "Stop testing at " + new SimpleDateFormat("yyyy-MM-dd kk:mm:ss zzz").format(new Date()));
            driver.quit();
        } catch (Exception ex) {
            System.out.println(Thread.currentThread().getId());
            ex.printStackTrace();
            Logger.write(this.logFileName, "Stop testing at " + new SimpleDateFormat("yyyy-MM-dd kk:mm:ss zzz").format(new Date()));
            driver.quit();
            throw new TryAgainException("TestFailed");
        }
//        } // зацикливание
        return 0;
    }

    public void setUpClass() throws Exception {
        System.setProperty("webdriver.chrome.driver", new File("").getAbsolutePath() + "\\chromedriver_win32_85.exe");
        System.out.println("localappdata: " + System.getenv("LOCALAPPDATA"));
        ChromeOptions options = new ChromeOptions();
        String arrOpt[] = new String[]{
            "force-device-scale-factor=0.6",
            "start-maximized",

            "user-data-dir=" + System.getenv("LOCALAPPDATA") + "\\Google\\Chrome\\User Data\\Default" //            "user-data-dir=sC:\\Users\\alexd\\AppData\\Local\\Temp\\scoped_dir15516_728140660",
        };
        options.addArguments(Arrays.asList(arrOpt));
        driver = new ChromeDriver(options);
        System.out.println("Всё ОК");
        driver.manage().timeouts().implicitlyWait(90, TimeUnit.MINUTES).pageLoadTimeout(90, TimeUnit.MINUTES);

//        if (false) {
//            Set<Cookie> cook = new HashSet<Cookie>(
//                    Arrays.asList(
//                            new Cookie("XSRF-TOKEN", "ya0ISBC0KNsj5MEcaqWfy0LTXFfsMPo%2F21HQv%2B955u%2B%2FOBKEy34jbXmDk5ux1iH8LWts31fl%2FQDR4eRL5ysD5w%3D%3D", "esxvm199", "\\", new Date(1894752000)),
//                            new Cookie("authToken", "eyJhbGciOiJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNobWFjLXNoYTI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiNzIiLCJuYmYiOjE1Nzk3MDA0MDYsImV4cCI6MTYxMTIzNjQwNiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdC9XaW5kb3dzQXV0aGVudGljYXRpb25TZXJ2aWNlIiwiYXVkIjoiODk2MzIxbTk2ODU0NzFrbDNib2I5Yzg4ZjAwajU2NzgifQ.TnGbYfpyTuwsPSyurxC5mJt_8TPrlUL3MEzx63gGMmA", "esxvm199", "\\", new Date(1894752000)),
//                            new Cookie("Lang", "ru-RU", "esxvm199", "\\", new Date(1894752000)),
//                            new Cookie("cldr", "ru-RU", "esxvm199", "\\", new Date(1894752000))
//                    )
//            );
//            cook.forEach(c -> driver.manage().addCookie(c));
//        }
        System.out.println("url: " + url);
        driver.navigate().to(url);

        int alertWait = 10;
        System.out.println("Пойдем");
        Alert alert = null;
        while (alertWait-- > 0) {
            try {
                alert = driver.switchTo().alert();

                alert.sendKeys("romans");
                Thread.sleep(500);
                new Actions(driver).sendKeys(Keys.TAB);
                alert.sendKeys("Prophix.01");
                Thread.sleep(500);
                alert.accept();
                System.out.println("Alert отработал!");
            } catch (NoAlertPresentException e) {
                System.out.println("Alert not found yet!");
                Thread.sleep(1000);
                continue;
            }
            if (alertWait == 0) {
                System.out.println("Alert так и не появился!!!");
            }
        }
    }
}
