pipeline {
    agent any

    options {
        // Stop the build early in case of compile or test failures
        skipStagesAfterUnstable()
    }

    stages {
        stage('Compile') {
            steps {
                sh './gradlew compileDebugSources'
            }
        }
        stage('Unit Test') {
            steps {
                // Compile and run the unit tests for the app and its dependencies
                sh './gradlew testDebugUnitTest testDebugUnitTest'

                // Analyse the test results and update the build result as appropriate
                junit '**/TEST-*.xml'
            }
        }
        stage('Build APK') {
            steps {
                // Finish building and packaging the APK
                sh './gradlew assembleDebug'

                // Archive the APKs so that they can be downloaded from Jenkins
                archiveArtifacts '**/*.apk'
            }
        }
        stage('Static analysis') {
            steps {
                // Run Lint and analyse the results
                sh './gradlew lintDebug'
                androidLint pattern: '**/lint-results-*.xml'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
    post {
        failure {
        // Notify developer team of the failure
        mail to: 'android-devs@example.com', subject: 'Oops!', body: "Build ${env.BUILD_NUMBER} failed; ${env.BUILD_URL}"
        }
    }
}
