# fastdfs install

* 文件下载
	* 请到 https://github.com/happyfish100/(作者) 下载文件包括：fastdfs、libfastcommon、fastdfs-nginx-module
* 安装依赖
	* gcc环境，需要安装gcc
	yum install -y gcc gcc-c++
	* FastDFS依赖libevent库，需要安装
	yum -y install libevent
	* nginx依赖，需要安装
		1. yum install gcc-c++
		2. yum install -y pcre pcre-devel
		3. yum install -y zlib zlib-devel
		4. yum install -y openssl openssl-devel
* 安装fastdfs
	* 解压，进入目录，执行：编译：./make.sh , 安装 ./make.sh install
* 配置tracker
	* 当安装完成后会在 /etc/fdfs下面生成 fastdfs所需的配置文件
	* 复制配置文件 cp tracker.conf.sample tracker.conf
	* 编辑配置文件，将 
		1. *base_path=/home/yuqing/fastdfs* 改为 *base_path=/data/fastdfs*
	* 启动，/etc/init.d/fdfs_trackerd start
* 配置storage
	* 复制配置文件 cp tracker.conf.sample tracker.conf
	* 编辑配置文件，将 
		1. *base_path=/home/yuqing/fastdfs* 改为 *base_path=/data/fastdfs/storage*
		2. *store_path0=/home/yuqing/fastdfs* 改为 *store_path0=/data/fastdfs/storage*
		3. *tracker_server=192.168.209.121:22122* 改为 *本机公网IP:22122*
	* 启动，/etc/init.d/fdfs_storage start
* 开通端口
	* 开通fastdfs所需端口 22122、23000
* 测试上传
	* 复制 cp client.conf.sample client.conf
	* 编辑，将：
		1. *base_path=/home/yuqing/fastdfs* 改为 *base_path=/data/fastdfs*
		2. *tracker_server=192.168.0.197:22122* 改为 *本机内网IP:22122*
	* 测试，/usr/bin/fdfs_upload_file /etc/fdfs/client.conf upload /img/test.jpg
* 配置nginx
	* 配置fastdfs-nginx-module 模块
		1. 解压 fastdfs-nginx-module 并移动到 /usr/local/下面
		2. 修改 fastdfs-nginx-module/src/config, 配置替换配置文件为：
			* ngx_module_incs="/usr/include/fastdfs /usr/include/fastcommon/"
			* CORE_INCS="$CORE_INCS /usr/include/fastdfs /usr/include/fastcommon/"
	* 安装nginx和 fastdfs-nginx-module
		1. 下载nginx，并解压
		2. 进入nginx配置 
		```
			./configure --add-module=../fastdfs-nginx-module-master/src
		```
		3. 编译安装nginx
		```
			make && make install
		```
	* 配置nginx和fastdfs-nginx-module
		1. 复制 fastdfs-nginx-module安装目录下面 conf/mod_fastdfs.conf 到 /etc/fdfs/下， 修改如下：
			* base_path=/tmp 修改为 base_path=/data/fastdfs
			* tracker_server=tracker:22122 修改为 tracker_server= 本机公网:22122
			* store_path0=/home/yuqing/fastdfs 修改为 store_path0=/data/fastdfs/storage
			* url_have_group_name=false 修改为 url_have_group_name=true
		2. 复制 fastdfs安装目录下的 conf/http.conf、conf/mime.types 到 /etc/fdfs/下
	* 配置nginx启动
		1. 修改 /usr/local/nginx/config/nginx.config ,添加
			```
				location /group1/M00/{
						ngx_fastdfs_module;
				}
			```
		2. 启动nginx /usr/local/nginx/sbin/nginx
