package org.zerock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.SampleVO;
import org.zerock.domain.Ticket;

import lombok.extern.log4j.Log4j;

//@Controller
@RestController
@RequestMapping("/sample/*")
@Log4j
public class SampleController {
	
	@GetMapping(value = "/getText", produces = "text/plain; charset = UTF-8")
	public String getText() {
		log.info("MIME TYPE : " + MediaType.TEXT_PLAIN_VALUE);
		return "안녕하세요!";
	}
	
	@GetMapping(value = "/getSample", produces = 
		{MediaType.APPLICATION_JSON_UTF8_VALUE, 
		MediaType.APPLICATION_XML_VALUE})
		public SampleVO getSample() {
			return new SampleVO(112, "연아", "김");
	}
	@GetMapping(value = "/getSample2")
	public SampleVO getSample2() {
		return new SampleVO(119, "나라", "장");
	}
	
	@GetMapping(value = "/getList")
	   public List<SampleVO> getList(){
	      // getList() 메서드의 리턴(return) 값으로
	      // 1부터 10미만까지 루프를 처리하면서
	      // SampleVO 객체를 만들어서 List<SampleVO>로 만들어서 보내줍니다.
	      return IntStream.range(1, 10).mapToObj(i -> new SampleVO(i, i + "First", i + " Last")).collect(Collectors.toList());
	   }
	   
	   // 맵형 자료구조의 경우, '키'와 '값'을 가지는 하나의 객체로 간주됩니다.
	   @GetMapping(value = "/getMap")
	   public Map<String, SampleVO> getMap(){
	      Map<String, SampleVO> map = new HashMap<String, SampleVO>();
	      map.put("First", new SampleVO(114, "지현", "전"));
	      return map;      
	   }
	   
	   // ResponseEntity는 데이터와 함께 HTTP 헤더의 상태 메시지 등을 같이 전달하는 용도로 사용합니다.
	   // HTTP의 상태 코드와 에러 메시지 등을 함께 데이터를 전달할 수 있기 때문에
	   // 받는 입장에서는 확실하게 결과를 알 수 있습니다.
	   // 아래의 check() 메서드는 반드시 "height"와 "weight"를 파라미터(매개변수)로 전달받습니다.
	   // 이때, 만일(if) 'height'값이 150보다 작다면 502(BAD_GATEWAY) 상태 코드와 데이터를 전송하고,
	   // 그렇지 않다면(else) 200(OK) 코드와 데이터를 전송합니다.
	   
	   @GetMapping(value = "/check", params = {"height", "weight"})
	   public ResponseEntity<SampleVO> check(Double height, Double weight){
		   SampleVO vo = new SampleVO(0, "" + height, "" + weight);
		   ResponseEntity<SampleVO> result = null;
		   if(height < 150) {
			   result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
		   }else {
			   result = ResponseEntity.status(HttpStatus.OK).body(vo);
		   }
		   return result;
	   }
	   
	   // @PathVariable : 일반 컨트롤러에서도 사용이 가능하지만
	   //                  REST 방식에서 자주 사용됩니다.
	   //                  URL 경로의 일부를 파라미터로 사용할 때 이용합니다.
	   // @PathVariable을 적용하고 싶은 경우에는 '{}'를 이용해서 변수명을 지정하고,
	   // @PathVariable을 이용해서 지정된 이름의 변수값을 얻을 수 있습니다.
	   // 값을 얻을 때에는 int, double과 같은 기본 자료형은 사용할 수 없습니다.
	   @GetMapping("/product/{cat}/{pid}")
	   public String[] getPath(
			   @PathVariable("cat") String cat,
			   @PathVariable("pid") Integer pid) {
		   return new String[] {"category: " + cat, "productid: " + pid};
	   }
	
	   // @RequestBody : JSON 데이터를 원하는 타입의 객체로 변환해야 하는 경우에 주로 사용합니다.
	   // @RequestBody는 전달된 요청(request)의 내용(body)을 이용해서
	   // 해당 파라미터의 타입으로 변환을 요구합니다. 내부적으로 HttpMessageConverter 타입의
	   // 객체들을 이용해서 다양한 포맷의 입력 데이터를 변환할 수 있습니다.
	   // SampleController의 다른 메서드와 달리 @PostMapping을 적용하였습니다.   
	   // 이것은 @RequestBody가 요청(request)한 내용(body)을 처리하기 때문에
	   // 일반적인 파라미터(매개변수) 전달 방식을 사용할 수 없기 때문입니다.
	@PostMapping("/ticket")
	public Ticket convert(@RequestBody Ticket ticket) {
		log.info("Ticket 타입의 ticket으로 요청(request) 내용(body)을 변환 처리함"
				+ ticket);
		return ticket;
	}		  
}
