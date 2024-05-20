pipeline {
    agent any

    environment {
        imageName = 'seoulpop/backend'
        registryCredential = 'docker-hub'
        dockerImage = ''

        releaseServerAccount = 'ubuntu'
        releaseServerUri = 'k10a409.p.ssafy.io'
    }

    parameters {
        string(name: 'DOCKER_NETWORK', defaultValue: 'proxy-network', description: 'docker network name')
        string(name: 'IMAGE_NAME', defaultValue: 'seoulpop-be', description: 'docker image name')
        string(name: 'MATTERMOST_CHANNEL', defaultValue: 'deploy-a409', description: 'mattermost channel name')
        string(name: 'MATTERMOST_ENDPOINT'
                , defaultValue: 'https://meeting.ssafy.com/hooks/uobkrknhgpdwbm5bc7yxmz7jjh'
                , description: 'mattermost hooks endpoint')
        choice(name: 'ENV_TYPE', choices: ['dev', 'prod'], description: 'Choose the environment type')
    }

    stages {
        stage('environment setup') {
            steps {
                script {
                    env.BRANCH_NAME = params.ENV_TYPE == 'prod'
                            ? 'release-be'
                            : 'dev-be'
                    env.DB_NAME = params.ENV_TYPE == 'prod'
                            ? 'serviceDB'
                            : 'seoulpopDB'
                    env.FRONTEND_URL = params.ENV_TYPE == 'prod'
                            ? 'https://seoul-pop.com'
                            : 'https://seoul-pop.com'
                    env.BACKEND_URL = params.ENV_TYPE == 'prod'
                            ? 'https://api.seoul-pop.com'
                            : 'https://api.seoul-pop.com'
                    env.DOCKER_PORT = params.ENV_TYPE == 'prod'
                            ? '8081'
                            : '8080'
                }
            }
        }

        stage('git clone') {
            steps {
                git branch: "${env.BRANCH_NAME}",
                        credentialsId: 'gitlab-account',
                        url: 'https://lab.ssafy.com/s10-final/S10P31A409'
            }
        }

        stage('Prepare configuration') {
            steps {
                dir('backend/src/main/resources') {
                    script {
                        withCredentials([file(credentialsId: 'application-secret', variable: 'APP_SECRET_FILE')]) {
                            sh "cp $APP_SECRET_FILE ./application-secret.yml"
                        }
                    }
                }
            }
        }

        stage('jar build') {
            steps {
                dir('backend') {
                    sh 'chmod +x ./gradlew'
                    sh './gradlew clean bootJar'
                    // sh './gradlew build'
                }
            }
        }

        stage('image build & docker-hub push') {
            steps {
                dir('backend') {
                    script {
                        docker.withRegistry('', registryCredential) {
                            sh 'docker buildx create --use --name mybuilder'
                            sh "docker buildx build --platform linux/amd64 -t $imageName:$BUILD_NUMBER --push ."
                            sh "docker buildx build --platform linux/amd64 -t $imageName:latest --push ."
                        }
                    }
                }
            }
        }

        stage('previous docker rm') {
            steps {
                sshagent(credentials: ['ubuntu']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${releaseServerAccount}@${releaseServerUri} 'sudo docker stop \$(sudo docker ps -aq --filter "ancestor=${imageName}:latest") || true'
                        ssh -o StrictHostKeyChecking=no ${releaseServerAccount}@${releaseServerUri} 'sudo docker rm -f \$(sudo docker ps -aq --filter "ancestor=${imageName}:latest") || true'
                        ssh -o StrictHostKeyChecking=no ${releaseServerAccount}@${releaseServerUri} 'sudo docker rmi ${imageName}:latest || true'
                    """
                }
            }
        }

        stage('docker-hub pull') {
            steps {
                sshagent(credentials: ['ubuntu']) {
                    sh "ssh -o StrictHostKeyChecking=no $releaseServerAccount@$releaseServerUri 'sudo docker pull $imageName:latest'"
                }
            }
        }

        stage('service start') {
            steps {
                sshagent(credentials: ['ubuntu']) {
                    sh '''
                        echo DB_URL=$DB_URL > env.list
                        echo DB_USERNAME=$DB_USERNAME >> env.list
                        echo DB_PASSWORD=$DB_PASSWORD >> env.list
                        echo REDIS_HOST=$REDIS_HOST >> env.list
                        echo REDIS_PORT=$REDIS_PORT >> env.list
                        echo REDIS_PASSWORD=$REDIS_PASSWORD >> env.list
                    '''

                    sh "scp -o StrictHostKeyChecking=no env.list $releaseServerAccount@$releaseServerUri:~"

                    sh """
                        ssh -o StrictHostKeyChecking=no ${env.releaseServerAccount}@${env.releaseServerUri} \
                        "sudo docker run -i -e TZ=Asia/Seoul \
                        --env-file ~/env.list \
                        --name ${params.IMAGE_NAME} \
                        --network ${params.DOCKER_NETWORK} \
                        -p ${env.DOCKER_PORT}:8080 \
                        -d ${env.imageName}:latest"
                    """
                }
            }
        }

        stage('service test & alert send') {
            steps {
                sshagent(credentials: ['ubuntu']) {
                    sh """
                        #!/bin/bash

                        for retry_count in \$(seq 20)
                        do
                            if curl -s "${env.BACKEND_URL}/ping" > /dev/null
                            then
                                curl -d '{"text":"${env.BRANCH_NAME} release:$BUILD_NUMBER success"}' -H "Content-Type: application/json" -X POST ${params.MATTERMOST_ENDPOINT}
                                break
                            fi

                            if [ \$retry_count -eq 20 ]
                            then
                                curl -d '{"text":"backend release fail"}' -H "Content-Type: application/json" -X POST ${params.MATTERMOST_ENDPOINT}
                                exit 1
                            fi

                            echo "The server is not alive yet. Retry health check in 5 seconds..."
                            sleep 5
                        done
                    """
                }
            }
        }
    }
}
