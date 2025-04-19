# Redis with Spring Boot


[![My Skills](https://skillicons.dev/icons?i=azure,java,redis,kubernetes,docker,spring)](https://skillicons.dev)

Benefitis of Redis:

- Agility, which positively impacts the customer experience;
- Useful for recommendation systems;
- Integrates with many programming languages ​​and frameworks;
- Data replication;
- Uninterrupted service;
- Reduction of financial waste due to the reduction of unnecessary requests to the microservice or database, located in the provider's cloud.
    <i>
        
    "Many applications use backend databases, such as SQL Server, when applications require frequent access to data. The cost of maintaining these backend services to cope with demand can be high, but you can use an effective caching strategy to reduce load on backend databases by reducing sizing and scaling requirements. This can help you reduce costs and improve the performance of your applications. Caching is a useful technique to save on costs related to read heavy workloads that use more expensive resources such as SQL Server. (https://docs.aws.amazon.com/prescriptive-guidance/latest/optimize-costs-microsoft-workloads/net-caching.html)"
                                
    </i> 

Essential reading:

- [Best practices development](https://learn.microsoft.com/pt-br/azure/azure-cache-for-redis/cache-best-practices-development)
  
Use examples images (Microsoft, Azure):

![exemplo](imgs/exemplo.png)

![exemplo](imgs/exemplo2.PNG)


- https://azure.microsoft.com/en-us/pricing/calculator/
- https://azure.microsoft.com/en-us/pricing/details/azure-sql-database/single/
- https://calculator.aws/#/
- https://aws.amazon.com/pt/rds/pricing/

## 🔗 Links
[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://br.linkedin.com/in/luiza-andrade-ti/)  [![git](https://img.shields.io/badge/github-000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/luizaandradeti/) 




- [Redis with Spring Boot](#redis-with-spring-boot)
- [Tech Stack](#tech-stack--)
    * [Redis](#redis)
    * [Workflow](#workflow-for-pull-requests-)
- [Hands on Redis](#hands-on-redis)
    * [Redis with Spring](#redis-with-spring)
        + [Run Redis Docker](#run-redis-docker)
        + [Access](#access)
        + [Connection betwen Spring and Redis](#connection-betwen-spring-and-redis)
        + [Create a Redis class in the Java application](#create-a-redis-class-in-the-java-application)
        + [Cache enable](#cache-enable)
        + [Results](#results)
- [TO DO - Deploy in Azure Kubernetes](#to-do---deploy-in-azure-kubernetes)
    * [Azure cache for Redis](#azure-cache-for-redis)
        + [Connecting to azure cloud via azure cli](#run-the-bash-install_redis-below) 
        + [Create a service Redis into Azure](#create-a-redis-azure-)
        + [Connect Redis Azure For Redis with Java](#connect-azure-cache-for-redis-with-java-setup-the-test-in-main-class-)
    * [Docker build](#docker-build)

> [!NOTE] 
> The context of these personal studies is strictly technical. There is no interest whatsoever in addressing political and religious issues.
> Please do not use any terms out of context.


## Tech Stack

**Acknowledges:**
- Spring Boot/Java
- Docker (https://docs.docker.com/get-docker/) 
- SQL

**Client tools:**
- Postman
- Azure account 

**Development tools :** 
- Java 17
- IntelliJ
- Docker
- Eksctl


## Redis 

<i>"Redis can be used as a database, cache, streaming engine, message broker, and more. The following quick start guides will show you how to use Redis for the following specific purposes:

Data structure store
Document database
Vector database." </i>

Redis can be used with lists, sets, maps, strings e others. 
https://redis.io/docs/latest/develop/get-started/

## Hands on Redis
Create an application using [Spring Initializr](https://start.spring.io/),

As per picture below:

![Spring Initializr](imgs/spring_initializr.jpeg)


[Docker](https://hub.docker.com/_/redis)
[mvn](https://mvnrepository.com/artifact/com.github.javafaker/javafaker/1.0.2)


## Redis with Spring

![Resume](imgs/classes.png)

### Run Redis Docker

Run docker redis in prompt

```bash
docker pull redis:8.0-M04-alpine
docker run -d -p 6379:6379 --name redis-local redis:8.0-M04-alpine
```


### Access
Turn on telnet:
![telnet](imgs/telnet.png)

Test connectivity in git bash: 

```bash
telnet localhost 6379
```
### Connection betwen Spring and Redis
Set environment variables:

Set TTL in the application.properties

```properties
spring.application.name=azure
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

### Create a Redis class in the Java application

```java
package com.kubernetes_hosted.azure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableCaching
@EnableScheduling
public class RedisSettings{
    public static final String KEYS_ARTICLES = "articles";
    @CacheEvict(allEntries = true, value = KEYS_ARTICLES)
    @Scheduled(fixedDelayString = "${cache.ms.ttl}")
    public void removeCache() {
        log.info("Cache reset");
    }
    @Bean
    public RedisCacheConfiguration cacheSettings() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
```

Set TTL in the application.properties 

```properties
spring.application.name=azure
spring.data.redis.host=localhost
spring.data.redis.port=6379
# clear cache in 70000ms
cache.ms.ttl=70000
```

### Cache enable

Note that cache memory has been included with the @Cacheable annotation

![Redis](imgs/classes2.png)

```java
package com.kubernetes_hosted.azure.repositorys;

import java.util.ArrayList;
import java.util.List;
import com.github.javafaker.Faker;
import com.kubernetes_hosted.azure.entitys.Articles;
import jakarta.annotation.PostConstruct;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import static com.kubernetes_hosted.azure.RedisSettings.KEYS_ARTICLES;

@Component
public class ArticlesRepository {
    // tool intended for simulation, use only in test environment,
    //  do not use in production environment
    private final Faker DATAMOCK = new Faker();
    private final List<Articles> BASEDB = new ArrayList<>();
    private static final int SIZE_ARTICLES_DB = 95;

    @PostConstruct
    public void config() {
        for (int i = 0; i < SIZE_ARTICLES_DB; i++) {
            BASEDB.add(Articles.builder()
                    .from(DATAMOCK.name().fullName())
                    .content(DATAMOCK.lorem().characters(870, 8_000))
                    .title(DATAMOCK.lorem().characters(8, 19))
                    .build());
        }    }

    @Cacheable(value = KEYS_ARTICLES)
    public List<Articles> findAll() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return BASEDB;
    }
}
```
### Results

Using postman, test the localhost:8080 endpoint several times.

Note that the time will decrease with each HTTP GET request, this is only possible due to cache memory.

I hope this documentation is useful! 

![Redis](imgs/1.png)

![Redis](imgs/2.png)

![Redis](imgs/3.png)


### TO DO - Deploy in Azure Kubernetes

[![Generic badge](https://img.shields.io/badge/status-developing-yellow.svg)](/#/)

https://azure.microsoft.com/pt-br/pricing/purchase-options/azure-account

## Connecting to the Azure Cloud via Azure CLI

**Install Azure Cli:**

```ps1
# Azure Cli simple commands
winget install --exact --id Microsoft.AzureCLI
az login
az upgrade

# Azure Powershell 
# Version query
$PSVersionTable.PSVersion

# Install Azure Powershell Module
Install-Module -Name Az -Repository PSGallery -Force
```

https://learn.microsoft.com/pt-br/cli/azure/install-azure-cli-windows?pivots=winget

The official documentation is excellent for understanding the following commands. Read it.
![doc](imgs/doc.png)
Let's continue! 
https://learn.microsoft.com/pt-br/cli/azure/get-started-tutorial-1-prepare-environment?tabs=bash

**Connect:**
```ps1
# Connect
Connect-AzAccount -UseDeviceAuthentication

# Create Resource Group 
New-AzResourceGroup -Name RecGroupAKSCLI -Location "BrazilSouth"
```

With the credentials provisioned by the cloud administrator, sign in to your Microsoft account.
![logar](imgs/logar.png)
If you don't have access yet, or if you lose it, you can easily recover this information by asking your technology administrator authorized, who will retrieve it via Entra ID.
You can also create an account with a free trial period if you're just learning. Azure provides that!

https://learn.microsoft.com/pt-br/entra/fundamentals/users-reset-password-azure-portal
![recover1](imgs/recovery.png)

![recover](imgs/rec1.png)

https://learn.microsoft.com/pt-br/microsoft-365/troubleshoot/sign-in/forgot-sign-in-password
https://azure.microsoft.com/en-us/pricing/purchase-options/azure-account
![create](imgs/account-create.png)

### Azure cache for Redis

To install Redis on Windows, you'll first need to enable WSL2 (Windows Subsystem for Linux). WSL2 lets you run Linux binaries natively on Windows. For this method to work, you'll need to be running Windows 10 version 2004 and higher or Windows 11. (https://redis.io/docs/latest/operate/oss_and_stack/install/install-redis/install-redis-on-windows/)

#### Run the bash install_redis below
```
📦kubernetes-redis-java
 ┣ 📂imgs
 ┣ 📂src
 ┣ 📜acr.azcli
 ┣ 📜Dockerfile
 ┣ 📜install_redis.sh
 ┣ 📜mvnw
 ┣ 📜mvnw.cmd
 ┣ 📜pom.xml
 ┗ 📜readme.md
```


![Redis site](imgs/redis1.png)

#### Create a Redis Azure ⚡

Create a Redis Azure
![Redis site](imgs/redis3.png)

The creation time is exactly the same as drinking a coffee. Wait a few minutes...
![Redis site](imgs/redis4.png)



![Redis site](imgs/redis5.png)
#### Connect Azure Cache for Redis with Java, setup the test in main class ⏬

```java
package com.kubernetes_hosted.azure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootApplication
public class AzureApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(AzureApplication.class);

	@Autowired
	private StringRedisTemplate template;

	public static void main(String[] args) {
		SpringApplication.run(AzureApplication.class, args);
	}

	@Override
	public void run(String... args) {
		ValueOperations<String, String> ops = this.template.opsForValue();
		String key = "testkeyaccess";
		if(!this.template.hasKey(key)){
			ops.set(key, "Hello World!!!!!!!!!!!!!!!!!!");
			LOGGER.info("Add a key is OK");
		}
		LOGGER.info("Please system, return the value from the cache, thanks! Where is? ... {}", ops.get(key));
	}
}
``` 

````properties
spring.application.name=azure
##---------------redis-----------------
spring.data.redis.host=<your-redis-name>.redis.cache.windows.net
spring.data.redis.port=6380
spring.data.redis.password==
spring.data.redis.ssl.enabled=true
# clear cache in 70000ms
cache.ms.ttl=70000
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
````

## Docker build
````ps1
az login
az acr create --resource-group RecGroupAKSCLI --name containerappwebredis --sku Basic
docker build -t apiweb .
docker images
docker container ls
docker run -d -p 8080:8080 apiweb
docker stop 
az acr login --name containerappwebredis
docker tag apiweb containerappwebredis.azurecr.io/apiweb
docker push containerappwebredis.azurecr.io/apiweb
docker pull containerappwebredis.azurecr.io/apiweb:latest
docker run -d -p 8080:8080 containerappwebredis.azurecr.io/apiweb:latest
````

![acr](imgs/acr.png)

![acr](imgs/acr2.png)

![create](imgs/create.png)
![gui](imgs/creategui.png)

- https://learn.microsoft.com/pt-br/azure/well-architected/operational-excellence/infrastructure-as-code-design
- https://learn.microsoft.com/en-us/azure/well-architected/operational-excellence/infrastructure-as-code-design

![example](imgs/example.png)
![pipe](imgs/pipe_.png)
- https://blog.trendmicro.com.br/tornando-pipeline-de-iac-mais-seguro/


**Read more (further reading):**
- Plans: https://azure.microsoft.com/en-us/support/plans
- Personal x Business plan: https://support.microsoft.com/en-us/office/what-s-the-difference-between-microsoft-365-plans-for-home-or-business-31c36a5d-a13d-4b7d-9b1f-2076accbeeab

- Entra ID: https://www.microsoft.com/en-us/security/business/identity-access/microsoft-entra-id (Azure Active Directory is now Microsoft Entra ID.)
  
- https://learn.microsoft.com/en-us/azure/azure-resource-manager/management/azure-subscription-service-limits

- ACR: https://azure.microsoft.com/en-us/products/container-registry


### Azure AKS start

#### AKS variables
### Workflow for Pull requests:

Linux
```bash
export RANDOM_ID="$(openssl rand -hex 3)"
export MY_RESOURCE_GROUP_NAME="myRecGroupCluster$RANDOM_ID"
export REGION="brazilsouth"
export MY_AKS_CLUSTER_NAME="myAKSCluster$RANDOM_ID"
export MY_DNS_LABEL="mydnslabel$RANDOM_ID"

```

Windows
```bash
set MY_RESOURCE_GROUP_NAME="myRecGroupClusterXyz
set REGION="brazilsouth"
set MY_AKS_CLUSTER_NAME="myAKSClusterXyz"
set MY_DNS_LABEL="mydnslabelXyz
```

Create AKS Cluster

```bash
az group create --name $_RESOURCE_GROUP_NAME --location $REGION
az aks create --name $_AKS_CLUSTER_NAME  --node-count 1  --generate-ssh-keys
```


>[!TIP]
>
>Azure plugin

![Plugin](imgs/plugin.png)

### Workflow 

![workflow](imgs/azure.png)

>[!NOTE] 
>
> Official documentation:
> 
> https://learn.microsoft.com/en-us/azure/aks/learn/quick-kubernetes-deploy-portal?tabs=azure-

![workflow](imgs/workflow.png)

![workflow](imgs/openproject-org-git-workflow.png)




-  https://docs.github.com/en/actions/managing-workflow-runs-and-deployments/managing-deployments/managing-environments-for-deployment
-  https://www.reddit.com/
-  https://azure.microsoft.com/en-us/products/deployment-environments
-  https://docs.github.com/pt/actions/use-cases-and-examples/deploying/deploying-to-azure-kubernetes-service
-  https://learn.microsoft.com/en-us/azure/aks/aksarc/overview
-  https://www.openproject.org/docs/development/git-workflow/



----------------------------------------------------------------------------------------------------------------------








Status: Still in development
