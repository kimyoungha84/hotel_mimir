package kr.co.sist.filepath;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FilePathMapper {
	
    List<FilePathDomain> selectAllFilePath();
    
    FilePathDTO selectOneFilePath(int FilePathNum);
    
    void insertFilePath(FilePathDTO fDTO);
    
    void updateFilePath(FilePathDTO fDTO);
   
}
