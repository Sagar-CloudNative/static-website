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
