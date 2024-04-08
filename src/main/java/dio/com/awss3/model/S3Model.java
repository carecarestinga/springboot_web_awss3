package dio.com.awss3.model;

/**
 *   Classe Modelo do Dominio
 */

public class S3Model {
    private String objectName;
    private String text;

    public S3Model(String objectName, String text) {
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}