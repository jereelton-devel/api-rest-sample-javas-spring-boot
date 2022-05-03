# api-rest-sample-javas-spring-boot
This project should be used with receive-request-response project join.

# Resources And Dependencies

* Java 8 (SDK - java-8-openjdk-amd64K)
* Spring Boot 2.6.2
* Maven Project
* Lombok
* Spring Data JPA
* Mysql Driver
* Spring Rest Docs
* Spring Web
* Rest Repositories
* Swagger (OpenAPI 3)

<h4>Details</h4>
<pre>
JDK
$ javac -version 
javac 11.0.13

JRE
$ java -version
openjdk version "11.0.13" 2021-10-19
OpenJDK Runtime Environment (build 11.0.13+8-Ubuntu-0ubuntu1.20.04)
OpenJDK 64-Bit Server VM (build 11.0.13+8-Ubuntu-0ubuntu1.20.04, mixed mode, sharing)

</pre>

Please, for more details see the pom.xml and spring-io.info files (root of the project)

> Other Details

Spring Initializr https://start.spring.io/

IntelliJ IDEA CE https://www.jetbrains.com/pt-br/idea/download/#section=linux

---------------------------------------------------------------------------------------------------

# How To Use

---------------------------------------------------------------------------------------------------

# Maven Commands (jar generate)

1. mvn package
2. mvn clean compile assembly:single
3. mvn clean
4. mvn clean install
5. mvn clean package spring-boot:repackage

---------------------------------------------------------------------------------------------------

# OpenAPI

> The openAPI(Swagger) is available to query and help understand the application ZipCode REST API

* http://localhost:8081/api-docs/
* http://localhost:8081/api-docs.yaml
* http://localhost:8081/swagger-ui.html

---------------------------------------------------------------------------------------------------

# Tests

<pre>
ApiRestSampleApplicationTests()
</pre>

<pre>
public void whenCorrectRequestToCreateCustomer_RetrieveCustomerCreated_201();
public void whenCorrectRequestToCreateCustomerAlreadyExists_RetrieveCustomerFound_302();
public void whenMissingBodyRequestToCreateCustomer_RetrieveBadRequest_400();
public void whenRequestCreateCustomerWithInvalidAuthorization_RetrieveUnauthorized_401();
public void whenCorrectRequestToCreateCustomer_RetrieveServerError_500();
public void whenCorrectRequestToReadAllCustomers_RetrieveAllCustomersFromDatabase_200();
public void whenRequestReadAllCustomersWithInvalidAuthorization_RetrieveUnauthorized_401();
public void whenCorrectRequestToReadAllCustomersButNotExistsAny_RetrieveNotFound_404();
public void whenCorrectRequestToReadAllCustomersButServerError_RetrieveServerError_500();
public void whenCorrectRequestToReadCustomer_RetrieveCustomerDetails_200();
public void whenRequestReadCustomerWithInvalidAuthorization_RetrieveUnauthorized_401();
public void whenCorrectRequestToReadCustomerButNotExists_RetrieveNotFound_404();
public void whenCorrectRequestToReadCustomerButServerError_RetrieveServerError_500();
public void whenCorrectRequestToUpdateCustomer_RetrieveCustomerUpdated_200();
public void whenMissingBodyRequestToUpdateCustomer_RetrieveBadRequest_400();
public void whenRequestUpdateCustomerWithInvalidAuthorization_RetrieveUnauthorized_401();
public void whenRequestUpdateCustomerButNotExists_RetrieveNotFound_404();
public void whenRequestUpdateCustomerWithInvalidBodySize_RetrieveNotAcceptable_406();
public void whenCorrectRequestToUpdateCustomerButServerError_RetrieveServerError_500();
public void whenCorrectRequestToDeleteCustomer_RetrieveOK_200();
public void whenRequestDeleteCustomerWithInvalidAuthorization_RetrieveUnauthorized_401();
public void whenCorrectRequestToDeleteCustomerButNotExists_RetrieveNotFound_404();
public void whenCorrectRequestToDeleteCustomerButServerError_RetrieveServerError_500();
public void whenCorrectRequestToPatchCustomer_RetrieveCustomerUpdated_200();
public void whenMissingBodyRequestToPatchCustomer_RetrieveBadRequest_400();
public void whenRequestPatchCustomerWithInvalidAuthorization_RetrieveUnauthorized_401();
public void whenRequestPatchCustomerButNotExists_RetrieveNotFound_404();
public void whenRequestPatchCustomerWithInvalidBodySize_RetrieveNotAcceptable_406();
public void whenCorrectRequestToPatchCustomerButServerError_RetrieveServerError_500();
public void whenIsGetAndInvalidRequestUriAnd_RetrieveNotAllowed_405();
public void whenIsPostAndInvalidRequestUriAnd_RetrieveNotAllowed_405();
public void whenIsPutAndInvalidRequestUriAnd_RetrieveNotAllowed_405();
public void whenIsDeleteAndInvalidRequestUriAnd_RetrieveNotAllowed_405();
public void whenIsPatchAndInvalidRequestUriAnd_RetrieveNotAllowed_405();
public void whenIsHeadAndInvalidRequestUriAnd_RetrieveNotAllowed_405();
public void whenIsOptionsAndInvalidRequestUriAnd_RetrieveNotAllowed_405();
</pre>