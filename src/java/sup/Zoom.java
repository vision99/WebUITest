package sup;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class Zoom {
    static Robot r ;
    final static long DELAY_ZOOM ;
    static {
        try {
           r  = new Robot();
        } catch (AWTException ex) {
           ex.getMessage();
        }
     DELAY_ZOOM = 300;
}
    private Zoom() { }
    
    public static void minus() throws InterruptedException{
        r.keyPress(KeyEvent.VK_CONTROL);
            r.keyPress(KeyEvent.VK_SUBTRACT);
            r.keyRelease(KeyEvent.VK_SUBTRACT);
            r.keyRelease(KeyEvent.VK_CONTROL);
        Thread.sleep(DELAY_ZOOM);
    }
    
    public static void plus() throws InterruptedException{
    r.keyPress(KeyEvent.VK_CONTROL);
            r.keyPress(KeyEvent.VK_ADD);
            r.keyRelease(KeyEvent.VK_ADD);
             r.keyRelease(KeyEvent.VK_CONTROL);
        Thread.sleep(DELAY_ZOOM);
    }
}
