package naver;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.naverDAO;
import dao.naverDAOimpl;
import dto.naverDTO;


public class ApiExamSearchBlog {

	public static void main(String[] args) {
		String clientId = "N5MJcjZLiyCif_kr1yxI"; // 애플리케이션 클라이언트 아이디값"
		String clientSecret = "tUxEs0bKcx"; // 애플리케이션 클라이언트 시크릿값"
		
		CusetumClass ct = new CusetumClass();

		String text = null;
		try {
			String word = ct.Search();
			text = URLEncoder.encode(word, "UTF-8"); //검색어 설정
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("검색어 인코딩 실패", e);
		}
		//테마별 검색 blog, news ....
		String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text; // json 결과
		// String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text;
		// // xml 결과

		// 값 추출
		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id", clientId);
		requestHeaders.put("X-Naver-Client-Secret", clientSecret);
		String responseBody = get(apiURL, requestHeaders); //검색 결과 가져오기


	    // 가장 큰 JSONObject를 가져옵니다.
	    JSONObject jObject = new JSONObject(responseBody);
	    // 배열을 가져옵니다.
	    JSONArray jArray = jObject.getJSONArray("items");
	    
	    
//	    System.out.println(responseBody); //검색 결과 출력
	    // 배열의 모든 아이템을 출력합니다.
	    for (int i = 0; i < 5; i++) {
	        JSONObject obj = jArray.getJSONObject(i);
	        
	        String title = obj.getString("title");
	        String url = obj.getString("link");
//	        boolean draft = obj.getBoolean("draft");
	        System.out.println("title(" + i + "): " + title);
	        System.out.println("url(" + i + "): " + url);
//	        System.out.println("draft(" + i + "): " + draft);
	        
	        naverDTO dto = new naverDTO();
	        dto.setTitle(obj.getString("title"));
	        dto.setUrl(obj.getString("link"));
	        
	        naverDAOimpl dao = new naverDAOimpl();
	        dao.insert(dto);
	        
	        System.out.println();
	    }
	    
		
	}

	private static String get(String apiUrl, Map<String, String> requestHeaders) {
		HttpURLConnection con = connect(apiUrl);
		try {
			con.setRequestMethod("GET");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
				return readBody(con.getInputStream());
			} else { // 에러 발생
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	private static HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}

	private static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);

		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}

			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}
}