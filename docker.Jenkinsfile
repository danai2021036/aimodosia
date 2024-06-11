pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '30'))
    }

    environment {
        EMAIL_TO = "it2021077@hua.gr"
        DOCKER_TOKEN = credentials('docker-push-secret')
        DOCKER_USER = 'nafsikap'
        DOCKER_SERVER = 'ghcr.io'
        DOCKER_PREFIX = 'ghcr.io/nafsikap/ds-spring'
        SENDGRID_KEY = credentials('SENDGRID_KEY')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'api', url: 'git@github.com:danai2021036/aimodosia.git'
            }
        }
//        stage('Replace SendGrid Key') {
//            steps {
//                sh '''
//                    sed -i 's/app.sendgrid.key=.*/app.sendgrid.key=${SENDGRID_KEY}/' ~/workspace/spring-aimodosia/src/main/resources/application.properties
//                '''
//            }
//        }
        stage('Replace SendGrid Key') {
            steps {
                sh '''
                    echo "Replacing SendGrid Key..."
                    sed -i "s|app.sendgrid.key=.*|app.sendgrid.key=${SENDGRID_KEY}|" ~/workspace/spring-aimodosia/src/main/resources/application.properties
                    echo "Replacement done. Verifying..."
                    grep "app.sendgrid.key=" ~/workspace/spring-aimodosia/src/main/resources/application.properties
                '''
            }
        }
//        stage('Test') {
//            steps {
//                sh 'chmod +x ./mvnw'
//            }
//        }
        stage('run ansible pipeline') {
            steps {
                build job: 'ansible-aimodosia'
            }
        }
        stage('Install project with docker compose') {
            steps {
                sh '''
                            sed -i 's/dbserver/51.103.251.219/g' ~/workspace/ansible-aimodosia/host_vars/appserver-vm.yaml
                            export ANSIBLE_CONFIG=~/workspace/ansible-aimodosia/ansible.cfg
                            ansible-playbook -i ~/workspace/ansible-aimodosia/hosts.yaml -l appserver-vm ~/workspace/ansible-aimodosia/playbooks/spring-vue-docker.yaml
                        '''
            }
        }
        stage('Docker build and push') {
            steps {
                sh '''
                    HEAD_COMMIT=$(git rev-parse --short HEAD)
                    TAG=$HEAD_COMMIT-$BUILD_ID
                    docker build --rm -t $DOCKER_PREFIX:$TAG -t $DOCKER_PREFIX:latest  -f nonroot.Dockerfile .
                    echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                    docker push $DOCKER_PREFIX --all-tags
                '''
            }
        }

    }

//    post {
//        always {
//            mail  to: "tsadimas@hua.gr", body: "Project ${env.JOB_NAME} <br>, Build status ${currentBuild.currentResult} <br> Build Number: ${env.BUILD_NUMBER} <br> Build URL: ${env.BUILD_URL}", subject: "JENKINS: Project name -> ${env.JOB_NAME}, Build -> ${currentBuild.currentResult}"
//        }
//    }
}