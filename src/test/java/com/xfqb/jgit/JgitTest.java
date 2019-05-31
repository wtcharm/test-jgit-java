package com.xfqb.jgit;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONArray;
import com.xfqb.jgit.util.JgitUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * JGit 测试类
 * @ClassName: JgitTest 
 * @date 2018年12月20日
 * @author tang wang
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JgitTest {
	//远程库路径
	private String remotePath = "https://github.com/wtcharm/SpringDemo.git";
	//下载已有仓库到本地路径
	private String localPath = "D:\\springDemo\\";
	//本地路径新建
	private String initPath = "D:\\springTest\\";
	//github账号
	private String username = "handsomethewt@163.com";
	//github密码
	private String password = "wt19941203";
	//项目分支
	private String dev = "master";

	@Autowired
	private JgitUtils jgitUtils;

	/**
	 * 进行日志打印输出
	 *    
	 * @author tao wang
	 * @date 2018年12月20日
	 */
	
	@Test
	public void testLog() {
		JSONArray logs = this.jgitUtils.log(localPath);
		log.info("logs **" + logs);
	}

	/**
	 * 进行远程clone 测试
	 *    
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	@Test
	public void testGetGitClone() {
		boolean clone = this.jgitUtils.getGitClone(remotePath, localPath, dev, username, password);
		log.info("getGitClone ** " + clone);
	}

	/**
	 * 进行本地仓库的创建
	 *    
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	@Test
	public void testSetCreateGitLocal() {
		boolean cre = this.jgitUtils.setCreateGitLocal(initPath);
		log.info("setCreateGitLocal ** " + cre);
	}

	/**
	 * 进行将文件提交的本地
	 *    
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	@Test
	public void testSetAddGitLocal() {
		String filename = "myfile";
		String fileUrl = localPath  + "/myfile.txt";
		boolean add = this.jgitUtils.setAddGitLocal(localPath, fileUrl, filename);
		log.info("setAddGitLocal ** " + add);
	}


	/**
	 * 进行代码的本地提交
	 *    
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	@Test
	public void testSetCommitGitLocal() {
		String commit = "提交成功";
		boolean comit = this.jgitUtils.setCommitGitLocal(localPath, commit);
		log.info("setCommitGitLocal ** " + comit);
	}

	/**
	 * 将远程仓库的数据拉取到本地
	 *    
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	@Test
	public void testSetPullGitLocal() {
		boolean pull = this.jgitUtils.setPullGitLocal(localPath, dev, username, password);
		log.info("setPullGitLocal ** " + pull);
	}

	/**
	 * 将本地项目进行远程推送
	 *    
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	@Test
	public void testSetPushGitLocal() {
		boolean push = this.jgitUtils.setPushGitLocal(localPath, username, password);
		log.info("setPushGitLocal ** " + push);
	}

	/**
	 * 获取当前项目的所有分支
	 *    
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	@Test
	public void testGetRefGitAll() {
		List<String> all = this.jgitUtils.getRefGitAll(localPath);
		for (String string : all) {
			System.err.println("getRefGitAll ** " + string);
		}
	}

	/**
	 * 添加新的分支
	 *    
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	@Test
	public void testSetCreateRefGit() {
		String refName = "dev";
		boolean ref = this.jgitUtils.setCreateRefGit(localPath, refName);
		log.info("setCreateRefGit ** " + ref);
	}

	/**
	 * 进行分支的删除
	 *    
	 * @author tao wang
	 * @date 2018年12月18日
	 */

	@Test
	public void testGetDeleteRefGit() {
		String refName = "dev";
		boolean del = this.jgitUtils.getDeleteRefGit(localPath, refName);
		log.info("getDeleteRefGit ** " + del);
	}

	/**
	 * 进行添加分支时如果出现已经存在的分支 先进行删除
	 * 然后进行添加
	 * @author tao wang
	 * @date 2018年12月18日
	 */
	@Test
	public void testSetCreFirstlyDel() {
		String refName = "dev";
		boolean crdel = this.jgitUtils.setCreFirstlyDel(localPath, refName);
		log.info("setCreFirstlyDel ** " + crdel);
	}

	/**
	 * 进行分支切换，分支不存在则创建分支
	 *    
	 * @author tao wang
	 * @date 2018年12月20日
	 */
	@Test
	public void testSetSwitchRrf() {
		String refName = "dev";
		boolean swit = this.jgitUtils.setSwitchRrf(localPath, refName);
		log.info("setSwitchRrf ** " + swit);
	}
}
