package diplom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplom.config.Config;
import diplom.gui.Frame;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Application {

    public static Gson gson;
    public static Config config;
    public static File currentDir;
    public static boolean time;
    public static boolean debug;
    public static final int REPETITION_COUNT = 100;
    public static final List<String> SUPPORTED_FILE_EXTENSIONS = Arrays.asList(".txt", ".docx");
    public static boolean experiments;

    public static void main(String[] args) throws Exception {
        gson = new GsonBuilder().setPrettyPrinting().create();
        config = Config.parse(args);
        currentDir = new File(System.getProperty("user.dir") + "/sets");
        time = false;
        debug = false;
        experiments = true;

        Frame.showForm();
    }

    public static boolean isSupportableFile(File file) {
        for (String s : SUPPORTED_FILE_EXTENSIONS) {
            if (file.getName().endsWith(s)) {
                return true;
            }
        }
        return false;
    }

}
