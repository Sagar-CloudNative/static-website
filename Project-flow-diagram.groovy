GitHub Repository
       |
       v
Jenkins Pipeline
       |
       |--> Clone Repository (Fetches the code from GitHub)
       |
       |--> Build (Optional - Compress files or prepare assets)
       |
       |--> Deploy to Docker
             |
             |--> Stop and remove existing container (if any)
             |
             |--> Start new Docker container (nginx-server on port 8081)
             |
             |--> Copy files (index.html, styles.css) to container’s web directory
       |
       |--> Notify Slack
             |
             |--> Send success or failure notification to #jenkins-notifications channel
       |
       v
Static Website Available at http://<jenkins-server-ip>:8081
