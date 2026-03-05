# Benefícios API

API REST para gerenciamento de benefícios corporativos.

Aplicação composta por: - Backend **Spring Boot** - Módulo **EJB**
executando no **WildFly** - Banco **H2 (em memória)**

------------------------------------------------------------------------

## 📌 Requisitos

-   **Java 17**
-   **Maven 3.9+**
-   **WildFly 26+** (ou compatível com Jakarta EE 9+)

Verificar versão do Java:

``` bash
java -version
```

------------------------------------------------------------------------

## ⚙️ Build do Projeto

Na raiz do projeto:

``` bash
mvn clean install
```

Isso irá gerar:

-   `ejb-module/target/ejb-module.jar`
-   `backend/target/backend.jar`

------------------------------------------------------------------------

## 🚀 Deploy do EJB no WildFly

### 1️⃣ Iniciar o WildFly

Linux/Mac:

``` bash
$WILDFLY_HOME/bin/standalone.sh
```

Windows:

``` bash
standalone.bat
```

------------------------------------------------------------------------

### 2️⃣ Fazer deploy do módulo EJB

Copiar o JAR gerado para:

    $WILDFLY_HOME/standalone/deployments/

Ou via CLI:

``` bash
$WILDFLY_HOME/bin/jboss-cli.sh --connect
deploy ejb-module/target/ejb-module.jar
```

Verifique no log do WildFly se o deploy foi realizado com sucesso.

------------------------------------------------------------------------

## ▶️ Executar o Backend Spring Boot

Na pasta `backend`:

``` bash
mvn spring-boot:run
```

ou

``` bash
java -jar target/backend.jar
```

A aplicação irá subir em:

    http://localhost:8080

------------------------------------------------------------------------

## 🗄 Banco H2

Banco em memória configurado como:

    jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE

Se habilitado, o console pode ser acessado em:

    http://localhost:8080/h2-console

------------------------------------------------------------------------

## 📘 Swagger

Documentação da API:

    http://localhost:8080/swagger-ui/index.html

------------------------------------------------------------------------

## 📡 Endpoint Base

    http://localhost:8080/api/v1/beneficios

------------------------------------------------------------------------

## 🛑 Ordem Correta de Execução

1.  Subir **WildFly**
2.  Fazer deploy do **EJB**
3.  Subir **Spring Boot**