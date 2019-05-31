package com.xfqb.jgit.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * git 底层类
 * @ClassName: JgitConfig 
 * @date 2018年12月17日
 * @author tang wang
 */

@Slf4j
@Component
public class JgitConfig {


	/**
	 * 输日志
	 * @param localPath 项目路径
	 * @return   返回日志数据
	 * @author tao wang
	 * @date 2018年12月20日
	 */
	public JSONArray log(String localPath) {
		try {
			JSONArray result = new JSONArray();
			Git git = Git.open(new File(localPath));
			LogCommand logCommand = git.log();
			Iterable<RevCommit> list = logCommand.call();
			for (RevCommit revCommit : list) {
				JSONObject obj = new JSONObject();
				obj.put("msg", revCommit.getFullMessage());
				obj.put("name", revCommit.getName());
				result.add(obj);
			}
			log.info("项目提交日志输出  ----- : " + result);
			return result;
		} catch (Exception e) {
			log.info("远程拉取失败");
			return null;
		}
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

	public boolean isGitClone(String remotePath, String localPath, String dev, String username, String password) {
		try {

			//克隆代码库命令
			CloneCommand cloneCommand = Git.cloneRepository();
			//设置远程URI
			cloneCommand.setURI(remotePath) 
			//设置clone下来的分支
			.setBranch(dev) 
			//设置下载存放路径
			.setDirectory(new File(localPath)) 
			//设置权限验证
			//设置远程服务器上的用户名和密码
			.setCredentialsProvider(credentialsProvider(username, password))
			.call();
			return true;
		} catch (Exception e) {
			log.info("远程拉取失败");
			return false;
		}
	}	

	/**
	 * 创建git本地仓库
	 * @param initPath 创建的本地路径
	 * @return   创建成功返回true否则返回flase
	 * @author tao wang
	 * @date 2018年12月17日
	 */

	public boolean createGitLocal(String initPath) {
		try {
			//本地新建仓库地址
			Repository newRepo  = FileRepositoryBuilder.create(new File(initPath));
			newRepo.create();
			return true;
		} catch (IOException e) {
			log.info("本地库 " + initPath + " 创建失败");
			return false;
		}
	}

	/**
	 * 添加单个文件到本地数据
	 * @param localPath 本地路径 + /文件名称.后缀
	 * @param filename  文件名称
	 * @return   添加成功返回true  否则返回false
	 * @author tao wang
	 * @date 2018年12月17日
	 */

	public boolean addGitLocal(String localPath, String fileUrl, String filename) {
		try {
			File myfile = new File(fileUrl);
			myfile.createNewFile();
			//git仓库地址
			Git git = connectGit(localPath);
			//添加文件
			git.add().addFilepattern(filename).call();
			return true;
		} catch (Exception e) {
			log.info(" 当前 " + localPath + " 项目   " + filename + " 文件添加失败");
			return false;
		}
	}


	/**
	 * 进行本地代码的提交
	 * @param localPath 本地路径
	 * @param commit	提交的描述
	 * @return   提交成功返回true否则返回false
	 * @author tao wang
	 * @date 2018年12月17日
	 */

	public boolean commitGitLocal(String localPath, String commit) {
		try {
			//git仓库地址
			Git git = connectGit(localPath);
			//提交代码
			git.commit().setMessage(commit).call();
			return true;
		} catch (Exception e) {
			log.info("当前项目 " + localPath + " commit失败：" +  commit);
			return false;
		}
	}


	/**
	 * 将远程仓库的数据拉取到本地
	 * @param localPath		本地路径
	 * @param dev 			要拉取的分支
	 * @param username		git用户名	
	 * @param password		git密码
	 * @return   拉取成功返回true否则返回false
	 * @author tao wang
	 * @date 2018年12月17日
	 */

	public boolean pullGitLocal(String localPath, String dev, String username, String password) {
		try {
			//git仓库地址
			Git git = connectGit(localPath);
			git.pull().setRemoteBranchName(dev).
			setCredentialsProvider(credentialsProvider(username, password)).call();
			return true;
		} catch (Exception e) {
			log.info("本地项目拉取失败");
			return false;
		}
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

	public boolean pushGitLocal(String localPath, String username, String password) {
		try {
			//git仓库地址
			Git git = connectGit(localPath);
			git.push().setRemote("origin").
			setCredentialsProvider(credentialsProvider(username, password)).call();
			return true;
		} catch (Exception e) {
			log.info("本地项目推送失败");
			return false;
		}
	}

	/**
	 * 获取当前git 项目路径里面的分支目录
	 * @param 		localPath 项目路径
	 * @return   	返回分支集合
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	public List<String> refGitAll(String localPath) {
		List<String> result = new ArrayList<String>();
		try {
			Git git = connectGit(localPath);
			List<Ref> refs = git.branchList().call();
			for (Ref ref : refs) {
				result.add(subBranch(ref.getName()));
			}            
			return result;
		} catch (Exception e) {
			log.info("当前路径  ：" + localPath + " ：分支获取错误");
			return null;
		}  
	}

	/**
	 * 添加新的git 分支
	 * @param 	localPath 项目路径
	 * @param 	branchName 分支名称
	 * @return  添加成功 返回true 否则返回false
	 * @author tao wang 
	 * @date 2018年12月18日
	 */

	public boolean createRefGit(String localPath, String refName) {
		try {
			Git git = connectGit(localPath);     
			//新建分支
			Ref ref = git.branchCreate().setName(refName).call();
			//推送到远程
			git.push().add(ref).call();
			return true;                
		} catch (Exception e) {
			log.info("当前路径 : " + localPath + "  下  " + refName + " 分支创建失败");
			return false;                
		}
	}

	/**
	 * 删除当前项目的分支
	 * @param localPath 项目路径
	 * @param refName   分支名称
	 * @return   删除成功返回 true 否则返回 false
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	public boolean deleteRefGit(String localPath, String refName) {
		try {
			String newBranchIndex = newBranchIndex(refName);
			Git git = connectGit(localPath); 
			//检查新建的分支是否已经存在，如果存在则将已存在的分支强制删除并新建一个分支
			List<Ref> refs = git.branchList().call();
			for (Ref ref : refs) {
				if (ref.getName().equals(newBranchIndex)) {
					log.info("分支存在 " + refName + "进行删除");
					git.branchDelete().setBranchNames(refName).setForce(true)
					.call();
					break;
				}
			}            
			return true;                
		} catch (Exception e) {
			log.info("分支 " + refName + " 删除失败");
			return false;                
		}
	}


	/**
	 * 进行分支切换,如果当前分支不存在则创建分支
	 * @param localPath 项目路径
	 * @param branch	要切换的分支
	 * @return   切换成功返回true 否则返回false
	 * @author tao wang
	 * @date 2018年12月20日
	 */

	public boolean switchBranch(String localPath, String branch) {
		try {
			Git git = Git.open(new File(localPath));
			CheckoutCommand checkoutCommand = git.checkout();
			List<String> list = refGitAll(localPath);
			//如果本地分支
			if (!list.contains(branch)) {
				checkoutCommand.setStartPoint(branch);
				checkoutCommand.setCreateBranch(true);
			}
			checkoutCommand.setName(branch);
			checkoutCommand.call();
			return true;
		} catch (Exception e) {
			log.info("分支 " + branch + " 切换失败...");
			e.printStackTrace();
			return false;
		}
	}



	/**
	 * 本地目录进行git连接
	 * @param localPath git 本地路径
	 * @return   
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	private Git connectGit(String localPath) {
		try {
			Git git = new Git(new FileRepository(localPath));
			return git;
		} catch (IOException e) {
			log.info("git 连接失败");
			e.printStackTrace();
			return null;
		} 
	}


	/**
	 * 进行仓库连接,设置远程服务器上的用户名和密码
	 * @param username 用户名
	 * @param password 密码
	 * @return    返回连接对象
	 * @author tao wang
	 * @date 2018年12月20日
	 */

	private UsernamePasswordCredentialsProvider credentialsProvider(String username, String password) {
		UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider 
				= new UsernamePasswordCredentialsProvider(username, password);
		return usernamePasswordCredentialsProvider;
	}

	/**
	 * 进行分支创建的前面路径的拼接
	 * @param branch 分支名称
	 * @return   返回拼装好的分支路径
	 * @author tao wang
	 * @date 2018年12月20日
	 */

	private String newBranchIndex(String branch) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("refs/heads/");
		sbf.append(branch);
		return sbf.toString();
	}

	/**
	 * 进行分支路径的截取，获取到分支名称
	 * @param str
	 * @return   
	 * @author tao wang
	 * @date 2018年12月20日
	 */

	private String subBranch(String str) {
		return str.substring(str.lastIndexOf("/") + 1, str.length());
	}
	
}
