package diplom.config;


import com.google.gson.annotations.SerializedName;
import diplom.Application;
import fi.purkka.jarpa.JarpaArgs;
import fi.purkka.jarpa.JarpaParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static fi.purkka.jarpa.JarpaArg.string;

public class Config {

    public Clustering clustering;
    public Classification classification;

    public static Config parse(String[] args) throws IOException {
        JarpaArgs jargs = JarpaParser.parsing(args).parse();
        String configPath = jargs.get(string("-config").optional()).orElse("config.json");

        File configFile = new File(configPath);

        String json = FileUtils.readFileToString(configFile, Charset.defaultCharset());
        Config config = Application.gson.fromJson(json, Config.class);

        config.clustering.validate();
        config.classification.validate();

        return config;
    }

    public static class Clustering {
        @SerializedName("separate_value")
        public double separateValue;

        @SerializedName("similarity_measure")
        public String similarityMeasure;

        void validate() {
            if (separateValue < 0.1 || 1.0 < separateValue)
                throw new InvalidConfigValueException("invalid separate value");

            if (!Arrays.asList("cos", "euclid").contains(similarityMeasure))
                throw new InvalidConfigValueException("invalid similarity measure");
        }
    }

    public static class Classification {
        @SerializedName("separate_value")
        public double separateValue;

        @SerializedName("similarity_measure")
        public String similarityMeasure;

        void validate() {
            if (!Arrays.asList("cos", "euclid").contains(similarityMeasure))
                throw new InvalidConfigValueException("invalid similarity measure");
        }
    }

    public static class InvalidConfigValueException extends RuntimeException {
        InvalidConfigValueException(String errorMessage) {
            super(errorMessage);
        }
    }
}
