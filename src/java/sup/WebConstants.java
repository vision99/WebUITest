
package sup;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;

public class WebConstants {
    public static final long WEBDRIVER_WAIT = 5400;
    public static final int THREAD_WAIT = 500;
    public static final String LOG_FILE_NAME = "log_";
    public static final String LOG_FILE_NAME_ERR = "logV2ERR.txt";
    public static final String LOG_FILE_FOLDER = "C:\\profixReportsTest\\";
    public static final List<Class<? extends Throwable>> exc = Arrays.asList(StaleElementReferenceException.class,NoSuchElementException.class, InterruptedException.class);
    
}

