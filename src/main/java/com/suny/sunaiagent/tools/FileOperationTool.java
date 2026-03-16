package com.suny.sunaiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.suny.sunaiagent.constant.FileConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 文件操作工具类
 */
@Slf4j
public class FileOperationTool {

    private final String FILE_DIR = FileConstant.FILE_SAVE_DIR + "/file";


    /**
     * 读取文件内容
     * @param fileName
     * @return
     */
    @Tool(description = "Read file content from file name")
    public String readFile (@ToolParam(description = "Name of a file to read") String fileName){
        String filePath = FILE_DIR + "/" + fileName;
        try {
            log.info("✅ 文本读取工具调用成功！");
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading file" + e.getMessage();
        }
    }

    /**
     * 写入文件内容
     * @param fileName
     * @param content
     * @return
     */
    public String writeFile (
            @ToolParam(description = "Name of a file to write") String fileName,
            @ToolParam(description = "Content to write to the file") String content){
        String filePath = FILE_DIR + "/" + fileName;
        try {
            FileUtil.writeString(content, filePath, "UTF-8");
            log.info("✅ 文本写入工具调用成功！");
            return "File written successfully to：" + filePath;
        } catch (Exception e) {
            return "Error writing file" + e.getMessage();
        }
    }

}
