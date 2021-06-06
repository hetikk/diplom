package diplom;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ExperimentsInterface {

    double getInitTime();

    double getWorkTime();

    Map<String, List<String>> run(File[] selectedFiles) throws Exception;

    String getBuilder();

}
