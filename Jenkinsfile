pipeline {
    agent any

    environment {
        IMAGE_NAME = 'dewuruge/library'
        IMAGE_TAG = "${env.GIT_COMMIT.take(7)}"  // unique tag based on commit SHA
        DOCKER_CREDENTIALS = 'dockerhub-credentials' // Jenkins credentials ID for Docker Hub
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'dev', url: 'https://github.com/DNuvin/library-back-end.git'
            }
        }

        stage('Build JAR') {
            steps {
                // Build the project using Maven wrapper
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image with unique tag
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Push the image to Docker Hub using credentials
                    docker.withRegistry('', "${DOCKER_CREDENTIALS}") {
                        sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                    }
                }
            }
        }
    }

    post {
        always {
            // Clean workspace after build
            cleanWs()
        }
    }
}
