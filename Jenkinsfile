@Library('nais-deploy')
import deploy

/*
IMPORTANT!!!

1) make sure 'nais-deploy' library already configured in Jenkins!!!

'Manage Jenkins -> Configure System -> Global Pipeline Libraries'
	-> Add
	https://github.com/navikt/jenkins-deploy-pipeline.git
	Name: nais-deploy / default-ver: master

2) Maven tool must be already defined in Jenkins global configuration.
    NB! Ansible-Playbook already does this, just check & verify it's done.

    'Manage Jenkins -> Global Tool Configuration'
        -> Maven -> Maven installations
        maven-3.5.3 : /opt/apache-maven-3.5.3

*/

def deployLib = new deploy()

node {
    // code repository and app info
    def application = "arena-nais-kafkaintegrasjon"
    def repo = "navikt/arena-nais-kafkaintegrasjon"
    def groupId = "no.nav.arena.nais"
    def branch = "master"

    def dockerRepo = "docker.adeo.no:5000/arena-nais"

    // general vars
    def committer, committerEmail, changelog, pom, releaseVersion, isSnapshot, nextVersion, buildMeta
    def slackMessage
    def mvnHome = tool "maven-3.5.3"
    def mvn = "${mvnHome}/bin/mvn"
    def mvnIgnoreSsl = "-Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true"

    // see https://github.com/nais/naisd
    def NAIS_HOME = "/opt/nais-cli/bin/nais"
    def naisConfig = "nais.yaml"
    // NAIS environment
    //def clusters = ['preprod-fss', 'prod-fss']
    def clusters = ['preprod-fss']
    //def environment = 'q0' // preprod
    //def zone = 'fss'
    //def namespace = 'default'

    try {

        stage("#0: pre-start") {
            buildMeta = "build# ${env.BUILD_ID} on ${env.JENKINS_URL} \n"

            slackMessage = "[INFO] Pre-start ${buildMeta} \n gitRepo: ${repo} / ${branch}"
            println("[INFO] Running ${buildMeta}")
            println("[INFO] gitRepo: ${repo} / ${branch}")
            slackSend([
                    color: 'good',
                    message: "${slackMessage}"
            ])
        }

        stage("#1: checkout sourcecode") {

            // NB! make sure you've added GITHUB_TOKEN (which has access to repo) to Jenkins Credentials
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'github-token', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_TOKEN']]) {
                git url: "https://${GITHUB_TOKEN}:x-oauth-basic@github.com/${repo}.git", branch: "${branch}"
            }
        }

        stage("#2: init MVN prj") {
            pom = readMavenPom file: 'pom.xml'
            releaseVersion = pom.version.tokenize("-")[0]
            isSnapshot = pom.version.contains("-SNAPSHOT")
            committer = sh(script: 'git log -1 --pretty=format:"%an (%ae)"', returnStdout: true).trim()
            committerEmail = sh(script: 'git log -1 --pretty=format:"%ae"', returnStdout: true).trim()
            changelog = sh(script: 'git log `git describe --tags --abbrev=0`..HEAD --oneline', returnStdout: true)
        }

        stage("#3: verify code-compile") {
            if (isSnapshot) {
                sh "${mvn} clean compile -Djava.io.tmpdir=/tmp/${application} -B -e"
            } else {
                println("[INFO] POM version is not a SNAPSHOT, it is ${pom.version}. Skipping build and testing of backend")
            }
        }

        stage("#4: package & dockerize & tag codebase") {
            if (isSnapshot) {
                sh "${mvn} versions:set -B -DnewVersion=${releaseVersion} -DgenerateBackupPoms=false"

                //sh "${mvn} clean package spring-boot:repackage -e -DskipTests"
                sh "${mvn} clean package -Dspring.profiles.active=local -DskipTests"

                //sh "docker build --build-arg version=${releaseVersion} --build-arg app_name=${application} -t ${dockerRepo}/${application}:${releaseVersion} ."
                sh "docker build --build-arg JAR_FILE=${application}-${releaseVersion}.jar --build-arg SPRING_PROFILES_ACTIVE=nais -t ${dockerRepo}/${application}:${releaseVersion} --rm=true ."

                sh "git commit -am \"set version to ${releaseVersion} (from Jenkins pipeline)\""
                sh "git push origin ${branch}"
                sh "git tag -a ${application}-${releaseVersion} -m ${application}-${releaseVersion}"
                sh "git push --tags"

                slackSend([
                        color: 'good',
                        message: "${buildMeta} *${application}:${releaseVersion}* package artifact has been released."
                ])
            }else{
                println("[INFO] POM version is not a SNAPSHOT, it is ${pom.version}. Skipping releasing")
            }
        }
        stage("#5: build-publish Docker-image and Artifact") {
            if (isSnapshot) {

                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'nexusUser', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                    sh "curl -s -F r=m2internal -F hasPom=false -F e=yaml -F g=${groupId} -F a=${application} -F " + "v=${releaseVersion} -F p=yaml -F file=@${naisConfig} -u ${env.USERNAME}:${env.PASSWORD} http://maven.adeo.no/nexus/service/local/artifact/maven/content"
                }
                sh "docker push ${dockerRepo}/${application}:${releaseVersion}"

                slackSend([
                        color: 'good',
                        message: "${buildMeta} *${application}:${releaseVersion}* Docker-image has been built and published to Docker repository."
                ])
            } else {
                println("[INFO] POM version is not a SNAPSHOT, it is ${pom.version}. Skipping publishing!")
            }
        }


        // --- Deploy using NAIS-cli ---
        stage("#6: validate & upload nais.yaml using NAIS-cli") {
            println("[INFO] display NAIS_HOME: ${NAIS_HOME}...")
            println("[INFO] display 'nais version'")
            sh "${NAIS_HOME} version"

            println("[INFO] Run 'nais validate'")
            sh "${NAIS_HOME} validate -f ${naisConfig}"
            slackSend([
                    color: 'good',
                    message: "${buildMeta} ${naisConfig} has been validated."
            ])

            println("[INFO] Run 'nais upload' ... to Nexus!")
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'nais-uploader', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD']]) {
                sh "${NAIS_HOME} upload -f ${naisConfig} -a ${application} -v ${releaseVersion} -u ${NEXUS_USERNAME} -p ${NEXUS_PASSWORD}"
            }
            slackSend([
                    color: 'good',
                    message: "${buildMeta} ${naisConfig} for *${application}:${releaseVersion}* has been uploaded to Nexus."
            ])
        }

        stage("#7: Deploy using NAIS-cli") {

            println("[INFO] Run 'nais deploy' ... to NAIS!")

            for (int ii=0; ii<clusters.size(); ii++) {
                timeout(time: 8, unit: 'MINUTES') {

                    // deploy by ignoring FASIT integration
                    sh "${NAIS_HOME} deploy -a ${application} -v ${releaseVersion} -c ${clusters[ii]} --skip-fasit --wait "

                }
            }
            slackSend([
                    color: 'good',
                    message: "${buildMeta} *${application}:${releaseVersion}* has been deployed to NAIS!!! :nais: "
            ])
        }

        slackMessage = "${buildMeta} _Jenkins_ har bygget, publisert og deployet akkurat nå *${application}:${releaseVersion}* til _NAIS!_"
        slackMessage += "  \n\n see url:  \n"
        slackMessage += "https://${application}.nais.preprod.local/  \n"
        slackMessage += "https://${application}.nais.adeo.no/  \n"
        slackSend([
                color: 'good',
                message: slackMessage
        ])
    } catch (e) {
        println("[ERROR] " + e)
        //slackSend (color: 'warning', message: "@here ${env.JOB_NAME} failed... \n \n Committer: ${committer} \n Jenkins: ${env.JENKINS_URL} buildId: ${env.BUILD_ID}")
        slackSend([
                color: 'danger',
                message: "@here ${buildMeta} ${env.JOB_NAME} failed at some stage... \n \n Committer: ${committer}"
        ])
    }
    finally {

        if (currentBuild.result == 'UNSTABLE') {
            slackSend([
                    color: 'danger',
                    message: "${buildMeta} ${env.JOB_NAME} was somehow unstable..."
            ])
        }

        // in any case tag DEV version in the end
        stage("finally: set new DEV-version") {

            if (isSnapshot) {
                def versionItems = releaseVersion.tokenize('.');
                nextVersion = versionItems.getAt(0) + "." + versionItems.getAt(1)
                // get the last element and increment
                nextVersion += "." + (versionItems.getAt(-1).toInteger() + 1)
                nextVersion += "-SNAPSHOT"

                println("[INFO] releaseVersion: ${releaseVersion} / nextVersion: ${nextVersion}")

                sh "${mvn} versions:set -B -DnewVersion=${nextVersion} -DgenerateBackupPoms=false"
                sh "git commit -am \"updated to new dev-version ${nextVersion} after release by ${committer}\""
                sh "git push origin ${branch}"

                slackSend([
                        color: 'good',
                        message: "${buildMeta} *${application}:${nextVersion}* as next-DEV-ver has been updated in pom and git."
                ])
            } else {
                println("[INFO] POM version is not a SNAPSHOT, it is ${pom.version}. Skip tagging dev-version!")
            }
        }
    }
}