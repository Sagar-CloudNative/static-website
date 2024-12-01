## Automated CI/CD Pipeline with Jenkins, Docker, and Slack Integration

This project demonstrates a fully automated CI/CD pipeline to deploy a static website using Jenkins and Docker, with Slack notifications for build updates.
---

# Pipeline Overview
1. Clone Repository: Fetch the latest code from the GitHub repository.
2. Optional Build: Perform any build-related tasks (e.g., asset compression).
3. Deploy to Docker: Deploy the static website to an Nginx container.
4. Slack Notification: Notify a Slack channel about the build's success or failure.
--- 

# Prerequisites
1. Jenkins Setup
- Installed and configured Jenkins on your server.
- Necessary plugins installed:
    - Git Plugin
    - Pipeline Plugin
    - Slack Notification Plugin
- Jenkins must have access to:
    - GitHub repository.
    - Docker CLI.
2. Docker Setup
- Docker must be installed and running on the Jenkins server.
- User running Jenkins must have permission to manage Docker.
3. GitHub Repository
- A public or private GitHub repository containing the static website files (e.g., index.html and styles.css).
4. Slack Configuration
- A Slack app created with necessary permissions:
    - chat:write scope.
    - Oauth Token configured in Jenkins.
- A Slack channel to receive notifications.

---

## Pipeline Steps

Here is the Jenkinsfile used in this project:

``` groovy

pipeline {
    agent any

    environment {
        DOCKER_CONTAINER = 'nginx-server'
        DOCKER_VOLUME = '/usr/share/nginx/html'
        SLACK_TOKEN = credentials('slack-token-id') // Replace with Jenkins credential ID
        SLACK_CHANNEL = '#jenkins-notifications'
    }

    stages {
        stage('Clone Repository') {
            steps {
                echo 'Cloning repository...'
                git branch: 'main', url: 'https://github.com/Sagar-CloudNative/static-website.git'
            }
        }

        stage('Optional Build') {
            steps {
                echo 'Optional Build Step (e.g., asset compression)'
                // Add build steps here if required
            }
        }

        stage('Deploy to Docker') {
            steps {
                echo 'Deploying to Docker container...'
                sh """
                docker stop ${DOCKER_CONTAINER} || true
                docker rm ${DOCKER_CONTAINER} || true
                docker run -d --name ${DOCKER_CONTAINER} -p 8081:80 nginx
                docker cp ./index.html ${DOCKER_CONTAINER}:${DOCKER_VOLUME}
                docker cp ./styles.css ${DOCKER_CONTAINER}:${DOCKER_VOLUME}
                """
            }
        }
    }

    post {
        success {
            echo 'Build and deployment successful!'
            slackSend(channel: "${SLACK_CHANNEL}", message: "Build #${env.BUILD_NUMBER} succeeded! Website deployed to http://<server-ip>:8081", token: "${SLACK_TOKEN}")
        }
        failure {
            echo 'Build failed. Check logs for details.'
            slackSend(channel: "${SLACK_CHANNEL}", message: "Build #${env.BUILD_NUMBER} failed. Check Jenkins logs for details.", token: "${SLACK_TOKEN}")
        }
    }
}

```
## Deployment Instructions
1. Clone the GitHub Repository

```bash
git clone https://github.com/Sagar-CloudNative/static-website.git
```

2. Configure Jenkins Pipeline
- Create a new Jenkins pipeline job.
- Add the above Jenkinsfile to the repository or use the Pipeline script from Jenkins.

3. Set Up Slack Integration

- Add the Slack token as a Secret Text credential in Jenkins.
- Replace slack-token-id in the Jenkinsfile with the Credential ID.

4. Run the Pipeline
- Trigger the pipeline manually or set up a webhook for automatic builds.

5. Access the Deployed Website
- Open your browser and navigate to http://<jenkins-server-ip>:8081.

-----

## Troubleshooting Guide
1. Pipeline Fails at Slack Notification
- Check Token Validity:
    - Ensure the Slack token has the required scopes.
- Verify Slack App Configuration:
    - Confirm the app is installed in the workspace and has access to the channel.
2. Docker Commands Fail
- Permissions Issue:
    - Ensure the Jenkins user has Docker permissions. Add it to the Docker group:
```bash
sudo usermod -aG docker jenkins
```
- Container Port Conflict:
    - Ensure port 8081 is not already in use on the server.
3. Website Not Accessible
- Firewall Issues:
    - Ensure port 8081 is open on the server.
- Incorrect Deployment:
    - Verify the files are correctly copied to /usr/share/nginx/html in the container.
4. GitHub Clone Fails
- Authentication Issues:
    - If cloning from a private repository, ensure Jenkins has the correct SSH key or personal access token.

---

