package kr.co.sist.administrator.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ReadData {

	@Value("${logFile.location}")
	private String filePath;

	public String[] getFileNamelist(String directoryname_userid) {
		String directoryName=filePath+"user_"+directoryname_userid;
		
		System.out.println("directoryName==================="+directoryName);
		
		File directory=new File(directoryName);
		
		String[] filenames;
		filenames=directory.list();
		
		if(filenames == null) {
			filenames=new String[] {"접근 기록이 없습니다."};
		}//end if
		
		
		return filenames;
	}//getFileNamelist
	
	
	public String readFileData(String filename,String staff_id) throws IOException{
		//1.File 생성
		File file=new File(filePath+"user_"+staff_id+"/"+filename);

		if(!file.exists()){
			return file+"이 존재하지 않습니다. 경로를 확인하세요."; //early return 사용
		}//end if

		BufferedReader br=null;
		
		StringBuilder sb=new StringBuilder();
		
		//3.File 안의 내용을 읽어들임(반복)
		String str="";
		try{
			br=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
			while((str=br.readLine())!=null){//EOF까지 읽어 들임
				sb.append(str).append("\n");
			}
		}finally{
			if(br!=null){br.close();}
		}
		

		return sb.toString();
	}//readFileData

}//class
