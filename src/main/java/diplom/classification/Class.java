package diplom.classification;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Class {

    public String name;

    @SerializedName("terms_frequency")
    public Map<String, Integer> termsFrequency;

    @Override
    public String toString() {
        return "Class{" +
                "name='" + name + '\'' +
                ", termsFrequency=" + termsFrequency +
                '}';
    }
}
