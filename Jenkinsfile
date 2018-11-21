pipeline {
  agent any
  stages {
    stage('compile') {
      steps {
        sh 'mvn clean install'
      }
    }
    stage('') {
      steps {
        sh 'java -jar mock-1.0-SNAPSHOT.jar'
      }
    }
  }
}