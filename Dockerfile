FROM openjdk:17
COPY ./kubernetes_hosted/azure/AzureApplication/ /tmp
WORKDIR /tmp
ENTRYPOINT ["java","AzureApplication"]