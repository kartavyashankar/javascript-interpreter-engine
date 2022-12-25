package com.openide.javascriptinterpreterengine.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component("docker-handler")
public class DockerHandler {

    private DockerClient dockerClient;
    private DockerClientConfig config;
    private DockerHttpClient dockerHttpClient;

    public DockerHandler() {
        this.config = getDockerClientConfig();
        this.dockerHttpClient = getDockerHttpClient();
        this.dockerClient = getDockerClient();
    }

    private DockerClientConfig getDockerClientConfig() {
        return DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerCertPath("/certs/client")
                .withDockerHost("tcp://docker:2376")
                .withDockerTlsVerify(true).build();
    }

    private DockerHttpClient getDockerHttpClient() {
        return new ApacheDockerHttpClient.Builder()
                .dockerHost(this.config.getDockerHost())
                .sslConfig(this.config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45)).build();
    }


    private DockerClient getDockerClient() {
        return DockerClientImpl.getInstance(this.config, this.dockerHttpClient);
    }

    private boolean imageExists(String image) {
        List<Image> imageList = getImageList();
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

    public List<Image> getImageList() {
        return dockerClient.listImagesCmd().withShowAll(true).exec();
    }

    public void buildImage(String image) throws InterruptedException, DockerException {
        Set<String> tags = new HashSet<>();
        tags.add(image);

        if(!imageExists("node:18-alpine")) {
            boolean pulled = dockerClient.pullImageCmd("node")
                    .withTag("18-alpine")
                    .exec(new PullImageResultCallback())
                    .awaitCompletion(30, TimeUnit.SECONDS);
            if (!pulled) {
                throw new DockerException("Error occurred while pulling the image", 500);
            }
        }

        boolean built = dockerClient.buildImageCmd()
                .withDockerfilePath("./NodeCustomDockerfile")
                .withTags(tags)
                .withNoCache(true)
                .withPull(true)
                .exec(new BuildImageResultCallback())
                .awaitCompletion(5, TimeUnit.SECONDS);
        if(!built) {
            throw new DockerException("Error occurred while building the image", 500);
        }
    }

    public InspectContainerResponse createContainer(String image) throws InterruptedException, DockerException {
        if(!imageExists(image)) {
            buildImage(image);
        }
        CreateContainerResponse container = dockerClient.createContainerCmd(image)
                .withName("node-js")
                .withWorkingDir("/home/node").exec();
        return dockerClient.inspectContainerCmd(container.getId()).exec();
    }

    public InspectContainerResponse startContainer(InspectContainerResponse container) {
        dockerClient.startContainerCmd(container.getId()).exec();
        return dockerClient.inspectContainerCmd(container.getId()).exec();
    }

    public InspectContainerResponse stopContainer(InspectContainerResponse container) {
        dockerClient.stopContainerCmd(container.getId()).exec();
        return dockerClient.inspectContainerCmd(container.getId()).exec();
    }

    public void removeContainer(InspectContainerResponse container) {
        dockerClient.removeContainerCmd(container.getId()).exec();
    }

    public String pingDaemon() throws IOException {
        DockerHttpClient.Request request = DockerHttpClient.Request.builder()
                .method(DockerHttpClient.Request.Method.GET)
                .path("/_ping").build();
        DockerHttpClient.Response response = dockerHttpClient.execute(request);
        return IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
    }
}
