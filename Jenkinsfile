pipeline {
    agent any
    environment {
        SERVICE_NAME = 'event-receiver'
    }
    stages {
        stage('greeting') {
            steps {
                echo "hello from pipeline: ${SERVICE_NAME}"
            }
        }
        stage('print version') {
            steps {
                env.SERVICE_VERSION = sh script: './gradlew -q printVersion', returnStdout: true
                echo "version: ${SERVICE_VERSION}"
            }
        }
    }
}
