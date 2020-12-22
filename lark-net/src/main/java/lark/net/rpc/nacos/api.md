#服务列表
curl -X GET 'http://register-qa.sanqlt.com:8848/nacos/v1/ns/service/list?pageNo=1&pageSize=100'
->
```
{
"doms": [
"lark-example-api",
"lark-example-service"
],
"count": 2
}
```

#服务详情
curl -X GET 'http://register-qa.sanqlt.com:8848/nacos/v1/ns/service?serviceName=lark-example-service'
->
```
{
"name": "lark-example-service",
"namespaceId": "public",
"protectThreshold": 0,
"metadata": { },
"selector": {
"type": "none"
},
"groupName": "DEFAULT_GROUP",
"clusters": [
{
"name": "DEFAULT",
"healthChecker": {
"type": "TCP"
},
"metadata": { }
}
]
}
```

#节点列表
curl -X GET 'http://register-qa.sanqlt.com:8848/nacos/v1/ns/instance/list?serviceName=lark-example-service'
->
```
{
"hosts": [
{
"ip": "172.16.0.24",
"port": 3001,
"valid": true,
"healthy": true,
"marked": false,
"instanceId": "172.16.0.24#3001#DEFAULT#DEFAULT_GROUP@@lark-example-service",
"metadata": {
"preserved.register.source": "SPRING_CLOUD"
},
"enabled": true,
"weight": 1,
"clusterName": "DEFAULT",
"serviceName": "lark-example-service",
"ephemeral": true
}
],
"dom": "lark-example-service",
"name": "DEFAULT_GROUP@@lark-example-service",
"cacheMillis": 3000,
"lastRefTime": 1608545898222,
"checksum": "c86a1a12eca9ec68cdfefb6ba269c38b",
"useSpecifiedURL": false,
"clusters": "",
"env": "",
"metadata": { }
}
```

#节点详情
curl -X GET 'http://register-qa.sanqlt.com:8848/nacos/v1/ns/instance?serviceName=lark-example-service&ip=172.16.0.24&port=3001&cluster=DEFAULT'
->
```
{
"service": "DEFAULT_GROUP@@lark-example-service",
"ip": "172.16.0.24",
"port": 3001,
"clusterName": "DEFAULT",
"weight": 1,
"healthy": true,
"instanceId": "172.16.0.24#3001#DEFAULT#DEFAULT_GROUP@@lark-example-service",
"metadata": {
"preserved.register.source": "SPRING_CLOUD"
}
}
```
