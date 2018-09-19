pipeline {
    agent any

    stages {
        stage('build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('deploy docker image') {
            steps {
                script {
                    checkout scm
                    docker.withRegistry('https://docker.adeo.no:5000/arena-nais/') {
                        def image = docker.build("arena-nais-kafkaintegrasjon:1.0.${env.BUILD_ID}", "--build-arg JAR_FILE=arena-nais-kafkaintegrasjon-0.0.1-SNAPSHOT.jar .")
                        image.push()
                        image.push 'latest'
                    }
                }
            }
        }

        stage('deploy nais.yaml to nexus m2internal') {
            steps {
                script {

                        sh "nais validate"
                        sh "nais upload --app arena-nais-kafkaintegrasjon -v 1.0.${env.BUILD_ID}"

                }
            }
        }

        stage('deploy to nais') {
            steps {
                script {

                        sh "nais deploy -c preprod-fss -z fss -a arena-nais-kafkaintegrasjon -v 1.0.${env.BUILD_ID} --skip-fasit"

                }
            }

        }


    }

    post {
        always {
            archive 'target/*.jar'
            deleteDir()
        }

    }
}