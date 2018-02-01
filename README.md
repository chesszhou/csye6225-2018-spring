## CSYE6225 - Network Structure And Cloud Computing (Spring 2018)
### Coursework Project Repository
####Team members:  
Yiyi Zhou: zhou.yiy@husky.neu.edu  
Zhenyu  Zhu: zhu.zheny@husky.neu.edu  
Beiwen: guo.bei@husky.neu.edu  

#### Software Configurations
Operating System: Ubantu 16.04.3  
IDE: IntelliJ  
Servlet Application: Tomcat 8  
Software Testing: Apache JMeter  
Continuous Build And Integration: Travis CI

#### Build and Deploy instructions
* `Open` the project of the `webapp` directory  
* Click the under scroll on the right top and select `Edit Configurations...`
* Add a new configuration of Tomcat server  
* Select `Gradle: ROOT.war` under deployment sheet
* Apply and run the app

#### Testing Instructions  
To run the JMeter tests, simply open the `*.jmx` file under jmeter folder, and make sure the servlet is running, then run the JMeter test, the results can be observed in the tree.  

#### TravisCI Build
Link: https://travis-ci.com/beiwen/csye6225-git-demo