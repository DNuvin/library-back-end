pipeline {
    agent any

    environment {
        IMAGE_NAME = 'dewuruge/library'
        IMAGE_TAG = "${env.GIT_COMMIT.take(7)}" // short SHA as tag
        DOCKER_CREDENTIALS = 'dockerhub-credentials'
        KUBECONFIG = '/etc/rancher/k3s/k3s.yaml' // adjust path if different
        DEPLOY_ENV = 'dev'  // can parametrize this for dev/stg/prod
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
                script {
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('', "${DOCKER_CREDENTIALS}") {
                        sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                    }
                }
            }
        }

        stage('Apply Config & Secrets') {
            steps {
                script {
                    dir("k3s/${DEPLOY_ENV}") {
                        sh """
                        sudo kubectl --kubeconfig=${KUBECONFIG} create configmap ${DEPLOY_ENV}-config \
                            --from-env-file=config.env -n dev --dry-run=client -o yaml | kubectl apply -f -

                        sudo kubectl --kubeconfig=${KUBECONFIG} create secret generic ${DEPLOY_ENV}-secret \
                            --from-env-file=secret.env -n dev --dry-run=client -o yaml | kubectl apply -f -
                        """
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh """
                    sudo kubectl --kubeconfig=${KUBECONFIG} set image deployment/library \
                        library-container=${IMAGE_NAME}:${IMAGE_TAG} -n ${DEPLOY_ENV} --record
                    """
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
