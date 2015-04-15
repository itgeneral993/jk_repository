package com.rpframework.module.common.event;

import javax.servlet.ServletContext;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rpframework.core.event.IModuleEvent;
import com.rpframework.core.utils.cache.CacheUtils;
import com.rpframework.module.common.utils.cache.CountryCache;

@Component
public class CommonEventModule implements IModuleEvent {
	final Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public void applySiteMeshCustomConfiguration(SiteMeshFilterBuilder builder) {
		
	}

	@Override
	public void init(ServletContext servletContext) {
		CacheUtils.getIntance().add(new CountryCache());
		
		//for test
//		FileService fileService = SpringUtils.getBean(FileService.class);
//		File file = new File("/Users/rplees/Desktop/maven");
//		String path = "file/saveFile/maven.txt";
//		try {
//			fileService.saveFile(file, path);
//			
//			InputStream inputStream = fileService.retrieveFileStream(path);
//			fileService.deleteFile(path);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}