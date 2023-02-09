package com.group04.tgdd;

import com.group04.tgdd.model.Comment;
import com.group04.tgdd.repository.CommentRepo;
import com.group04.tgdd.repository.OrderRepo;
import com.group04.tgdd.utils.MoneyConvert;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@SpringBootTest
class TgddApplicationTests {

	@Autowired
	private CommentRepo commentRepo;

	@Test
	void contextLoads() throws IOException {
		Locale locale = new Locale("vn", "VN");
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

		System.out.println(currencyFormatter.format(1000000));
	}

	@Test
	void testTimeZone() throws IOException {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		date.setTime(date.getTime()+5*60*1000);
// Use Madrid's time zone to format the date in
		df.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		System.out.println(df.format(date));
	}

}
