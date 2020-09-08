
package sup;

import java.io.IOException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TagTextContext {
    ChromeDriver drv = null;
    static TagTextContext  ctx ; 
    public static TagTextContext getTagTextContext(ChromeDriver drv) throws IOException{
        
        return ctx = new TagTextContext(drv);
    }
    
    TagTextContext( ChromeDriver drv) throws IOException {
        Logger.write("log.txt", this.drv+" : "+ this.drv.equals(drv));
        if ( this.drv == null && !this.drv.equals(drv) ) this.drv = drv; 
    }
    
    public WebElement rwebelem(String tag, String text){
        List <WebElement> list = this.drv.findElements(By.tagName(tag));
//        System.out.println("list size: "+list.size());
        for(WebElement el : list) if (el.getText().compareToIgnoreCase(text)==0) return el;
        return null;
    }
}
