jboss-syslog
============

Syslog facility for JBoss AS 7

Compile the jar file with maven: `mvn package`

Unzip the archive created in the `target/zip` folder at `$JBOSS_HOME/modules`.

Modify the JBoss configuration in `standalone/configuration/standalone.xml` like this:

<pre>
&lt;subsystem xmlns="urn:jboss:domain:logging:1.1"&gt;
    ...
    &lt;custom-handler name="SYSLOG" class="x1.jboss.syslog.SyslogHandler" module="x1.jboss-syslog"&gt;
        &lt;level name="INFO"/&gt;
        &lt;properties&gt;
            &lt;property name="loghost" value="localhost"/>
            &lt;property name="application" value="jboss-as7"/>
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

 * loghost: hostname or IP address of the log server (default localhost)
 * port: port of the log server (default 514)
 * protocol: udp (default) or tcp
 * application: name of the application, (default "java")
