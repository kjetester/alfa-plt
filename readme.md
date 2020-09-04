# To run

####Set up environment
1. Install Homebrew ```/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"```
1. Install Java ```brew install java```
1. Install Maven ```brew install maven```
1. Run ```brew install kubectl```
   1. Run ```vi ~/.kube/config```
   1. Paste either content of the ```config``` file from the project root directory or replace the whole file itself
   1. Replace ```{token}``` values with appropriate valid tokens

####Before running tests execute
* For review @k8s ```kubectl config use-context alfa-rw```
* For develop @k8s ```kubectl config use-context alfa-dev```
* For preprod @k8s ```kubectl config use-context alfa-preprod```
* For dev @k8s ```kubectl config use-context alfa-infra```
* For review @k8ng ```kubectl config use-context alfa-rw-new```
* For develop @k8ng ```kubectl config use-context alfa-dev-new```
* For preprod @k8ng ```kubectl config use-context alfa-preprod-new```
* For dev @k8ng ```kubectl config use-context alfa-infra-new```

####Review stage
`mvn clean -pl {module} -Denv=review-{Jira ticket num} -am test`

####Develop stage
`mvn clean -pl {module} -Denv=develop -am test`

####ACMS-feature stage
`mvn clean -pl {module} -Denv=acms_feature-{Jira ticket num} -am test`

####PreProd stage
`mvn clean -pl {module} -Denv=preprod -am test`

####Prod stage
`mvn clean -pl {module} -Denv=prod -am test`
___
_Note: If you're using module using database access you'd specify also ```-Ddblogin={login} -Ddbpassword={password}``` environment variables_ 