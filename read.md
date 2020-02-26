







用户表：

| u_id（primary key） | userName | password | others   |
| ------------------- | -------- | -------- | -------- |
| 用户id              | 用户名   | 密码     | 其它信息 |



角色表：

| r_id（primary key） | r_name | r_type                            |
| ------------------- | ------ | --------------------------------- |
| 角色id              | 角色名 | 角色类型（管理员，普通/特殊用户） |



权限表：

| p_id（primary key） | p_url             | p_name | p_code |
| ------------------- | ----------------- | ------ | ------ |
| 权限id              | url（正则表达式） | 权限名 | 权限码 |

权限码格式为x+p_id，x是一个字符，（p:系统关键权限，a:一般权限，b...自设）



用户-角色表：

| r_id（foreign key） | u_id（foreign key） |
| ------------------- | ------------------- |
| 角色id              | 用户id              |



角色-权限表：

| r_id（foreign key） | p_id（foreign key） |
| ------------------- | ------------------- |
| 角色id              | 权限id              |



个人感觉前后端分离项目中，后端提供可访问的api接口，api可访问性的权限是很扁平的

一个权限可以表示为一个正则表达式，如/system/.*可以匹配所有/system/...下的路径，如果这个是所有系统管理的api，一个子权限如日志查看(url:/system/log/)，另外有一个角色有查看日志的权限，无其它权限，那么就可以给这个角色一个正则表达式为/system/log/的权限，严格匹配日志查看的api地址。

在逻辑上权限是有父子继承关系，如上面的/system/log/是/system/.\*的子权限，把所有权限同等对待，尽管会出现同时包含/system/log/和/system/.\*的情况，确实是重复了，但是对于一个小型的项目并不会有太多的权限，或者说可以把权限的路径设计得合理一些，因此可以忽略它。角色也是如此。

约定权限都是平等关系，无继承等复杂关系，角色同理。

用户可以有多个角色，其权限为所拥有的角色的权限的并，正是因为权限平等，可以简单地用并来处理。





2019/12/5

添加权限的增删改查功能。

对于如何给用户赋权限有点迷惑，想了几种方式都有些问题，最后决定由系统管理员（高级别管理员）创建权限和角色，并对角色赋权限，用户管理员只能对其他用户赋角色，且所能赋的角色在该用户管理者自身角色的范围之内，避免越权行为。



给角色赋权限，原本简单的设计为前端传一个角色id和一个权限id的数组即可，然后想想还是p:系统预设，g:普通，...自设使用权限码代替，首先权限码包含了权限的id，然后token中又带有权限码，这样可以免去额外的数据查询，又能够控制权限的合法性。



那么给用户赋角色呢，token中可没有携带角色的信息，本着能不查数据库就不查的想法，还是在token中添加上角色的信息。于是就需要一个角色码的东西，参考权限码，权限码不就是权限类型加上id，正好角色不也有个角色类型，把角色类型和角色id拼一下不就好了。之前权限直接存了权限码，权限码只对外部有用，完全可以只存一个权限类型，然后权限码在外部拼成，这样貌似可以省点空间了。

角色表：

| r_id（primary key） | r_name | r_type                                |
| ------------------- | ------ | ------------------------------------- |
| 角色id              | 角色名 | a:管理员，g:普通，v:特殊用户，...自设 |

r_type=g的权限为普通角色，所有自主注册的用户都会获得这些角色



权限表：

| p_id（primary key） | p_url             | p_name | p_type                      |
| ------------------- | ----------------- | ------ | --------------------------- |
| 权限id              | url（正则表达式） | 权限名 | p:系统预设，g:普通，...自设 |



又有问题出现了，当系统管理员创建一个权限后，为了传递这个权限，可以使系统管理员角色拥有这个权限。但角色却不能这样轻易解决，按照原来的方法，一个系统管理员创建一个角色，这个系统管理员拥有这个角色，这个角色可以从这个系统管理员传递，但是其他系统管理员不拥有新建的角色，则无法传递，这与常理不符合。暂时想到的方法有：1.一个系统管理员新建角色后，为所有拥有系统管理员角色的用户添加新角色；2.系统管理员默认拥有所有的角色，即若角色码中包含系统管理员角色，就开放所有的角色传递。个人更倾向于第二种，但还存在着问题，如果前端传回来的角色码中存在数据库中没有的角色码，这就会留下安全的隐患，所以需要查询一次数据库，拿出所有的角色码，跟传回的角色码比对。



2019/12/6



整体框架基本差不多了，用postman测试了一下，基本没什么问题。开始着手写一些简单的前端，用来进一步测试。

发现问题：

1.删除权限时，由于权限id是角色-权限表的一个外键，要先删除外键。	

2.删除权限后，登录的用户的token中还保留着原来的权限码，但是问题不大，权限控制会用权限码去查一边数据库，找不到相应的url正则表达式，达到了预期的效果。权限码会在在下次重新登录的时候更新。

3.每个角色需要有一个级别码，用来角色之间地位高低的比较。用户也有一个级别码，其值为该用户所拥有的角色的级别码的最大值。在用户管理的时候，具有用户管理权限的用户只能管理级别比自己低的用户，且为低级别用户赋角色所能到达的最大级别也要小于自身的级别。



2019/12/7

终于写完了昨日留下的坑

删除权限后，需要更新相关用户的级别码。



下一步要完成对用户的撤销角色和对角色撤销权限

直接在用户-角色表和角色-权限表中删除，用户去角色还需要更新该用户的级别码。



2019/12/10

最近几日在研究前端，最后还是决定使用element UI + vue
vue对新手真心不太友好，研究了很久，还是很懵。

2020/1/12

添加权限：系统管理员用户在执行添加权限的操作时，如果操作成功，后台会重新计算该用户的新token并返回给前端。其他用户将会在重新登陆后，生效该权限。

删除权限：由于处于第一次的用户名密码验证，其它验证都依赖token。当一个权限被删除，可能会影响相应角色，从而影响用户。正处于登录状态的用户只有重新登录才会更新token，但是在未更新前对应权限依然可访问，这里就需要考虑一下。
可以查询出哪些用户将会被影响，然后在redis中直接删除这些用户的token，实现强制踢下线的功能。

准备制作静态资源权限管理模块（文件,图片,音频……）

file:

| f_id（primary key） | rile_name | file_url|
| ------------------- | ------ | ------------------------------------- |
| 资源id  | 资源名 | 资源的地址 |

| f_id | type | user_id | group_id|  permission | ispublic|
| ------------------- | ---------------------------- | -----|---------- | ------| ---|
| 资源id  | 权限类型 当type为0时user_id;为1时group_id| 用户id |用户组id|三位二进制数表示：读写改 | 是否公开|

对该类文件使用统一使用handle来处理

请求资源过程：
http请求（f_id，u_id） --->handle<br/>
判断file类型，如是否为public，在根据对应文件id，用户id和操作类型进行判断该用户对该文件的操作是否合法<br/>
非法直接返回错误信息<br/>
合法则去数据库中读出file的url，根据url读取文件的二进制流，并返回<br/>














