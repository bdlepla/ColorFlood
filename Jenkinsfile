pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh './gradlew assembleDebug'
                archiveArtifacts artifacts: '**/debug/**/*.apk', fingerprint: true 
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
