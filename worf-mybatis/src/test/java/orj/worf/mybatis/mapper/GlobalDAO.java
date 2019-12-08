package orj.worf.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import orj.worf.mybatis.model.Global;

@Repository
public interface GlobalDAO {
	 List<Global> selectCodeAndText();
	    
	 int updateTextByCode(@Param("text")String text, @Param("code")String code);
	    
	 Global selectByCode(@Param("code")String code);
	    
	 String selectTextByCode(@Param("code")String code);
}
