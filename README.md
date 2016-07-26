# Redis Session Java

redis-session-java implement session management for java web application.

## Basic Usage

1. Edit `/etc/hosts` file add redis server host

    ```
    127.0.0.1 redis.host.name
    ```

1. Edit `web.xml` file add follow lines

    ```
    <filter>
        <filter-name>sessionFilter</filter-name>
        <filter-class>com.github.ququzone.session.SessionFilter</filter-class>
        <init-param>
            <param-name>factory</param-name>
            <param-value>com.github.ququzone.session.DefaultSessionRepositoryFactory</param-value>
        </init-param>
        <init-param>
            <param-name>sessionKey</param-name>
            <param-value>session</param-value>
        </init-param>
        <init-param>
            <param-name>cookieName</param-name>
            <param-value>sessionid</param-value>
        </init-param>
        <init-param>
            <param-name>httpOnly</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>sessionFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    ```

1. Access session from jsp

    ```
    <c:out value="${requestScope.session.nickName}" />
    ```

1. Access session from controller

    ```
    import com.github.ququzone.session.Session;
    import com.github.ququzone.session.SessionFilter;

    ...

    Session userSession = (Session) request.getAttribute(SessionFilter.SESSION_KEY);
    ```
