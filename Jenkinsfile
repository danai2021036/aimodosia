pipeline {
    agent any

    environment {
        EMAIL_TO = "it2021077@hua.gr"
        SENDGRID_KEY = credentials('SENDGRID_KEY')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'api', url: 'git@github.com:danai2021036/aimodosia.git'
            }
        }

        stage('Test') {
            steps {
                sh 'chmod +x ./mvnw && ./mvnw test'
            }
        }
        stage('run ansible pipeline') {
            steps {
                build job: 'ansible-aimodosia'
            }
        }
        //einai peritto
        stage('install ansible prerequisites') {
            steps {
                sh '''
                    ansible-galaxy install geerlingguy.postgresql
                '''
            }
        }
        stage('Install postgres') {
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible-aimodosia/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible-aimodosia/hosts.yaml -l azure-db-server ~/workspace/ansible-aimodosia/playbooks/postgres.yaml
                '''
            }
        }

        stage('Deploy spring boot app') {
            steps {

                sh '''
                    # edit host var for appserver
                    export ANSIBLE_CONFIG=~/workspace/ansible-aimodosia/ansible.cfg
                    export SENDGRID_KEY=${SENDGRID_KEY}
                    ansible-playbook -i ~/workspace/ansible-aimodosia/hosts.yaml -l backend-server -e dbvm_ip=4.233.185.183 ~/workspace/ansible-aimodosia/playbooks/spring.yaml
                '''
            }
        }
        stage('Deploy frontend') {
            steps {
                sh '''
                   
                    export ANSIBLE_CONFIG=~/workspace/ansible-aimodosia/ansible.cfg
                     ansible-playbook -i ~/workspace/ansible-aimodosia/hosts.yaml -l frontend-server -e frontendvm_ip=172.167.33.29 -e backendvm_ip=104.155.97.142 ~/workspace/ansible-aimodosia/playbooks/vuejs.yaml
                '''
            }
        }
    }

//    post {
//        always {
//            mail  to: "${EMAIL_TO}", body: "Project ${env.JOB_NAME} <br>, Build status ${currentBuild.currentResult} <br> Build Number: ${env.BUILD_NUMBER} <br> Build URL: ${env.BUILD_URL}", subject: "JENKINS: Project name -> ${env.JOB_NAME}, Build -> ${currentBuild.currentResult}"
//        }
//    }
}