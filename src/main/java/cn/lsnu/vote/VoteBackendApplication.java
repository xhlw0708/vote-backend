package cn.lsnu.vote;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("cn.lsnu.vote.mapper")
@EnableTransactionManagement
public class VoteBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoteBackendApplication.class, args);
    }

}
