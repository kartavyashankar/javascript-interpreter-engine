package com.openide.javascriptinterpreterengine.utils;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component("docker-handler")
public class DockerHandler {

    private DockerClient dockerClient;

    public DockerHandler() {
        this.dockerClient = getDockerClient();
    }

    private DockerClientConfig getDockerClientConfig() {
        return DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerCertPath("/certs/client")
                .withDockerHost("tcp://docker:2376")
                .withDockerTlsVerify(true).build();
    }

    private DockerHttpClient getDockerHttpClient() {
        DockerClientConfig config = getDockerClientConfig();
        return new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45)).build();
    }


    private DockerClient getDockerClient() {
        return DockerClientImpl.getInstance(getDockerClientConfig(), getDockerHttpClient());
    }

    private boolean imageExists(String image) {
        DockerClient dockerClient = getDockerClient();
        List<Image> imageList = dockerClient.listImagesCmd().withShowAll(true).exec();
        for(Image currentImage : imageList) {
            List<String> imageTags = new ArrayList<>(List.of(currentImage.getRepoTags()));
            for(String tag : imageTags) {
                if(tag.equals(image)) {
                    return true;
                }
            }
        }
        return false;
    }

    public InspectContainerResponse createContainer(String image) {
        if(!imageExists(image)) {

        }
        DockerClient dockerClient = getDockerClient();
        CreateContainerResponse container = dockerClient.createContainerCmd(image)
                .withName("node-js")
                .withWorkingDir("/home/node").exec();
        return dockerClient.inspectContainerCmd(container.getId()).exec();
    }

    public InspectContainerResponse startContainer(InspectContainerResponse container) {
        DockerClient dockerClient = getDockerClient();
        dockerClient.startContainerCmd(container.getId()).exec();
        return dockerClient.inspectContainerCmd(container.getId()).exec();
    }

    public InspectContainerResponse stopContainer(InspectContainerResponse container) {
        DockerClient dockerClient = getDockerClient();
        dockerClient.stopContainerCmd(container.getId()).exec();
        return dockerClient.inspectContainerCmd(container.getId()).exec();
    }

    public void removeContainer(InspectContainerResponse container) {
        DockerClient dockerClient = getDockerClient();
        dockerClient.removeContainerCmd(container.getId()).exec();
    }

    public String pingDaemon() throws IOException {
        DockerHttpClient dockerHttpClient = getDockerHttpClient();
        DockerHttpClient.Request request = DockerHttpClient.Request.builder()
                .method(DockerHttpClient.Request.Method.GET)
                .path("/_ping").build();
        DockerHttpClient.Response response = dockerHttpClient.execute(request);
        return IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
    }
}
