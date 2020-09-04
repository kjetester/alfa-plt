# To run

####Set up environment
1. Install Homebrew ```/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"```
1. Install Java ```brew install java```
1. Install Maven ```brew install maven```
1. Run ```brew install kubectl```
   1. Run ```vi ~/.kube/config```
   1. Paste either content of the ```config``` file from the project root directory or replace the whole file itself
   1. Replace ```{token}``` value

####Before running tests execute
1. For review @k8s ```kubectl config use-context alfa-rw```
1. For develop @k8s ```kubectl config use-context alfa-dev```
1. For preprod @k8s ```kubectl config use-context alfa-preprod```
1. For dev @k8s ```kubectl config use-context alfa-infra```
1. For review @k8ng ```kubectl config use-context alfa-rw-new```
1. For develop @k8ng ```kubectl config use-context alfa-dev-new```
1. For preprod @k8ng ```kubectl config use-context alfa-preprod-new```
1. For dev @k8ng ```kubectl config use-context alfa-infra-new```

####Develop stage
1. `mvn clean -pl abtests -Denv=develop -am test`
1. `mvn clean -pl acms -Denv=develop -am test`
1. `mvn clean -pl cities -Denv=develop -am test`
1. `mvn clean -pl contentstore -Denv=develop -am test`
1. `mvn clean -pl tranbilog -Denv=develop -am test`
1. `mvn clean -pl geofacade -Denv=develop -am test`
1. `mvn clean -pl feedback -Denv=develop -am test`
1. `mvn clean -pl offices -Denv=develop -Ddblogin={login} -Ddbpassword={password} -am test`
1. `mvn clean -pl tranbilog -Denv=develop -am test`

####PreProd stage
1. `mvn clean -pl abtests -Denv=preprod -am test`
1. `mvn clean -pl acms -Denv=preprod -am test`
1. `mvn clean -pl cities -Denv=preprod -am test`
1. `mvn clean -pl contentstore -Denv=preprod -am test`
1. `mvn clean -pl tranbilog -Denv=preprod -am test`
1. `mvn clean -pl geofacade -Denv=preprod -am test`
1. `mvn clean -pl feedback -Denv=preprod -am test`
1. `mvn clean -pl offices -Denv=preprod -Ddblogin={login} -Ddbpassword={password} -am test`
1. `mvn clean -pl tranbilog -Denv=preprod -am test`

####Prod stage
1. `mvn clean -pl offices -Denv=prod -Ddblogin={login} -Ddbpassword={password} -am test`

####ACMS-feature stage
1. `mvn clean -pl abtests -Denv=acms_feature-#### -am test`
1. `mvn clean -pl acms -Denv=acms_feature-#### -am test`
1. `mvn clean -pl cities -Denv=acms_feature-#### -am test`
1. `mvn clean -pl contentstore -Denv=acms_feature-#### -am test`
1. `mvn clean -pl tranbilog -Denv=acms_feature-#### -am test`
1. `mvn clean -pl geofacade -Denv=acms_feature-#### -am test`
1. `mvn clean -pl feedback -Denv=acms_feature-#### -am test`
1. `mvn clean -pl tranbilog -Denv=acms_feature-#### -am test`

####CS-feature stage
1. `mvn clean -pl abtests -Denv=cs_feature-#### -am test`
1. `mvn clean -pl acms -Denv=cs_feature-#### -am test`
1. `mvn clean -pl cities -Denv=cs_feature-#### -am test`
1. `mvn clean -pl contentstore -Denv=cs_feature-#### -am test`
1. `mvn clean -pl tranbilog -Denv=cs_feature-#### -am test`
1. `mvn clean -pl geofacade -Denv=cs_feature-#### -am test`
1. `mvn clean -pl feedback -Denv=cs_feature-#### -am test`
1. `mvn clean -pl tranbilog -Denv=cs_feature-#### -am test`