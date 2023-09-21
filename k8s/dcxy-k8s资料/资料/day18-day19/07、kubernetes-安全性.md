<center>
<h1>
    Kubernetes 安全性
    </h1>    
</center>

https://kubernetes.io/zh/docs/concepts/security/controlling-access/

![Kubernetes API 请求处理步骤示意图](assets/access-control-overview.svg)

NFS的动态供应； Pod；pvc---自动创建pv

k8s会认为每个Pod也可以是操作集群的一个用户。给这个用户会给一个ServiceAccount（服务账号）



权限控制流程：

- 用户携带令牌或者证书给k8s的api-server发送请求要求修改集群资源
- k8s开始认证。认证通过
- k8s查询用户的授权（有哪些权限）
- 用户执行操作。过程中的一些操作（cpu、内存、硬盘、网络等....），利用准入控制来判断是否可以允许这样操作









# 一、RBAC

什么是RBAC？（基于角色的访问控制）

RBAC API 声明了四种 Kubernetes 对象：`Role、ClusterRole、RoleBinding 和 ClusterRoleBinding`

Role：基于名称空间的角色。可以操作名称空间下的资源

​	RoleBinding： 来把一个Role。绑定给一个用户

ClusterRole：基于集群的角色。可以操作集群资源

​	ClusterRoleBinding： 来把一个ClusterRole，绑定给一个用户





# 二、ClusterRole与Role

- RBAC 的 *Role* 或 *ClusterRole* 中包含一组代表相关权限的规则。 这些权限是纯粹累加的（不存在拒绝某操作的规则）。
- Role 总是用来在某个[名称空间](https://kubernetes.io/zh/docs/concepts/overview/working-with-objects/namespaces/) 内设置访问权限；在你创建 Role 时，你必须指定该 Role 所属的名字空间
- ClusterRole 则是一个集群作用域的资源。这两种资源的名字不同（Role 和 ClusterRole）是因为 Kubernetes 对象要么是名字空间作用域的，要么是集群作用域的， **不可两者兼具。**

> 我们kubeadm部署的apiserver是容器化部署的。默认没有同步机器时间。

## 1、Role

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: default
  name: pod-reader
rules:
- apiGroups: [""] # "" 标明 core API 组
  resources: ["pods"]
  verbs: ["get", "watch", "list"]
```

注意：资源写复数形式

## 2、ClusterRole

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  # "namespace" 被忽略，因为 ClusterRoles 不受名字空间限制
  name: secret-reader
rules:
- apiGroups: [""]
  # 在 HTTP 层面，用来访问 Secret 对象的资源的名称为 "secrets"
  resources: ["secrets"]
  verbs: ["get", "watch", "list"]
```



## 3、常见示例

https://kubernetes.io/zh/docs/reference/access-authn-authz/rbac/#role-examples



# 四、RoleBinding、ClusterRoleBinding









# 五、ServiceAccount

## 1、创建ServiceAccount

- 每个名称空间都会有自己默认的服务账号
  - 空的服务账号。
  - 每个Pod都会挂载这个默认服务账号。
  - 每个Pod可以自己声明 serviceAccountName： lfy
  - 特殊Pod（比如动态供应等）需要自己创建SA，并绑定相关的集群Role。给Pod挂载。才能操作

集群几个可用的角色

```yaml
cluster-admin:  整个集群全部全部权限  *.* ** *
admin: 很多资源的crud，不包括 直接给api-server发送http请求。/api
edit: 所有资源的编辑修改创建等权限
view: 集群的查看权限
```





## 2、测试基于ServiceAccount的rbac

```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: lfy
  namespace: default
# ---
# ## 写Role
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  # namespace: default  ## 所属的名称空间
  name: ns-reader 
rules: ## 当前角色的规则
- apiGroups: [""] # "" 标明 core API 组
  resources: ["namespaces"] ## 获取集群的所有名称空间
  verbs: ["get", "watch", "list"] # 动词。
---
## 编写角色和账号的绑定关系
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: read-ns-global
subjects:  ## 主体
- kind: ServiceAccount
  name: lfy # 'name' 是不区分大小写的
  namespace: default
  apiGroup: ""
roleRef:
  kind: ClusterRole
  name: ns-reader 
  apiGroup: rbac.authorization.k8s.io
```









## 3、使用命令行

```sh
## role
kubectl create role pod-reader --verb=get --verb=list --verb=watch --resource=pods
kubectl create role pod-reader --verb=get --resource=pods --resource-name=readablepod --resource-name=anotherpod
kubectl create role foo --verb=get,list,watch --resource=replicasets.apps
kubectl create role foo --verb=get,list,watch --resource=pods,pods/status
kubectl create role my-component-lease-holder --verb=get,list,watch,update --resource=lease --resource-name=my-component
```



```sh
#kubectl create clusterrole
kubectl create clusterrole pod-reader --verb=get,list,watch --resource=pods
kubectl create clusterrole pod-reader --verb=get --resource=pods --resource-name=readablepod --resource-name=anotherpod
kubectl create clusterrole foo --verb=get,list,watch --resource=replicasets.apps
kubectl create clusterrole foo --verb=get,list,watch --resource=pods,pods/status
kubectl create clusterrole "foo" --verb=get --non-resource-url=/logs/*
kubectl create clusterrole monitoring --aggregation-rule="rbac.example.com/aggregate-to-monitoring=true"
```



```sh
#kubectl create rolebinding
kubectl create rolebinding bob-admin-binding --clusterrole=admin --user=bob --namespace=acme
kubectl create rolebinding myapp-view-binding --clusterrole=view --serviceaccount=acme:myapp --namespace=acme
kubectl create rolebinding myappnamespace-myapp-view-binding --clusterrole=view --serviceaccount=myappnamespace:myapp --namespace=acme

```



```sh
###kubectl create clusterrolebinding
kubectl create clusterrolebinding myapp-view-binding --clusterrole=view --serviceaccount=acme:myapp
```





## 4、扩展-RestAPI访问k8s集群

步骤：

- 1、创建ServiceAccount、关联相关权限
- 2、使用ServiceAccount对应的Secret中的token作为http访问令牌
- 3、可以参照k8s集群restapi进行操作



请求头 Authorization: Bearer 自己的token即可



java也可以这样

```xml
<dependency>
    <groupId>io.kubernetes</groupId>
    <artifactId>client-java</artifactId>
    <version>10.0.0</version>
</dependency>
```



