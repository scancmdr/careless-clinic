# careless-clinic
Clinic with that doesn't care about your data

Spring Security for authentication and authorization, and Spring Data JPA for data access.


With more time, we could leverage modern application design patterns that support secure coding practices.
Some examples are Spring Modulith for domain driven design separation - allowing compartmentalized modules for security, data access, and business logic.


We could go further here - implement more security anti-patterns (like broken access control, insecure direct object references, improper error handling that leaks
information, etc.), All in the spirit of an educational focus.




## JWT

```shell
$ scripts/sign-jwt.py --key private.pem '{"user": "jay"}'
eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoiamF5IiwiaWF0IjoxNzYyNzIwODI5fQ.KJXgF5VZr6dHKFt57FmSdr7e_L5IoQTgbLFVG1qY7JqU_qQiOglX3ogyZz3M5x41F_oxcIc8QNmzSpTNOPQC0A
```

## Examples

mod-security SQL injection rule block:
```json
{
  "transaction": {
    "client_ip": "10.0.1.10",
    "client_port": 55982,
    "host_ip": "10.0.1.33",
    "host_port": 8080,
    "messages": [
      {
        "details": {
          "accuracy": "0",
          "data": "Matched Data: s);Tn found within ARGS:text: ','2025-11-08 09:00:54.482','2025-11-08 09:00:54.482'); INSERT INTO text_entries (content, created_at, updated_at) VALUES ( (select COALESCE(STRING_AGG(content,','),'NONE') from text_entries),'2025-11-08 09:00:54.482','2025-11-08 09:00:54.482'); --",
          "file": "/etc/modsecurity.d/owasp-crs/rules/REQUEST-942-APPLICATION-ATTACK-SQLI.conf",
          "lineNumber": "46",
          "match": "detected SQLi using libinjection.",
          "maturity": "0",
          "reference": "v1531,248",
          "rev": "",
          "ruleId": "942100",
          "severity": "2",
          "tags": [
            "application-multi",
            "language-multi",
            "platform-multi",
            "attack-sqli",
            "paranoia-level/1",
            "OWASP_CRS",
            "OWASP_CRS/ATTACK-SQLI",
            "capec/1000/152/248/66"
          ],
          "ver": "OWASP_CRS/4.19.0"
        },
        "message": "SQL Injection Attack Detected via libinjection"
      },
      {
        "details": {
          "accuracy": "0",
          "data": "Matched Data: COALESCE( found within ARGS:text: ','2025-11-08 09:00:54.482','2025-11-08 09:00:54.482'); INSERT INTO text_entries (content, created_at, updated_at) VALUES ( (select COALESCE(STRING_AGG(content,','),'NONE') from text_entries),'2025-11-08 09:00:54.482','2025-11-08 09:00:54.482'); --",
          "file": "/etc/modsecurity.d/owasp-crs/rules/REQUEST-942-APPLICATION-ATTACK-SQLI.conf",
          "lineNumber": "110",
          "match": "Matched \"Operator `Rx' with parameter `(?i)\\b(?:a(?:dd(?:dat|tim)e|es_(?:de|en)crypt|s(?:cii(?:str)?|in)|tan2?)|b(?:enchmark|i(?:n_to_num|t_(?:and|count|length|x?or)))|c(?:har(?:acter)?_length|iel(?:ing)?|o(?:alesce|ercibility|llation|(?:m (2239 characters omitted)' against variable `ARGS:text' (Value: `','2025-11-08 09:00:54.482','2025-11-08 09:00:54.482'); INSERT INTO text_entries (content, created_a (148 characters omitted)' )",
          "maturity": "0",
          "reference": "o132,9v1531,248t:urlDecodeUni",
          "rev": "",
          "ruleId": "942151",
          "severity": "2",
          "tags": [
            "modsecurity",
            "application-multi",
            "language-multi",
            "platform-multi",
            "attack-sqli",
            "paranoia-level/1",
            "OWASP_CRS",
            "OWASP_CRS/ATTACK-SQLI",
            "capec/1000/152/248/66"
          ],
          "ver": "OWASP_CRS/4.19.0"
        },
        "message": "SQL Injection Attack: SQL function name detected"
      },
      {
        "details": {
          "accuracy": "0",
          "data": "Matched Data: ; INSERT INTO found within ARGS:text: ','2025-11-08 09:00:54.482','2025-11-08 09:00:54.482'); INSERT INTO text_entries (content, created_at, updated_at) VALUES ( (select COALESCE(STRING_AGG(content,','),'NONE') from text_entries),'2025-11-08 09:00:54.482','2025-11-08 09:00:54.482'); --",
          "file": "/etc/modsecurity.d/owasp-crs/rules/REQUEST-942-APPLICATION-ATTACK-SQLI.conf",
          "lineNumber": "432",
          "match": "Matched \"Operator `Rx' with parameter `(?i)create[\\s\\x0b]+function[\\s\\x0b].+[\\s\\x0b]returns|;[\\s\\x0b]*?(?:alter|(?:(?:cre|trunc|upd)at|renam)e|d(?:e(?:lete|sc)|rop)|(?:inser|selec)t|load)\\b[\\s\\x0b]*?[\\(\\[]?[0-9A-Z_a-z]{2,}' against variable `ARGS:text' (Value: `','2025-11-08 09:00:54.482','2025-11-08 09:00:54.482'); INSERT INTO text_entries (content, created_a (148 characters omitted)' )",
          "maturity": "0",
          "reference": "o54,13v1531,248t:urlDecodeUni",
          "rev": "",
          "ruleId": "942350",
          "severity": "2",
          "tags": [
            "modsecurity",
            "application-multi",
            "language-multi",
            "platform-multi",
            "attack-sqli",
            "paranoia-level/1",
            "OWASP_CRS",
            "OWASP_CRS/ATTACK-SQLI",
            "capec/1000/152/248/66"
          ],
          "ver": "OWASP_CRS/4.19.0"
        },
        "message": "Detects MySQL UDF injection and other data/structure manipulation attempts"
      },
      {
        "details": {
          "accuracy": "0",
          "data": "Matched Data: ','2025-11-08 09:00:54.482','2025-11-08 09:00:54.482'); INSERT INTO found within ARGS:text: ','2025-11-08 09:00:54.482','2025-11-08 09:00:54.482'); INSERT INTO text_entries (content, created_at, updated_at) VALUES ( (select COALESCE(STRING_AGG(content,','),'NONE') from text_entries),'2025-11-08 09:00:54.482','2025-11-08 09:00:54.482'); --",
          "file": "/etc/modsecurity.d/owasp-crs/rules/REQUEST-942-APPLICATION-ATTACK-SQLI.conf",
          "lineNumber": "471",
          "match": "Matched \"Operator `Rx' with parameter `(?i)\\b(?:(?:alter|(?:(?:cre|trunc|upd)at|renam)e|de(?:lete|sc)|(?:inser|selec)t|load)[\\s\\x0b]+(?:char|group_concat|load_file)\\b[\\s\\x0b]*\\(?|end[\\s\\x0b]*?\\);)|[\\s\\x0b\\(]load_file[\\s\\x0b]*?\\(|[\\\"'`][\\s\\ (1249 characters omitted)' against variable `ARGS:text' (Value: `','2025-11-08 09:00:54.482','2025-11-08 09:00:54.482'); INSERT INTO text_entries (content, created_a (148 characters omitted)' )",
          "maturity": "0",
          "reference": "o0,67v1531,248t:urlDecodeUni",
          "rev": "",
          "ruleId": "942360",
          "severity": "2",
          "tags": [
            "modsecurity",
            "application-multi",
            "language-multi",
            "platform-multi",
            "attack-sqli",
            "paranoia-level/1",
            "OWASP_CRS",
            "OWASP_CRS/ATTACK-SQLI",
            "capec/1000/152/248/66"
          ],
          "ver": "OWASP_CRS/4.19.0"
        },
        "message": "Detects concatenated basic SQL injection and SQLLFI attempts"
      },
      {
        "details": {
          "accuracy": "0",
          "data": "",
          "file": "/etc/modsecurity.d/owasp-crs/rules/REQUEST-949-BLOCKING-EVALUATION.conf",
          "lineNumber": "222",
          "match": "Matched \"Operator `Ge' with parameter `5' against variable `TX:BLOCKING_INBOUND_ANOMALY_SCORE' (Value: `20' )",
          "maturity": "0",
          "reference": "",
          "rev": "",
          "ruleId": "949110",
          "severity": "0",
          "tags": [
            "modsecurity",
            "anomaly-evaluation",
            "OWASP_CRS"
          ],
          "ver": "OWASP_CRS/4.19.0"
        },
        "message": "Inbound Anomaly Score Exceeded (Total Score: 20)"
      }
    ],
    "producer": {
      "components": [
        "OWASP_CRS/4.19.0\""
      ],
      "connector": "ModSecurity-nginx v1.0.4",
      "modsecurity": "ModSecurity v3.0.14 (Linux)",
      "secrules_engine": "Enabled"
    },
    "request": {
      "headers": {
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
        "Accept-Encoding": "gzip, deflate, br, zstd",
        "Accept-Language": "en-US,en;q=0.9",
        "Cache-Control": "max-age=0",
        "Content-Length": "339",
        "Content-Type": "application/x-www-form-urlencoded",
        "Cookie": "hubspotutk=ea1eb2f710d72a4119d081d2efd2c56e; cf_clearance=UVzgs.PIkLi6Szn9W7r1_6C5ut_j1A.E3nBllKsFnMs-1762464647-1.2.1.1-lBehBU366aP8eWozgLJfa3TdPiseg1t8Gx4mcsIgj8LP7zTfwyBqmxtpRIVEZMt3uJaT_onWpajWA_eoHU4QKHaJRyABNkM.A8Ge0egiw20JE9M4i6mFtfkh6L59CbBAzgLnpQR5eBhYURe7Sv66FGjGz7.2hSwQ9G_AK465FDYCQ5eqrdtQCh9LSoWqEuyOBT4Gg12LatLE8p1YCdH664QCYor09uZWnCiqG7k4VYs; __hstc=79357195.ea1eb2f710d72a4119d081d2efd2c56e.1755378752825.1755378752825.1762464647152.2; __hssrc=1; JSESSIONID=BDCD9206F79EC0580A43F78BF71F75BB",
        "Host": "careless.firebind.com",
        "Origin": "https://careless.firebind.com",
        "Priority": "u=0, i",
        "Referer": "https://careless.firebind.com/page-one",
        "Sec-Ch-Ua": "\"Chromium\";v=\"142\", \"Google Chrome\";v=\"142\", \"Not_A Brand\";v=\"99\"",
        "Sec-Ch-Ua-Mobile": "?0",
        "Sec-Ch-Ua-Platform": "\"Linux\"",
        "Sec-Fetch-Dest": "document",
        "Sec-Fetch-Mode": "navigate",
        "Sec-Fetch-Site": "same-origin",
        "Sec-Fetch-User": "?1",
        "Upgrade-Insecure-Requests": "1",
        "User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36",
        "X-Forwarded-For": "64.163.3.11",
        "X-Forwarded-Host": "careless.firebind.com",
        "X-Forwarded-Port": "443",
        "X-Forwarded-Proto": "https",
        "X-Forwarded-Server": "0687d1944a1a",
        "X-Real-Ip": "64.163.3.11"
      },
      "http_version": 1.1,
      "method": "POST",
      "uri": "/page-one/submit"
    },
    "response": {
      "body": "<html>\r\n<head><title>403 Forbidden</title></head>\r\n<body>\r\n<center><h1>403 Forbidden</h1></center>\r\n<hr><center>nginx</center>\r\n</body>\r\n</html>\r\n<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n<!-- a padding to disable MSIE and Chrome friendly error page -->\r\n",
      "headers": {
        "Access-Control-Allow-Headers": "*",
        "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, OPTIONS",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Max-Age": "3600",
        "Connection": "keep-alive",
        "Content-Length": "548",
        "Content-Type": "text/plain",
        "Date": "Sun, 09 Nov 2025 21:31:17 GMT",
        "Server": "nginx"
      },
      "http_code": 403
    },
    "server_id": "7bbc27cd238d558141bcd1dbd716315d9dca41a5",
    "time_stamp": "Sun Nov  9 21:31:17 2025",
    "unique_id": "176272387782.731488"
  }
}
```