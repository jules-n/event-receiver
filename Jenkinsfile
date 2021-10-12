pipeline {
    agent any
    environment {
        SERVICE_NAME = 'event-receiver'
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
                GOOGLE_APPLICATION_CREDENTIALS = credentials('jenkins-single-system-dev-SA-KEY-a8f12ceb3ab2')
                GCP_PROJECT_ID = 'single-system-dev'
            }
            steps {
                script {
                    sh "gcloud auth activate-service-account "
                        + "--key-file=$GOOGLE_APPLICATION_CREDENTIALS --project=$PROJECT_ID"
                    echo "TODO: implement helm deployment here (version = ${SERVICE_VERSION})"
                }
            }
        }
    }
}
