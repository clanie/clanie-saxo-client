# Saxo Client

SaxoClient is a Java client for accessing (parts of) Saxo Banks Open Api.

To use it, you need a Saxo account and an API token. And to enable (read) api-access on your Saxo account.

The client is meant to be used in Spring Boot applications, and will be auto-configured, if you provide the required configuration properties:

```
saxo:
  appKey: <app-key>
  appSecret: <app-secret>
  appRedirectUrl: <app-redirect-url>
  authzEndpoint: https://sim.logonvalidation.net/authorize
  tokenEndpoint: https://sim.logonvalidation.net/token
  openApiUrl: https://gateway.saxobank.com/sim/openapi
  wiretap: false
```
