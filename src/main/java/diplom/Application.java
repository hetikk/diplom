package diplom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplom.config.Config;
import diplom.gui.Frame;

import java.io.File;

public class Application {

    public static Gson gson;
    public static Config config;
    public static File currentDir;

    public static void main(String[] args) throws Exception {
        gson = new GsonBuilder().setPrettyPrinting().create();
        config = Config.parse(args);
        currentDir = new File(System.getProperty("user.dir") + "/sets");

        Frame.showForm();
    }

}
