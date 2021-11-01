pipeline {
    agent any
    environment {
        SERVICE_NAME = 'event-receiver'
        SS_DEV_ARTIFACTORY_USERNAME = credentials("jenkins-artifactory-username")
        SS_DEV_ARTIFACTORY_PASSWORD = credentials("jenkins-artifactory-password")
    }
    stages {
        stage('init') {
            steps {
                script {
                    env.SERVICE_VERSION = sh script: './gradlew -q printVersion', returnStdout: true
                    echo "version: ${SERVICE_VERSION}"
                }
            }
        }
        stage('build') {
            steps {
                sh './gradlew build -x test -x integrationTest'
            }
        }
        stage('unit test') {
            steps {
                sh './gradlew test'
            }
        }
        stage('integration test') {
            steps {
                sh './gradlew integrationTest'
            }
        }
        stage('deploy') {
            environment {
                GCP_PROJECT_ID = 'single-system-dev'
            }
            steps {
                script {
                    withCredentials([file(credentialsId: 'jenkins-service-account-key-file', variable: 'GC_KEY')]) {
                        sh "gcloud auth activate-service-account jenkins-dev@single-system-dev.iam.gserviceaccount.com --key-file=${GC_KEY}"
                    }
                    echo "TODO: implement helm deployment here"
                }
            }
        }
    }
}
