####  Este exemplo de API Rest possui as funcionalidades de operações de Buckets e Objetos do Serviço S3 da AWS Contendo as Seguintes Tecnologias : 

- **[Java 11](https://www.oracle.com/java)**
- **[Spring Boot ](https://spring.io/projects/spring-boot)**
- **[Maven](https://maven.apache.org)**
- **[Swagger2](http://localhost:8080/swagger-ui.html)**






###       Mapeamentos do S3

* /s3/bucket/create/{bucketName} :- Para criação de bucket
* /s3/bucket/list :- Obter todos os buckets na região atual no arquivo de propriedades
* /s3/bucket/files/{bucketName} :- Obtenha todos os detalhes dos arquivos com o nome do bucket selecionado
* /s3/bucket/delete/hard/{bucketName} :- Excluir bucket selecionado com todos os arquivos
* /s3/bucket/delete/{bucketName} :- Excluir bucket selecionado
* /s3/file/upload/{bucketName} :- Carregar arquivo no bucket selecionado
* /s3/file/delete/{bucketName}/{fileName} :- Excluir arquivo com o bucket selecionado e o nome do arquivo
* /s3/file/download/{bucketName}/{fileName} :- Baixar arquivo com o bucket selecionado e o nome do arquivo

###    Regras para Nome de Bucket

* Os nomes dos buckets devem ter entre 3 e 63 caracteres.
* Os nomes de bucket podem consistir apenas em letras minúsculas, números, pontos (.) e hífens (-).
* Os nomes de bucket devem começar e terminar com uma letra ou número.
* Os nomes de bucket não devem ser formatados como um endereço IP (por exemplo, 192.168.5.4).
* Os nomes de bucket devem ser exclusivos dentro de uma partição. Uma partição é um agrupamento de Regiões. Atualmente, a AWS tem três partições: aws (regiões padrão), aws-cn (regiões da China) e aws-us-gov (regiões do AWS GovCloud [EUA]).
* Os buckets usados com o Amazon S3 Transfer Acceleration não podem ter pontos (.) em seus nomes. Para obter mais informações sobre aceleração de transferência, consulte Aceleração de transferência do Amazon S3.



