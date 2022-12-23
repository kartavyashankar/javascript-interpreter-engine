package com.openide.javascriptinterpreterengine.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;

public class DockerHandler {
    private static DockerClient getDockerClient() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://docker:2375").build();
        return DockerClientBuilder.getInstance(config).build();
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
}
