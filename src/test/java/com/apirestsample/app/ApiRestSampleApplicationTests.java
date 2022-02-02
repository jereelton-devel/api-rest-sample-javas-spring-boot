package com.apirestsample.app;

import com.apirestsample.app.utils.Helpers;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Properties;

import static com.apirestsample.app.utils.Helpers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiRestSampleApplicationTests extends AbstractTest {

    private final Properties props = Helpers.extractProps();
	private final String authRequest = props.getProperty("application.basic-authorization");

	String uriWrongTest = "/";
	String uri2WrongTest = "/api";
	String uriBaseTest = "/api/customers";
	String userIdFoundTest = "97befb51c3240e10119f4e80b3a05b71";
	String userIdNotFoundTest = "97befb51c3240e10119f4e80b3a05000";
	String wrongBasicAuthTest = "Basic X1X2X3X4X5X6ZX7X8X9X0";

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	/**
	 * Create Customer
	 */

	@Test
	public void whenCorrectRequestToCreateCustomer_RetrieveCustomerCreated_201() throws Exception {

		String dataRequest = props.getProperty("application.test.post-new-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.appendField("id", md5Id);
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
				MockMvcRequestBuilders
					.post(uriBaseTest)
					.content(customerPost)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.header("Authorization", authRequest)
		)
		.andExpect(status().isCreated())
		.andReturn();

	}

	@Test
	public void whenCorrectRequestToCreateCustomerAlreadyExists_RetrieveCustomerFound_302() throws Exception {

	}

	@Test
	public void whenMissingBodyRequestToCreateCustomer_RetrieveBadRequest_400() throws Exception {

	}

	@Test
	public void whenRequestCreateCustomerWithInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenCorrectRequestToCreateCustomer_RetrieveServerError_500() throws Exception {

	}

	/**
	 * ReadAll Customer
	 */

	@Test
	public void whenCorrectRequestToReadAllCustomers_RetrieveAllCustomersFromDatabase_200() throws Exception {

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(uriBaseTest)
						.accept(MediaType.ALL)
						.header("Authorization", authRequest)
				).andReturn();

		int status = mvcResult.getResponse().getStatus();

		Assertions.assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Assertions.assertTrue(content.length() > 0);

	}

	@Test
	public void whenRequestReadAllCustomersWithInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenCorrectRequestToReadAllCustomersButNotExistsAny_RetrieveNotFound_404() throws Exception {

	}

	@Test
	public void whenCorrectRequestToReadAllCustomersButServerError_RetrieveServerError_500() throws Exception {

	}

	/**
	 * Read Customers
	 */

	@Test
	public void whenCorrectRequestToReadCustomer_RetrieveCustomerDetails_200() throws Exception {

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(uriBaseTest+"/"+userIdFoundTest)
						.accept(MediaType.ALL)
						.header("Authorization", authRequest)
				).andReturn();

		int status = mvcResult.getResponse().getStatus();

		Assertions.assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Assertions.assertTrue(content.length() > 0);

	}

	@Test
	public void whenRequestReadCustomerWithInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenCorrectRequestToReadCustomerButNotExists_RetrieveNotFound_404() throws Exception {

	}

	@Test
	public void whenCorrectRequestToReadCustomerButServerError_RetrieveServerError_500() throws Exception {

	}

	/**
	 * Update Customer
	 */

	@Test
	public void whenCorrectRequestToUpdateCustomer_RetrieveCustomerUpdated_200() throws Exception {

	}

	@Test
	public void whenMissingBodyRequestToUpdateCustomer_RetrieveBadRequest_400() throws Exception {

	}

	@Test
	public void whenRequestUpdateCustomerWithInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenRequestUpdateCustomerButNotExists_RetrieveNotFound_404() throws Exception {

	}

	@Test
	public void whenRequestUpdateCustomerWithInvalidBodySize_RetrieveNotAcceptable_406() throws Exception {

	}

	@Test
	public void whenCorrectRequestToUpdateCustomerButServerError_RetrieveServerError_500() throws Exception {

	}

	/**
	 * Delete Customer
	 */

	@Test
	public void whenCorrectRequestToDeleteCustomer_RetrieveOK_200() throws Exception {

	}

	@Test
	public void whenRequestDeleteCustomerWithInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenCorrectRequestToDeleteCustomerButNotExists_RetrieveNotFound_404() throws Exception {

	}

	@Test
	public void whenCorrectRequestToDeleteCustomerButServerError_RetrieveServerError_500() throws Exception {

	}

	/**
	 * Patch Customer
	 */

	@Test
	public void whenCorrectRequestToPatchCustomer_RetrieveCustomerUpdated_200() throws Exception {

	}

	@Test
	public void whenMissingBodyRequestToPatchCustomer_RetrieveBadRequest_400() throws Exception {

	}

	@Test
	public void whenRequestPatchCustomerWithInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenRequestPatchCustomerButNotExists_RetrieveNotFound_404() throws Exception {

	}

	@Test
	public void whenRequestPatchCustomerWithInvalidBodySize_RetrieveNotAcceptable_406() throws Exception {

	}

	@Test
	public void whenCorrectRequestToPatchCustomerButServerError_RetrieveServerError_500() throws Exception {

	}

	/**
	 * Reject Request (not POST|GET)
	 */

	@Test
	public void whenInvalidRequestUriAndInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenInvalidRequestUriAndIsPutOrPatchOrDeleteOrHeadOrOptionsHttpMethod_RetrieveNotAllowed_405() throws Exception {

	}

	/**
	 * Reject Request (only POST|GET)
	 */

	@Test
	public void whenInvalidRequestUriAndInvalidAuthorizationGetPost_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenInvalidRequestUriAndIsPostOrGetHttpMethod_RetrieveNotAllowed_405() throws Exception {

	}

}
