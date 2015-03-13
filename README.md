AD Password Handler
=======

Overview
-------------
If you are a linux guy trapped in a corporate windows environment with an active directory domain controller and annoying password policies, this tool is for you. It checks the expiration of your active directory password on your linux machine. If the password will expire in a given time, a popup is shown where you can directly change it:

![Screenshot](https://raw.githubusercontent.com/chrisipa/ad-password-handler/master/public/screenshot_password_change.png)

Prerequisites
-------------
* [smbpasswd](http://www.samba.org/samba/docs/man/manpages-3/smbpasswd.8.html) must be installed
* [Java 6](http://www.oracle.com/technetwork/java/javase/downloads/index.html) must be installed
* [JAVA_HOME](http://docs.oracle.com/cd/E19182-01/820-7851/inst_cli_jdk_javahome_t/index.html) environment variable has to be set
* SMB share must be mounted with fstab and a [credentials file](http://www.samba.org/samba/docs/using_samba/ch05.html#samba2-CHP-5-SECT-4.1)

Installation
-------------
* Download and extract the zip archive:
```
sudo wget https://raw.githubusercontent.com/chrisipa/ad-password-handler/master/public/ad-password-handler.zip
sudo unzip ad-password-handler.zip -d /opt
```

Configuration
-------------
* Create the config directory:
```
mkdir ~/.ad-password-handler
```
* Create the SMB credentials file with your user account data:
```
vi ~/.ad-password-handler/smb.properties
...
username=my-username
password=my-password
domain=my-domain
```
* Create the AD configuration file with system user account data:
```
vi ~/.ad-password-handler/ad.properties
...
host=my-ad-controller-hostname-or-ip
port=389
baseDn=DC=my,DC=domain,DC=grp
userDn=CN=Name,OU=Group,OU=Users,OU=Organisation,DC=my,DC=domain,DC=grp
userSecret=Password
```
* You can get your AD configuration with a tool called [AD-Explorer](http://technet.microsoft.com/de-de/sysinternals/bb963907.aspx)
* For security reasons you should be the only one who can read the config directory:
```
chown -R myuser.mygroup ~/.ad-password-handler
chmod -R 700 ~/.ad-password-handler
```

Usage
-------------
* Show help text:
```
/opt/ad-password-handler/ad-password-handler --help
usage: ad-password-handler
 -c,--config-dir <arg>          Config directory with smb credentials and
                                ad configuration
                                [default: ~/.ad-password-handler]
 -d,--days-till-warning <arg>   Days until password expiration to show
                                warning
                                [default: 14]
 -h,--help                      Print this help text
```
* Run directly from console:
```
/opt/ad-password-handler/ad-password-handler
```
* Setup as session script:
```
gnome-session-properties -> add script
```
* Share your SMB credentials with fstab:
```
sudo vi /etc/fstab
...
//10.10.10.1/share /media/share smbfs credentials=/home/myuser/.ad-password-handler/smb.properties,uid=1000,gid=1000 0 0
```