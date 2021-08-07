package com.dailyProject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
class DailyProjectApplicationTests {

	@DisplayName("출력 테스트")
	@Test
	public void 출력_테스트(){
		System.out.println("hello");
	}

	@DisplayName("JWT 토큰 생성")
	@Test
	public void create_jwt_test(){

	}
}
