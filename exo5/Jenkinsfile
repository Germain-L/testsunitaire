pipeline {
    agent any

    tools {
        // Install the Maven version required
        maven 'Maven 3.6.3'
        // Install the JDK version required
        jdk 'JDK 21'
    }

    environment {
        // Set the Maven settings if required
        MAVEN_OPTS = '-Xmx1024m'
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the source code from the repository
                git url: 'https://your-repo-url.git', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                // Build the project using Maven
                sh 'mvn clean install'
            }
        }

        stage('Run Tests') {
            steps {
                // Run all tests in the ./exo5 folder/package
                sh 'mvn -Dtest=**/exo5/*Test test'
            }
        }

        stage('Archive Results') {
            steps {
                // Archive the test results and build artifacts
                junit '**/target/surefire-reports/*.xml'
                archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            // Clean up the workspace
            cleanWs()
        }
        success {
            // Send notifications on success
            echo 'Build and tests succeeded.'
        }
        failure {
            // Send notifications on failure
            echo 'Build or tests failed.'
        }
    }
}
