pipeline {
  agent any
  tools {
    maven 'Maven-3.9'
  }
  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }
    stage('Build & Publish') {
      agent {
        docker {
          image 'registry.x1/j7beck/x1-maven3:jdk-1.8.0'
          args '-v $HOME/.m2/repository:/var/lib/jenkins/.m2/repository'
	  reuseNode true
        }
      }
      stages {
        stage('Build') {
          steps {
            sh '$MAVEN_HOME/bin/mvn -B clean package'
          }
        }
        stage('Publish') {
          steps {
            sh '$MAVEN_HOME/bin/mvn -B deploy site-deploy -DskipTests'
          }
        }
      }
    }
    stage('Sonar') {
      tools {
        jdk 'JDK-21'
      }
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

