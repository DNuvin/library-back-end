pipeline {
    agent any

    environment {
        IMAGE_NAME = 'dewuruge/library'
        IMAGE_TAG = "${env.GIT_COMMIT.take(7)}"
        DOCKER_CREDENTIALS = 'dockerhub-credentials'
        KUBECONFIG = '/var/lib/jenkins/.kube/config'
    }

    parameters {
        string(name: 'DEPLOY_ENV', defaultValue: 'dev', description: 'Environment to deploy (dev/stg/prod)')
        string(name: 'GIT_BRANCH', defaultValue: 'dev', description: 'Git branch to build')
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: "${params.GIT_BRANCH}", url: 'https://github.com/DNuvin/library-back-end.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Run Tests & Coverage') {
            steps {
                sh './mvnw test jacoco:report'

                // Copy JaCoCo report into resources/static/jacoco so it will be in Docker image
                sh 'mkdir -p src/main/resources/static/jacoco'
                sh 'cp -r target/site/jacoco/* src/main/resources/static/jacoco/'
            }
            post {
                always {
                    publishHTML(target: [
                        reportDir: 'target/site/jacoco',
                        reportFiles: 'index.html',
                        reportName: "Code Coverage - ${params.DEPLOY_ENV}",
                        keepAll: true
                    ])
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
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
                dir("k3s/${params.DEPLOY_ENV}") {
                    sh """
                    kubectl --kubeconfig=${KUBECONFIG} create configmap ${params.DEPLOY_ENV}-config \
                        --from-env-file=config.env -n ${params.DEPLOY_ENV} --dry-run=client -o yaml | kubectl apply -f -

                    kubectl --kubeconfig=${KUBECONFIG} create secret generic ${params.DEPLOY_ENV}-secret \
                        --from-env-file=secret.env -n ${params.DEPLOY_ENV} --dry-run=client -o yaml | kubectl apply -f -
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh """
                kubectl --kubeconfig=${KUBECONFIG} set image deployment/library \
                    library-container=${IMAGE_NAME}:${IMAGE_TAG} -n ${params.DEPLOY_ENV} --record
                """
            }
        }
    }

    post {
        failure {
            echo "Deployment failed, rolling back..."
            sh """
            kubectl --kubeconfig=${KUBECONFIG} rollout undo deployment/library -n ${params.DEPLOY_ENV}
            """
        }
        always {
            cleanWs()
        }
    }
}
