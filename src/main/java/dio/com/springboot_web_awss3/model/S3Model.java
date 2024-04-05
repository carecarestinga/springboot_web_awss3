package dio.com.springboot_web_awss3.model;

/*
 *   Classe Modelo do Dominio
 * */

public class S3Model {
    private String objectName;
    private String text;

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