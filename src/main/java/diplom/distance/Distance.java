package diplom.distance;

public interface Distance {

    static Distance distance(String similarityMeasure) {
        switch (similarityMeasure) {
            case "cos":
                return new CosineDistance();
            case "euclid":
                return new EuclideanDistance();
        }

        return new CosineDistance();
    }

    double calc(double[] vector1, double[] vector2);

}
