package com.habibi.stockstoryapi;

import jakarta.transaction.Transactional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StockStoryApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	@WithMockUser(authorities = {"USER"})
	@Transactional
	public void 주식구매이야기등록후조회API테스트() throws Exception {
		// 주식 구매 이야기 등록
		ResultActions responseOfPost = mockMvc.perform(
				MockMvcRequestBuilders
						.post("/api/stock-story")
						.contentType("application/json")
						.content("""
							{
							  "long": true,
							  "stockCode": "066570",
							  "stockPrices": [30000, 40000, 50000],
							  "dt": "2023-12-31",
							  "story": "첫 주식 이야기 등록"
							}
						""")
		);
		responseOfPost.andExpect(MockMvcResultMatchers.status().isCreated());

		// id로 주식 스토리 반환
		ResultActions responseOfStoryById = mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/stock-story/1")
						.contentType("application/json")
		);

		responseOfStoryById.andExpect(MockMvcResultMatchers.jsonPath("$.storyId", CoreMatchers.is(1)));
		responseOfStoryById.andExpect(MockMvcResultMatchers.jsonPath("$.stockName", CoreMatchers.is("LG전자")));
		responseOfStoryById.andExpect(MockMvcResultMatchers.jsonPath("$.stockPrices", CoreMatchers.is(List.of(30000, 40000, 50000))));
		responseOfStoryById.andExpect(MockMvcResultMatchers.jsonPath("$.dt", CoreMatchers.is("2023-12-31")));
		responseOfStoryById.andExpect(MockMvcResultMatchers.jsonPath("$.story", CoreMatchers.is("첫 주식 이야기 등록")));
		responseOfStoryById.andExpect(MockMvcResultMatchers.jsonPath("$.long", CoreMatchers.is(true)));

		// 현재 소유한 주식 조회
		ResultActions responseOfOwnStock = mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/own-stock")
						.contentType("application/json")
		);

		responseOfOwnStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockName", CoreMatchers.is("LG전자")));
		responseOfOwnStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockCode", CoreMatchers.is("066570")));
		responseOfOwnStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockCount", CoreMatchers.is(3)));
		responseOfOwnStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].averagePurchasePrice", CoreMatchers.is(40000)));

		// 특정 시점에 소유한 주식 조회
		//// 주식 스토리 1 등록일 이후 조회
		ResultActions responseOfOwnStockAt0101 = mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/own-stock?at=2024-01-01")
						.contentType("application/json")
		);

		responseOfOwnStockAt0101.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockName", CoreMatchers.is("LG전자")));
		responseOfOwnStockAt0101.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockCode", CoreMatchers.is("066570")));
		responseOfOwnStockAt0101.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockCount", CoreMatchers.is(3)));
		responseOfOwnStockAt0101.andExpect(MockMvcResultMatchers.jsonPath("$[0].averagePurchasePrice", CoreMatchers.is(40000)));

		//// 주식 스토리 1 등록일 or 이전 조회 (조회 일 0시0분 기준으로 반환)
		ResultActions responseOfOwnStockAt1231 = mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/own-stock?at=2023-12-31")
						.contentType("application/json")
		);

		responseOfOwnStockAt1231.andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(0)));

		// 특정 주식의 이야기 조회
		ResultActions responseOfStockStoryOfCertainStock = mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/stock-story?stock-code=066570")
						.contentType("application/json")
		);

		responseOfStockStoryOfCertainStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].storyId", CoreMatchers.is(1)));
		responseOfStockStoryOfCertainStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockCode", CoreMatchers.is("066570")));
		responseOfStockStoryOfCertainStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockName", CoreMatchers.is("LG전자")));
		responseOfStockStoryOfCertainStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockPrices", CoreMatchers.is(List.of(30000, 40000, 50000))));
		responseOfStockStoryOfCertainStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].dt", CoreMatchers.is("2023-12-31")));
		responseOfStockStoryOfCertainStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].story", CoreMatchers.is("첫 주식 이야기 등록")));
		responseOfStockStoryOfCertainStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].long", CoreMatchers.is(true)));

		// 특정 기간의 모든 구매 기록 반환 조회
		ResultActions responseOfAllPurchaseRecordOnPeriod= mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/stock-purchase-record?start-period=2022-12-31&end-period=2023-12-31")
						.contentType("application/json")
		);

		responseOfAllPurchaseRecordOnPeriod.andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(3)));
		responseOfAllPurchaseRecordOnPeriod.andExpect(MockMvcResultMatchers.jsonPath("$[*].stockCode", CoreMatchers.everyItem(CoreMatchers.is("066570"))));
		responseOfAllPurchaseRecordOnPeriod.andExpect(MockMvcResultMatchers.jsonPath("$[*].stockName", CoreMatchers.everyItem(CoreMatchers.is("LG전자"))));
		responseOfAllPurchaseRecordOnPeriod.andExpect(MockMvcResultMatchers.jsonPath("$[*].dt", CoreMatchers.everyItem(CoreMatchers.is("2023-12-31"))));
		responseOfAllPurchaseRecordOnPeriod.andExpect(
				MockMvcResultMatchers.jsonPath("$[*].stockPrice",
						CoreMatchers.everyItem(
								CoreMatchers.anyOf(CoreMatchers.is(30000),CoreMatchers.is(40000),CoreMatchers.is(50000))
						)
				)
		);
	}

	@Test
	@WithMockUser(authorities = {"USER"})
	@Transactional
	public void 주식판매이야기등록후조회API테스트() throws Exception {
		// 주식 구매 이야기 등록
		ResultActions responseOfCreateLongStory = mockMvc.perform(
				MockMvcRequestBuilders
						.post("/api/stock-story")
						.contentType("application/json")
						.content("""
							{
							  "long": true,
							  "stockCode": "066570",
							  "stockPrices": [30000, 35000, 37000, 40000, 41100, 45600, 50000],
							  "dt": "2024-01-02",
							  "story": "주식 구매 이야기 등록"
							}
						""")
		);
		responseOfCreateLongStory.andExpect(MockMvcResultMatchers.status().isCreated());

		// 주식 판매 이야기 등록
		ResultActions responseOfCreateShortStory = mockMvc.perform(
				MockMvcRequestBuilders
						.post("/api/stock-story")
						.contentType("application/json")
						.content("""
							{
							  "long": false,
							  "stockCode": "066570",
							  "stockPrices": [67100, 68500, 65100, 68000],
							  "dt": "2024-01-03",
							  "story": "주식 판매 이야기 등록"
							}
						""")
		);
		responseOfCreateShortStory.andExpect(MockMvcResultMatchers.status().isCreated());

		// 현재 소유한 주식 조회
		ResultActions responseOfOwnStock = mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/own-stock")
						.contentType("application/json")
		);

		responseOfOwnStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockName", CoreMatchers.is("LG전자")));
		responseOfOwnStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockCode", CoreMatchers.is("066570")));
		responseOfOwnStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockCount", CoreMatchers.is(3)));
		responseOfOwnStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].averagePurchasePrice", CoreMatchers.is(39814)));

		// 실현된 주식 투자를 조회
		ResultActions responseOfRealizedStock = mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/realized-stock")
						.contentType("application/json")
		);

		responseOfRealizedStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockName", CoreMatchers.is("LG전자")));
		responseOfRealizedStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockCode", CoreMatchers.is("066570")));
		responseOfRealizedStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].stockCount", CoreMatchers.is(4)));
		responseOfRealizedStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].averageSellPrice", CoreMatchers.is(67175)));
		responseOfRealizedStock.andExpect(MockMvcResultMatchers.jsonPath("$[0].averagePurchasePrice", CoreMatchers.is(39814)));


		// 특정 기간의 모든 판매 기록 반환 조회 (끝나는 날짜까지 포함하여 판매 기록 반환)
		ResultActions responseOfAllPurchaseRecordOnPeriod= mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/stock-sell-record?start-period=2024-01-01&end-period=2024-01-03")
						.contentType("application/json")
		);

		responseOfAllPurchaseRecordOnPeriod.andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(4)));
		responseOfAllPurchaseRecordOnPeriod.andExpect(MockMvcResultMatchers.jsonPath("$[*].stockCode", CoreMatchers.everyItem(CoreMatchers.is("066570"))));
		responseOfAllPurchaseRecordOnPeriod.andExpect(MockMvcResultMatchers.jsonPath("$[*].stockName", CoreMatchers.everyItem(CoreMatchers.is("LG전자"))));
		responseOfAllPurchaseRecordOnPeriod.andExpect(MockMvcResultMatchers.jsonPath("$[*].dt", CoreMatchers.everyItem(CoreMatchers.is("2024-01-03"))));
		responseOfAllPurchaseRecordOnPeriod.andExpect(
				MockMvcResultMatchers.jsonPath("$[*].stockPrice",
						CoreMatchers.everyItem(
								CoreMatchers.anyOf(CoreMatchers.is(67100),CoreMatchers.is(68500),CoreMatchers.is(65100),CoreMatchers.is(68000))
						)
				)
		);
	}
}
