# JNDI App using Oracle on Cloud Foundry

## This project shows ways to run Jboss JNDI App using Oracle DB on Cloud Foundry via modified Liberty Build Pack


### Pre-requisites

#### 1. Maven

Spring Boot is compatible with Apache Maven 3.2 or above. If you don’t already have Maven installed you can follow the instructions at maven.apache.org.

On MAC, [Install Homebrew](https://brew.sh/) if not already installed. Then install Maven
```
$ brew install maven
```

#### 2. GIT Bash

On Windows, if you don't already have GIT Bash installed, download [GIT Bash here](https://git-scm.com/downloads)

#### 3. Cloud Foundry - PCF Dev or any Cloud Foundry Install

You can run [PCF Dev](https://pivotal.io/pcf-dev) on your local machine following instructions here: https://pivotal.io/platform/pcf-tutorials/getting-started-with-pivotal-cloud-foundry-dev/introduction

### GET YOUR ENVIRONMENT READY
**Login to PCF (target your CF endpoint, I'm using my PCF Dev URI):**
```
cf login -a https://api.local.pcfdev.io
```

**Create User Defined Service:**
Make sure to use **PCF** as your instance name as JNDI will be created using that name. You can use the Oracle URI if you don't have your own Oracle instance:
```
cf cups PCF -p '{"uri":"oracle://admin:management@pcfdb.cizqc6uzdesg.us-east-1.rds.amazonaws.com:1521/ORCLPCF?reconnect=true"}'
```

**Clone code:**

``` 
git clone https://github.com/mayureshkrishna/jbossjndi-oracle-liberty-pcf.git
```
Navigate to the cloned directory


### Now you can deploy this to Cloud Foundry 2 ways

## [Option 1: Just "cf push" your WAR file](https://github.com/mayureshkrishna/jbossjndi-oracle-liberty-pcf#option-1-just-cf-push-your-war-file-1)
## [Option 2: "cf push" websphere/liberty SERVER directory](https://github.com/mayureshkrishna/jbossjndi-oracle-liberty-pcf#option-2-cf-push-websphereliberty-server-directory)


### Option 1: Just "cf push" your WAR file

**Package code using maven:**
Go to your cloned code root directory and run
```
mvn clean package
```
**Push code to Cloud Foundry:**
*Make sure you are running this from your cloned code root directory*
```
cf push -b https://github.com/mayureshkrishna/ibm-websphere-liberty-buildpack.git -p target/HibernateJndi.war HibernateJndi
```

**Bind Service to App:**
Bind the Oracle User Defined service which your created earlier to the app you just pushed.

```
cf bind-service HibernateJndi PCF
```
Make sure to restage the app so that auto configuration of JNDI picks up.

```
cf restage HibernateJndi
```

Here's the restage logs and you will notice the Autowiring:

***-----> Auto-configuration is creating config for service instance 'PCF' of type 'oracle'***

```
Restaging app **HibernateJndi** in org **pcfdev-org** / space **pcfdev-space** as **user**...

  

Staging app and tracing logs...

Creating container

Successfully created container

Downloading app package...

Downloaded app package (6.2M)

Downloading build artifacts cache...

Downloaded build artifacts cache (191.6M)

Staging...

-----> Liberty Buildpack Version: ecec66f | https://github.com/mayureshkrishna/ibm-websphere-liberty-buildpack.git#ecec66f

-----> Downloading IBM 1.8.0_sr5fp7 JRE from https://public.dhe.ibm.com/ibmdl/export/pub/systems/cloud/runtimes/java/8.0.5.7/linux/x86_64/ibm-java-jre-8.0-5.7-x86_64-archive.bin ... (0.2s)

Expanding JRE to .java ... (20.7s)

-----> Downloading from https://public.dhe.ibm.com/ibmdl/export/pub/software/websphere/wasdev/downloads/wlp/18.0.0.1/wlp-webProfile7-18.0.0.1.zip ... (0.2s)

Installing archive ... (1.0s)

-----> Downloading and installing client jar(s) from https://github.com/mayureshkrishna/ibm-websphere-liberty-buildpack/raw/master/resources/oracle/ojdbc6.jar (0.8s)

-----> Warning: Liberty feature set is not specified. Using the default feature set: ["beanValidation-1.1", "cdi-1.2", "ejbLite-3.2", "el-3.0", "jaxrs-2.0", "jdbc-4.1", "jndi-1.0", "jpa-2.1", "jsf-2.2", "jsonp-1.0", "jsp-2.3", "managedBeans-1.0", "servlet-3.1", "websocket-1.1"]. For the best results, explicitly set the features via the JBP_CONFIG_LIBERTY environment variable or deploy the application as a server directory or packaged server with a custom server.xml file.

-----> Auto-configuration is creating config for service instance 'PCF' of type 'oracle'

-----> Liberty buildpack is done creating the droplet

Exit status 0

Staging complete

Uploading droplet, build artifacts cache...

Uploading droplet...

Uploading build artifacts cache...

Uploaded build artifacts cache (194.1M)

Uploaded droplet (195.4M)

Uploading complete

Destroying container

Successfully destroyed container

Waiting for app to start...

name:  HibernateJndi

requested state: started

instances: 1/1

usage: 256M x 1 instances

routes:  hibernatejndi.local.pcfdev.io

last uploaded: Fri 20 Apr 11:42:01 EDT 2018

stack: cflinuxfs2

buildpack: https://github.com/mayureshkrishna/ibm-websphere-liberty-buildpack.git

start command: .liberty/create_vars.rb wlp/usr/servers/defaultServer/runtime-vars.xml && .liberty/calculate_memory.rb && WLP_SKIP_MAXPERMSIZE=true JAVA_HOME="$PWD/.java"

WLP_USER_DIR="$PWD/wlp/usr" exec .liberty/bin/server run defaultServer

**state** **since**  **cpu**  **memory**  **disk** **details**

#0 running 2018-04-20T15:51:03Z 0.0% 86.5M of 256M 274.8M of 512M
```
Now your App is up and running. You can run the app in your browser if you are using PCF Dev at: http://hibernatejndi.local.pcfdev.io/GetEmployeeById?empId=1

You can get URL info under ***routes*** by running:

```
cf app HibernateJndi

Showing health and status for app **HibernateJndi** in org **pcfdev-org** / space **pcfdev-space** as **user**...
name:  HibernateJndi
requested state: started
instances: 1/1
usage: 256M x 1 instances
routes:  hibernatejndi.local.pcfdev.io
last uploaded: Fri 20 Apr 11:42:01 EDT 2018
stack: cflinuxfs2

buildpack: https://github.com/mayureshkrishna/ibm-websphere-liberty-buildpack.git

**state** **since**  **cpu**  **memory** **disk** **details**

#0 running 2018-04-20T15:51:03Z 0.7% 171.9M of 256M 277.2M of 512M
``` 

### Option 2: "cf push" your websphere/liberty SERVER directory

Make sure you are at the cloned root directory

```
MK:jbossjndi-oracle-liberty-pcf mkrishna$ ls

LICENSE  README.md  WebContent  manifest.yml  pcfliberty  pom.xml  src  target
```
You will notice there's pcfliberty directory here, which is essentially websphere/liberty server directory. There is no use of this directory in the previous option. 

We will use this pre-configured server directory which has the built "WAR" file of the app as well.

*P.S.: In this option you can change the **server.xml** as per your need and all you have to do is copy your packaged **"WAR"** file to **"apps"** directory under the server directory.*

Let's move the manifest file from pcfliberty directory to root so that we can exclude server.xml updates via auto-configuration.
```
mv pcfliberty/manifest.yml .
```

If you look at manifest.yml, it has additional exclude parameter.
```
MK:jbossjndi-oracle-liberty-pcf mkrishna$ cat manifest.yml
---
env:
  IBM_JVM_LICENSE: L-PMAA-A3Z8P2
  IBM_LIBERTY_LICENSE: L-CTUR-AVDTCN
  services_autoconfig_excludes: oracle=config
```

Now let's just **"cf push"** it.
*Make sure you are running this from your cloned code root directory*
```
cf push -b https://github.com/mayureshkrishna/ibm-websphere-liberty-buildpack.git -p pcfliberty/ HibernateJndi
```

**Bind Service to App:**
Bind the Oracle User Defined service which your created earlier to the app you just pushed.

```
cf bind-service HibernateJndi PCF
```
Make sure to restage the app so that auto configuration of JNDI picks up.

```
cf restage HibernateJndi
```
In the restage logs and you will notice the Autowiring:

***-----> Auto-configuration is creating config for service instance 'PCF' of type 'oracle'***

Now your App is up and running. You can run the app in your browser if you are using PCF Dev at: http://hibernatejndi.local.pcfdev.io/GetEmployeeById?empId=1

You can get URL info under ***routes*** by running:

```
cf app HibernateJndi

Showing health and status for app **HibernateJndi** in org **pcfdev-org** / space **pcfdev-space** as **user**...
name:  HibernateJndi
requested state: started
instances: 1/1
usage: 256M x 1 instances
routes:  hibernatejndi.local.pcfdev.io
last uploaded: Fri 20 Apr 11:42:01 EDT 2018
stack: cflinuxfs2

buildpack: https://github.com/mayureshkrishna/ibm-websphere-liberty-buildpack.git

**state** **since**  **cpu**  **memory** **disk** **details**

#0 running 2018-04-20T15:51:03Z 0.7% 171.9M of 256M 277.2M of 512M
``` 
