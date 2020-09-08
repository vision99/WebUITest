package sup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logger {

    private static Logger instance = null;
    private static Path path;
    private static List<String> lst = new ArrayList<>();
    
    private static synchronized void getLogger(Path file) throws IOException {
        if (Files.exists(file)) {
            path = file.toAbsolutePath();
            return;
        }
        path = Files.createFile(file).toAbsolutePath();
        return ;
    }
    static List<String> modifyStr(String str) {

        lst.add(new SimpleDateFormat("E yyyy.MM.dd - kk:mm:ss_zzz").format(new Date()) + " ; " + str);
        return lst;
    }

    private Logger(Path file) {
        path = file;
        System.out.println("Absolute: " + path);
    }


//    public static void clear(String file) throws IOException {
//        Path currentPath = Paths.get(file);
//        Logger.getLogger(currentPath.toAbsolutePath());
//        if (Files.isRegularFile(path)) {
//            Files.delete(path);
//        }
//        path = Files.createFile(path);
////        = Paths.get(file);
//    }

    public static void write(String file, String str) throws IOException {
        Path currentPath = Paths.get(file);
        Logger.getLogger(currentPath.toAbsolutePath());
        Files.write(path, modifyStr(str), StandardOpenOption.APPEND);
        lst.clear();
    }
}
