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
//        stage('Replace SendGrid Key') {
//            steps {
//                sh '''
//                    sed -i 's/app.sendgrid.key=.*/app.sendgrid.key=${SENDGRID_KEY}/' ~/workspace/spring-aimodosia/src/main/resources/application.properties
//                '''
//            }
//        }
        stage('Replace SendGrid Key') {
            steps {
                script {
                    echo "SENDGRID_KEY: ${SENDGRID_KEY}"
                    sh """
                echo "Replacing SendGrid Key..."
                sed -i "s|app.sendgrid.key=.*|app.sendgrid.key=${SENDGRID_KEY}|" ~/workspace/spring-aimodosia/src/main/resources/application.properties
                echo "Replacement done. Verifying..."
                grep "app.sendgrid.key=" ~/workspace/spring-aimodosia/src/main/resources/application.properties
            """
                }
            }
        }
//        stage('Test') {
//            steps {
//                sh 'chmod +x ./mvnw && ./mvnw test'
//            }
//        }
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
                   # replace dbserver in host_vars
                   # sed -i 's/dbserver/51.13.41.31/g' ~/workspace/ansible-aimodosia/host_vars/appserver-vm.yaml
                   # replace workingdir in host_vars
                    # sed -i 's/vagrant/azureuser/g' ~/workspace/ansible-aimodosia/host_vars/appserver-vm.yaml
                '''
                sh '''
                    # edit host var for appserver

                    export ANSIBLE_CONFIG=~/workspace/ansible-aimodosia/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible-aimodosia/hosts.yaml -l gcloud-app-server ~/workspace/ansible-aimodosia/playbooks/spring.yaml
                '''
            }
        }
        stage('Deploy frontend') {
            steps {
                sh '''
                   # sed -i 's/dbserver/51.13.41.31/g' ~/workspace/ansible-aimodosia/host_vars/appserver-vm.yaml
                    export ANSIBLE_CONFIG=~/workspace/ansible-aimodosia/ansible.cfg
                     ansible-playbook -i ~/workspace/ansible-aimodosia/hosts.yaml -l frontend-vm -e branch=devops ~/workspace/ansible-aimodosia/playbooks/vuejs.yaml
                   # ansible-playbook -i ~/workspace/ansible-aimodosia/hosts.yaml -l frontend-vm -e branch=devops ~/workspace/ansible-aimodosia/playbooks/vuejs.yaml
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