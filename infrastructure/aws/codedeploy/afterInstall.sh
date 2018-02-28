#!/bin/bash

# update the permission and ownership of WAR file in the tomcat webapps directory
sudo chown tomcat8:tomcat8 ROOT.war
sudo chmod 777 ROOT.war
