
package load;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import sup.Logger;

public class PageObject {
    WebDriver driver;
    List<Field> fl = Arrays.asList(this.getClass().getDeclaredFields());
    
    public PageObject(WebDriver driver) {
        this.driver = driver;
        fl.forEach((f) -> { 
            if(f.getType() == By.class) try {
                byMass.add((By)f.get(this));
            } catch (IllegalArgumentException ex) {
                try{
                Logger.write("log.txt",this.getClass().getName()+" ConstructorException .");
                        }
                catch(IOException  ioEx){
                    System.out.println(ioEx.getMessage());
                }
                
            } catch (IllegalAccessException ex) {
                try {
                    Logger.write("log.txt",ex.getMessage());
                } catch (IOException ex1) {
                    System.out.println(ex1.getMessage());
                }
            }
        });
        
    }

    public List<By> getByMass() { return byMass; }
    
    ArrayList<By> byMass = new ArrayList<By>();
    
    String [] arr = {
        "",
        "",
        "",
        "",
        "",
        "",
    };
    By mainMenu = By.xpath("//button[@class='navbar-toggle toggle-collapsable-menu']");
    By explorer  = By.xpath("//a[contains(text(),'Проводник')]");//Document Explorer
    By pbl  = By.xpath("//span[contains(text(),'Общий')]");//Public
    By norProd  = By.xpath("//span[@class='ng-star-inserted' and contains(text(),'Общий')]");
//    By norProd  = By.xpath("//span[@class='ng-star-inserted' and contains(text(),'Норильский Никель (в продуктив)')]");
//    By inner = By.cssSelector("div[id$=viewport-inner]");
//    By canvas  = By.xpath("//canvas[contains(text(),'You need a browser which full supports HTML5 Canvas to run SpreadJS')]");
//    class="p-icon icon-close tab-close"
//    By lastElem  = By.xpath("//button[@class='navbar-toggle toggle-collapsable-menu']");
    
    
    public  WebElement findFields(String str) throws ClassNotFoundException, InterruptedException, IllegalArgumentException, IllegalAccessException{
       for(Field f : fl) if (f.getName().compareToIgnoreCase(str) == 0 ) {
           System.out.println(f.getName());
            return driver.findElement((By)f.get(this));
       }
       return null;
    }

}
