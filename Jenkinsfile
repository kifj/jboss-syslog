pipeline {
  agent any
  tools {
    jdk 'JDK-21'
    maven 'Maven-3.9'
  }
  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }
    stage('Build & Publish') {
      stages {
        stage('Build') {
          steps {
            sh 'mvn -B clean package'
          }
        }
        stage('Publish') {
          steps {
            sh 'mvn -B deploy site-deploy -DskipTests'
          }
        }
      }
    }
    stage('Sonar') {
      steps {
        sh 'mvn sonar:sonar -DskipTests -Dsonar.java.coveragePlugin=jacoco -Dsonar.jacoco.reportPath=target/jacoco.exec -Dsonar.host.url=https://www.x1/sonar'
      }
    }
  }
  post {
    always {
      recordIssues tools: [spotBugs(pattern: 'target/spotbugsXml.xml')]
    }
  }
}

