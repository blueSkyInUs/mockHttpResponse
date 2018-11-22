pipeline {
  agent any
  stages {
    stage('pull') {
      steps {
        git(url: 'git@github.com:blueSkyInUs/mockHttpResponse.git', branch: 'master', changelog: true)
      }
    }
    stage('compile') {
      steps {
        sh 'mvn clean install'
      }
    }
    stage('run') {
      steps {
        sh 'java -jar1 target/mock-1.0-SNAPSHOT.jar'
      }
    }
  }
}