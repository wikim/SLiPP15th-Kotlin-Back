SLiPP15th 백엔드 개발 템플릿
==============================

# ● 상용화 금지

## ◎ 사용된 라이브러리
□  [Gradle](http://gradle.org/): 2.10
> Groovy를 DSL로 사용하는 스크립트형 빌드툴
* [Gradle User Guide](http://gradle.org/docs/current/userguide/userguide.html)
* [Gradle DSL Reference](http://gradle.org/docs/current/dsl/index.html)


□  [Spring Boot](http://projects.spring.io/spring-boot/): 1.3.4.RELEASE
* [Spring Boot Reference Guide](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#getting-started-installing-the-cli)

□  [h2datbase](http://h2database.com): 1.4.X
> File or In-memory를 지원하는 데이터베이스

□  [HikariCP](https://brettwooldridge.github.io/HikariCP/): 2.4.3
>  high-performance JDBC connection pool.

□  [QueryDSL](http://www.querydsl.com): 3.7.0
> XML 혹은 문자열로 작성하는 쿼리 대신 자바메서드를 통해 작성하는 쿼리

□  [Lombok](http://projectlombok.org/): 1.16.6
> Java 객체에서 생성하는 Getter/Setter 등의 메서드를 손쉽게 추가할 수 있는 Annotation을 컴파일 단계에서 추가해주는 라이브러리

## ◎ Spring Boot
스프링부트[Spring Boot](http://projects.spring.io/spring-boot/)를 기반으로 한 Application을 구성한다.
> 스프링부트는 스프링기반의 단독실행되는 애플리케이션을 손쉽게 생성할 수 있다. 스프링 설정을 최소화하여 빠르게
시작할 수 있다.

### □ 기능
* 단독실행Stand-alone형 스프링 애플리케이션 생성
* 내장형 톰캣 혹은 제티 실행(WAR 빌드가 필요없음)
* 간결한 메이븐 설정을 위한 'starter' POM을 제공
* 스프링 설정에 대한 자동화
* 출시를 위한 측정, 상태 점검 및 외부화된 구성(파라메터로 설정값을 변경가능)
* XML 코드를 사용하지 않고 설정가능

