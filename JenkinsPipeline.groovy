pipeline {
    agent any
    options { timestamps() }
    stages {
        stage("Tools initialization") {
            steps {
                sh "mvn --version"
                sh "java -version"
            }
        }
        stage('git') {
            steps {
                git credentialsId: 'dda4af50-a518-422c-99e3-59c8d822940f', url: 'https://github.com/MarinaPysarenko/MentoringCourse.git'
                sh 'git pull origin master'
            }
        }

        stage('Test FramesPage') {
            steps {
                sh 'mvn test -Dtest=FramesPageTest'
            }
        }
        stage('Test ModalsTab') {
            steps {
                sh "mvn test -Dtest=ModalsTabTest#openSmallModal"
            }
        }
        stage('Allure report') {
            steps {
                script {
                    allure([
                            includeProperties: false,
                            jdk              : '',
                            properties       : [],
                            reportBuildPolicy: 'ALWAYS',
                            results          : [[path: 'target/allure-results']]
                    ])
                }
            }
        }
    }
}

