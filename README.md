# Qualcomm 5g Data retrieval Svc
Qualcomm - Data Rerieval Svc

[![Build Status](http://localhost:8080/job/data-retrieval-svc/) ]

* [Description](#desc)
* [Tools and Technologies](#tools)
* [CICD](#cicd)
* [HealthChecks](#health)


## Overview ##
Service to retrieve and process patent application numbers from Queue
<a name="desc"></a>
## Description

This service listens to events from SQS for patent numbers and does the following:
<li> Use the USPTO Api to get information on the patent</li>
<li> Store response metadata of the Patent</li>
<li> Download patent pdf dynamically by parsing the text from response</li>
<li> Scan pdf pages and convert to images</li>
<li> Extract text using tessaract</li>


<a name="tools"></a>
## Tool and Technologies
    Codebase:  Java - 1.8.x / Springboot - 2.x.x
    Build/Store: Maven / Docker / Artifactory
    Eventbus: Amazon SQS
    Database: Oracle / RDS
    CICD: Jenkins

<a name="endpoints"></a>
## HealthChecks
| Environments|Status |
| :----| :---:|
| [Local](http://localhost:8082/actuator/health) | [Manual](#localsetup)|


## How to run this application locally

To run this application on your local, please follow the steps below:

* mvn clean install

* Run [DataRetrievalSvcApplication](/Users/sravindra1/code/personal/data-retrieval-svc/src/main/java/com/personal/dataretrievalsvc/DataRetrievalSvcApplication.java) class. No special VM arguments are needed.

## How to run Jenkins-Docker locally
Start Jenkins :

* docker run -p 8080:8080 -p 50000:50000 -v ~/jenkins_home:/var/jenkins_home jenkins/jenkins:lts

## How to run Oracle-Docker locally
Start Oracle :
* docker run -d -p 1521:1521 --name oracle store/oracle/database-enterprise:12.2.0.1-slim