pipeline {
  environment {
      registry = 'darylnauman/project2'
      dockerHubCreds = 'docker_hub'
      dockerImage = ''
  }
  agent any
  stages {
    stage('Quality Gate') {
      steps {
          echo 'Quality Gate'     
      }
    }
    stage('Unit Testing') {
      when {
            // anyOf {branch 'ft_*'; branch 'bg_*'}
            branch 'ft_jenkins'
        }
        steps {
            withMaven {
                sh 'mvn test'
            }
            // junit skipPublishingChecks: true, testResults: 'target/surefire-reports/*.xml'
        }
    }
    stage('Build') {
      when {
          // branch 'main'
          branch 'ft_jenkins'
      }
      steps{
          withMaven {
              sh 'mvn /var/lib/jenkins/workspace/project-two_main/recipe-api/pom.xml package -DskipTests'
          }
      }

    }
    stage('Docker Image') {
      when {
          // branch 'main'
          branch 'ft_jenkins'
      }
      steps{
          script {
            echo "$registry:$currentBuild.number"
            dockerImage = docker.build "$registry:$currentBuild.number"
          }
      }

    }
    stage('Docker Deliver') {
      when {
        //   branch 'main'
        branch 'ft_jenkins'
      }
      steps{
          script{
              docker.withRegistry("", dockerHubCreds) {
                dockerImage.push("$currentBuild.number")
                dockerImage.push("latest")
              }
          }
      }
    }
    stage('Wait for approval') {
        when {
            // branch 'main'
            branch 'ft_jenkins'
        }
        steps {
            script {
            try {
                timeout(time: 20, unit: 'MINUTES') {
                    approved = input message: 'Deploy to production?', ok: 'Continue',
                        parameters: [choice(name: 'approved', choices: 'Yes\nNo', description: 'Deploy build to production')]

                    if(approved != 'Yes') {
                        error('Build did not pass approval')
                    }
                }
            } catch(error) {
                error('Build failed because timeout was exceeded');
            }
        }
        }
    }
    stage('Deploy') {
      steps {
          echo 'Deploy'
      }
    }
  }
}