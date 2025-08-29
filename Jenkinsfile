pipeline {
    agent any

    environment {
        IMAGE_NAME = "library-api"
        IMAGE_TAG = "${env.GIT_COMMIT.take(7)}" // first 7 chars of commit SHA
        REGISTRY = "docker.io/dnuvin"
        EC2_USER = "ec2-user"                // optional: your EC2 user
        EC2_HOST = "<EC2-PUBLIC-IP>"         // optional: your EC2 public IP
    }

    triggers {
        // Optional fallback if webhook fails
        // pollSCM('H/5 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'dev', url: 'https://github.com/DNuvin/library-back-end.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry([credentialsId: 'dockerhub-creds', url: '']) {
                    sh "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker push ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Deploy to EC2 (Optional)') {
            steps {
                sshagent (credentials: ['ec2-ssh-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} << 'EOF'
                            docker pull ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
                            docker stop ${IMAGE_NAME} || true
                            docker rm ${IMAGE_NAME} || true
                            docker run -d --name ${IMAGE_NAME} -p 8080:8080 ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
                        EOF
                    """
                }
            }
        }
    }
}
