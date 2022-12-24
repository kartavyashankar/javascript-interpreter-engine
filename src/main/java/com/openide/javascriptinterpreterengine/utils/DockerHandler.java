package com.openide.javascriptinterpreterengine.utils;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class DockerHandler {
    private static DockerClientConfig getDockerClientConfig() {
        return DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerCertPath("/certs/client")
                .withDockerHost("tcp://docker:2376")
                .withDockerTlsVerify(true).build();
    }

    private static DockerHttpClient getDockerHttpClient() {
        DockerClientConfig config = getDockerClientConfig();
        return new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45)).build();
    }


    private static DockerClient getDockerClient() {
        return DockerClientImpl.getInstance(getDockerClientConfig(), getDockerHttpClient());
    }

    public static InspectContainerResponse createContainer(String image) {
        DockerClient dockerClient = getDockerClient();
        CreateContainerResponse container = dockerClient.createContainerCmd(image)
                .withName("node-js")
                .withWorkingDir("/home/node").exec();
        return dockerClient.inspectContainerCmd(container.getId()).exec();
    }

    public static InspectContainerResponse startContainer(InspectContainerResponse container) {
        DockerClient dockerClient = getDockerClient();
        dockerClient.startContainerCmd(container.getId()).exec();
        return dockerClient.inspectContainerCmd(container.getId()).exec();
    }

    public static InspectContainerResponse stopContainer(InspectContainerResponse container) {
        DockerClient dockerClient = getDockerClient();
        dockerClient.stopContainerCmd(container.getId()).exec();
        return dockerClient.inspectContainerCmd(container.getId()).exec();
    }

    public static void removeContainer(InspectContainerResponse container) {
        DockerClient dockerClient = getDockerClient();
        dockerClient.removeContainerCmd(container.getId()).exec();
    }

    public static String pingDaemon() throws IOException {
        DockerHttpClient dockerHttpClient = getDockerHttpClient();
        DockerHttpClient.Request request = DockerHttpClient.Request.builder()
                .method(DockerHttpClient.Request.Method.GET)
                .path("/_ping").build();
        DockerHttpClient.Response response = dockerHttpClient.execute(request);
        return IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
    }
}
