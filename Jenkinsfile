pipeline {
  agent any
  stages {
    stage('load') {
      steps {
        sh 'git branch --set-upstream-to=origin/master master && git pull '
      }
    }
    stage('compile') {
      steps {
        sh 'mvn clean install'
      }
    }
    stage('run') {
      steps {
        sh 'java -jar target/mock-1.0-SNAPSHOT.jar'
      }
    }
  }
}