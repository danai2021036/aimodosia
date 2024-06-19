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
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'api', url: 'git@github.com:danai2021036/aimodosia.git'
            }
        }
//        stage('Test') {
//            steps {
//                sh './mvnw test'
//            }
//        }
        stage('Kubectl commands to run postgres') {
            steps {
                sh '''
                    ~/kubectl apply -f ~/workspace/k8s-aimodosia/k8s/postgres/postgres-deployment.yaml
                    ~/kubectl apply -f ~/workspace/k8s-aimodosia/k8s/postgres/postgres-pvc.yaml
                    ~/kubectl apply -f ~/workspace/k8s-aimodosia/k8s/postgres/postgres-svc.yaml
                '''
            }
        }
        stage('Kubectl commands to run spring') {
            steps {
                sh '''
                    ~/kubectl create cm spring-config --from-env-file=~/workspace/k8s-aimodosia/k8s/spring/spring.env
                    ~/kubectl apply -f ~/workspace/k8s-aimodosia/k8s/spring/spring-deployment.yaml
                    ~/kubectl apply -f ~/workspace/k8s-aimodosia/k8s/spring/spring-svc.yaml
                '''
            }
        }
        stage('Kubectl commands to run vue') {
            steps {
                sh '''
                    ~/kubectl apply -f ~/workspace/k8s-aimodosia/k8s/vue/vue-deployment.yaml
                    ~/kubectl apply -f ~/workspace/k8s-aimodosia/k8s/vue/vue-svc.yaml
                    ~/kubectl apply -f ~/workspace/k8s-aimodosia/k8s/vue/vue-ingress.yaml
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
        stage('deploy to k8s') {
            steps {
                sh '''
                    HEAD_COMMIT=$(git rev-parse --short HEAD)
                    TAG=$HEAD_COMMIT-$BUILD_ID
                    ~/kubectl set image deployment/spring-deployment spring=$DOCKER_PREFIX:$TAG
                    ~/kubectl rollout status deployment spring-deployment --watch --timeout=2m
                '''
            }
        }


    }


}