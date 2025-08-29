pipeline {
    agent any

    environment {
        IMAGE_NAME = 'dewuruge/library'
        IMAGE_TAG = "${env.GIT_COMMIT.take(7)}" // short SHA as tag
        DOCKER_CREDENTIALS = 'dockerhub-credentials'
        // Use a Jenkins-dedicated kubeconfig (copy k3s config once, see instructions)
        KUBECONFIG = '/var/lib/jenkins/.kube/config'
        DEPLOY_ENV = 'dev'  // dev/stg/prod
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
                docker.withRegistry('', "${DOCKER_CREDENTIALS}") {
                    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Apply Config & Secrets') {
            steps {
                dir("k3s/${DEPLOY_ENV}") {
                    sh """
                    kubectl create configmap ${DEPLOY_ENV}-config \
                        --from-env-file=config.env -n ${DEPLOY_ENV} --dry-run=client -o yaml | \
                        kubectl apply -f -

                    kubectl create secret generic ${DEPLOY_ENV}-secret \
                        --from-env-file=secret.env -n ${DEPLOY_ENV} --dry-run=client -o yaml | \
                        kubectl apply -f -
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh """
                kubectl set image deployment/library \
                    library-container=${IMAGE_NAME}:${IMAGE_TAG} -n ${DEPLOY_ENV} --record
                """
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
