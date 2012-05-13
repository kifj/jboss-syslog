jboss-syslog
============

Syslog facility for JBoss AS 7

Copy the folders in modules to $JBOSS_HOME/modules and at the jar file to modules/x1/jboss-syslog/main.
Modify the JBoss configuration in standalone/configuration/standalone.xml like this:

<subsystem xmlns="urn:jboss:domain:logging:1.1">
    ...
    <custom-handler name="SYSLOG" class="x1.jboss.syslog.SyslogHandler" module="x1.jboss-syslog">
        <level name="INFO"/>
        <!-- 
        <formatter>
            <pattern-formatter pattern="%-5p [%c] %s"/>
        </formatter>
         -->
        <properties>
            <property name="loghost" value="localhost"/>
            <property name="application" value="jboss-as7"/>
        </properties>
    </custom-handler>
    ...
    <root-logger>
        <level name="INFO"/>
        <handlers>
            <handler name="CONSOLE"/>
            <handler name="FILE"/>
            <handler name="SYSLOG"/>
        </handlers>
    </root-logger>
</subsystem>
