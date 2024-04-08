package dio.com.awss3.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;


import dio.com.awss3.model.S3Model;
import dio.com.awss3.service.S3Service;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 *    Classe que fornece acesso ao usuário através de Requisições HTTP
 */
@RestController
@RequestMapping(value = "v1/buckets")
@RequiredArgsConstructor
@Api(tags = "S3 Controller", description = "Endpoints para operações relacionadas a Buckets e Objetos S3")
public class S3Controller {


    private final S3Service s3Service;

    /**
     * Método para Criar um Bucket
     * [POST] http://localhost:8080/buckets/nomeDoBucket?publicBucket=true
     * @param nomeDoBucket identifica o nome do Bucket
     * @param publicBucket identifica se já existe um Bucket com mesmo nome
     * @return Não existe retorno neste método
     */
    @ApiOperation("Método para Criar um Bucket")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Bucket criado com sucesso"),
            @ApiResponse(code = 400, message = "Requisição inválida"),
            @ApiResponse(code = 500, message = "Erro interno no servidor")
    })
    @PostMapping(value = "/{nomeDoBucket}")
    public ResponseEntity<Void> createBucket(@PathVariable String nomeDoBucket, @RequestParam boolean publicBucket) {
        s3Service.createS3Bucket(nomeDoBucket, publicBucket);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Método para Listar todos os Buckets
     * [POST] http://localhost:8080/buckets
     * @return retorna uma Lista de de todos os Buckets
     */
    @ApiOperation("Listar todos os Buckets")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de Buckets recuperada com sucesso"),
            @ApiResponse(code = 500, message = "Erro interno no servidor")
    })
    @GetMapping
    public ResponseEntity<List<String>> listBuckets() {
        var buckets = s3Service.listBuckets();
        return ResponseEntity.ok(buckets.stream().map(Bucket::getName).collect(Collectors.toList()));
    }

    /**
     * Método para Apagar um Bucket pelo nome
     * [DELETE] http://localhost:8080/buckets/nomeDoBucket
     * @param nomeDoBucket identifica o nome do Bucket
     * @return Não existe retorno neste método
     */

    @ApiOperation("Apagar um Bucket pelo nome")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Bucket apagado com sucesso"),
            @ApiResponse(code = 404, message = "Bucket não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno no servidor")
    })
    @DeleteMapping(value = "/{nomeDoBucket}")
    public ResponseEntity<Void> deleteBucket(@PathVariable String nomeDoBucket) {
        s3Service.deleteBucket(nomeDoBucket);
        return ResponseEntity.noContent().build();
    }

    /**
     * Método para Criar um Objeto no Bucket
     * [POST] http://localhost:8080/buckets/careca-bucket/objects?publicObject=true
     * Exemplo : {"objectName": "nomeDoObjeto.txt", "text": "Testando escrita no objeto 3"}
     * @param nomeDoBucket identifica o nome do Bucket
     * @param model identifica o nome do Dominio ( classe modelo )
     * @param publicObject identifica se o Objeto existe no Bucket
     */
    @ApiOperation("Criar um Objeto no Bucket")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Objeto criado com sucesso"),
            @ApiResponse(code = 400, message = "Requisição inválida"),
            @ApiResponse(code = 500, message = "Erro interno no servidor")
    })
    @PostMapping(value = "/{nomeDoBucket}/objects")
    public ResponseEntity<Void> createObject(@PathVariable String nomeDoBucket, @RequestBody S3Model model, @RequestParam boolean publicObject) throws IOException {
        s3Service.putObject(nomeDoBucket, model, publicObject);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     *    Fazer Donwload do Obejto buscando pelo nome
     *    [GET] http://localhost:8080/buckets/nomeDoBucket/objects/nomeDoObjeto
     */
    @ApiOperation("Fazer Download do Objeto buscando pelo nome")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Download do objeto realizado com sucesso"),
            @ApiResponse(code = 404, message = "Objeto não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno no servidor")
    })
    @GetMapping(value = "/{nomeDoBucket}/objects/{nomeDoObjeto}")
    public ResponseEntity<File> downloadObject(@PathVariable String nomeDoBucket, @PathVariable String nomeDoObjeto) {
        File file = s3Service.downloadObject(nomeDoBucket, nomeDoObjeto);
        return ResponseEntity.ok().body(file);
    }

    /**
     *    Transferir todos os arquivos de um Bucket para um outro Bucket
     *    [PATCH]  http://localhost:8080/buckets/nomeDoBucketOrigem/objects/nomeDoObjeto.txt/nomeDoBucketDestino
     *    Obs.: nomeDoObjeto com a extensão
     */
    @ApiOperation("Transferir todos os arquivos de um Bucket para um outro Bucket")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Objetos transferidos com sucesso"),
            @ApiResponse(code = 404, message = "Objeto ou bucket de destino não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno no servidor")
    })
    @PatchMapping(value = "/{nomeDoBucketOrigem}/objects/{nomeDoObjeto}/{nomeDoBucketDestino}")
    public ResponseEntity<Void> moveObject(@PathVariable String nomeDoBucketOrigem, @PathVariable String nomeDoObjeto, @PathVariable String nomeDoBucketDestino) {
        s3Service.moveObject(nomeDoBucketOrigem, nomeDoObjeto, nomeDoBucketDestino);
        return ResponseEntity.noContent().build();
    }

    /**
     *    Listar os Objetos do Bucket
     *    [GET] http://localhost:8080/buckets/careca-bucket/objects/
     */
    @ApiOperation("Listar os Objetos do Bucket")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de objetos recuperada com sucesso"),
            @ApiResponse(code = 404, message = "Bucket não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno no servidor")
    })
    @GetMapping(value = "/{nomeDoBucket}/objects")
    public ResponseEntity<List<String>> listObjects(@PathVariable String nomeDoBucket) {
        List<String> objects = s3Service.listObjects(nomeDoBucket).stream()
                .map(S3ObjectSummary::getKey).collect(Collectors.toList());
        return ResponseEntity.ok(objects);
    }

    /**
     *    Apaga Um Objeto do Bucket pelo nome do Objeto
     *    [DELETE] http://localhost:8080/buckets/nomeDoBucket/objects/nomeDoObjeto.txt
     */
    @ApiOperation("Apagar Um Objeto do Bucket pelo nome do Objeto")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Objeto apagado com sucesso"),
            @ApiResponse(code = 404, message = "Objeto não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno no servidor")
    })
    @DeleteMapping(value = "/{nomeDoBucket}/objects/{nomeDoObjeto}")
    public ResponseEntity<Void> deleteObject(@PathVariable String nomeDoBucket, @PathVariable String nomeDoObjeto) {
        s3Service.deleteObject(nomeDoBucket, nomeDoObjeto);
        return ResponseEntity.noContent().build();
    }

    /**
     *   Apaga todos os Objetos do Bucket
     *   [DELETE] http://localhost:8080/buckets/nomeDoBucket/objects/
     */
    @ApiOperation("Apagar todos os Objetos do Bucket")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Objetos apagados com sucesso"),
            @ApiResponse(code = 404, message = "Bucket não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno no servidor")
    })
    @DeleteMapping(value = "/{nomeDoBucket}/objects")
    public ResponseEntity<Void> deleteListObjects(@PathVariable String nomeDoBucket, @RequestBody List<String> objects) {
        s3Service.deleteMultipleObjects(nomeDoBucket, objects);
        return ResponseEntity.noContent().build();
    }

}
