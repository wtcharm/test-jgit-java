package com.xfqb.jgit.util;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xfqb.jgit.config.JgitConfig;

/**
 * git 业务实现类
 * @ClassName: JgitUtils 
 * @date 2018年12月17日
 * @author tang wang
 */

@Component
public class JgitUtils {

	@Autowired
	private JgitConfig jgitConfig;

	/**
	 * 输出当项目每次提交的提交人和提交内容
	 * @param localPath 项目路径
	 * @return   返回提交的JSONArray对象
	 * [{"mge":"信息","name":"提交人"},{...}]
	 * @author tao wang
	 * @date 2018年12月20日
	 */
	
	public JSONArray log(String localPath) {
		return this.jgitConfig.log(getLocalPath(localPath));	
	}

	/**
	 * 进行git远程的克隆
	 * @param remotePath 	克隆的git地址
	 * @param localPath  	克隆之后保存的地址
	 * @param dev 		  	拉取的分支
	 * @param username   	git 用户名
	 * @param password		git 密码
	 * @return   拉取成功返回true 否则返回false
	 * @author tao wang
	 * @date 2018年12月17日
	 */

	public boolean getGitClone(String remotePath, String localPath, String dev, String username, String password) {
		return this.jgitConfig.isGitClone(remotePath, localPath, dev, username, password);
	}	

	/**
	 * 创建git本地仓库
	 * @param initPath 创建的本地路径
	 * @return   创建成功返回true否则返回flase
	 * @author tao wang
	 * @date 2018年12月17日
	 */

	public boolean setCreateGitLocal(String initPath) {
		return this.jgitConfig.createGitLocal(getLocalPath(initPath));
	}

	/**
	 * 添加单个文件到本地仓库
	 * @param localPath 本地路径 
	 * @param fileUrl 本地路径/文件名称.后缀
	 * @param filename  文件名称
	 * @return   添加成功返回true  否则返回false
	 * @author tao wang
	 * @date 2018年12月17日
	 */

	public boolean setAddGitLocal(String localPath, String fileUrl, String filename) {
		return this.jgitConfig.addGitLocal(getLocalPath(localPath), fileUrl, filename);
	}

	/**
	 * 添加多个文件到本地仓库
	 * @param gitArray 添加的多个文件路径
	 * @return   添加成功返回true 否则返回false
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	public boolean setAddGitLoca(JSONArray gitArray) {
		for (int i = 0; i < gitArray.size(); i++) {
			JSONObject obj = (JSONObject) gitArray.get(i);
			String localPath = getLocalPath(obj.getString("localPath"));
			String fileUrl = obj.getString("fileUrl");
			String filename = obj.getString("filename");
			this.jgitConfig.addGitLocal(localPath, fileUrl, filename);
		}
		return true;
	}

	/**
	 * 进行本地代码的提交
	 * @param localPath 本地路径
	 * @param commit	提交的描述
	 * @return   提交成功返回true否则返回false
	 * @author tao wang
	 * @date 2018年12月17日
	 */

	public boolean setCommitGitLocal(String localPath, String commit) {
		if (StringUtils.isBlank(commit)) {
			return false;
		}
		return this.jgitConfig.commitGitLocal(getLocalPath(localPath), commit);
	}

	/**
	 * 将远程仓库的数据拉取到本地,分支不填 默认是master分支
	 * @param localPath		本地路径
	 * @param dev 			要拉取的分支(分支默认是master)
	 * @param username		git用户名	
	 * @param password		git密码
	 * @return   拉取成功返回true否则返回false
	 * @author tao wang
	 * @date 2018年12月17日
	 */

	public boolean setPullGitLocal(String localPath, String dev, String username, String password) {
		//等于null的时候
		if (StringUtils.isBlank(dev)) {
			dev = "master";
		}
		return this.jgitConfig.pullGitLocal(getLocalPath(localPath), dev, username, password);
	}

	/**
	 * 将本地项目进行远程推送
	 * @param localPath		本地路径
	 * @param username		用户名称
	 * @param password		用户密码
	 * @return   推送成功返回true否则返回false
	 * @author tao wang
	 * @date 2018年12月17日
	 */

	public boolean setPushGitLocal(String localPath, String username, String password) {
		return this.jgitConfig.pushGitLocal(getLocalPath(localPath), username, password);
	}

	/**
	 * 获取当前Git路径下的分支数据
	 * @param localPath git 路径
	 * @return   返回分支数据集合
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	public List<String> getRefGitAll(String localPath) {
		return this.jgitConfig.refGitAll(getLocalPath(localPath));
	}

	/**
	 * 添加新的git 分支
	 * @param 	localPath 项目路径
	 * @param 	branchName 分支名称
	 * @return  添加成功 返回true 否则返回false
	 * @author tao wang 
	 * @date 2018年12月18日
	 */
	public boolean setCreateRefGit(String localPath, String refName) {
		return this.jgitConfig.createRefGit(getLocalPath(localPath), refName);                
	}

	/**
	 * 删除当前项目的分支
	 * @param localPath 项目路径
	 * @param refName   分支名称
	 * @return   删除成功返回 true 否则返回 false
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	public boolean getDeleteRefGit(String localPath, String refName) {
		return this.jgitConfig.deleteRefGit(getLocalPath(localPath), refName);                
	}

	/**
	 * 进行分支添加，如果当前分支存在则先进行删除在进行添加
	 * @param localPath 项目路径
	 * @param refName   分支名称
	 * @return   添加成功返回true否则返回false
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	public boolean setCreFirstlyDel(String localPath, String refName) {
		this.getDeleteRefGit(localPath, refName);
		this.setCreateRefGit(localPath, refName);
		return true;
	}

	/**
	 * 进行分支切换,如果当前分支不存在则创建分支
	 * @param localPath 项目路径
	 * @param branch	要切换的分支
	 * @return   切换成功返回true 否则返回false
	 * @author tao wang
	 * @date 2018年12月20日
	 */

	public boolean setSwitchRrf(String localPath, String refName) {
		return this.jgitConfig.switchBranch(getLocalPath(localPath), refName);
	}

	/**
	 * 项目路径 添加git 后缀
	 * @param localPath 项目路径
	 * @return   添加后缀之后的数据
	 * @author tao wang
	 * @date 2018年12月20日
	 */

	private String getLocalPath(String localPath) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(localPath);
		sbf.append("/.git");
		return sbf.toString();
	}
	
}
