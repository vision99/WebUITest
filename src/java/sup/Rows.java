
package sup;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
enum columns {FILE_NAME, DATA_TYPE};

public class Rows {
    //    By lastElem  = By.xpath("//button[@class='navbar-toggle toggle-collapsable-menu']");
    private WebElement path, dataType, pathSpan, dataTypeDiv;
    private String pathText, dataTypeText;
    private Rows  parent;
    public Rows(){}
    public Rows(WebDriver driver, WebElement path, WebElement dataType) {
        this.parent = null;
        this.path = path;
        this.dataType = dataType;
        this.pathSpan = this.path.findElement(By.cssSelector("span[class='item-name']"));
        this.pathText = this.pathSpan.getText();
        this.dataTypeDiv = this.dataType.findElement(By.xpath("./div"));
        this.dataTypeText = this.dataType.getText();
    }

    public Rows setParent(Rows  parent) {   this.parent = parent; return this; }
    public Rows getParent() { return parent; }
    public WebElement getPath() {  return path; }
    public WebElement getDataType() { return dataType; }
    public WebElement getPathSpan() {   return pathSpan; }
    public WebElement getDataTypeDiv() {   return dataTypeDiv;   }
    public String getPathText() {    return pathText;  }
    public String getDataTypeText() {    return dataTypeText;  }
    
    List <?> list;
   

    public Rows(ChromeDriver driver) {
        list = (List<?>)driver.executeScript(""
                + "var arr=[];\n" +
"                var tmp = document.querySelectorAll('div[id$=viewport-inner]')[0];\n" +
"                var inner = tmp.getElementsByTagName('div');\n"
                + "var j=0;\n" +
"                for( let i of inner)\n" +
"                if(i.hasAttribute('role')&& i.getAttribute('role') == 'row')\n" +
"                arr.push([i]);\n"
                + "return arr;");
    
    }

    public List<?> getList() {
        return list;
    }
   public  PathType chekFolder(){
        switch(this.dataTypeText){
            case "Шаблон студия-smart": return PathType.SMART; //"Шаблон студия-smart" Template Studio Classic
            case "Шаблон студия-web": return PathType.WEB;
            case "Папка": return PathType.FOLDER;//"Папка"Folder
        }
        return PathType.DEF;
    }
    @Override
    public String toString(){
        return "pathText : "+this.getPathText()+"; dataTypeText : "+this.getDataTypeText()+";";
    } 
}
