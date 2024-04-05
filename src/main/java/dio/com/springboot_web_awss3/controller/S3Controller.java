package dio.com.springboot_web_awss3.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;


import dio.com.springboot_web_awss3.model.S3Model;
import dio.com.springboot_web_awss3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/*
 *    Classe que fornece acesso ao usuário através de Requisições HTTP
 * */
@RestController
@RequestMapping(value = "/buckets")
@RequiredArgsConstructor
public class S3Controller {


    private final S3Service s3Service;

    /*
     *     Criar um Bucket
     *     [POST] http://localhost:8080/buckets/nomeDoBucket?publicBucket=true
     * */
    @PostMapping(value = "/{nomeDoBucket}")
    public void createBucket(@PathVariable String nomeDoBucket, @RequestParam boolean publicBucket){
        s3Service.createS3Bucket(nomeDoBucket, publicBucket);
    }

    /*
     *    Listar todos os Buckets
     *    [POST] http://localhost:8080/buckets/nomeDoBucket/objects?publicObject=true
     * */
    @GetMapping
    public List<String> listBuckets(){
        var buckets = s3Service.listBuckets();
        var names = buckets.stream().map(Bucket::getName).collect(Collectors.toList());
        return names;
    }

    /*
     *    Apagar um Bucket pelo nome
     *    [DELETE] http://localhost:8080/buckets/nomeDoBucket
     * */
    @DeleteMapping(value = "/{nomeDoBucket}")
    public void deleteBucket(@PathVariable String nomeDoBucket){
        s3Service.deleteBucket(nomeDoBucket);
    }

    /*
     *    Criar um Objeto no Bucket
     *    [POST] http://localhost:8080/buckets/careca-bucket/objects?publicObject=true
     *    {"objectName": "nomeDoObjeto.txt", "text": "Testando escrita no objeto 3"}
     * */
    @PostMapping(value = "/{nomeDoBucket}/objects")
    public void createObject(@PathVariable String nomeDoBucket, @RequestBody S3Model representaion, @RequestParam boolean publicObject) throws IOException {
        s3Service.putObject(nomeDoBucket, representaion, publicObject);
    }

    /*
     *    Fazer Donwload do Obejto buscando pelo nome
     *    [GET] http://localhost:8080/buckets/nomeDoBucket/objects/nomeDoObjeto
     * */
    @GetMapping(value = "/{nomeDoBucket}/objects/{nomeDoObjeto}")
    public File downloadObject(@PathVariable String nomeDoBucket, @PathVariable String nomeDoObjeto) {
        s3Service.downloadObject(nomeDoBucket, nomeDoObjeto);
        return new File("./" + nomeDoObjeto);
    }

    /*
     *    Transferir todos os arquivos de um Bucket para um outro Bucket
     *    [PATCH]  http://localhost:8080/buckets/nomeDoBucketOrigem/objects/nomeDoObjeto.txt/nomeDoBucketDestino
     *    Obs.: nomeDoObjeto com a extensão
     * */
    @PatchMapping(value = "/{nomeDoBucketOrigem}/objects/{nomeDoObjeto}/{nomeDoBucketDestino}")
    public void moveObject(@PathVariable String nomeDoBucketOrigem, @PathVariable String nomeDoObjeto, @PathVariable String nomeDoBucketDestino) {
        s3Service.moveObject(nomeDoBucketOrigem, nomeDoObjeto, nomeDoBucketDestino);
    }

    /*
     *    Listar os Objetos do Bucket
     *    [GET] http://localhost:8080/buckets/careca-bucket/objects/
     * */
    @GetMapping(value = "/{nomeDoBucket}/objects")
    public List<String> listObjects(@PathVariable String nomeDoBucket) {
        return s3Service.listObjects(nomeDoBucket).stream()
                .map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

    /*
     *    Apaga Um Objeto do Bucket pelo nome do Objeto
     *    [DELETE] http://localhost:8080/buckets/nomeDoBucket/objects/nomeDoObjeto.txt
     * */
    @DeleteMapping(value = "/{nomeDoBucket}/objects/{nomeDoObjeto}")
    public void deleteObject(@PathVariable String nomeDoBucket, @PathVariable String nomeDoObjeto) {
        s3Service.deleteObject(nomeDoBucket, nomeDoObjeto);
    }

    /*
     *   Apaga todos os Objetos do Bucket
     *   [DELETE] http://localhost:8080/buckets/nomeDoBucket/objects/
     * */
    @DeleteMapping(value = "/{nomeDoBucket}/objects")
    public void deleteObject(@PathVariable String nomeDoBucket, @RequestBody List<String> objects) {
        s3Service.deleteMultipleObjects(nomeDoBucket, objects);
    }

}
