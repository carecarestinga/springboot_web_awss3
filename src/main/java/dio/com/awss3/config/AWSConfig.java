package dio.com.awss3.config;

import org.springframework.beans.factory.annotation.Value;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *  Classe de Configuração das Credenciais do AWS
 **/
@Configuration
@Slf4j
public class AWSConfig {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.key}")
    private String key;

    @Value("${aws.secret}")
    private String secret;

    /**
     *    Configura as Credencias de Acesso na AWS
     */
    public AWSCredentials credentials() {
        AWSCredentials credentials = new BasicAWSCredentials(
                this.key,            //  access key
                this.secret          //  secret key
        );
        log.info("Logando na AWS com as Credenciais do AWS...");
        return credentials;
    }

    @Bean
    public AmazonS3 amazonS3() {
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials()))
                .withRegion(this.region)
                .build();
        return s3client;
    }
}