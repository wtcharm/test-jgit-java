package com.xfqb.jgit;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * JGit API测试
 */

public class GitTest {
	//远程库路径
	private  String remotePath = "http://gitlab.cafewallet.com.cn/liukang/xfqb-project-prototype.git"; 
	//下载已有仓库到本地路径
	private String localPath = "D:\\project\\"; 
	//本地路径新建
	private String initPath = "D:\\test\\"; 

	private String username = "wangtao@infohold.com.cn";
	private String password = "handsome03";
	/**
	 * 克隆远程库
	 * @throws IOException
	 * @throws GitAPIException
	 */

	@Test
	public void testClone() throws IOException, GitAPIException {

		//设置远程服务器上的用户名和密码
		UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider 
		= new UsernamePasswordCredentialsProvider(username, password);

		//克隆代码库命令
		CloneCommand cloneCommand = Git.cloneRepository();
		//设置远程URI
		Git git = cloneCommand.setURI(remotePath) 
				//设置clone下来的分支
				.setBranch("master") 
				//设置下载存放路径
				.setDirectory(new File(localPath))
				//设置权限验证
				.setCredentialsProvider(usernamePasswordCredentialsProvider) 
				.call();

		System.out.print(git.tag());
	}

	/**
	 * 本地新建仓库
	 */
	@Test
	public void testCreate() throws IOException {
		//本地新建仓库地址
		Repository newRepo = FileRepositoryBuilder.create(new File(initPath + "/.git"));
		newRepo.create();
	}

	/**
	 * 本地仓库新增文件
	 */
	@SuppressWarnings("resource")
	@Test
	public void testAdd() throws IOException, GitAPIException {
		File myfile = new File(localPath + "/myfile.txt");
		myfile.createNewFile();
		//git仓库地址
		Git git = new Git(new FileRepository(localPath + "/.git"));

		//添加文件
		git.add().addFilepattern("myfile").call();
	}

	/**
	 * 本地提交代码
	 */
	@SuppressWarnings("resource")
	@Test
	public void testCommit() throws IOException, GitAPIException,
	JGitInternalException {
		//git仓库地址
		Git git = new Git(new FileRepository(localPath + "/.git"));
		//提交代码
		git.commit().setMessage("test jGit").call();
	}


	/**
	 * 拉取远程仓库内容到本地
	 */

	@SuppressWarnings("resource")
	@Test
	public void testPull() throws IOException, GitAPIException {

		UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider 
		= new UsernamePasswordCredentialsProvider(username, password);
		//git仓库地址
		Git git = new Git(new FileRepository(localPath + "/.git"));
		git.pull().setRemoteBranchName("master").
		setCredentialsProvider(usernamePasswordCredentialsProvider).call();
	}

	/**
	 * push本地代码到远程仓库地址
	 */
	@SuppressWarnings("resource")
	@Test
	public void testPush() throws IOException, JGitInternalException,
	GitAPIException {

		UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider 
		= new UsernamePasswordCredentialsProvider(username, password);
		//git仓库地址
		Git git = new Git(new FileRepository(localPath + "/.git"));
		git.push().setRemote("origin").
		setCredentialsProvider(usernamePasswordCredentialsProvider).call();
	}
}

