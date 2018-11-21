pipeline {
  agent any
  stages {
    stage('update') {
      steps {
        sh 'git checkout master'
      }
    }
    stage('compile') {
      steps {
        sh 'mvn clean install'
      }
    }
    stage('') {
      steps {
        sh 'java -jar target/mock-1.0-SNAPSHOT.jar'
      }
    }
  }
}