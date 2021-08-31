[![Actions Status](https://github.com/kifj/jboss-syslog/workflows/Java%20CI/badge.svg)](https://github.com/kifj/jboss-syslog/actions) ![Licence](https://img.shields.io/github/license/kifj/jboss-syslog) ![Issues](https://img.shields.io/github/issues/kifj/jboss-syslog) ![Stars](https://img.shields.io/github/stars/kifj/jboss-syslog)

jboss-syslog
============

Syslog facility for JBoss AS 7.1

Please note that JBoss EAP 6.1 contains a syslog module, which makes this addition obsolete.

Compile the jar file with maven: `mvn package`. If you need to support Java 6, change the compiler settings in the pom.

Unzip the archive created in the `target/zip` folder at `$JBOSS_HOME/modules`.

Modify the JBoss configuration in `standalone/configuration/standalone.xml` like this:

<pre>
&lt;subsystem xmlns="urn:jboss:domain:logging:1.1"&gt;
    ...
    &lt;custom-handler name="SYSLOG" class="x1.jboss.syslog.SyslogHandler" module="x1.jboss-syslog"&gt;
        &lt;level name="INFO"/&gt;
        &lt;properties&gt;
            &lt;property name="loghost" value="localhost"/&gt;
            &lt;property name="application" value="jboss-as7"/&gt;
            &lt;property name="facility" value="daemon"/&gt;
        &lt;/properties&gt;
    &lt;/custom-handler&gt;
    ...
    &lt;root-logger&gt;
        &lt;level name="INFO"/&gt;
        &lt;handlers&gt;
            &lt;handler name="CONSOLE"/&gt;
            &lt;handler name="FILE"/&gt;
            &lt;handler name="SYSLOG"/&gt;
        &lt;/handlers&gt;
    &lt;/root-logger&gt;
&lt;/subsystem&gt;
</pre>

Valid properties are:

 * loghost: hostname or IP address of the log server (default: localhost)
 * port: port of the log server (default: 514)
 * protocol: udp (default) or tcp
 * application: name of the application (default: java)
 * facility: name of the syslog facility (default: daemon)
