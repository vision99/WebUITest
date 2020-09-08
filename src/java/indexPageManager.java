
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.ejb.AccessTimeout;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import load.Debug;
import sup.TryAgainException;

//@Named("mytest")
@ManagedBean(name = "mytest")
@RequestScoped
//@Stateful
//@AccessTimeout (value=90, unit = TimeUnit.MINUTES)
//@StatefulTimeout (value=90, unit = TimeUnit.MINUTES)
public class indexPageManager implements java.io.Serializable {
//    private List<String> arr = new LinkedList<String>();

    private String status = "";
    private String ret = "";
    private String url = "http://romans:Prophix.01@esxvm199/auth/";
    private String host = "esxvm199";
    private static int size = 0;
    private static String[] tmpArr;
    private boolean isClean = true;

    public boolean isIsClean() {
        return isClean;
    }

    public void setIsClean(boolean isClean) {
        this.isClean = isClean;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    static {
        tmpArr = new String[]{
            "03. Отчеты по снепшотам",
            "03. Ввод данных",
            "98. разработка",
            "10. Технические файлы",
            "07. Мепинги",
            "03. Отчеты по годам",
            "03. Отчеты по месяцам",
            "02. Макропараметры",
            //            "old",
            "06. Макеты",
            "06. Коннекторы к данным",
            "05. Рабочие схемы",
            "04. Процессы",
            "01. КТ",
            "99. Корзина",};
        size = tmpArr.length;

    }

//    public void setArr(List<String> arr) { this.arr = arr; }
    public void setRet(String ret) {
        this.ret = ret;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setTmpArr(String[] tmpArr) {
        this.tmpArr = tmpArr;
    }
//    public List<String> getArr() { return arr; }

    public String getRet() {
        return ret;
    }

    public String[] getTmpArr() {
        return tmpArr;
    }

    public indexPageManager() {
    }
//    public indexPageManager() { for (String item : tmpArr ) arr.add(item); }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @AccessTimeout(value = 90, unit = TimeUnit.MINUTES)

    public int doTestOne(String index) throws InterruptedException, IllegalAccessException, IOException, Exception {
        setRet(index);
        System.out.println("Index: " + index + " : " + Integer.parseInt(index) + " : " + tmpArr[Integer.parseInt(index)] + " : " + this.url);
        System.setProperty("host.name.db", this.host);
        System.setProperty("cacheclear", String.valueOf(this.isClean));
        try {
            System.out.println(Integer.parseInt(index) + " bbbb");
            new Debug(this.url, this.host, tmpArr[Integer.parseInt(index)]).debug();
        } catch (TryAgainException | org.openqa.selenium.InvalidArgumentException ex) {
            this.status = "TestFalse!";
            System.out.println("Cause: " + ex.getCause() + " : " + ex.getMessage());
            ex.printStackTrace();
            return 0;
        }
        this.status = "TestPassed!";
        return 1;
    }

    public void print(String str) {
//        url = str;
//        System.out.println("url : "+url+" : "+str);
//        url = str;
        url += str;
    }

}
