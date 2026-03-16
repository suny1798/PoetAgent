package com.suny.imagesearchsever;

import com.suny.imagesearchsever.tools.ImageSearchTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ImageSearchSeverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageSearchSeverApplication.class, args);
	}

	/**
	 * 创建并配置一个图片搜索工具的回调提供者
	 * 该方法将ImageSearchTool工具对象包装为MethodToolCallbackProvider
	 * @param imageSearchTool 图片搜索工具实例
	 * @return 配置好的MethodToolCallbackProvider实例，用于提供图片搜索工具的回调功能
	 */
	@Bean
	public ToolCallbackProvider ImageSearchTool(ImageSearchTool imageSearchTool){ // 声明一个Bean方法，返回ToolCallbackProvider接口类型
		return MethodToolCallbackProvider.builder() // 使用构建器模式创建MethodToolCallbackProvider实例
				.toolObjects(imageSearchTool) // 设置工具对象为传入的imageSearchTool实例
				.build(); // 完成构建并返回实例
	}

}
