package cn.com.ut.config.fs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.aliyun.oss.OSSClient;

import cn.com.ut.core.common.fs.api.FsOperation;
import cn.com.ut.core.common.fs.fileoperation.AliOssOperation;

@Configuration
public class AliOssConfig {

	@Autowired
	private Environment env;

	@Bean
	public OSSClient oSSClient() {

		boolean usevpc = env.getProperty("alioss.usevpc", Boolean.class, false);
		String region = env.getProperty("alioss.region", "oss-cn-shenzhen");
		String endpoint = region + ".aliyuncs.com";
		if (usevpc) {
			endpoint = region + "-internal.aliyuncs.com";
		}
		String accessKeyId = env.getProperty("alioss.accessKeyId");
		String accessKeySecret = env.getProperty("alioss.accessKeySecret");
		OSSClient oSSClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		return oSSClient;
	}

	@Bean
	public FsOperation aliossOperation() {

		String bucketName = env.getProperty("alioss.bucketName");
		AliOssOperation aliOssOperation = new AliOssOperation();
		aliOssOperation.setBucketName(bucketName);
		aliOssOperation.setoSSClient(oSSClient());
		String region = env.getProperty("alioss.region", "oss-cn-shenzhen");
		aliOssOperation.setRegion(region);
		return aliOssOperation;
	}
}
