# [Yeah 24 프로젝트] 1. 프로젝트 생성



## 1) 인텔리제이 프로젝트 생성

![1](https://lh4.googleusercontent.com/Gtg3QXham6zXAJByUfzW6KCeWeYNZAwug-JWHZ_MMPcYAtl-OPrx60SfnMxJJgV-RTYjBiSi17lgCyQqVB84VogXUe3FdEmRxW5U-Jr_aHxG7cWnmVr52umP8Vl_RB53RrLdmvLF)

- gradle 프로젝트
- 자바는 8버전을 씁니다





### 의존성 추가

![2](https://lh3.googleusercontent.com/ItpdiIcnnFdPrOyHJDpbAxGKHrZiq2wZvYVISWi3MB7vJPCaWsJiOCKXtH20ra0qcsNIv2UfGr67sVfliNurE_Pd_oMUHjXPNLb2XvxzSOtponrjmuvh2AP1ELmGvZtSz3O6NRIl)

- 위와 같이 의존성을 추가해줍시다
  - Spring Boot DevTools
  - Lombok
  - Spring Web
  - Thymeleaf
  - Spring Data JPA
  - H2 Database
  - Validation
- 프로젝트를 진행하면서 필요한 것들은 따로 추가하겠습니다
  - ex) spring security







빌드가 완료되면 p6spy와 querydsl도 추가해줍니다

p6spy는 쿼리를 로깅하기 위해 사용하고,

querydsl은 jpql 쿼리를 문자열로 작성하지 않고 자바 코드로 작성할 수 있께 도와주는 프레임워크입니다.







#### # p6spy 추가

**buid.gradle**

```
plugins {
    id 'org.springframework.boot' version '2.5.3'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
    id 'java'
}

group = 'com'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    runtimeOnly 'com.h2database:h2'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    
    // p6spy 추가
    implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.7.1'
}

test {
    useJUnitPlatform()
}
```

- `implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.7.1'`을 추가해줍니다







config.p6spy 패키지를 만들어주고 그 안에 

`P6spyLogMessageFormatConfiguration`, `P6spySqlFormatConfiguration` 클래스를 작성합시다



**디렉토리 구조**

![3](https://lh4.googleusercontent.com/Vm43RGXgyqpBPuGK6pbQNfTVg-wtQDVIZHMN60LXMlIudaEgrJSIT6uknLY2JEEUqZWou5Oa5-gQTGQLsKqFe35NY_k1p62I9LeNTfr--buqV0TKmsBaTO9mjVWLbMbsWC9L5yNW)





**P6spyLogMessageFormatConfiguration**

```java
import com.p6spy.engine.spy.P6SpyOptions;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;

@Configuration
public class P6spyLogMessageFormatConfiguration {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(P6spySqlFormatConfiguration.class.getName());
    }
}
```



**P6spySqlFormatConfiguration**

```java
import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import java.util.Locale;

public class P6spySqlFormatConfiguration implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        return now + "|" + elapsed + "ms|" + category + "|connection " + connectionId + "|" + P6Util.singleLine(prepared) + sql;
    }

    private String formatSql(String category,String sql) {
        if(sql ==null || sql.trim().equals("")) return sql;

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.getName().equals(category)) {
            String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
            if(tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            }else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
        }

        return sql;
    }
}
```



여기까지 하셨다면 p6spy 설정은 끝났습니다..

이제 querydsl 프레임워크를 추가해봅시다





#### \# querydsl 추가

**build.gradle**

```
plugins {
    id 'org.springframework.boot' version '2.5.3'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
    id 'java'

    // querydsl 추가
    id 'com.ewerk.gradle.plugins.querydsl' version '1.0.10'
}

group = 'com'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    runtimeOnly 'com.h2database:h2'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'

    // querydsl 추가
    implementation 'com.querydsl:querydsl-jpa'

    // p6spy 추가
    implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.7.1'

}

test {
    useJUnitPlatform()
}


// querydsl 설정 추가
def querydslDir = "$buildDir/generated/querydsl"

querydsl {
    jpa = true
    querydslSourcesDir = querydslDir
}

sourceSets {
    main.java.srcDir querydslDir
}

configurations {
    querydsl.extendsFrom compileClasspath
}

compileQuerydsl {
    options.annotationProcessorPath = configurations.querydsl
}
```

- 총 세군데를 수정해야 합니다.
  - plugins, dependencies, `def querydslDir = ...`이하 부분





### 인텔리제이 빌드 설정

![9](https://lh4.googleusercontent.com/Lozpzu7UbJDM-tUyiQSo4F4Wy8RggfDfiLiRhttdvFF7V7e_F8A3BleyodgU5UPHpKvJQeJWtow5kTtYWFhfp-WH8nu8-ZXnUqv1d4UMGmfgl8qTXMK2nqYjonKzTo8oAKrALNph)

- file -> settings 들어가서 위의 빨간 네모처럼 바꿔줍니다.





### `application.yml` 파일 설정

![4](https://lh5.googleusercontent.com/xINLDur-lNNSH_0GeUSErtiotWyfGQWy99n4DLsjNM0n8lcV-Sjgywc5pDUP9jlEBO7XMVYZP5-FhUiUk-gq9h_MGHwkd9yANH3WzeADfNNIH4ORFZbbFxPFS4qU0z4SPSqLqWuF)

src/main/resources 폴더에 가보시면 처음에는 `application.properties` 파일이 보입니다.

저는 yml 파일 형식을 선호하기 때문에 확장자를 yml로 바꿔주겠습니다.



**application.yml**

```yaml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:tcp://localhost/~/yeah24
    username: sa
    password:

  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate.format_sql: true
```

- spring.datasource 설정
  - DB 커넥션을 설정하는 부분입니다.
  - 아직은 H2 DB 설정이 안되어 있습니다.
- spring.jpa.hibernate.ddl-auto
  - 하이버네이트의 초기화 전략을 설정하는 부분입니다.
  - create로 설정하면 시작될 때 기존의 DB를 drop하고 엔티티 정보를 읽어서 테이블을 생성해 줍니다.

- spring.jpa.properties.hibernate.format_sql
  - 쿼리 로그를 보기좋게 만들어 줍니다.







### H2 DB 설정

h2 DB가 설치되어 있지 않다면 [홈페이지](http://h2database.com/html/main.html)에 들어가서 Download 해줍니다.

설치한 뒤 시작 탭에서 h2 Console을 검색해서 실행해주면 아래와 같은 창이 뜹니다.

![5](https://lh4.googleusercontent.com/AdChLvVSxUtK0jdLU2imhFA9iJR8WSjtDY8-dZWkg0tzRe4F1slV875fO1_LY0ako4zD4VwBlGsiZyVPZIu56Y9-8w7UjB94iEFDB6DzjB7_-MvkfQgIGXL6Bq_SckuH1emKl6ip)







여기서 DB 파일을 생성하기 위해 아래(local mode)와 같이 설정해줍니다.

![6](https://lh5.googleusercontent.com/NxM59z83WklQ0SUeIhctChwe_xYxQvSU5OGTB86G7bGjppFzulH7ERyfXjwFY3nXBfip_E7UrL_nUqkOMgg8JQ2U8eIMAnps-BHElE5MFg7M7JCabUakhRzxaYvoC19E3Cmh2DuV)

그리고 연결을 누릅니다.



![8](https://lh6.googleusercontent.com/Z2OsqpjGmKl7H5f1_c3-KsjxL02DqematiNz9ID6Uaa4_jUsgs47ohR8TNkYfSRUl-EUoZCl8d1lti59WwI53TNR8yAqp2Wk0tXFMqaV9KknHmztoRkgqBxALKvJ2jzd3Oae_pRU)

위와 같은 창이 뜨면 성공입니다.





![7](https://lh5.googleusercontent.com/syXZ0MbZMWsW_qtYbsroXCHksSs5JyrMjzSLQxGalfKSMT9iaTIt2Sjnzr4YP2EYnhrWk41uE8fM34CK2KBKv15UXad5RLbJNfm0_DJWFfKWaD_d-eRMykaiNHYYjiAlv7E8Gr7j)

`C:\Users\사용자명` 폴더에 가보면 맨 아래 보이는 것처럼 `yeah24.mv.db` 파일이 생성되었습니다.

> 저의 경우 사용자명이 user입니다







DB 파일을 생성했으니 이제부터 DB에 접속할 때는 아래와 같은 설정(server mode)으로 접속합니다.

![5](https://lh4.googleusercontent.com/AdChLvVSxUtK0jdLU2imhFA9iJR8WSjtDY8-dZWkg0tzRe4F1slV875fO1_LY0ako4zD4VwBlGsiZyVPZIu56Y9-8w7UjB94iEFDB6DzjB7_-MvkfQgIGXL6Bq_SckuH1emKl6ip)

- 아니 그럼 두가지 설정이 뭐가 다르냐? 생각이 들 수 있습니다.

  - Generic H2 (Embedded) 설정 + JDBC URL: jdbc:h2:~/yeah24
    - 얘는 Local Mode 입니다.
  - Generic H2 (Server) 설정 + JDBC URL: jdbc:h2:tcp://localhost/~/yeah24
    - 얘는 Server Mode 입니다.

  - 자세한 차이점은 [링크](https://lob-dev.tistory.com/entry/H2%EC%9D%98-LocalIn-Memory-%EC%99%80-ServerTCP-%EB%AA%A8%EB%93%9C)를 참고합시다.









## 2) 설정 테스트

이제 설정이 제대로 됐는지 테스트해봅시다.

domain 패키지를 하나 만들고 `Hello`라는 엔티티를 추가해줍시다.



**Hello**

```java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hello {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hello_id")
    private Long id;

    private String name;

    public Hello(String name) {
        this.name = name;
    }
}
```



**HelloRepository**

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelloRepository extends JpaRepository<Hello, Long>, HelloRepositoryCustom {
}
```



**HelloRepositoryCustom**

```java
public interface HelloRepositoryCustom {

    Hello findByIdCustom(Long id);
}
```



**Yeah24Application**

```java
@SpringBootApplication
public class Yeah24Application {

    public static void main(String[] args) {
        SpringApplication.run(Yeah24Application.class, args);
    }


    @Bean
    public JPAQueryFactory queryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
```

- JPAQueryFactory를 빈으로 등록해줍시다.



**HelloRepositoryImpl**

```java
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.prac.domain.QHello.hello;

@RequiredArgsConstructor
public class HelloRepositoryImpl implements HelloRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    public Hello findByIdCustom(Long id) {
        return queryFactory
                .selectFrom(QHello.hello)
                .where(QHello.hello.id.eq(id))
                .fetchOne();
    }
}
```

- 아마 위의 코드는 빨간줄이 뜰 겁니다.

![10](https://lh4.googleusercontent.com/JkA0-zWQDri12iGUGB_O_ndTzN_P0RwwWjpkzdz4HPlKesSFDQta5HCqz0nQ6243nWhPC4jn1V1bN0vOy7jKCP-IQcwb1aK1JvwWXtMFsem-REts2DtjAyyU3PfJaX4QvLXYlLVi)

- 인텔리제이 우측에 보시면 Gradle 탭을 클릭하고,

  Tasks -> other -> compileQuerydsl을 더블클릭하면 Q타입 클래스가 생성됩니다.

  - 그럼 이제 빨간 줄이 없어졌을 겁니다.







**디렉토리 구조**

![11](https://lh5.googleusercontent.com/QR0abdTrTf4mweQtZTIKQfiyhue0PYArVzH92-kqMxHYqd7Z2rM-wtNo3e7QoPQ_U_RHukNRZwJeJdhNYzw67yVrFaBj1gnQf-UvLyLPgm0FziK8o9yHa3mNKiCoDbH75AtLUkDB)







### 테스트 작성

자 그럼 이제 제대로 작동하는지 테스트를 작성해보겠습니다.

HelloRepository 클래스에 가서 Windows 기준 `Ctrl+Shift+T`를 누르면 테스트 클래스를 쉽게 생성할 수 있습니다.

아니면 그냥 직접 테스트 클래스를 작성해주셔도 됩니다.





**HelloRepositoryTest**

```java
@SpringBootTest
@Transactional
class HelloRepositoryTest {

    @Autowired
    HelloRepository helloRepository;


    @Test
    @Rollback(value = false)
    void saveTest() {
        helloRepository.save(new Hello("name"));
    }
}
```

- 먼저 엔티티가 저장이 잘 되는지 테스트해봅시다.

- 위의 `saveTest()` 메서드를 실행하면,

  ![12](https://lh6.googleusercontent.com/aKOjVDi6ye4sXOjv9Ln0XgoB8R8SBSZz_ohL21uw5L3ZD-cIq90KrYgu8sky3oiDbhYMzcFSP09lHZo4ODXBB1-g5XCC3g5Dhopc946r_SEoKKGha4kPWGiQINPIdFpUxNMkUKUU)

  이렇게 나오면 성공입니다

  - p6spy 덕분에 실제로 insert 쿼리에 들어가는 파라미터 값을 확인할 수 있습니다.

  

- DB에도 가서 직접 확인해 봅시다.

  ![13](https://lh4.googleusercontent.com/FWvQ45heL8keaYO5cbBCoehtUGiFu2bg-PezVkFnkRzmGkjwBqFTsrJS65WeyVEcu9kRGPPAUdYDr2Ww-r5q6Q_mmlSlUyACX37uvEo-AXV0UfK1XOAmz-C14GX30GJcbBjFo-v6)

  잘 저장이 되었습니다.









이제 저장된 엔티티도 잘 불러와지는지 테스트하기 전에 `application.yml`에 가서 ddl-auto 설정을 `update`로 바꿔줍니다.



```java
@Test
void findTest() {
    Hello hello = helloRepository.findByIdCustom(1L);
    System.out.println(hello);
}
```

- 위의 코드를 실행하면, 아래와 같은 결과가 나오면 성공입니다.
- ![14](https://lh3.googleusercontent.com/_Gj8tJItKelIC3ApsDyqBkbSqk9XIa4zB0WnILoi3omWEJmgSjsHgTTvIWFgapFCQBZndP9wcIqfXiiFRTOMF3OAvJGjvesQi6cPwBwFszoY9_nvG5xB5BzClSnMs5wazOvnZjyw)
  - querydsl 쿼리도 잘 나가는 것을 확인할 수 있습니다.





여기까지 결과가 잘 나왔다면 프로젝트 설정이 잘 됐다고 볼 수 있습니다.

그리고 설정이 잘 됐다면 domain 패키지 내에 있는 모든 클래스들은 테스트 용도로 작성했으므로 삭제하겠습니다.