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
                GOOGLE_APPLICATION_CREDENTIALS = credentials('JENKINS_SERVICE_ACC_KEY')
                GCP_PROJECT_ID = 'single-system-dev'
            }
            steps {
                script {
                    sh "gcloud auth activate-service-account jenkins-dev"
                        + "--key-file=$GOOGLE_APPLICATION_CREDENTIALS --project=$GCP_PROJECT_ID"
                    echo "TODO: implement helm deployment here"
                }
            }
        }
    }
}
