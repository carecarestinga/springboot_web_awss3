package dio.com.springboot_web_awss3.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;


import dio.com.springboot_web_awss3.model.S3Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/**
 * Classe de serviço que faz as Operações de um Bucket e seus Objetos
 *
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3Client;

    /*
     * Cria um Bucket verificando se não existe outro com mesmo nome
     * */
    public void createS3Bucket(String bucketName, boolean publicBucket) {
        if(amazonS3Client.doesBucketExist(bucketName)) {
            log.info("Nome do bucket já em uso. Tente outro nome");
            return;
        }
        if(publicBucket) {
            amazonS3Client.createBucket(bucketName);
        } else {
            amazonS3Client.createBucket(new CreateBucketRequest(bucketName).withCannedAcl(CannedAccessControlList.Private));
        }
    }

    /*
     *   Faz a listagem de Buckets existentes
     * */
    public List<Bucket> listBuckets(){
        return amazonS3Client.listBuckets();
    }

    /*
     *   Deleta um Bucket pelo Nome
     * */
    public void deleteBucket(String bucketName){
        try {
            amazonS3Client.deleteBucket(bucketName);
        } catch (AmazonServiceException e) {
            log.error(e.getErrorMessage());
            return;
        }
    }

    /*
     *   Insere um Texto no Objeto e Insere o Objeto no Bucket
     * */
    public void putObject(String bucketName, S3Model representation, boolean publicObject) throws IOException {

        String objectName = representation.getObjectName();
        String objectValue = representation.getText();

        File file = new File("." + File.separator + objectName);
        FileWriter fileWriter = new FileWriter(file, false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(objectValue);
        printWriter.flush();
        printWriter.close();

        try {
            if(publicObject) {
                var putObjectRequest = new PutObjectRequest(bucketName, objectName, file).withCannedAcl(CannedAccessControlList.PublicRead);
                amazonS3Client.putObject(putObjectRequest);
            } else {
                var putObjectRequest = new PutObjectRequest(bucketName, objectName, file).withCannedAcl(CannedAccessControlList.Private);
                amazonS3Client.putObject(putObjectRequest);
            }
        } catch (Exception e){
            log.error("Ocorreu algum erro..");
        }

    }

    /*
     *   Listar os Nomes dos Objetos do Bucket
     * */
    public List<S3ObjectSummary> listObjects(String bucketName){
        ObjectListing objectListing = amazonS3Client.listObjects(bucketName);
        return objectListing.getObjectSummaries();
    }

    /*
     *   Faz download do Objeto do Bucket
     * */
    public void downloadObject(String bucketName, String objectName){
        S3Object s3object = amazonS3Client.getObject(bucketName, objectName);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File("." + File.separator + objectName));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /*
     *   Deleta o Objeto do Bucket pelo Nome
     * */
    public void deleteObject(String bucketName, String objectName){
        amazonS3Client.deleteObject(bucketName, objectName);
    }

    /*
     *    Deleta Todos os Objetos do Bucket
     * */
    public void deleteMultipleObjects(String bucketName, List<String> objects){
        DeleteObjectsRequest delObjectsRequests = new DeleteObjectsRequest(bucketName)
                .withKeys(objects.toArray(new String[0]));
        amazonS3Client.deleteObjects(delObjectsRequests);
    }

    /*
     *   Transfere um Objeto de um Bucket para Outro Bucket
     * */
    public void moveObject(String bucketSourceName, String objectName, String bucketTargetName){
        amazonS3Client.copyObject(
                bucketSourceName,
                objectName,
                bucketTargetName,
                objectName
        );
    }

}