# slack proxy server

### 어플리케이션별 로그 양식
* springboot admin
```json
{
  "attachments": [
    {
      "color": "good",
      "mrkdwn_in": [
        "text"
      ],
      "text": "*INTERNAL-GATEWAY* (c3ae92d89fbc) is *UP*"
    }
  ],
  "channel": "infra-dev",
  "username": "Admin(Local)"
}
```
* logback
```json
{
  "icon_url": null,
  "attachments": [
    {
      "color": "warning",
      "text": "POST /sample/search\n{\"telNumber\":\"010-1234-1241\"}\n400(Bad Request) in 4 ms\n{\"success\":false,\"code\":\"BAD_PARAMETER\",\"message\":\"유효하지 않은 전화번호 입니다.\",\"data\":\"\"}\n"
    }
  ],
  "icon_emoji": ":fire:",
  "channel": "infra-dev",
  "text": "[*WARN*] com.global.logging.LoggingFilter",
  "username": "base-api (local)"
}
```